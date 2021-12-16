package com.example.musicplayer.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.musicplayer.R
import com.example.musicplayer.databinding.FragmentHomeBinding
import com.example.musicplayer.extensions.currentSeconds
import com.example.musicplayer.extensions.seconds
import com.example.musicplayer.utils.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate){

    private var mediaPlayer: MediaPlayer? = null
    private var currentSong: MutableList<Int> = mutableListOf(R.raw.sound)

    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    private val seekForwardTime = 5000
    private val seekBackwardTime = 5000

    companion object {
        const val SECOND = 1000
    }

    override fun startCreating(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init(){
        controlSound(currentSong[0])
    }

   private fun controlSound(id: Int){
        binding.playBtn.setOnClickListener {
            if(mediaPlayer==null){
                mediaPlayer = MediaPlayer.create(requireContext(), id)

                initialSeekBar()

            }
            mediaPlayer?.start()

            updateSeekBar()
        }

       binding.pauseBtn.setOnClickListener {
           if(mediaPlayer!=null){
               mediaPlayer?.pause()
           }
       }

       binding.stopBtn.setOnClickListener {
           mediaPlayer?.stop()
           mediaPlayer?.prepare()
           mediaPlayer?.start()
       }

       binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
           override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               if(fromUser){
                   mediaPlayer?.seekTo(progress * SECOND)
               }
           }

           override fun onStartTrackingTouch(seekBar: SeekBar?) {
           }

           override fun onStopTrackingTouch(seekBar: SeekBar?) {
           }

       })

       binding.rightSkipBtn.setOnClickListener {

           val currentPosition: Int? = mediaPlayer?.currentPosition
           if (currentPosition != null) {
               if (currentPosition + seekForwardTime <= mediaPlayer?.duration!!) {
                   mediaPlayer?.seekTo(currentPosition + seekForwardTime)
               } else {
                   mediaPlayer?.duration?.let { it1 -> mediaPlayer?.seekTo(it1) }
               }
           }
       }

       binding.leftSkipBtn.setOnClickListener {
           val currentPosition: Int? = mediaPlayer?.currentPosition
           if (currentPosition != null) {
               if (currentPosition - seekBackwardTime >= 0) {
                   mediaPlayer?.seekTo(currentPosition - seekBackwardTime)
               } else {
                   mediaPlayer?.seekTo(0)
               }
           }
       }
   }

    private fun timeInString(seconds: Int): String {
        return String.format(
            "%02d:%02d",
            (seconds / 3600 * 60 + ((seconds % 3600) / 60)),
            (seconds % 60)
        )
    }

    private fun initialSeekBar(){

        binding.seekBar.max = mediaPlayer!!.seconds()
        binding.textProgress.text = getString(R.string.default_value)
        binding.textTotalTime.text = timeInString(mediaPlayer!!.seconds())
        binding.playBtn.isEnabled = true

    }

    private fun updateSeekBar() {
        runnable = Runnable {

            binding.textProgress.text = timeInString(mediaPlayer!!.currentSeconds())
            binding.seekBar.progress = mediaPlayer!!.currentSeconds()
            handler.postDelayed(runnable, SECOND.toLong())
        }
        handler.postDelayed(runnable, SECOND.toLong())
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        mediaPlayer?.release()
    }
}