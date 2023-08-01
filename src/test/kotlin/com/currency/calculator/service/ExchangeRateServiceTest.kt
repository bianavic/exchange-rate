package com.currency.calculator.service

import com.currency.calculator.client.error.UnsupportedCodeException
import com.currency.calculator.client.feign.ExchangeFeignClient
import com.currency.calculator.client.model.ExchangeRatesResponse
import com.currency.calculator.client.model.RatesResponse
import com.currency.calculator.client.model.formatRatesToTwoDecimalPlaces
import com.currency.calculator.mock.RatesResponseMock
import io.mockk.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.boot.test.mock.mockito.MockBean

class ExchangeRateServiceTest {

    @MockBean
    private lateinit var exchangeRateService: ExchangeRateService
    private lateinit var exchangeFeignClient: ExchangeFeignClient
    private val ratesResponseMock = RatesResponseMock()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        exchangeFeignClient = mockk<ExchangeFeignClient>()
        exchangeRateService = ExchangeRateServiceImpl(exchangeFeignClient)
    }

    @Test
    fun `should return latest rates for a valid base code`() {

        val baseCode = "BRL"
        val expectedResponse = "{\"base_code\": \"$baseCode\", \"conversion_rates\": {\"BRL\": 1.0, \"USD\": 1.234, \"EUR\": 0.876, \"INR\": 17.07}}"

        every { exchangeFeignClient.getLatestExchangeFor(baseCode) } returns expectedResponse

        val ratesResponse = exchangeRateService.getLatestByBaseCode(baseCode)

        assertEquals(1.0, ratesResponse.BRL)
        assertEquals(1.23, ratesResponse.USD)
        assertEquals(0.88, ratesResponse.EUR)
        assertEquals(17.07, ratesResponse.INR)

        verify { exchangeFeignClient.getLatestExchangeFor(baseCode) }
    }

    @Test
    fun `should calculate currency conversion`() {

        val amount = 529.99
        val baseCode = "BRL"
        val rates = ratesResponseMock.getLatestMockRates()

        every { exchangeFeignClient.getLatestExchangeFor(any()) } returns getMockApiResponse(rates)
        val exchangeRateService = spyk(ExchangeRateServiceImpl(exchangeFeignClient))
        every { exchangeRateService.getLatestByBaseCode(baseCode) } returns rates
        val result = exchangeRateService.getAmountCalculated(amount)

        val expectedResponse = mapOf(
            "EUR" to (amount * rates.EUR),
            "USD" to (amount * rates.USD),
            "INR" to (amount * rates.INR)
        )
        val formattedExpectedResponse =
            expectedResponse.mapValues { (_, value) -> String.format("%.2f", value).toDouble() }

        assertEquals(formattedExpectedResponse, result)

        val responseJsonString = """
            {
                "base_code": "BRL",
                "conversion_rates": {
                    "BRL": 1.0,
                    "EUR": ${rates.EUR},
                    "USD": ${rates.USD},
                    "INR": ${rates.INR}
                }
            }
        """.trimIndent()
        val json = Json { ignoreUnknownKeys = true }
        val exchangeRatesResponse =
            json.decodeFromString(ExchangeRatesResponse.serializer(), responseJsonString)

        val expectedRatesResponse = exchangeRatesResponse.ratesResponse
        expectedRatesResponse.formatRatesToTwoDecimalPlaces(2)

        assertEquals(expectedRatesResponse, rates)
    }

    @Test
    fun `should format rates to two decimal places with scale 2`() {
        val ratesResponse = RatesResponse(
            BRL = 1.234567,
            EUR = 2.987654,
            INR = 3.6543,
            USD = 4.333333
        )

        ratesResponse.formatRatesToTwoDecimalPlaces(2)

        assertEquals(1.23, ratesResponse.BRL)
        assertEquals(2.99, ratesResponse.EUR)
        assertEquals(3.65, ratesResponse.INR)
        assertEquals(4.33, ratesResponse.USD)
    }

    @Test
    fun `should decode ExchangeRatesResponse correctly`() {

        val baseCode = "BRL"
        val mockedResponse = getMockedResponse(baseCode)
        every { exchangeFeignClient.getLatestExchangeFor(baseCode) } returns mockedResponse

        val json = Json { ignoreUnknownKeys = true }

        val response = exchangeFeignClient.getLatestExchangeFor(baseCode)
        val exchangeRatesResponse = json.decodeFromString<ExchangeRatesResponse>(response)

        val result = exchangeRatesResponse.ratesResponse

        val expectedRatesResponse = ratesResponseMock.getLatestMockRates()
        assertEquals(expectedRatesResponse, result)
    }

    @ParameterizedTest
    @ValueSource(strings = ["BRL", "EUR", "INR", "USD"])
    fun `test isValidBaseCode with valid base codes`(baseCode: String) {
        val isValid = isValidBaseCode(baseCode)
        assertEquals(true, isValid)
    }

    @ParameterizedTest
    @ValueSource(strings = ["ABC", "EEE", "XYZ", "123"])
    fun `test isValidBaseCode with invalid base codes`(baseCode: String) {
        assertThrows(UnsupportedCodeException::class.java) {
            isValidBaseCode(baseCode)
        }
    }

    private fun getMockApiResponse(rates: RatesResponse): String {
        return """
            {
                "base_code": "BRL",
                "conversion_rates": {
                    "BRL": 1.0,
                    "EUR": ${rates.EUR},
                    "USD": ${rates.USD},
                    "INR": ${rates.INR}
                }
            }
        """.trimIndent()
    }

    private fun getMockedResponse(baseCode: String): String {
        val ratesResponse = ratesResponseMock.getLatestMockRates()
        val exchangeRatesResponse = ExchangeRatesResponse(baseCode, ratesResponse)
        return Json.encodeToString(ExchangeRatesResponse.serializer(), exchangeRatesResponse)
    }

}

private fun isValidBaseCode(baseCode: String): Boolean {
    val validBaseCodes = listOf("BRL", "EUR", "INR", "USD")
    if (!validBaseCodes.contains(baseCode)) {
        throw UnsupportedCodeException("Unsupported currency code: $baseCode")
    }
    return true
}