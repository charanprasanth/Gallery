package com.xlorit.gallery.feature_gallery.domain.repository

import android.net.Uri
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun uploadMedia(fileUri: Uri): Flow<Resource<MediaItem>>
}
