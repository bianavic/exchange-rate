package com.currency.calculator.client

import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ConversionRatesResponse
import com.currency.calculator.wiremock.WireMockServerConfig
import com.currency.calculator.wiremock.WireMockServerConst
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import feign.RetryableException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.server.ResponseStatusException
import java.io.FileReader

@ActiveProfiles("test")
@SpringBootTest(classes = [WireMockServerConfig::class])
internal class ExchangeFeignClientTest(
    private val wireMockServer: WireMockServer,
    private val exchangeFeignClient: ExchangeFeignClient
): BehaviorSpec ({

    Given("getLatestExchangeFor stub - SUCCESS") {
        wireMockServer.stubGetLatest(
            WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(WireMockServerConst.GetLatest.responsePath)
        )

        When("calling feign getLatestExchangeFor") {
            val then = exchangeFeignClient.getLatestExchangeFor("BRL")

            Then("verify response") {
                val expected: ConversionRatesResponse = Gson().fromJson(
                    JsonReader(FileReader(WireMockServerConst.GetLatest.absoluteResponsePath())),
                    ConversionRatesResponse::class.java
                )
                then shouldBe expected
            }
        }
    }

    Given("getLatestExchangeFor stub - BAD_REQUEST") {
        wireMockServer.stubGetLatest(WireMock.badRequest())

        When("calling feign getLatestExchangeFor") {
            val then = shouldThrow<ResponseStatusException> {
                exchangeFeignClient.getLatestExchangeFor("BRL")
            }
            Then("verify BAD_REQUEST") {
                then.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }

    Given("getLatestExchangeFor stub - INTERNAL_SERVER_ERROR") {
        wireMockServer.stubGetLatest(WireMock.serverError())

        When("calling feign getLatestExchangeFor") {
            val then = shouldThrow<ResponseStatusException> {
                exchangeFeignClient.getLatestExchangeFor("BRL")
            }
            Then("verify INTERNAL_SERVER_ERROR") {
                then.statusCode shouldBe HttpStatus.INTERNAL_SERVER_ERROR
            }
        }
    }

    Given("getLatestExchangeFor stub - Connection refused") {
        wireMockServer.stop()

        When("calling feign getLatestExchangeFor") {
            val then = shouldThrow<RetryableException> {
                exchangeFeignClient.getLatestExchangeFor("BRL")
            }
            Then("verify Connection refused") {
                then.message shouldStartWith "Connection refused"
            }
        }
    }

})

private fun WireMockServer.stubGetLatest(responseDefinitionBuilder: ResponseDefinitionBuilder) {
    stubFor(
        WireMock.get(WireMock.urlPathMatching(WireMockServerConst.GetLatest.url))
            .willReturn(responseDefinitionBuilder)
    )
}