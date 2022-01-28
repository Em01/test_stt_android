package com.emma.testtts

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var btn_stt : Button
    lateinit var et_text_input : EditText


    //The intent holds the recognized text

    companion object {
        private const val REQUEST_CODE_STT = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_stt = findViewById(R.id.btn_stt)


        btn_stt.setOnClickListener {
            val sttIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            //use the langauge model to define the purpose.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            //Any langauge can be used from the locale class.
            sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            sttIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something now!")

            try {
                getResult.launch(sttIntent)
//                startActivityForResult(sttIntent, REQUEST_CODE_STT)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this, "Device does not support stt.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data !== null) {
            val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            if(!result.isNullOrEmpty()) {
                val recognizedText = result[0]
                et_text_input = findViewById(R.id.et_text_input)
                et_text_input.setText(recognizedText)
            }
        }
    }


}