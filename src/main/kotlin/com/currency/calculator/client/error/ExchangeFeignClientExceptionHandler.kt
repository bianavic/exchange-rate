package com.currency.calculator.client.error

import feign.Response
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ExchangeFeignClientExceptionHandler : FeignHttpExceptionHandler {

    override fun handle(response: Response): Exception {
        val httpStatus: HttpStatus? = HttpStatus.resolve(response.status())
        val body: String = response.body().toString()
        if (HttpStatus.NOT_FOUND == httpStatus) {
            return BaseCodeNotFoundException(body)
        }
        return RuntimeException(body)
    }

}