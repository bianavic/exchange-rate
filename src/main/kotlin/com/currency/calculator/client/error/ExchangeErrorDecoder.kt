package com.currency.calculator.client.error

import feign.Response
import feign.codec.ErrorDecoder
import org.springframework.http.HttpStatus


class ExchangeErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {
        val statusCode = response?.status() ?: HttpStatus.INTERNAL_SERVER_ERROR.value()

        return when (statusCode) {
            HttpStatus.BAD_REQUEST.value() -> MalformedRequestException("Malformed request")
            HttpStatus.UNAUTHORIZED.value() -> InvalidKeyException("Invalid API key")
            HttpStatus.FORBIDDEN.value() -> InactiveAccountException("Inactive account")
            HttpStatus.TOO_MANY_REQUESTS.value() -> QuotaReachedException("API quota reached")
            HttpStatus.NOT_FOUND.value() -> {
                val baseCode = extractBaseCodeFromMethodKey(methodKey)
                throw BaseCodeNotFoundException(baseCode)
            }
            else -> UnknownErrorException("Unknown error occurred")
        }
}

    private fun extractBaseCodeFromMethodKey(methodKey: String?): String {
        if (methodKey != null) {
            val parts = methodKey.split('/')
            if (parts.size >= 2) {
                // The base code should be the last part after splitting by '/'
                return parts.last()
            }
        }

        // If the methodKey is null, doesn't contain '/', or has fewer than 2 parts after splitting, throw an exception.
        throw BaseCodeNotFoundException("defaultBaseCode")
    }
}

sealed class ExchangeRateException(message: String) : RuntimeException(message)
class BaseCodeNotFoundException(message: String) : ExchangeRateException(message)
class InactiveAccountException(message: String) : ExchangeRateException(message)
class InvalidKeyException(message: String) : ExchangeRateException(message)
class MalformedRequestException(message: String) : ExchangeRateException(message)
class QuotaReachedException(message: String) : ExchangeRateException(message)
class UnknownErrorException(message: String) : ExchangeRateException(message)