package com.example.nfcwallet.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.R
import com.example.nfcwallet.data.NfcStatus
import com.example.nfcwallet.ui.theme.NFCWalletTheme

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
        val icon = if (nfcStatus == NfcStatus.Enabled) Icons.Default.Nfc else Icons.Default.ErrorOutline

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val headingText = if (nfcStatus == NfcStatus.Enabled) {
                stringResource(R.string.welcome_to, stringResource(R.string.app_name))
            } else if (nfcStatus == NfcStatus.Disabled) {
                stringResource(R.string.nfc_is_disabled)
            } else {
                stringResource(R.string.device_unsupported)
            }

            val bodyText = if (nfcStatus == NfcStatus.Enabled) {
                stringResource(R.string.nfc_tags_will_show_here)
            } else if (nfcStatus == NfcStatus.Disabled) {
                stringResource(R.string.please_enable_nfc)
            } else {
                stringResource(R.string.nfc_unsupported)
            }


            Text(
                text = headingText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.size(16.dp))

            Text(
                text = bodyText,
                textAlign = TextAlign.Center
            )

            if (nfcStatus == NfcStatus.Enabled) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.add_your_first_tag))
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

@Preview
@Composable
fun OnBoardingNfcDisabledPreview() {
    NFCWalletTheme {
        OnBoardingScreen(nfcStatus = NfcStatus.Disabled, onContinue = {})
    }
}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenNfcUnsupportedPreview() {
    NFCWalletTheme {
        OnBoardingScreen(onContinue = {}, nfcStatus = NfcStatus.Unsupported)
    }
}