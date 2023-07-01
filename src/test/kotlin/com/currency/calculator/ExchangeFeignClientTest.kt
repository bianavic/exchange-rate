package com.currency.calculator

import com.currency.calculator.client.feign.ExchangeFeignClient
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import com.google.gson.Gson
import feign.Feign
import feign.gson.GsonDecoder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(WireMockExtension::class)
class ExchangeFeignClientTest {

    private lateinit var exchangeFeignClient: ExchangeFeignClient

    @BeforeEach
    fun setup(wireMock: WireMockServer) {
        val baseUrl = wireMock.baseUrl()
        val gson = Gson()

        val feignBuilder = Feign.builder()
            .decoder(GsonDecoder(gson))
            .target(ExchangeFeignClient::class.java, baseUrl)

        exchangeFeignClient = feignBuilder

    }

    @Test
    fun testGetLatestExchangeFor(wireMock: WireMockServer) {
        val baseCode = "BRL"
        val expectedResponse = "your_expected_response_here"

        wireMock.stubFor(
            get(urlEqualTo("/latest/$baseCode"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(expectedResponse)
                )
        )

        val response = exchangeFeignClient.getLatestExchangeFor(baseCode)

        // Assertions
        assertEquals(expectedResponse, response)
        verify(
            getRequestedFor(urlEqualTo("/latest/$baseCode"))
                .withHeader("Content-Type", equalTo("application/json"))
        )
        verify(
            1,
            getRequestedFor(urlEqualTo("/latest/$baseCode"))
                .withHeader("Content-Type", equalTo("application/json"))
        )

    }
}