package com.example.nfcwallet.ui

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.R
import com.example.nfcwallet.Tag
import com.example.nfcwallet.components.BadgeIcon
import com.example.nfcwallet.ui.theme.NFCWalletTheme


@Composable
fun HomeScreen(
    listData: SnapshotStateList<Tag>,
    systemPadding: PaddingValues,
    onTagClicked: (Tag) -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val verticalPadding = 16.dp
    val horizontalPadding = 24.dp

    LazyColumn(
        contentPadding = PaddingValues(
            top = verticalPadding + systemPadding.calculateTopPadding(),
            bottom = verticalPadding + systemPadding.calculateBottomPadding(),
            start = horizontalPadding,
            end = horizontalPadding
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyListState,
        modifier = modifier.fillMaxSize()
    ) {
        items(listData) { item ->
            val icon: Bitmap? = if (item.getImage() == null) {
                null
            } else {
                item.getImage()
            }
            TagCard(
                name = item.name,
                icon = icon,
                Modifier,
                onClicked = { onTagClicked(item) })
        }
    }
}

@Composable
fun TagCard(
    name: String,
    icon: Bitmap?,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit = {}
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
                    bitmap = icon.asImageBitmap(),
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
        TagCard("Pigeon Card", BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.pigeon))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TagCardPreviewNight() {
    NFCWalletTheme {
        TagCard("Pigeon Card", BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.pigeon))
    }
}