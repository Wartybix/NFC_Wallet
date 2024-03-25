package com.example.nfcwallet

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nfcwallet.ui.CommunicationScreen
import com.example.nfcwallet.ui.HomeScreen
import com.example.nfcwallet.ui.WalletViewModel
import com.example.nfcwallet.ui.theme.NFCWalletTheme

enum class WalletScreen {
    Home,
    CommunicationScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcWalletAppBar(
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    showTagActions: Boolean = true,
    onEditAction: () -> Unit,
    onDeleteAction: () -> Unit,
    navigateUp: () -> Unit
) {
    var dropDownVisible by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (!canNavigateBack) {
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
fun DeleteDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    tagName: String
) {
    AlertDialog(
        text = {
            Text(stringResource(R.string.tag_delete_message, tagName))
        },
        onDismissRequest = { onCancel() },
        confirmButton = {
            FilledTonalButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun TagOptionsDialog(
    image: Bitmap? = null,
    tagName: String = "",
    onNameEdit: (String) -> Unit,
    onImageAdd: () -> Unit,
    onImageRemove: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
     Dialog(onDismissRequest = onCancel) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            val innerPadding = 24.dp

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = innerPadding)
            ) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = innerPadding)
                        .fillMaxWidth()
                        .aspectRatio(1.6f),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.large
                ) {
                    if (image == null) {
                        Icon(
                            imageVector = Icons.Outlined.HideImage,
                            contentDescription = stringResource(R.string.no_image_attached),
                            modifier = Modifier.padding(32.dp)
                        )
                    } else {
                        Image(
                            bitmap = image.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                    }
                }


                Row(modifier = Modifier.padding(top = 8.dp)) {
                    TextButton(onClick = onImageAdd) {
                        val icon: ImageVector
                        val caption: String

                        if (image == null) {
                            icon = Icons.Outlined.AddAPhoto
                            caption = stringResource(R.string.add_photo)
                        } else {
                            icon = Icons.Outlined.Edit
                            caption = stringResource(R.string.edit_photo)
                        }

                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = caption)
                    }

                    if (image != null) {
                        TextButton(
                            onClick = onImageRemove
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.HideImage,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(stringResource(R.string.remove_photo))
                        }
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 32.dp))

                OutlinedTextField(
                    value = tagName,
                    onValueChange = onNameEdit,
                    label = { Text(stringResource(R.string.tag_name)) },
                    modifier = Modifier.padding(horizontal = innerPadding)
                )

                Row(
                    modifier = Modifier
                        .padding(top = innerPadding, end = innerPadding)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onCancel) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    FilledTonalButton(onClick = onConfirm, Modifier.padding(start = 8.dp)) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
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
    val lazyListState = rememberLazyListState() // Saves state of the lazy column in the home page.
    val uiState = viewModel.uiState.collectAsState().value
    var deleteDialogShown by remember { mutableStateOf(false) }
    var editDialogShown by remember { mutableStateOf(false) }

    if (editDialogShown) {
        val contentResolver = LocalContext.current.contentResolver

        var newTagName by remember { mutableStateOf(uiState.selectedTag.name) }
        var newTagImage by remember { mutableStateOf(uiState.selectedTag.getImage()) }

        /*
        Thank you to 'Ika' on Stack Overflow.
        The following is based on their answer at https://stackoverflow.com/a/58008340
        ********************************************************************************************
         */
        @Suppress("DEPRECATION") val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                newTagImage = if (Build.VERSION.SDK_INT >= VERSION_CODES.P)
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                else
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
        }
        /* ************************************************************************************** */

        TagOptionsDialog(
            image = newTagImage,
            tagName = newTagName,
            onCancel = { editDialogShown = false },
            onNameEdit = { newTagName = it },
            onImageAdd = {
                launcher.launch("image/*") // Restricts user to only their images.
            },
            onImageRemove = { newTagImage = null },
            onConfirm = {
                uiState.selectedTag.name = newTagName
                uiState.selectedTag.setImage(newTagImage)
                viewModel.saveTags()
                editDialogShown = false
            }
        )
    }

    if (deleteDialogShown) {
        DeleteDialog(
            onCancel = { deleteDialogShown = false },
            onConfirm = {
                deleteDialogShown = false
                viewModel.removeTag(uiState.selectedTag)
                navController.navigateUp()
            },
            tagName = uiState.selectedTag.name
        )
    }

    Scaffold(
        topBar = {
            NfcWalletAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                showTagActions = uiState.projectionMode,
                onEditAction = { editDialogShown = true },
                onDeleteAction = { deleteDialogShown = true },
                navigateUp = { navController.navigateUp() })
        },
        floatingActionButton = {
            Column {
                if (currentScreen == WalletScreen.Home) {
                    NewTagFAB(
                        onClick = {
                            viewModel.enableReceiver()
                            viewModel.addTag(Tag("New Tag", null))
                            navController.navigate(WalletScreen.CommunicationScreen.name)
                        },
                        expanded = lazyListState.isScrollingUp()
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WalletScreen.Home.name
        ) {
            composable(
                route = WalletScreen.Home.name,
                enterTransition = {
                    slideInHorizontally( initialOffsetX = { -it / 4 } )
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it / 4 })
                }
            ) {
                HomeScreen(
                    listData = viewModel.tags,
                    systemPadding = innerPadding,
                    onTagClicked = {
                        viewModel.setTag(it)
                        navController.navigate(WalletScreen.CommunicationScreen.name)
                    },
                    lazyListState = lazyListState
                )
            }
            composable(
                route = WalletScreen.CommunicationScreen.name,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                CommunicationScreen(
                    projectionMode = uiState.projectionMode,
                    tagName = uiState.selectedTag.name,
                    tagImage = uiState.selectedTag.getImage(),
                    modifier = Modifier.padding(innerPadding)
                )
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

@Preview
@Composable
fun DeleteDialogPreview() {
    NFCWalletTheme {
        DeleteDialog(onCancel = {}, onConfirm = {}, tagName = "Example Tag")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DeleteDialogPreviewNight() {
    NFCWalletTheme {
        DeleteDialog(onCancel = {}, onConfirm = {}, tagName = "Example Tag")
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditDialogPreviewNight() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            onImageRemove = {},
            onImageAdd = {}
        )
    }
}

@Preview
@Composable
fun EditDialogPreview() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            onImageAdd = {},
            onImageRemove = {}
        )
    }
}

@Preview
@Composable
fun EditDialogPreviewWithTag() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            image = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.pigeon),
            tagName = "Example tag",
            onImageAdd = {},
            onImageRemove = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditDialogPreviewWithTagNight() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            image = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.pigeon),
            tagName = "Example tag",
            onImageAdd = {},
            onImageRemove = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    NFCWalletTheme {
        Menu()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppPreviewNight() {
    NFCWalletTheme {
        Menu()
    }
}
