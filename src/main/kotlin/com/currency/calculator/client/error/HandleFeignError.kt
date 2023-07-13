package com.currency.calculator.client.error

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class HandleFeignError(
    val value: KClass<out FeignHttpExceptionHandler>
)
