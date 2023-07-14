package com.currency.calculator.client.exceptions

import org.springframework.http.HttpStatus

class ExchangeRateException(val errorType: String?, val statusCode: HttpStatus) : Exception()