package com.example.bookwyrm.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.Marker
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    granted: Boolean,
    userLocation: Point?  // can be null
) {
    val viewport = rememberMapViewportState()

    LaunchedEffect(userLocation) {
        userLocation?.let { point ->
            viewport.flyTo(
                cameraOptions = CameraOptions.Builder()
                    .center(point)
                    .zoom(12.0)  // city-wide zoom
                    .pitch(50.0) // tilted perspective angle
                    .build()
            )
        }
    }

    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = viewport,
    ) {
        MapEffect(granted) { mapView ->
            if (granted) {
                mapView.location.updateSettings { enabled = true }
            }
        }

        Marker(
            point = Point.fromLngLat(-63.6875859018569, 44.7684185607905),
            text = "Cape and Cowl Comics and Collectibles"
        )
    }
}
