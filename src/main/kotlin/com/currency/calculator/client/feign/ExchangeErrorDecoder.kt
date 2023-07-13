package com.currency.calculator.client.feign

import feign.Response
import feign.codec.ErrorDecoder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.lang.Exception

class ExchangeErrorDecoder : ErrorDecoder {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun decode(methodKey: String?, response: Response?): Exception {
        log.info("### ${response?.status()}, methodKey = $methodKey");

        if (response?.status() == HttpStatus.BAD_REQUEST.value()) {
            return ResponseStatusException(
                HttpStatus.valueOf(response.status()),
                "Bad Request"
            )
        }

        if (response?.status() == HttpStatus.NOT_FOUND.value()) {
            return ResponseStatusException(
                HttpStatus.valueOf(response.status()),
                "Unsupported Base Code"
            )
        }

        if (response?.status() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return ResponseStatusException(
                HttpStatus.valueOf(response.status()),
                "Server Error"
            )
        }

        return Exception(response?.reason())
    }

}