package com.currency.calculator.client.error

class FeignHttpException(val status: Int) : RuntimeException()