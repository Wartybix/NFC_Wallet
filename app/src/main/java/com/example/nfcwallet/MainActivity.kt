package com.example.nfcwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nfcwallet.ui.theme.NFCWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            NFCWalletTheme {
                Menu()
            }
        }
    }
}