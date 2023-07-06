package com.currency.calculator.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean

@TestConfiguration
class WireMockServerConfig: ApplicationListener<ApplicationReadyEvent> {

    @Bean
    fun wireMockServer(): WireMockServer = WireMockServer(9999)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        wireMockServer().start()
    }

}
