package com.example.nfcwallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.nfcwallet.ui.theme.NFCWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    NFCWalletTheme {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text(stringResource(R.string.new_tag)) }
                )
            }
        ) { padding ->
            TagList(Modifier)
        }
    }
}

@Composable
fun TagList(modifier: Modifier) {
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(tagTestData) { item ->
            if (item.icon == null) {
                TagCard(item.name, null, Modifier)
            } else {
                TagCard(item.name, ImageBitmap.imageResource(item.icon), Modifier)
            }
        }
    }
}

@Composable
fun TagCard(
    name: String,
    icon: ImageBitmap?,
    modifier: Modifier
) {
    Card (
        shape = MaterialTheme.shapes.extraLarge,
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
            if (icon != null) {
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

private val tagTestData = listOf(
    R.drawable.pigeon to "Pigeon Card",
    null to "Passport",
    null to "Shopping Card"
).map { DrawableStringPair(it.first, it.second) }

private data class DrawableStringPair(
    @DrawableRes val icon: Int?,
    val name: String
)

@Preview
@Composable
fun TagCardPreview() {
    NFCWalletTheme {
        TagCard("Pigeon Card", ImageBitmap.imageResource(R.drawable.pigeon), Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    NFCWalletTheme {
        Menu()
    }
}