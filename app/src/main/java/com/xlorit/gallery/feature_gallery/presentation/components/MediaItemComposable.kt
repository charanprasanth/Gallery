package com.xlorit.gallery.feature_gallery.presentation.components

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.xlorit.gallery.R
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MediaItemComposable(mediaItem: MediaItem) {
    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(mediaItem.url) {
        if (mediaItem.type == "video") {
            thumbnail = getVideoThumbnail(mediaItem.url)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(color = Color.White),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (mediaItem.type == "image") {
            Image(
                painter = rememberAsyncImagePainter(model = mediaItem.url),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                thumbnail?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Video Thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_play),
                    contentDescription = "Play Video",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                        .padding(12.dp)
                )
            }
        }
    }
}

suspend fun getVideoThumbnail(videoUrl: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoUrl, HashMap()) // Required for remote URLs

            // Get video duration in milliseconds
            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    ?.toLongOrNull() ?: 0L

            // Calculate center frame timestamp (in microseconds)
            val centerFrameTime = (duration / 2) * 1000

            // Extract the center frame
            val bitmap = retriever.getFrameAtTime(
                centerFrameTime,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )
            retriever.release()

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
