package com.example.nfcwallet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

/**
 * Thank you to Android Developers for the template for this code.
 * https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/views-in-compose#case-study-broadcastreceivers
 */
@Composable
fun NfcBroadcastReceiver(
    systemAction: String,
    onReceive: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current

    val currentOnReceive by rememberUpdatedState(newValue = onReceive)
    
    DisposableEffect(context) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnReceive(intent)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}