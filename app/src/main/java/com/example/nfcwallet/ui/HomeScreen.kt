package com.example.nfcwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.DrawableStringPair
import com.example.nfcwallet.R
import com.example.nfcwallet.components.BadgeIcon
import com.example.nfcwallet.ui.theme.NFCWalletTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    listData: List<DrawableStringPair>,
    onTagClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(id = R.string.app_name)) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /*TODO*/ },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text(stringResource(R.string.new_tag)) }
            )
        }
    ) { paddingValues ->
        val verticalPadding = 16.dp
        val horizontalPadding = 24.dp

        LazyColumn(
            contentPadding = PaddingValues(
                top = verticalPadding + paddingValues.calculateTopPadding(),
                bottom = verticalPadding + paddingValues.calculateBottomPadding(),
                start = horizontalPadding,
                end = horizontalPadding
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            items(listData) { item ->
                if (item.icon == null) {
                    TagCard(item.name, null, onTagClicked, Modifier)
                } else {
                    TagCard(item.name, ImageBitmap.imageResource(item.icon), onTagClicked, Modifier)
                }
            }
        }
    }
}

@Composable
fun TagCard(
    name: String,
    icon: ImageBitmap?,
    onClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface (
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = onClicked,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iconContentScale = ContentScale.Crop
            val iconModifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
            if (icon == null) {
                Surface(
                    modifier = iconModifier,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    BadgeIcon()
                }

            } else {
                Image(
                    bitmap = icon,
                    contentDescription = null,
                    contentScale = iconContentScale,
                    modifier = iconModifier
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Preview
@Composable
fun TagCardPreview() {
    NFCWalletTheme {
        TagCard("Pigeon Card", ImageBitmap.imageResource(R.drawable.pigeon), {})
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagCardPreviewNight() {
    NFCWalletTheme {
        TagCard("Pigeon Card", ImageBitmap.imageResource(R.drawable.pigeon), {})
    }
}