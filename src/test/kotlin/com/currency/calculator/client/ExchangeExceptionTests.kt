package com.currency.calculator.client

import com.currency.calculator.client.error.*
import feign.Response
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus

class ExchangeExceptionTests {

    private val errorDecoder = ExchangeErrorDecoder()

    @Test
    fun `test should return MalformedRequestException for BAD REQUEST`() {
        // Arrange
        val response = createMockResponse(HttpStatus.BAD_REQUEST.value())

        // Act
        val exception = errorDecoder.decode("", response)

        // Assert
        assertEquals(MalformedRequestException::class.java, exception.javaClass)
        assertEquals("Malformed request", exception.message)
    }

    @Test
    fun `test should return InvalidKeyException for UNAUTHORIZED`() {
        // Arrange
        val response = createMockResponse(HttpStatus.UNAUTHORIZED.value())

        // Act
        val exception = errorDecoder.decode("", response)

        // Assert
        assertEquals(InvalidKeyException::class.java, exception.javaClass)
        assertEquals("Invalid API key", exception.message)
    }

    @Test
    fun `test should return InactiveAccountException for FORBIDDEN`() {
        // Arrange
        val response = createMockResponse(HttpStatus.FORBIDDEN.value())

        // Act
        val exception = errorDecoder.decode("", response)

        // Assert
        assertEquals(InactiveAccountException::class.java, exception.javaClass)
        assertEquals("Inactive account", exception.message)
    }

    @Test
    fun `test should return BaseCodeNotFoundException NOT FOUND`() {
        val methodKey = "ExchangeFeignClient#getLatestExchangeFor" // Without base code
        val response = createMockResponse(HttpStatus.NOT_FOUND.value())
        val errorDecoder = ExchangeErrorDecoder()

        // Act and Assert
        val exception = assertThrows<BaseCodeNotFoundException> {
            errorDecoder.decode(methodKey, response)
        }
        assertEquals("defaultBaseCode", exception.message)
    }

    @Test
    fun `test should throw BaseCodeNotFoundException with default base code`() {
        // Arrange
        val methodKey = "ExchangeFeignClient#getLatestExchangeFor" // Without base code
        val response = createMockResponse(HttpStatus.NOT_FOUND.value())
        val errorDecoder = ExchangeErrorDecoder()

        // Act and Assert
        val exception = assertThrows<BaseCodeNotFoundException> {
            errorDecoder.decode(methodKey, response)
        }
        assertEquals("defaultBaseCode", exception.message)
    }

    @Test
    fun `test should return QuotaReachedException for TOO MANY REQUESTS`() {
        // Arrange
        val response = createMockResponse(HttpStatus.TOO_MANY_REQUESTS.value())

        // Act
        val exception = errorDecoder.decode("", response)

        // Assert
        assertEquals(QuotaReachedException::class.java, exception.javaClass)
        assertEquals("API quota reached", exception.message)
    }

    @Test
    fun `test should return UnknownErrorException for INTERNAL SERVER ERROR`() {
        // Arrange
        val response = createMockResponse(HttpStatus.INTERNAL_SERVER_ERROR.value())

        // Act
        val exception = errorDecoder.decode("", response)

        // Assert
        assertEquals(UnknownErrorException::class.java, exception.javaClass)
        assertEquals("Unknown error occurred", exception.message)
    }

    private fun createMockResponse(statusCode: Int): Response {
        val response = mockk<Response>()
        every { response.status() } returns statusCode
        return response
    }

}