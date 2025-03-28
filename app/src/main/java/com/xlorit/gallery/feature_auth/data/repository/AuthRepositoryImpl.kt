package com.xlorit.gallery.feature_auth.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_auth.data.model.UserModel
import com.xlorit.gallery.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<UserModel>> = flow {
        try {
            emit(Resource.Loading())

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw Exception("User not found")

            val snapshot = fireStore.collection("users").document(user.uid).get().await()
            val data = snapshot.data ?: throw Exception("Invalid user, Please Register")

            val userModel = UserModel.fromFirestore(data)

            emit(Resource.Success(userModel))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error"))
        }
    }

    override fun register(email: String, password: String, name: String): Flow<Resource<UserModel>> = flow {
        emit(Resource.Loading())
        val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("User creation failed")
        val userModel = UserModel(uid = user.uid, email = email, name = name)
        fireStore.collection("users").document(user.uid).set(userModel).await()
        emit(Resource.Success(userModel))
    }.catch { emit(Resource.Error(it.message ?: "Unknown error")) }

    override fun logout() {
        firebaseAuth.signOut()
    }
}