package com.xlorit.gallery.feature_gallery.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : MediaRepository {

    override fun uploadMedia(fileUri: Uri): Flow<Resource<MediaItem>> = flow {
        try {
            emit(Resource.Loading())

            val uid = firebaseAuth.currentUser?.uid ?: ""
            val fileName = fileUri.lastPathSegment ?: "unknown"
            val storageRef = firebaseStorage.reference.child("uploads/$uid/$fileName")

            // Uploading file
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            // Storing metadata in Firestore
            val mediaItem = MediaItem(
                id = fileName,
                url = downloadUrl,
                type = if (fileName.endsWith("mp4")) "video" else "image",
                timestamp = System.currentTimeMillis()
            )

            firestore.collection("Docs").document(uid)
                .collection("uploads").document(fileName).set(mediaItem).await()

            emit(Resource.Success(mediaItem))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Upload failed"))
        }
    }
}
