package com.currency.calculator.client

import com.currency.calculator.client.error.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import feign.Response
import feign.codec.ErrorDecoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

class ExchangeExceptionTests {

    private lateinit var errorDecoder: ErrorDecoder
    private lateinit var gson: Gson

    @BeforeEach
    fun setUp() {
        gson = GsonBuilder().create()
        errorDecoder = FeignErrorDecoder(gson)
    }

    @Test
    fun testDecode_WithUnsupportedCode_ReturnsUnsupportedCodeException() {
        // Create a mock Response
        val responseBody = """{"result": "error", "errorType": "unsupported-code"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mock(Response::class.java)

        // Configure the mock Response
        `when`(responseMock.body()).thenReturn(responseBodyMock)

        // Call the error decoder
        val exception = errorDecoder.decode("methodKey", responseMock)

        // Verify the exception type and message
        assertEquals(UnsupportedCodeException::class.java, exception::class.java)
        assertEquals("Unsupported currency code", exception.message)
    }

    @Test
    fun testDecode_WithInvalidKeyCode_ReturnsInvalidKeyException() {
        // Create a mock Response
        val responseBody = """{"result": "error", "errorType": "invalid-key"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mock(Response::class.java)

        // Configure the mock Response
        `when`(responseMock.body()).thenReturn(responseBodyMock)

        // Call the error decoder
        val exception = errorDecoder.decode("methodKey", responseMock)

        // Verify the exception type and message
        assertEquals(InvalidKeyException::class.java, exception::class.java)
        assertEquals("Invalid API key", exception.message)
    }

    @Test
    fun testDecode_WithInactiveAccountCode_ReturnsInactiveAccountException() {
        // Create a mock Response
        val responseBody = """{"result": "error", "errorType": "inactive-account"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mock(Response::class.java)

        // Configure the mock Response
        `when`(responseMock.body()).thenReturn(responseBodyMock)

        // Call the error decoder
        val exception = errorDecoder.decode("methodKey", responseMock)

        // Verify the exception type and message
        assertEquals(InactiveAccountException::class.java, exception::class.java)
        assertEquals("Inactive account", exception.message)
    }

    @Test
    fun testDecode_WithQuotaReachedCode_ReturnsQuotaReachedException() {
        // Create a mock Response
        val responseBody = """{"result": "error", "errorType": "quota-reached"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mock(Response::class.java)

        // Configure the mock Response
        `when`(responseMock.body()).thenReturn(responseBodyMock)

        // Call the error decoder
        val exception = errorDecoder.decode("methodKey", responseMock)

        // Verify the exception type and message
        assertEquals(QuotaReachedException::class.java, exception::class.java)
        assertEquals("API quota reached", exception.message)
    }

    @Test
    fun testDecode_WithUnknownCode_ReturnsUnknownCodeException() {
        // Create a mock Response
        val responseBody = """{"result": "error", "errorType": "unknown-error-type"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mock(Response::class.java)

        // Configure the mock Response
        `when`(responseMock.body()).thenReturn(responseBodyMock)

        // Call the error decoder
        val exception = errorDecoder.decode("methodKey", responseMock)

        // Verify the exception type and message
        assertEquals(UnknownCodeException::class.java, exception::class.java)
        assertEquals("Unknown error code: unknown-error-type", exception.message)
    }

    // Helper method to create a mock Response.Body from an InputStream
    private fun createResponseBody(inputStream: ByteArrayInputStream): Response.Body {
        val responseBodyMock = mock(Response.Body::class.java)
        `when`(responseBodyMock.asInputStream()).thenReturn(inputStream)
        return responseBodyMock
    }

}