package com.xlorit.gallery.feature_gallery.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.usecase.UploadMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val uploadMediaUseCase: UploadMediaUseCase
) : ViewModel() {

    private val _selectedFile = MutableStateFlow<Uri?>(null)
    val selectedFile: StateFlow<Uri?> = _selectedFile

    private val _uploadState = MutableStateFlow<Resource<MediaItem>?>(null)
    val uploadState: StateFlow<Resource<MediaItem>?> = _uploadState

    fun selectFile(uri: Uri) {
        _selectedFile.value = uri
    }

    fun uploadMedia() {
        val fileUri = _selectedFile.value ?: return

        viewModelScope.launch {
            uploadMediaUseCase.invoke(UploadMediaUseCase.Params(fileUri))
                .collect { resource ->
                    _uploadState.value = resource
                    if (resource is Resource.Success) {
                        _selectedFile.value = null
                    }
                }
        }
    }
}
