package com.xlorit.gallery.feature_gallery.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xlorit.gallery.R

@Composable
fun GalleryScreen() {
    val imageList = listOf(
        R.drawable.test1,
        R.drawable.test2,
        R.drawable.test3,
        R.drawable.test4,
        R.drawable.test5,
        R.drawable.test6
    )

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount =
        (screenWidthDp / 150.dp).toInt().coerceAtLeast(2) // Adjusting column size dynamically

    Scaffold(
        topBar = {
            Text(
                "Gallery",
                fontSize = 25.sp,
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 16.dp)
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount), // Dynamically adjusting columns
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(imageList) { imageRes ->
                ImageItem(imageRes)
            }
        }
    }
}

@Composable
fun ImageItem(imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(name = "Phone Portrait", device = Devices.PHONE)
@Preview(name = "Phone Landscape", widthDp = 720, heightDp = 360)
@Preview(name = "Tablet Portrait", widthDp = 800, heightDp = 1280)
@Preview(name = "Tablet Portrait", device = Devices.TABLET)
@Composable
private fun GalleryScreenPreview() {
    GalleryScreen()
}