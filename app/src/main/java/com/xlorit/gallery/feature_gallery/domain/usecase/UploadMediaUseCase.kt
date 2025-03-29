package com.xlorit.gallery.feature_gallery.domain.usecase

import android.net.Uri
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.core.data.UseCase
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UploadMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) : UseCase<UploadMediaUseCase.Params, Flow<Resource<MediaItem>>>() {

    data class Params(val fileUri: Uri, val type: String)

    override suspend fun invoke(params: Params): Flow<Resource<MediaItem>> {
        return mediaRepository.uploadMedia(params.fileUri, params.type)
    }
}
