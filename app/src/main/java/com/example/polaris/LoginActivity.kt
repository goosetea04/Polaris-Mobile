package com.example.polaris

import android.content.Intent
import android.os.Bundle
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private val testUsername = "supplier"
    private val testPassword = "supplier123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loginLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }

        // Create rounded background for EditText
        val editTextBackground = GradientDrawable().apply {
            setColor(ContextCompat.getColor(this@LoginActivity, android.R.color.white))
            setStroke(2, ContextCompat.getColor(this@LoginActivity, android.R.color.darker_gray))
            cornerRadius = 16f
        }

        // Create rounded background for Button
        val buttonBackground = GradientDrawable().apply {
            setColor(ContextCompat.getColor(this@LoginActivity, R.color.purple_700))
            cornerRadius = 16f
        }

        val usernameInput = EditText(this).apply {
            hint = getString(R.string.hint_username)
            textSize = 16f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setPadding(32, 24, 32, 24)
            background = editTextBackground

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
        }

        val passwordInput = EditText(this).apply {
            hint = getString(R.string.hint_password)
            textSize = 16f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            setPadding(32, 24, 32, 24)
            background = editTextBackground

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 24)
            }
        }

        val loginButton = Button(this).apply {
            text = getString(R.string.button_login)
            textSize = 18f
            typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
            isAllCaps = false
            background = buttonBackground
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            elevation = 4f

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                height = 160 // Fixed height for the button
                setMargins(0,32,0,0)
            }

            setOnClickListener {
                handleLogin(usernameInput.text.toString(), passwordInput.text.toString())
            }
        }

        loginLayout.apply {
            addView(usernameInput)
            addView(passwordInput)
            addView(loginButton)
        }

        setContentView(loginLayout)
    }

    private fun handleLogin(username: String, password: String) {
        if (username == testUsername && password == testPassword) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("isLoggedIn", true)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, getString(R.string.toast_login_failed),
                Toast.LENGTH_LONG).show()
        }
    }
}