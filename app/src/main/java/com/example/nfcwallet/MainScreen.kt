package com.example.nfcwallet

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nfcwallet.data.NfcStatus
import com.example.nfcwallet.data.WalletScreen
import com.example.nfcwallet.ui.CommunicationScreen
import com.example.nfcwallet.ui.DeleteDialog
import com.example.nfcwallet.ui.ImageViewer
import com.example.nfcwallet.ui.OnBoardingScreen
import com.example.nfcwallet.ui.TagList
import com.example.nfcwallet.ui.TagOptionsDialog
import com.example.nfcwallet.ui.WalletViewModel
import com.example.nfcwallet.ui.theme.NFCWalletTheme


const val IMAGE_VIEWER_TRANSITION_SCALE = 0.9f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcWalletAppBar(
    canNavigateBack: Boolean,
    showAppTitle: Boolean,
    showTagActions: Boolean,
    modifier: Modifier = Modifier,
    onEditAction: () -> Unit,
    onDeleteAction: () -> Unit,
    navigateUp: () -> Unit
) {
    var dropDownVisible by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (showAppTitle && !canNavigateBack) {
                Text(stringResource(id = R.string.app_name))
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (canNavigateBack && showTagActions) {
                IconButton(onClick = { dropDownVisible = !dropDownVisible }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.tag_options)
                    )
                }
                DropdownMenu(
                    expanded = dropDownVisible,
                    onDismissRequest = { dropDownVisible = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit_tag)) },
                        onClick = {
                            onEditAction()
                            dropDownVisible = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete_tag)) },
                        onClick = {
                            onDeleteAction()
                            dropDownVisible = false
                        }
                    )
                }
            }
        },
        modifier = modifier,
    )
}

/**
 * Returns whether the lazy list is currently scrolling up
 * (from https://developer.android.com/codelabs/jetpack-compose-animation)
 * Thank you Yuichi Araki and Rebecca Franks for the following function:
 */
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) {
        mutableIntStateOf(firstVisibleItemScrollOffset)
    }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun NewTagFAB(
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.Add, null) },
        text = { Text(stringResource(id = R.string.new_tag)) },
        expanded = expanded,
        modifier = modifier
    )
}

@Composable
fun Menu(
    navController: NavHostController = rememberNavController(),
    viewModel: WalletViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WalletScreen.valueOf(
        backStackEntry?.destination?.route ?: WalletScreen.Home.name
    )
    val lazyListState = rememberLazyListState() // Saves state of the lazy column in the tag list.
    val uiState = viewModel.uiState.collectAsState().value
    var deleteDialogShown by remember { mutableStateOf(false) }
    var editDialogShown by remember { mutableStateOf(false) }

    if (editDialogShown) {
        val contentResolver = LocalContext.current.contentResolver

        var newTagName by remember {
            mutableStateOf(if (uiState.projectionMode) uiState.selectedTag.name else "")
        }
        var newTagImage by remember {
            mutableStateOf(if (uiState.projectionMode) uiState.selectedTag.image else null)
        }

        var isSaving by remember { mutableStateOf(false) }

        /*
        Thank you to 'Ika' on Stack Overflow.
        The following is based on their answer at https://stackoverflow.com/a/58008340
        ********************************************************************************************
         */
        @Suppress("DEPRECATION") val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                newTagImage = (
                    if (Build.VERSION.SDK_INT >= VERSION_CODES.P)
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                    else
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    ).asImageBitmap()
            }
        }
        /* ************************************************************************************** */

        TagOptionsDialog(
            image = newTagImage,
            tagName = newTagName,
            saving = isSaving,
            onCancel = { editDialogShown = false },
            onNameEdit = { newTagName = it },
            onImageAdd = {
                launcher.launch("image/*") // Restricts user to only their images.
            },
            onImageRemove = { newTagImage = null },
            onConfirm = {
                isSaving = true

                if (uiState.projectionMode) {
                    uiState.selectedTag.name = newTagName
                    uiState.selectedTag.image = newTagImage

                    viewModel.saveTags(onSave = { editDialogShown = false })
                } else {
                    val newTag = Tag(newTagName, newTagImage)
                    viewModel.addTag(
                        tag = newTag,
                        onSave = {
                            editDialogShown = false
                            navController.navigateUp()
                        }
                    )
                }
            }
        )
    }

    if (deleteDialogShown) {
        var isSaving by remember { mutableStateOf(false) }
        
        DeleteDialog(
            onCancel = { deleteDialogShown = false },
            onConfirm = {
                isSaving = true

                viewModel.removeTag(
                    tag = uiState.selectedTag,
                    onSave = {
                        deleteDialogShown = false
                        navController.navigateUp()
                    }
                )
            },
            tagName = uiState.selectedTag.name,
            saving = isSaving
        )
    }

    val context = LocalContext.current

    fun getNfcStatus() : NfcStatus {
        val nfcManager = context.getSystemService(Context.NFC_SERVICE) as NfcManager
        val nfcAdapter = nfcManager.defaultAdapter

        return if (nfcAdapter == null) // Is NFC *unsupported* by the device?
                NfcStatus.Unsupported
            else if (nfcAdapter.isEnabled) // Is NFC *enabled* on the device?
                NfcStatus.Enabled
            else // NFC must be disabled on the device
                NfcStatus.Disabled
    }

    var nfcStatus by remember { mutableStateOf(getNfcStatus()) }

    NfcBroadcastReceiver(
        systemAction = NfcAdapter.ACTION_ADAPTER_STATE_CHANGED,
        onReceive = { // When the status of the NFC setting has changed
            nfcStatus = getNfcStatus() // Fetch the NFC state again.
        }
    )

    Scaffold(
        topBar = {
            NfcWalletAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                showTagActions = uiState.projectionMode,
                showAppTitle = viewModel.anyTagsPresent() && nfcStatus == NfcStatus.Enabled,
                onEditAction = { editDialogShown = true },
                onDeleteAction = { deleteDialogShown = true },
                navigateUp = { navController.navigateUp() })
        },
        floatingActionButton = {
            if (currentScreen == WalletScreen.Home && viewModel.anyTagsPresent()
                && nfcStatus == NfcStatus.Enabled) {
                NewTagFAB(
                    onClick = {
                        viewModel.enableReceiver()
                        navController.navigate(WalletScreen.CommunicationScreen.name)
                    },
                    expanded = lazyListState.isScrollingUp()
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WalletScreen.Home.name
        ) {
            composable(
                route = WalletScreen.Home.name,
                popEnterTransition = {
                    slideInHorizontally( initialOffsetX = { -it / 4 } )
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it / 4 })
                }
            ) {
                if (viewModel.anyTagsPresent() && nfcStatus == NfcStatus.Enabled) {
                    TagList(
                        listData = viewModel.tags,
                        systemPadding = innerPadding,
                        onTagClicked = {
                            viewModel.setTag(it)
                            navController.navigate(WalletScreen.CommunicationScreen.name)
                        },
                        lazyListState = lazyListState
                    )
                } else {
                    OnBoardingScreen(
                        nfcStatus = nfcStatus,
                        onContinue = {
                            viewModel.enableReceiver()
                            navController.navigate(WalletScreen.CommunicationScreen.name)
                        },
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding()
                            .fillMaxSize()
                    )
                }
            }
            composable(
                route = WalletScreen.CommunicationScreen.name,
                popEnterTransition = {
                    fadeIn()
                },
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                CommunicationScreen(
                    projectionMode = uiState.projectionMode,
                    tagName = uiState.selectedTag.name,
                    tagImage = uiState.selectedTag.image,
                    modifier = Modifier.padding(innerPadding),
                    onTagScan = { editDialogShown = true },
                    onExpandButtonClicked = {
                        navController.navigate(WalletScreen.ImageViewer.name)
                    }
                )
            }
            composable(
                route = WalletScreen.ImageViewer.name,
                enterTransition = {
                    scaleIn(initialScale = IMAGE_VIEWER_TRANSITION_SCALE) + fadeIn()
                },
                popExitTransition = {
                    scaleOut(targetScale = IMAGE_VIEWER_TRANSITION_SCALE) + fadeOut()
                }
            ) {
                uiState.selectedTag.image?.let { it1 -> ImageViewer(image = it1) }
            }
        }
    }
}

@Preview
@Composable
fun HomeAppBarPreview() {
    NFCWalletTheme {
        NfcWalletAppBar(
            canNavigateBack = false,
            showAppTitle = true,
            showTagActions = false,
            navigateUp = {},
            onDeleteAction = {},
            onEditAction = {}
        )
    }
}
@Preview
@Composable
fun ProjectionScreenAppBarPreview() {
    NFCWalletTheme {
        NfcWalletAppBar(
            canNavigateBack = true,
            showTagActions = true,
            showAppTitle = false,
            navigateUp = {},
            onDeleteAction = {},
            onEditAction = {}
        )
    }
}

@Preview
@Composable
fun ReceptionScreenAppBarPreview() {
    NFCWalletTheme {
        NfcWalletAppBar(
            canNavigateBack = true,
            showTagActions = false,
            showAppTitle = false,
            navigateUp = {},
            onDeleteAction = {},
            onEditAction = {}
        )
    }
}

@Preview
@Composable
fun NewTagFabExpandedPreview() {
    NFCWalletTheme {
        NewTagFAB(onClick = {}, expanded = true)
    }
}

@Preview
@Composable
fun NewTagFabShrunkPreview() {
    NFCWalletTheme {
        NewTagFAB(onClick = {}, expanded = false)
    }
}