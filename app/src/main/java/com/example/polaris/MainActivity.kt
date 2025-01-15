package com.example.polaris

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PolygonAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPolygonAnnotationManager

class MainActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var polygonAnnotationManager: PolygonAnnotationManager
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)

        // Main Layout
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }



        // Header Layout
        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setPadding(16, 32, 16, 16)
            }
        }

        // Back Button
        val backButton = ImageButton(this).apply {
            setImageResource(R.drawable.ic_back) // Make sure to add this icon
            background = null
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginEnd = 16
            }
            setOnClickListener { onBackPressed() }
        }

        // Title
        val titleText = TextView(this).apply {
            text = "Route Planner"
            textSize = 20f
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            setPadding(0,8,0,0)
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            ).apply {
                setMargins(0,8,0,0)
            }
        }

        // Menu Button
        val menuButton = ImageButton(this).apply {
            setImageResource(if (!isLoggedIn) R.drawable.ic_menu else R.drawable.ic_back) // Make sure to add this icon
            background = null
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnClickListener {
                if (!isLoggedIn) {
                    // Launch LoginActivity
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    handleLogout()
                }
            }
        }

        // Search Bar
        val searchLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            background = ContextCompat.getDrawable(context, R.drawable.search_background)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 16, 16)
                setPadding(16, 12, 16, 12)
            }
        }

        val searchInput = EditText(this).apply {
            hint = "Search specific location"
            background = null
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
        }

        val mapButton = ImageButton(this).apply {
            setImageResource(R.drawable.ic_map) // Make sure to add this icon
            background = null
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Create map view with custom style
        mapView = MapView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1.0f
            )
        }

        // Set custom map style
        mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) {
            // Custom map styling can be added here
        }

        mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-95.0, 42.5))
                .pitch(0.0)
                .zoom(15.0)
                .bearing(0.0)
                .build()
        )

        // Initialize polygon annotation manager
        polygonAnnotationManager = mapView.annotations.createPolygonAnnotationManager()

        // Assemble the layouts
        headerLayout.apply {
            addView(backButton)
            addView(titleText)
            addView(menuButton)
        }

        searchLayout.apply {
            addView(searchInput)
            addView(mapButton)
        }

        mainLayout.apply {
            addView(headerLayout)
            addView(searchLayout)
            addView(mapView)
        }

        setContentView(mainLayout)

        showDefaultDangerZones()
    }

    private fun handleLogout() {
        isLoggedIn = false
        // Reset the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showDefaultDangerZones() {
        // Sample danger zones
        val dangerZones = listOf(
            // Example 1: A triangle in North America
            listOf(
                Point.fromLngLat(-100.0, 40.0),
                Point.fromLngLat(-95.0, 45.0),
                Point.fromLngLat(-90.0, 40.0),
                Point.fromLngLat(-100.0, 40.0),
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
                .withPoints(listOf(points))  // points is a list of coordinates
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