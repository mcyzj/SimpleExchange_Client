package com.mcyzj.exchange.client.socket

object SocketManager {
    fun createUDPClient(host: String, port: Int): UDPClient {
        return UDPClient(host, port)
    }
}