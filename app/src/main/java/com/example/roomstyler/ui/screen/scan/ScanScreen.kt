package com.example.roomstyler.ui.screen.scan

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.sceneview.ar.ARSceneView

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    onNavigateBack: () -> Unit,
    @Suppress("UNUSED_PARAMETER") onNavigateToProposals: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // val context = LocalContext.current // Unused for now
    
    // Camera permission
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("ARスキャン") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    @Suppress("DEPRECATION")
                    Icon(Icons.Default.ArrowBack, contentDescription = "戻る")
                }
            },
            actions = {
                if (uiState.isArActive) {
                    IconButton(onClick = { viewModel.captureSnapshot() }) {
                        Icon(Icons.Default.PhotoCamera, contentDescription = "スナップショット")
                    }
                    IconButton(onClick = { viewModel.exportSceneToJson() }) {
                        Icon(Icons.Default.Save, contentDescription = "JSONエクスポート")
                    }
                    IconButton(onClick = { viewModel.showLoadDialog() }) {
                        Icon(Icons.Default.FileOpen, contentDescription = "JSONインポート")
                    }
                    IconButton(onClick = { viewModel.clearScene() }) {
                        Icon(Icons.Default.Delete, contentDescription = "クリア")
                    }
                }
            }
        )

        if (cameraPermissionState.status.isGranted) {
            if (uiState.isArActive) {
                // AR Scene View
                Box(modifier = Modifier.fillMaxSize()) {
                    AndroidView(
                        factory = { context ->
                            ARSceneView(context).apply {
                                viewModel.setupArSceneView(this)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // Overlay UI
                    ArOverlayUI(
                        modifier = Modifier.fillMaxSize(),
                        uiState = uiState,
                        onAddBox = { viewModel.addBoxAtCenter() },
                        onDeleteSelected = { viewModel.deleteSelectedObject() }
                    )
                }
            } else {
                // Start AR UI
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "ARスキャンを開始",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "床面をタップして立方体を配置できます",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.startAr() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("AR開始")
                    }
                }
            }
        } else {
            // Permission not granted
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "カメラ権限が必要です",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { cameraPermissionState.launchPermissionRequest() }
                ) {
                    Text("権限を許可")
                }
            }
        }

        // Error message
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        // Success message
        uiState.message?.let { message ->
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(5000) // Extended to 5 seconds
                viewModel.clearMessage()
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        // Load dialog
        if (uiState.showLoadDialog) {
            LoadSceneDialog(
                onDismiss = { viewModel.hideLoadDialog() },
                onLoadJson = { json -> viewModel.importSceneFromJson(json) }
            )
        }
    }
}

@Composable
private fun LoadSceneDialog(
    onDismiss: () -> Unit,
    onLoadJson: (String) -> Unit
) {
    var jsonText by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("JSONからシーンを読み込み") },
        text = {
            Column {
                Text("JSONテキストを貼り付けてください:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = { Text("JSON...") },
                    maxLines = 10
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (jsonText.isNotBlank()) {
                        onLoadJson(jsonText)
                        onDismiss()
                    }
                }
            ) {
                Text("読み込み")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("キャンセル")
            }
        }
    )
}

@Composable
private fun ArOverlayUI(
    modifier: Modifier = Modifier,
    uiState: ScanUiState,
    onAddBox: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top info
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "床面をタップして立方体を配置",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "オブジェクト数: ${uiState.objectCount}",
                    style = MaterialTheme.typography.bodySmall
                )
                uiState.selectedObjectId?.let {
                    Text(
                        text = "選択中: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Bottom controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = onAddBox,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = "立方体を追加")
            }
            
            if (uiState.selectedObjectId != null) {
                FloatingActionButton(
                    onClick = onDeleteSelected,
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "削除")
                }
            }
        }
    }
}
