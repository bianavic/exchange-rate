package com.currency.calculator.wiremock

object WireMockServerConst {

    const val port = 9999
    const val rootResponsePath = "src/test/resources/__files/"

            object GetLatest {
                const val url = "/latest/BRL"
                const val responsePath = "payload/getLatest.json"

                fun absoluteResponsePath() : String = rootResponsePath + responsePath

            }

}