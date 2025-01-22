package com.example.proiectpiu_managementfinanciar.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.proiectpiu_managementfinanciar.R

class StartActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        initializeViews()
        setListeners()
    }

    private fun setListeners() {
        startButton.setOnClickListener(this)
    }

    private fun initializeViews() {
        startButton = findViewById(R.id.start_button)
    }

    override fun onClick(p0: View) {
        if (p0.id == R.id.start_button) {
            val intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }
}
