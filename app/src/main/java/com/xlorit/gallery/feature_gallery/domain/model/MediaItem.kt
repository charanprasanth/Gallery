package com.xlorit.gallery.feature_gallery.domain.model

data class MediaItem(
    val id: String,
    val url: String,
    val type: String,
    val timestamp: Long
) {

    companion object {
        fun fromFirestore(data: Map<String, Any?>): MediaItem {
            return MediaItem(
                id = data["id"] as? String ?: "",
                url = data["url"] as? String ?: "",
                type = data["type"] as? String ?: "",
                timestamp = data["timestamp"] as? Long ?: 0,
            )
        }
    }
}
