package com.xlorit.gallery.feature_gallery.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.domain.repository.MediaRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : MediaRepository {

    override fun uploadMedia(fileUri: Uri, type: String): Flow<Resource<MediaItem>> = flow {
        try {
            emit(Resource.Loading())

            val uid = firebaseAuth.currentUser?.uid ?: ""
            val fileName = fileUri.lastPathSegment ?: "unknown"
            val storageRef = firebaseStorage.reference.child("uploads/$uid/$fileName")

            // Uploading file
            storageRef.putFile(fileUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            val fileNameWithTime = "$fileName-${System.currentTimeMillis()}"

            // Storing metadata in Firestore
            val mediaItem = MediaItem(
                id = fileName,
                url = downloadUrl,
                type = type,
                timestamp = System.currentTimeMillis()
            )

            firestore.collection("Docs").document(uid)
                .collection("uploads").document(fileNameWithTime).set(mediaItem).await()

            emit(Resource.Success(mediaItem))
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Upload failed"))
        }
    }

    override fun getUserMedia(): Flow<Resource<List<MediaItem>>> = callbackFlow {
        try {
            trySend(Resource.Loading())

            val uid = firebaseAuth.currentUser?.uid ?: ""
            val collectionRef = firestore.collection("Docs")
                .document(uid)
                .collection("uploads")
                .orderBy("timestamp", Query.Direction.DESCENDING)

            val listener = collectionRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.localizedMessage ?: "Failed to fetch media"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val mediaList = snapshot.documents.mapNotNull { document ->
                        MediaItem.fromFirestore(document.data ?: emptyMap())
                    }
                    trySend(Resource.Success(mediaList))
                }
            }
            awaitClose { listener.remove() }
        } catch (e: Exception) {
            trySend(Resource.Error(e.localizedMessage ?: "Failed to fetch media"))
        }
    }
}
