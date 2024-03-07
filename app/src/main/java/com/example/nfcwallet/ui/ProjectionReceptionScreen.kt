package com.example.nfcwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.R
import com.example.nfcwallet.components.BadgeIcon
import com.example.nfcwallet.ui.theme.NFCWalletTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectionScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.MoreVert, "More")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .aspectRatio(1.6f),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.pigeon),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInFull,
                                contentDescription = stringResource(R.string.expand_image),
                                tint = MaterialTheme.colorScheme.inverseOnSurface,
                            )
                        }
                    }
                }

            }
            Text(
                text = "Pigeon Card",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            )
            Text(
                text = stringResource(
                    R.string.place_the_back_of_your_phone_to_the_reader_when_ready),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 64.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectionPreview() {
    NFCWalletTheme {
        ProjectionScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProjectionPreviewNight() {
    NFCWalletTheme {
        ProjectionScreen()
    }
}