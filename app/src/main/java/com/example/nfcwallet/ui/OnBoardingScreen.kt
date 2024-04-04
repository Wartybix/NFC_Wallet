package com.example.nfcwallet.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.R
import com.example.nfcwallet.data.NfcStatus
import com.example.nfcwallet.ui.theme.NFCWalletTheme

/**
 * Used to welcome the user into the app, or warn them that there is an issue with NFC in their device.
 */
@Composable
fun OnBoardingScreen(
    nfcStatus: NfcStatus,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // If there's an error with NFC, the icon displayed will be a warning symbol, rather than the usual app logo
        val icon = if (nfcStatus == NfcStatus.Enabled)
            Icons.Default.Nfc
        else
            Icons.Default.ErrorOutline

        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .size(128.dp)
            )
        }

        Column(
            //Used to keep text in the middle horizontally, as the button takes the max width
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val headingText = when (nfcStatus) {
                NfcStatus.Enabled -> {
                    stringResource(R.string.welcome_to, stringResource(R.string.app_name))
                }
                NfcStatus.Disabled -> {
                    stringResource(R.string.nfc_is_disabled)
                }
                else -> { // I.e. when nfcStatus == NfcStatus.Unsupported
                    stringResource(R.string.device_unsupported)
                }
            }

            val bodyText = when (nfcStatus) {
                NfcStatus.Enabled -> {
                    stringResource(R.string.nfc_tags_will_show_here)
                }
                NfcStatus.Disabled -> {
                    stringResource(R.string.please_enable_nfc)
                }
                else -> { // I.e. when nfcStatus == NfcStatus.Unsupported
                    stringResource(R.string.nfc_unsupported)
                }
            }


            Text(
                text = headingText,
                textAlign = TextAlign.Center, // Ensures new lines aren't justified left of the text bounds
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = bodyText,
                textAlign = TextAlign.Center
            )

            //Only show the button if NFC is supported by the device
            if (nfcStatus != NfcStatus.Unsupported) {
                val context = LocalContext.current //Get current context

                Button(
                    onClick = {
                        if (nfcStatus == NfcStatus.Enabled) {
                            onContinue()
                        } else {
                            // Opens the settings app into the NFC page.
                            context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                        }
                    },
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                ) {
                    val buttonIcon = if (nfcStatus == NfcStatus.Enabled) {
                        Icons.Default.Add
                    } else {
                        Icons.AutoMirrored.Filled.OpenInNew
                    }

                    Icon(
                        imageVector = buttonIcon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))

                    val buttonText = if (nfcStatus == NfcStatus.Enabled) {
                        stringResource(R.string.add_your_first_tag)
                    } else {
                        stringResource(R.string.open_settings)
                    }

                    Text(buttonText)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    NFCWalletTheme {
        OnBoardingScreen(onContinue = {}, nfcStatus = NfcStatus.Enabled)
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingNfcDisabledPreview() {
    NFCWalletTheme {
        OnBoardingScreen(onContinue = {}, nfcStatus = NfcStatus.Disabled)
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenNfcUnsupportedPreview() {
    NFCWalletTheme {
        OnBoardingScreen(onContinue = {}, nfcStatus = NfcStatus.Unsupported)
    }
}