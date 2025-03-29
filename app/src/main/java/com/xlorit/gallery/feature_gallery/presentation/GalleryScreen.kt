package com.xlorit.gallery.feature_gallery.presentation

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xlorit.gallery.R
import com.xlorit.gallery.core.data.Resource
import com.xlorit.gallery.feature_auth.presentation.AuthViewModel
import com.xlorit.gallery.feature_gallery.domain.model.MediaItem
import com.xlorit.gallery.feature_gallery.presentation.components.ErrorComposable
import com.xlorit.gallery.feature_gallery.presentation.components.LoaderComposable
import com.xlorit.gallery.feature_gallery.presentation.components.LogoutDialog
import com.xlorit.gallery.feature_gallery.presentation.components.MediaItemComposable
import com.xlorit.gallery.feature_gallery.presentation.components.UploadMediaDialog
import com.xlorit.gallery.navigation.Screens

@Composable
fun GalleryScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel(),
    galleryViewModel: GalleryViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val columnCount = (screenWidthDp / 150.dp).toInt().coerceAtLeast(2)

    var showLogoutDialog by remember { mutableStateOf(false) }

    val mediaItems by galleryViewModel.mediaItems.collectAsState()

    var showUploadDialog by remember { mutableStateOf(false) }
    val selectedFile by galleryViewModel.selectedFile.collectAsState()
    val uploadState by galleryViewModel.uploadState.collectAsState()

    val filePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let { galleryViewModel.selectFile(it) }
        }

    // Handling Upload Result
    LaunchedEffect(uploadState) {
        uploadState?.let { state ->
            when (state) {
                is Resource.Success -> {
                    showUploadDialog = false
                    Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(context, state.message ?: "Upload Failed", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    "Gallery",
                    fontSize = 25.sp,
                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 16.dp)
                )
                IconButton(
                    onClick = { showLogoutDialog = true }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_logout),
                        contentDescription = "Logout"
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showUploadDialog = true },
                containerColor = Color.Blue,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_upload),
                    contentDescription = "Upload",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        when (mediaItems) {
            is Resource.Loading -> LoaderComposable()
            is Resource.Error -> ErrorComposable("Error Loading Media")
            is Resource.Success -> {
                val items = (mediaItems as Resource.Success<List<MediaItem>>).data ?: arrayListOf()
                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No Items found")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(columnCount),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(items) { mediaItem ->
                            MediaItemComposable(mediaItem)
                        }
                    }
                }
            }
            else -> Box(modifier = Modifier)
        }
    }

    if (showUploadDialog) {
        UploadMediaDialog(
            selectedFile = selectedFile,
            onPickFile = { filePickerLauncher.launch(arrayOf("image/*", "video/*")) },
            onUpload = { galleryViewModel.uploadMedia(context) },
            onDismiss = { showUploadDialog = false },
            uploadState = uploadState
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                authViewModel.logout()
                navController.navigate(Screens.LOGIN) {
                    popUpTo(Screens.GALLERY) { inclusive = true }
                }
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}
