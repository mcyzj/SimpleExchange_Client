package com.mcyzj.exchange.client.voice

import com.mcyzj.exchange.client.driver.DriverManager
import com.mcyzj.exchange.client.socket.ConnectData
import com.mcyzj.exchange.client.socket.SocketManager
import com.mcyzj.exchange.client.socket.UDPClient
import java.lang.Thread.sleep

class VoiceChat(val host: String, val port: Int) {
    companion object {
        var recording = true
        var play = true
        val client = HashMap<String, UDPClient>()
        lateinit var serverClient: UDPClient
        //lateinit var checkThread: Thread
        //var isCheckStart = false
        lateinit var sendThread: Thread
        var isSendStart = false
        val receiveThread = HashMap<String, Thread>()
    }

    init {
        println("初始化语音聊天模块")
        println("初始化语音聊天传输流")
        serverClient = SocketManager.createUDPClient(host, port)
        client["server"] = serverClient
        //checkThread = Thread{
            //check(30)
        //}
        println("初始化语音聊天传输流守护进程")
        //checkThread.start()
        println("初始化语音聊天音频发送线程")
        sendThread = Thread{
            chatSend()
        }
        sendThread.start()
        println("初始化语音聊天音频接收线程")
        Thread{
            chatReceive("server", serverClient)
        }.start()
    }

    /*private fun check(time: Int){
        if (isCheckStart) {
            return
        }
        isCheckStart = true
        try {
            while (true) {
                println("检查语音聊天传输流是否存活，当前频率${time}秒进行一次检查")
                if (client.isclose) {
                    client = SocketManager.createUDPClient(host, port)
                    println("语音聊天传输流中断，尝试重新初始化")
                }
                sleep((time * 1000).toLong())
            }
        } catch (e: Exception) {
            isCheckStart = false
            e.printStackTrace()
            check(time)
        }
    }

     */

    private fun sendVoice(data: ByteArray, receiver: UDPClient) {
        Thread {
            receiver.send(data)
        }.start()
    }

    private fun chatSend(){
        if (isSendStart) {
            return
        }
        isSendStart = true
        try {
            while (true) {
                if (recording) {
                    val data = DriverManager.voiceDriver.getAudioByteArray()
                    if (data == null) {
                        println("获取麦克风数据失败，请检查录音设备")
                        println("3秒后将自动重启录音")
                        sleep(3000)
                        continue
                    }
                    for (receiver in client.values) {
                        sendVoice(data, receiver)
                    }
                }
            }
        } catch (e: Exception) {
            isSendStart = false
            e.printStackTrace()
            println("语音聊天音频发送出错啦！")
            println("3秒后将自动重启模块")
            sleep(3000)
            chatSend()
        }
    }

    private fun chatReceive(name: String ,sender: UDPClient) {
        if (name in receiveThread.keys) {
            receiveThread[name]!!.stop()
            receiveThread.remove(name)
        }
        val receive = Thread {
            try {
                while (true) {
                    if (play) {
                        val data = sender.receive()
                        if (data == null) {
                            println("获取音频数据失败，请检查网络连接是否顺畅")
                            println("3秒后将自动重启音频接受")
                            sleep(3000)
                            continue
                        }
                        Thread {
                            DriverManager.voiceDriver.sentAudioByteArray(data)
                        }.start()
                    }
                }
            } catch (e: Exception) {
                receiveThread.remove(name)
                e.printStackTrace()
                println("语音聊天音频接收出错啦！")
                println("3秒后将自动重启模块")
                sleep(3000)
                chatReceive(name, sender)
            }
        }
        receiveThread[name] = receive
        receive.start()
    }
}