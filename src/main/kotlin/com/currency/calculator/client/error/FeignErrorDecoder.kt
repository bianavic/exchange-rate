package com.currency.calculator.client.error

import com.google.gson.Gson
import feign.Response
import feign.codec.ErrorDecoder
import java.io.InputStreamReader


class FeignErrorDecoder(private val gson: Gson) : ErrorDecoder {

    override fun decode(methodKey: String?, response: Response?): Exception {

        val responseBody = response?.body()?.asInputStream()
        val errorResponse = gson.fromJson(InputStreamReader(responseBody), ErrorResponse::class.java)

        return when (errorResponse.errorType) {
            "unsupported-code" -> UnsupportedCodeException("Unsupported currency code")
            "malformed-request" -> MalformedRequestException("Malformed request")
            "invalid-key" -> InvalidKeyException("Invalid API key")
            "inactive-account" -> InactiveAccountException("Inactive account")
            "quota-reached" -> QuotaReachedException("API quota reached")
            else -> UnknownCodeException("Unknown error code: ${errorResponse.errorType}")
        }
    }

}

open class ExchangeRateException(message: String) : RuntimeException(message)
class UnsupportedCodeException(message: String) : ExchangeRateException(message)
class InactiveAccountException(message: String) : ExchangeRateException(message)
class InvalidKeyException(message: String) : ExchangeRateException(message)
class MalformedRequestException(message: String) : ExchangeRateException(message)
class QuotaReachedException(message: String) : ExchangeRateException(message)
class UnknownCodeException(message: String) : ExchangeRateException(message)