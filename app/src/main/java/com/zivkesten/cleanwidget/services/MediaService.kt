package com.zivkesten.cleanwidget.services

import android.content.Context
import android.media.MediaPlayer

object MediaService {
    var mediaPlayer: MediaPlayer? = null

    fun playOrStopSound(context: Context, assetPath: String) {
        // Check if mediaPlayer is playing
        if (mediaPlayer?.isPlaying == true) {
            // If playing, stop and release the player
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } else {
            // If not playing, prepare a new MediaPlayer instance and play the sound
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

            // Set up MediaPlayer cleanup on completion
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
        }
    }
}