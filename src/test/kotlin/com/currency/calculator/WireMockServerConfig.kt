package com.currency.calculator

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WireMockServerConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun wireMockServer(): WireMockServer {
        val wireMockServer = WireMockServer(8001)
        return wireMockServer
    }
}
