package com.xlorit.gallery.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_auth.data.model.UserModel
import com.xlorit.gallery.feature_auth.domain.use_case.LoginUseCase
import com.xlorit.gallery.feature_auth.domain.use_case.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Resource<UserModel>>(Resource.Idle())
    val authState: StateFlow<Resource<UserModel>> get() = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = Resource.Loading()
            loginUseCase(LoginUseCase.Params(email, password)).collect { result ->
                _authState.value = result
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            registerUseCase(RegisterUseCase.Params(email, password, name)).collect { result ->
                _authState.value = result
            }
        }
    }
}