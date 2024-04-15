package com.example.nfcwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.nfcwallet.R
import com.example.nfcwallet.ui.theme.NFCWalletTheme

@Composable
fun TagOptionsDialog(
    image: ImageBitmap? = null,
    tagName: String = "",
    saving: Boolean = false,
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
                            bitmap = image,
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
                            icon = Icons.Outlined.AddPhotoAlternate
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
                    FilledTonalButton(
                        onClick = onConfirm,
                        enabled = tagName != "", // Disable the button when text field is empty.
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        if (saving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        } else {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
            }
        }
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
fun EditDialogPreviewWithTagSaving() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            image = ImageBitmap.imageResource(R.drawable.pigeon),
            tagName = "Example tag",
            saving = true,
            onImageAdd = {},
            onImageRemove = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditDialogPreviewWithTagSavingNight() {
    NFCWalletTheme {
        TagOptionsDialog(
            onCancel = {},
            onConfirm = {},
            onNameEdit = {},
            image = ImageBitmap.imageResource(R.drawable.pigeon),
            tagName = "Example tag",
            saving = true,
            onImageAdd = {},
            onImageRemove = {}
        )
    }
}