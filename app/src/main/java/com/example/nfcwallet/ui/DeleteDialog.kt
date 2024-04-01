package com.example.nfcwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.nfcwallet.R
import com.example.nfcwallet.ui.theme.NFCWalletTheme

@Composable
fun DeleteDialog(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    tagName: String,
    saving: Boolean = false
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
                if (saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                } else {
                    Text(stringResource(R.string.delete))
                }
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