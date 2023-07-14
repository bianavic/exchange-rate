package com.currency.calculator.client

import com.currency.calculator.client.exceptions.*
import com.currency.calculator.client.feign.ExchangeErrorDecoder
import feign.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class ExchangeExceptionTests {

    private val error = ExchangeErrorDecoder()

    @Test
    fun `test should return MalformedRequestException`() {
        // Arrange
        val response = createMockResponse(400)

        // Act
        val exception = error.decode("", response)

        // Assert
        assertEquals(MalformedRequestException::class.java, exception.javaClass)
        assertEquals("Malformed request", exception.message)
    }

    @Test
    fun `test should return InvalidKeyException`() {
        // Arrange
        val response = createMockResponse(401)

        // Act
        val exception = error.decode("", response)

        // Assert
        assertEquals(InvalidKeyException::class.java, exception.javaClass)
        assertEquals("Invalid API key", exception.message)
    }

    @Test
    fun `test should return InactiveAccountException`() {
        // Arrange
        val response = createMockResponse(403)

        // Act
        val exception = error.decode("", response)

        // Assert
        assertEquals(InactiveAccountException::class.java, exception.javaClass)
        assertEquals("Inactive account", exception.message)
    }

    @Test
    fun `test should return QuotaReachedException`() {
        // Arrange
        val response = createMockResponse(429)

        // Act
        val exception = error.decode("", response)

        // Assert
        assertEquals(QuotaReachedException::class.java, exception.javaClass)
        assertEquals("API quota reached", exception.message)
    }

    @Test
    fun `test should return BaseCodeNotFoundException`() {
        // Arrange
        val response = createMockResponse(404)

        // Act
        val exception = error.decode("", response)

        // Assert
        assertEquals(BaseCodeNotFoundException::class.java, exception.javaClass)
        assertEquals("Base code not found", exception.message)
    }

    private fun createMockResponse(statusCode: Int): Response {
        val response = mock(Response::class.java)
        `when`(response.status()).thenReturn(statusCode)
        return response
    }

}