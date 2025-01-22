package com.example.proiectpiu_managementfinanciar.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proiectpiu_managementfinanciar.R

class SelectionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var adolescentRadioButton: RadioButton
    private lateinit var parentRadioButton: RadioButton
    private lateinit var registerButton: View
    private lateinit var authenticateButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        initializeViews()
        setListeners()
    }

    private fun initializeViews() {
        adolescentRadioButton = findViewById(R.id.adolescent_radio)
        parentRadioButton = findViewById(R.id.parent_radio)
        registerButton = findViewById(R.id.register_button)
        authenticateButton = findViewById(R.id.authenticate_button)
    }

    private fun setListeners() {
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            val selectedRole = when {
                adolescentRadioButton.isChecked -> "Adolescent"
                parentRadioButton.isChecked -> "Parent"
                else -> {
                    Toast.makeText(this, getString(R.string.select_account_type), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            intent.putExtra("USER_TYPE", selectedRole)
            startActivity(intent)
        }



        authenticateButton.setOnClickListener {
            val intent = Intent(this, AuthenticationActivity::class.java)
            if (adolescentRadioButton.isChecked) {
                intent.putExtra("USER_TYPE", "Adolescent")
            } else if (parentRadioButton.isChecked) {
                intent.putExtra("USER_TYPE", "Parent")
            } else {
                Toast.makeText(this, getString(R.string.select_account_type), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            startActivity(intent)
        }
        findViewById<View>(R.id.adolescent_option).setOnClickListener(this)
        findViewById<View>(R.id.parent_option).setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.adolescent_option -> {
                if (adolescentRadioButton.isChecked) {
                    adolescentRadioButton.isChecked = false
                    Toast.makeText(this, getString(R.string.adolescent_deselected), Toast.LENGTH_SHORT).show()
                } else {
                    adolescentRadioButton.isChecked = true
                    parentRadioButton.isChecked = false
                    Toast.makeText(this, getString(R.string.adolescent_selected), Toast.LENGTH_SHORT).show()
                }
            }
            R.id.parent_option -> {
                if (parentRadioButton.isChecked) {
                    parentRadioButton.isChecked = false
                    Toast.makeText(this, getString(R.string.parent_deselected), Toast.LENGTH_SHORT).show()
                } else {
                    parentRadioButton.isChecked = true
                    adolescentRadioButton.isChecked = false
                    Toast.makeText(this, getString(R.string.parent_selected), Toast.LENGTH_SHORT).show()
                }
            }
            R.id.register_button -> {
                if (!adolescentRadioButton.isChecked && !parentRadioButton.isChecked) {
                    Toast.makeText(this, getString(R.string.select_account_type), Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.authenticate_button -> {
                val intent = Intent(this, AuthenticationActivity::class.java)
                if (adolescentRadioButton.isChecked) {
                    intent.putExtra("USER_TYPE", "Adolescent")
                } else if (parentRadioButton.isChecked) {
                    intent.putExtra("USER_TYPE", "Parent")
                } else {
                    Toast.makeText(this, getString(R.string.select_account_type), Toast.LENGTH_SHORT).show()
                    return
                }
                startActivity(intent)
            }
        }
    }
}
