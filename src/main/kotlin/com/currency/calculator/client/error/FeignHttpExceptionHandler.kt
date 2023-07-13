package com.currency.calculator.client.error

import feign.Response

interface FeignHttpExceptionHandler {
    fun handle(response: Response): Exception
}