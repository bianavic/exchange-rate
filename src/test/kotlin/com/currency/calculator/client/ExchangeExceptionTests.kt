package com.currency.calculator.client

import com.currency.calculator.client.error.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import feign.Response
import feign.codec.ErrorDecoder
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `should return UnsupportedCodeException`() {
        val responseMock = createMockedResponse("unsupported-code")

        val exception = errorDecoder.decode("methodKey", responseMock)

        assertEquals(UnsupportedCodeException::class.java, exception::class.java)
        assertEquals("Unsupported currency code", exception.message)
    }

    @Test
    fun `should return InvalidKeyException`() {
        val responseMock = createMockedResponse("invalid-key")

        val exception = errorDecoder.decode("methodKey", responseMock)

        assertEquals(InvalidKeyException::class.java, exception::class.java)
        assertEquals("Invalid API key", exception.message)
    }

    @Test
    fun `should return InactiveAccountException`() {
        val responseMock = createMockedResponse("inactive-account")

        val exception = errorDecoder.decode("methodKey", responseMock)

        assertEquals(InactiveAccountException::class.java, exception?.javaClass)
        assertEquals("Inactive account", exception?.message)
    }

    @Test
    fun `should return QuotaReachedException`() {
        val responseMock = createMockedResponse("quota-reached")

        val exception = errorDecoder.decode("methodKey", responseMock)

        assertEquals(QuotaReachedException::class.java, exception::class.java)
        assertEquals("API quota reached", exception.message)
    }

    @Test
    fun `should return UnknownCodeException`() {
        val responseMock = createMockedResponse("unknown-error-type")

        val exception = errorDecoder.decode("methodKey", responseMock)

        assertEquals(UnknownCodeException::class.java, exception::class.java)
        assertEquals("Unknown error code: unknown-error-type", exception.message)
    }

    private fun createMockedResponse(errorType: String): Response {
        val responseBody = """{"result": "error", "errorType": "$errorType"}"""
        val inputStream = ByteArrayInputStream(responseBody.toByteArray(StandardCharsets.UTF_8))
        val responseBodyMock = createResponseBody(inputStream)
        val responseMock = mockk<Response>()
        every { responseMock.body() } returns responseBodyMock
        return responseMock
    }

    private fun createResponseBody(inputStream: ByteArrayInputStream): Response.Body {
        val responseBodyMock = mockk<Response.Body>()
        every { responseBodyMock.asInputStream() } returns inputStream
        return responseBodyMock
    }

}