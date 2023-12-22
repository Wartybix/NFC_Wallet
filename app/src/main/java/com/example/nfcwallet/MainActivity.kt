package com.example.nfcwallet

import android.app.PendingIntent
import android.content.IntentFilter
import android.content.res.Configuration
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nfcwallet.ui.theme.NFCWalletTheme

class MainActivity : ComponentActivity() {
    //Using tutorial

    lateinit var nfcAdapter: NfcAdapter
    var writeMode: Boolean = false
    var myTag: Tag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        setContent {
            NFCWalletTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Menu()
                }
            }
        }
    }
}

@Composable
fun Menu() {
    Column {
        var writeMode by remember { mutableStateOf(false) }

        Button(onClick = {
            writeMode = false
        }) {
            Text(text = "Read")
        }

        Button(onClick = {
            writeMode = true
        }) {
            Text(text = "Project")
        }


        Text(
            text = if (writeMode) "Write Mode" else "Read Mode",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Tag data here",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true
)
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
fun Preview() {
    NFCWalletTheme {
        Surface {
            Menu()
        }
    }
}