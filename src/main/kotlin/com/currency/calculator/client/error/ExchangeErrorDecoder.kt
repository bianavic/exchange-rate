package com.currency.calculator.client.error

import com.currency.calculator.client.exceptions.*
import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.http.HttpStatus


class ExchangeErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): java.lang.Exception {
        val statusCode = response?.status() ?: HttpStatus.INTERNAL_SERVER_ERROR.value()

        return when (statusCode) {
            HttpStatus.BAD_REQUEST.value() -> MalformedRequestException("Malformed request")
            HttpStatus.UNAUTHORIZED.value() -> InvalidKeyException("Invalid API key")
            HttpStatus.FORBIDDEN.value() -> InactiveAccountException("Inactive account")
            HttpStatus.TOO_MANY_REQUESTS.value() -> QuotaReachedException("API quota reached")
            HttpStatus.NOT_FOUND.value() -> BaseCodeNotFoundException("Base code not found")
            else -> UnknownErrorException("Unknown error occurred")
        }
    }

}