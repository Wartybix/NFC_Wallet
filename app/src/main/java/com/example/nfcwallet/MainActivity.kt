package com.example.nfcwallet

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.ui.theme.NFCWalletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Menu()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu() {
    NFCWalletTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(R.string.app_name)) })
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = { /*TODO*/ },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text(stringResource(R.string.new_tag)) }
                )
            }
        ) { padding ->
            TagList(Modifier, padding)
        }
    }
}

@Composable
fun BadgeIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = Icons.Outlined.Badge,
        contentDescription = null,
        modifier = modifier
            .padding(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectionScreen(modifier: Modifier = Modifier) {
    NFCWalletTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Outlined.ArrowBack, "Back")
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
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(width = 256.dp, 160.dp),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    BadgeIcon(modifier = Modifier.padding(16.dp)
                    )
                    //Image(
                    //    painter = painterResource(id = R.drawable.pigeon),
                    //    contentDescription = null,
                    //    contentScale = ContentScale.Crop,
                    //)
                }
                Text(
                    text = "Card Name",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )
                Text(
                    text = "Place the back of your phone to the reader when ready.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 64.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Composable
fun TagList(modifier: Modifier, paddingValues: PaddingValues) {
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
    Surface (
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = { /*TODO*/ },
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

private val tagTestData = listOf(
    R.drawable.pigeon to "Pigeon Card",
    null to "Passport",
    null to "Shopping Card",
    null to "Other Card",
    null to "Cardigan Card",
    null to "Student Card",
    null to "Staff Card",
    null to "Cardboard card"
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

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    NFCWalletTheme {
        Menu()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppPreviewNight() {
    NFCWalletTheme {
        Menu()
    }
}