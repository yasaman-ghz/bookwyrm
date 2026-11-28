
package com.example.bookwyrm

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookwyrm.ui.screens.MapScreen
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookwyrm.ui.theme.CharcoalGrey
import com.example.bookwyrm.ui.theme.EspressoBrown
import com.example.bookwyrm.ui.theme.MutedSage
import com.example.bookwyrm.ui.theme.WarmTan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(mainViewModel: MainViewModel = viewModel()) {

    val context = LocalContext.current
    val userLocation by mainViewModel.userLocation.collectAsState()

    var granted by remember { mutableStateOf(false) }

    // Request location permissions
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {  // if granted, fetches device location into the MainViewModel
            mainViewModel.fetchDeviceLocation(context)
        }
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
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmTan
                )
            )
        },

        // Bottom navigation bar containing the Map/Location + Favorites icon
        bottomBar = {
            NavigationBar(
                containerColor = WarmTan
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map"
                        )
                    },
                    label = {
                        Text(
                            text = "Map",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EspressoBrown,
                        selectedTextColor = EspressoBrown,
                        indicatorColor = Color.White,
                        unselectedIconColor = CharcoalGrey,
                        unselectedTextColor = CharcoalGrey
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites"
                        )
                    },
                    label = {
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = EspressoBrown,
                        selectedTextColor = EspressoBrown,
                        indicatorColor = Color.White,
                        unselectedIconColor = CharcoalGrey,
                        unselectedTextColor = CharcoalGrey
                    )
                )
            }
        },

        // Background color of the main screen
        containerColor = Color(0xFFF5F5F5)

    ) { innerPadding ->

        // Area between the navigation bars
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (selectedTab) {
                1 -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "meow",
                        style = MaterialTheme.typography.titleLarge,
                        color = EspressoBrown
                    )
                }

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        //.verticalScroll(rememberScrollState())  // This part doesn't let the map be easily scrollable
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "HELLO, BOOKWYRMS!",
                        style = MaterialTheme.typography.labelMedium,
                        color = EspressoBrown
                    )
                    Text(
                        text = "Browse book spots",
                        style = MaterialTheme.typography.titleLarge,
                        color = EspressoBrown
                    )

                    // Embedded Mapbox map inside the rounded box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        MapScreen(
                            modifier = Modifier.fillMaxSize(),
                            granted = granted,
                            userLocation = userLocation  // passing into the map screen
                        )
                    }

                    // This week's recommendations header box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(CharcoalGrey)
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CharcoalGrey)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "See our recommended spots below!",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }

                    // Card placeholders
                    val recommendations = listOf(
                        "Spot 1",
                        "Spot 2",
                        "Spot 3",
                        "Spot 4",
                        "Spot 5"
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recommendations) { spotName ->
                            Box(
                                modifier = Modifier
                                    .width(170.dp)
                                    .height(110.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MutedSage)
                                    .padding(12.dp),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                Text(
                                    text = spotName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}