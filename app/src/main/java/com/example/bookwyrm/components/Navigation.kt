
package com.example.bookwyrm

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.bookwyrm.ui.screens.MapScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {

    var granted by remember { mutableStateOf(false) }

    // Request location permissions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permissions ->
        granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


    // Tracks which screen is currently displayed
    var showMap by remember {
        mutableStateOf(false)
    }

    Scaffold(

        // Top navigation bar displaying the app name
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BookWyrm",
                        fontSize = 22.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF263238),
                    titleContentColor = Color.White
                )
            )
        },

        // Bottom navigation bar containing the Map icon
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF263238)
            ) {
                NavigationBarItem(
                    selected = showMap,
                    onClick = {
                        showMap = !showMap
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Open Map"
                        )
                    },
                    label = {
                        Text("Map")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = Color(0xFF455A64),
                        unselectedIconColor = Color.LightGray,
                        unselectedTextColor = Color.LightGray
                    )
                )
            }
        },

        // Background color of the main screen
        containerColor = Color(0xFFF5F5F5)

    ) { innerPadding ->

        // Main content area between the navigation bars
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (showMap) {

                // Display MapScreen when the Map icon is selected
                MapScreen(Modifier, granted)

            } else {

                // Initial BookWyrm home screen
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Home screen content goes here
                }
            }
        }
    }
}
