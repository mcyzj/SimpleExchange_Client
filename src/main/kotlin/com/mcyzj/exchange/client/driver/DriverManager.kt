package com.mcyzj.exchange.client.driver

object DriverManager {
    lateinit var voiceDriver: VoiceDriver

    fun refuseDriver() {
        voiceDriver = VoiceDriver()
    }
}