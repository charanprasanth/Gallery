package com.xlorit.gallery.feature_auth.domain.use_case

import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.core.data.UseCase
import com.xlorit.gallery.feature_auth.data.model.UserModel
import com.xlorit.gallery.feature_auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) :
    UseCase<LoginUseCase.Params, Flow<Resource<UserModel>>>() {

    data class Params(val email: String, val password: String)

    override suspend fun invoke(params: Params): Flow<Resource<UserModel>> {
        return authRepository.login(params.email, params.password)
    }
}
