package com.example.nfcwallet.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BadgeIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Outlined.Badge,
        contentDescription = null,
        modifier = modifier
            .padding(12.dp)
    )
}