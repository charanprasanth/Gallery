package com.xlorit.gallery.feature_gallery.domain.usecase

import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.core.data.UseCase
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) : UseCase<GetMediaUseCase.Params, Flow<Resource<List<MediaItem>>>>() {

    data class Params(val text: String)

    override suspend fun invoke(params: Params): Flow<Resource<List<MediaItem>>> {
        return mediaRepository.getUserMedia()
    }
}
