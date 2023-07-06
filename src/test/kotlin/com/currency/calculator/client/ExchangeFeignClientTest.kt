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
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import java.io.FileReader

@ActiveProfiles("test")
@SpringBootTest(classes = [WireMockServerConfig::class])
internal class ExchangeFeignClientTest(
    private val wireMockServer: WireMockServer,
    private val exchangeFeignClient: ExchangeFeignClient
): BehaviorSpec ({

    Given("getLatestExchangeFor stub - success") {
        wireMockServer.stubGetLatest(
            WireMock.aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBodyFile(WireMockServerConst.GetLatest.responsePath)
        )

        When("feign get Latest") {
            val then = exchangeFeignClient.getLatestExchangeFor("BRL")

            Then("response") {
                val expected: ConversionRatesResponse = Gson().fromJson(
                    JsonReader(FileReader(WireMockServerConst.GetLatest.absoluteResponsePath())),
                    ConversionRatesResponse::class.java
                )
                then shouldBe expected
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

//    @BeforeEach
//    fun setup() {
//        val mockClient = MockClient()
//
//        mockClient.ok(
//            RequestKey.builder(
//                HttpMethod.GET, "$BASE_URL/latest/{baseCode}")
//                .build(),
//            RESPONSE_EXCHANGE
//        )
//
//        val gson = Gson()
//
//        exchangeFeignClient = Feign.builder()
//            .client(ApacheHttpClient())
//            .encoder(GsonEncoder(gson))
//            .decoder(GsonDecoder(gson))
//            .target(ExchangeFeignClient::class.java, BASE_URL)
//    }

//    @Test
//    fun getLatestRatesFor(baseCode: String) {
//        val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
//
//        assert(response.isNotEmpty())
//        assert((response == "BRL").equals(1.0))
//        assert(response=="EUR").equals(0.19)
//        assert(response=="INR").equals(16.914)
//        assert(response=="USD").equals(0.2061)
//
//    }

//
////    Given("getLatestRatesFor stub - success") {
//        wireMockServer.stubGetLatestRatesFor(
//            WireMock.aResponse()
//                .withStatus(HttpStatus.OK.value())
//                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .withBodyFile(WireMockServerConst.GetLatestRatesFor.resonsePath)
//
//
////        When("feign get Latest") {
//            val then = exchangeFeignClient.getLatestExchangeFor("BRL")
//
//            Then("response") {
//                val expected: ConversionRatesResponse = Gson().fromJson(
//                    JsonReader(FileReader(WireMockServerConst.GetLatestRatesFor.absoluteResponsePath())),
//                    ConversionRatesResponse::class.java
//                )
//                then shouldBe expected
//            }
//        }
//    }

//
//    @BeforeEach
//    fun setup(wireMock: WireMockServer) {
//        val baseUrl = wireMock.baseUrl()
//        val gson = Gson()
//
//        val feignBuilder = Feign.builder()
//            .decoder(GsonDecoder(gson))
//            .target(ExchangeFeignClient::class.java, baseUrl)
//
//        exchangeFeignClient = feignBuilder
//
//    }
//
//    @Test
//    fun testGetLatestExchangeFor(wireMock: WireMockServer) {
//        val baseCode = "BRL"
//        val expectedResponse = "your_expected_response_here"
//
//        wireMock.stubFor(
//            get(urlEqualTo("/latest/$baseCode"))
//                .willReturn(
//                    aResponse()
//                        .withStatus(200)
//                        .withHeader("Content-Type", "application/json")
//                        .withBody(expectedResponse)
//                )
//        )
//
//        val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
//
//        // Assertions
//        assertEquals(expectedResponse, response)
//        verify(
//            getRequestedFor(urlEqualTo("/latest/$baseCode"))
//                .withHeader("Content-Type", equalTo("application/json"))
//        )
//        verify(
//            1,
//            getRequestedFor(urlEqualTo("/latest/$baseCode"))
//                .withHeader("Content-Type", equalTo("application/json"))
//        )
//
//    }



//    private val BASE_URL = "https://v6.exchangerate-api.com/v6/e8fd368fdb99836aaec5e272"
//    private val RESPONSE_EXCHANGE="{\n" +
//            "  \"BRL\": 1.0,\n" +
//            "  \"EUR\": 0.19,\n" +
//            "  \"INR\": 16.914,\n" +
//            "  \"USD\": 0.2061\n" +
//            "}"

    // Assertions
//    val response = exchangeFeignClient.getLatestExchangeFor(baseCode = "BRL")
//
//    assert(response.isNotEmpty())
//    assert((response == "BRL").equals(1.0))
//    assert(response=="EUR").equals(0.19)
//    assert(response=="INR").equals(16.914)
//    assert(response=="USD").equals(0.2061)