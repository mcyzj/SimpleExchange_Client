package com.mcyzj.exchange.client

import com.mcyzj.exchange.client.driver.DriverManager
import com.mcyzj.exchange.client.socket.ConnectData
import com.mcyzj.exchange.client.socket.TCPClient
import com.mcyzj.exchange.client.voice.VoiceChat
import org.json.simple.JSONObject

class SimpleExchangeClient {
    companion object {
        lateinit var instance: SimpleExchangeClient
        lateinit var voiceChat: VoiceChat
        var inStart: Boolean = false
        var start: Boolean = false

        lateinit var mainClient: TCPClient
        lateinit var localConnect: ConnectData
    }

    fun start() {
        if ((start).or(inStart)) {
            println("SimpleExchange已经初始化了！")
            return
        }
        inStart = true
        instance = this

        println("SimpleExchange注册硬件抽象驱动")
        DriverManager.refuseDriver()

        println("SimpleExchange连接服务器")
        mainClient = TCPClient("zh-sh1.mcyzj.cn", 10001)
        refuseBasicInfo()

        println("SimpleExchange启动语音聊天模块")
        voiceChat = VoiceChat("zh-sh1.mcyzj.cn", 10001)

        println("SimpleExchange完成启动")

        inStart = false
        start = true
    }

    fun refuseBasicInfo() {
        //获取服务器视角的本地地址
        var json = JSONObject()
        json["type"] = "connect"
        json["token"] = "Tree0123"
        mainClient.send(json.toString().toByteArray())
    }
}