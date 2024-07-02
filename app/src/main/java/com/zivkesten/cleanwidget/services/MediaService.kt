package com.zivkesten.cleanwidget.services

import android.content.Context
import android.media.MediaPlayer

object MediaService {
    var mediaPlayer: MediaPlayer? = null
    private var currentSoundDuration: Long = 0L  // Duration of the current sound

    fun playOrStopSound(context: Context, assetPath: String): Long {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
            currentSoundDuration = 0L
            return 0L  // Return 0 when stopping the sound
        } else {
            val assetFileDescriptor = context.assets.openFd(assetPath)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                prepare()
                start()
            }
            currentSoundDuration = mediaPlayer?.duration?.toLong() ?: 0L  // Get the duration in milliseconds
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
                currentSoundDuration = 0L
            }
            return currentSoundDuration
        }
    }
}