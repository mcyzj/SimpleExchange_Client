package com.mcyzj.exchange.client.driver

import java.io.ByteArrayOutputStream
import javax.sound.sampled.*


/**
 * @author admin
 */
class VoiceDriver {
    private var recordingAudioFormat: AudioFormat? = null
    private var recordingTargetDataLine: TargetDataLine? = null
    private var bass: ByteArrayOutputStream? = ByteArrayOutputStream()

    private var playAudioFormat: AudioFormat? = null
    private var sourceDataLine: SourceDataLine? = null

    init {
        // 8000,11025,16000,22050,44100
        val sampleRate = 22050f
        // 8,16
        val sampleSizeInBits = 16
        // 1,2
        val channels = 1
        // true,false
        val signed = true
        // true,false
        val bigEndian = false
        // end getAudioFormat
        val encoding = AudioFormat.Encoding.PCM_SIGNED
        val signedString = "signed"
        recordingAudioFormat = AudioFormat(
            encoding, sampleRate, sampleSizeInBits, channels,
            sampleSizeInBits / 8 * channels, sampleRate, bigEndian
        )
        recordingTargetDataLine = AudioSystem.getTargetDataLine(recordingAudioFormat)
        recordingTargetDataLine!!.open()
        recordingTargetDataLine!!.start()

        playAudioFormat = AudioFormat(22050f, 16, 1, true, false)
        val info = DataLine.Info(SourceDataLine::class.java, playAudioFormat)
        sourceDataLine = AudioSystem.getLine(info) as SourceDataLine
        sourceDataLine!!.open(playAudioFormat)
        sourceDataLine!!.start()
    }

    fun getAudioByteArray(size: Int = 1024): ByteArray? {
        return try {
            val data = ByteArray(size) // 接收的数据
            val bytesRead = recordingTargetDataLine!!.read(data, 0, data.size)
            val finalData = ByteArray(bytesRead)
            data.inputStream().read(finalData)
            finalData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sentAudioByteArray(data: ByteArray) {
        sourceDataLine!!.write(data, 0, data.size)
        sourceDataLine!!.start()
    }
}


