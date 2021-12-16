package com.example.musicplayer.extensions

import android.media.MediaPlayer
import com.example.musicplayer.ui.HomeFragment

fun MediaPlayer.seconds(): Int {
    return this.duration / HomeFragment.SECOND
}

fun MediaPlayer.currentSeconds(): Int{
    return this.currentPosition / HomeFragment.SECOND
}