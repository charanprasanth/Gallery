package com.xlorit.gallery.feature_auth.domain.repository

import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_auth.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<UserModel>>
    fun register(email: String, password: String, name: String): Flow<Resource<UserModel>>
    fun logout()
}
