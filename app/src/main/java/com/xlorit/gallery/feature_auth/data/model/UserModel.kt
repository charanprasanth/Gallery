package com.xlorit.gallery.feature_auth.data.model

data class UserModel(
    val uid: String = "",
    val name: String = "",
    val email: String = ""
) {

    companion object {
        fun fromFirestore(data: Map<String, Any?>): UserModel {
            return UserModel(
                uid = data["uid"] as? String ?: "",
                name = data["name"] as? String ?: "",
                email = data["email"] as? String ?: ""
            )
        }
    }
}
