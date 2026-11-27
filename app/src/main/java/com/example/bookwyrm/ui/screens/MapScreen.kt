package com.example.bookwyrm.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.Marker
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapScreen(modifier: Modifier = Modifier, granted: Boolean) {


    val viewport = rememberMapViewportState()

    MapboxMap(
        modifier = modifier.fillMaxSize(),
        mapViewportState = viewport
    ) {
        MapEffect(granted) { mapView ->
            if (granted) {
                mapView.location.updateSettings { enabled = true }
                viewport.transitionToFollowPuckState()
            }
        }

        Marker(
            point = Point.fromLngLat(-63.6875859018569, 44.7684185607905),
            text = "Cape and Cowl Comics and Collectibles"
        )
    }
}