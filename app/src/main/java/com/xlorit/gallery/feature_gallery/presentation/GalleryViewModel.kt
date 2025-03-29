package com.xlorit.gallery.feature_gallery.presentation

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.usecase.GetMediaUseCase
import com.xlorit.gallery.feature_gallery.domain.usecase.UploadMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val getMediaUseCase: GetMediaUseCase
) : ViewModel() {

    private val _selectedFile = MutableStateFlow<Uri?>(null)
    val selectedFile: StateFlow<Uri?> = _selectedFile

    private val _uploadState = MutableStateFlow<Resource<MediaItem>?>(null)
    val uploadState: StateFlow<Resource<MediaItem>?> = _uploadState

    private val _mediaItems = MutableStateFlow<Resource<List<MediaItem>>>(Resource.Loading())
    val mediaItems: StateFlow<Resource<List<MediaItem>>> = _mediaItems

    init {
        fetchMedia()
    }

    fun selectFile(uri: Uri) {
        _selectedFile.value = uri
    }

    fun uploadMedia(context: Context) {
        val fileUri = _selectedFile.value ?: return

        val mimeType = context.contentResolver.getType(fileUri)

        val type = when {
            mimeType?.startsWith("video") == true -> "video"
            mimeType?.startsWith("image") == true -> "image"
            else -> "unknown"
        }

        viewModelScope.launch {
            uploadMediaUseCase.invoke(UploadMediaUseCase.Params(fileUri, type))
                .collect { resource ->
                    _uploadState.value = resource
                    if (resource is Resource.Success) {
                        _selectedFile.value = null
                    }
                }
        }
    }

    private fun fetchMedia() {
        viewModelScope.launch {
            getMediaUseCase.invoke(GetMediaUseCase.Params("")).collect { result ->
                _mediaItems.value = result
            }
        }
    }
}
