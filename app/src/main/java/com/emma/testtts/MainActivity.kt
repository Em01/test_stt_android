package com.emma.testtts

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer

import android.speech.RecognizerIntent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    lateinit var speechRecognizer: SpeechRecognizer

    lateinit var micButton: ImageView
    lateinit var editText: EditText

    //The intent holds the recognized text

    companion object {
        private const val REQUEST_CODE_STT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission()
        }

        editText = findViewById(R.id.text);
        micButton = findViewById(R.id.button);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something now!")

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onBeginningOfSpeech() {
                editText.setText("")
                editText.setHint("Listening")
            }

            override fun onPartialResults(partialResults: Bundle?) {

            }

            override fun onResults(results: Bundle?) {
                micButton.setImageResource(R.drawable.ic_mic_off)
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) ?: arrayListOf()
                editText.setText(data[0])
            }

            override fun onEvent(eventType: Int, params: Bundle?) {

            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onRmsChanged(rmsdB: Float) {
            }
        })

        micButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        speechRecognizer.stopListening()
                        editText.hint = "You will see the speech here"
                    }
                    MotionEvent.ACTION_DOWN -> {
                        micButton.setImageResource(R.drawable.ic_mic)
                        editText.hint = "Listening..."
                        speechRecognizer.startListening(sttIntent)
                    }
                }
                return false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_STT
            )
        }
    }
}