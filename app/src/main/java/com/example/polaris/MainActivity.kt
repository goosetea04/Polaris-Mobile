package com.example.polaris

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var polygonAnnotationManager: PolygonAnnotationManager
    private var isLoggedIn = false

    // Test credentials
    private val testUsername = "supplier"
    private val testPassword = "supplier123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Main Layout
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        // Create login header
        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
        }

        val usernameInput = EditText(this).apply {
            hint = getString(R.string.hint_username)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
        }

        val passwordInput = EditText(this).apply {
            hint = getString(R.string.hint_password)
            inputType = android.text.InputType.TYPE_CLASS_TEXT or
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
        }

        val loginButton = Button(this).apply {
            text = getString(if (!isLoggedIn) R.string.button_login else R.string.button_logout)
            setOnClickListener {
                if (!isLoggedIn) {
                    handleLogin(usernameInput.text.toString(), passwordInput.text.toString())
                } else {
                    handleLogout()
                }
            }
        }

        // Add views to header
        headerLayout.addView(usernameInput)
        headerLayout.addView(passwordInput)
        headerLayout.addView(loginButton)

        // Create map view
        mapView = MapView(this)
        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-100.0, 39.5))
                .pitch(0.0)
                .zoom(13.0)
                .bearing(0.0)
                .build()
        )

        // Initialize polygon annotation manager
        polygonAnnotationManager = mapView.annotations.createPolygonAnnotationManager()

        // Add views to main layout
        mainLayout.addView(headerLayout)
        mainLayout.addView(mapView)

        setContentView(mainLayout)

        // Show default danger zones
        showDefaultDangerZones()
    }

    private fun handleLogin(username: String, password: String) {
        if (username == testUsername && password == testPassword) {
            isLoggedIn = true
            Toast.makeText(this, getString(R.string.toast_login_success), Toast.LENGTH_SHORT).show()
            // Update UI elements post-login
            findViewById<Button>(0).text = getString(R.string.button_logout)
        } else {
            Toast.makeText(this, getString(R.string.toast_login_failed),
                Toast.LENGTH_LONG).show()
        }
    }

    private fun handleLogout() {
        isLoggedIn = false
        Toast.makeText(this, getString(R.string.toast_logout_success), Toast.LENGTH_SHORT).show()
        findViewById<Button>(0).text = getString(R.string.button_login)
    }

    private fun showDefaultDangerZones() {
        // Sample danger zones
        val dangerZones = listOf(
            // Example 1: A triangle in North America
            listOf(
                Point.fromLngLat(-100.0, 40.0),
                Point.fromLngLat(-95.0, 45.0),
                Point.fromLngLat(-90.0, 40.0),
                Point.fromLngLat(-100.0, 40.0)  // Close the polygon
            ),
            // Example 2: A rectangle in South America
            listOf(
                Point.fromLngLat(-60.0, -10.0),
                Point.fromLngLat(-55.0, -10.0),
                Point.fromLngLat(-55.0, -15.0),
                Point.fromLngLat(-60.0, -15.0),
                Point.fromLngLat(-60.0, -10.0)  // Close the polygon
            )
        )

        // Add each danger zone to the map
        dangerZones.forEach { points ->
            val polygonAnnotationOptions = PolygonAnnotationOptions()
                .withPoints(listOf(points))  // Mapbox expects a list of list of points
                .withFillColor("#FF0000")    // Red color
                .withFillOpacity(0.5)

            polygonAnnotationManager.create(polygonAnnotationOptions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        polygonAnnotationManager.deleteAll()
    }
}