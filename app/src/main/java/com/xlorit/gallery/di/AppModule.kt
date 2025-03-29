package com.xlorit.gallery.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xlorit.gallery.feature_auth.data.repository.AuthRepositoryImpl
import com.xlorit.gallery.feature_auth.domain.repository.AuthRepository
import com.xlorit.gallery.feature_auth.domain.use_case.LoginUseCase
import com.xlorit.gallery.feature_auth.domain.use_case.RegisterUseCase
import com.xlorit.gallery.feature_gallery.data.repository.MediaRepositoryImpl
import com.xlorit.gallery.feature_gallery.domain.repository.MediaRepository
import com.xlorit.gallery.feature_gallery.domain.usecase.UploadMediaUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//Binding Module for Repositories
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMediaRepository(
        mediaRepositoryImpl: MediaRepositoryImpl
    ): MediaRepository
}

//Firebase Dependencies
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()
}

//Use Cases
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    fun provideRegisterUseCase(authRepository: AuthRepository): RegisterUseCase {
        return RegisterUseCase(authRepository)
    }

    @Provides
    fun provideUploadMediaUseCase(mediaRepository: MediaRepository): UploadMediaUseCase {
        return UploadMediaUseCase(mediaRepository)
    }
}
