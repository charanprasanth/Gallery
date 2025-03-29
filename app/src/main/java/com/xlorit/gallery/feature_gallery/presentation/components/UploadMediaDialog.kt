package com.xlorit.gallery.feature_gallery.presentation.components

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xlorit.gallery.core.data.Resource

@Composable
fun UploadMediaDialog(
    selectedFile: Uri?,
    onPickFile: () -> Unit,
    onUpload: () -> Unit,
    onDismiss: () -> Unit,
    uploadState: Resource<*>?
) {
    var isUploading by remember { mutableStateOf(false) }
    isUploading =  (uploadState is Resource.Loading)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload Media") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = onPickFile) {
                    Text("Select Document")
                }
                Spacer(modifier = Modifier.height(8.dp))

                selectedFile?.let {
                    Text("Selected: ${it.lastPathSegment}")
                }

                if (isUploading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                }
            }
        },
        confirmButton = {
            if (selectedFile != null) {
                Button(onClick = onUpload, enabled = !isUploading) {
                    Text("Upload")
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss, enabled = !isUploading) {
                Text("Cancel")
            }
        }
    )
}