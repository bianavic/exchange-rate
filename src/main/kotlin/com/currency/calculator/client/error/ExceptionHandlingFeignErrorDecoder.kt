package com.currency.calculator.client.error

import feign.Feign
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.ApplicationContext
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.util.ReflectionUtils
import java.lang.Exception
import java.lang.reflect.Method
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap

@Component
class ExceptionHandlingFeignErrorDecoder(
    private val applicationContext: ApplicationContext
) : ErrorDecoder {

    private val exceptionHandlerMap: MutableMap<String, FeignHttpExceptionHandler> = HashMap()

    @EventListener
    fun onApplicationEvent(event: ContextRefreshedEvent) {
        val feignClients: Map<String, Any> = applicationContext.getBeansWithAnnotation(FeignClient::class.java)
        val clientMethods: List<Method> = feignClients.values.stream()
            .map { obj: Any -> obj.javaClass }
            .map { aClass: Class<*> -> aClass.interfaces[0] }
            .map { aClass: Class<*> -> ReflectionUtils.getDeclaredMethods(aClass) }
            .flatMap { methods: Array<Method> -> Arrays.stream(methods) }
            .collect(Collectors.toList())
        for (m in clientMethods) {
            val configKey: String = Feign.configKey(m.declaringClass, m)
            val handlerAnnotation: HandleFeignError? = getHandleFeignErrorAnnotation(m)
            if (handlerAnnotation != null) {
                val handler: FeignHttpExceptionHandler = applicationContext.getBean(handlerAnnotation.value.java)
                exceptionHandlerMap[configKey] = handler
            }
        }
    }

    private fun getHandleFeignErrorAnnotation(m: Method): HandleFeignError? {
        var result: HandleFeignError? = m.getAnnotation(HandleFeignError::class.java)
        if (result == null) {
            result = m.declaringClass.getAnnotation(HandleFeignError::class.java)
        }
        return result
    }

    override fun decode(methodKey: String, response: Response): Exception {
        val handler: FeignHttpExceptionHandler? = exceptionHandlerMap[methodKey]
        if (handler != null) {
            return handler.handle(response)
        }
        return defaultDecoder.decode(methodKey, response)
    }

    companion object {
        private val defaultDecoder: ErrorDecoder.Default = ErrorDecoder.Default()
    }

}
