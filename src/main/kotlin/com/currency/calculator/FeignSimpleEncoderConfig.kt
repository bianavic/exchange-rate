package com.currency.calculator

import feign.FeignException
import feign.Response
import feign.RetryableException
import feign.Util
import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.FormEncoder
import org.springframework.context.annotation.Bean
import java.io.IOException
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


class FeignSimpleEncoderConfig {

    @Bean
    fun encoder(): Encoder? {
        return FormEncoder()
    }

    @Bean
    fun errorDecoder(): ErrorDecoder? {
        return ErrorDecoder { methodKey: String?, response: Response ->
            var body = byteArrayOf()
            try {
                if (response.body() != null) {
                    body = Util.toByteArray(response.body().asInputStream())
                }
            } catch (ignored: IOException) { // NOPMD
            }
            val exception: FeignException =
                FeignException.BadRequest(response.reason(), response.request(), body, response.headers())
            if (response.status() >= 400) {
                return@ErrorDecoder RetryableException(
                    response.status(),
                    response.reason(),
                    response.request().httpMethod(),
                    exception,
                    Date.from(Instant.now().plus(15, ChronoUnit.MILLIS)),
                    response.request()
                )
            }
            exception
        }
    }

}