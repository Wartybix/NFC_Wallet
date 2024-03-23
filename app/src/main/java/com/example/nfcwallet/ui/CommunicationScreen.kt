package com.example.nfcwallet.ui

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcwallet.R
import com.example.nfcwallet.components.BadgeIcon
import com.example.nfcwallet.ui.theme.NFCWalletTheme

@Composable
fun ReceptionTutorialImage(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(196.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Smartphone,
            contentDescription = null,
            modifier = Modifier.size(196.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(128.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .fillMaxSize()
            )
            Icon(
                imageVector = Icons.Outlined.CreditCard,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun CommunicationScreen(
    projectionMode: Boolean,
    modifier: Modifier = Modifier,
    tagName: String = "",
    tagImage: ImageBitmap? = null
) {
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (projectionMode) {
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .aspectRatio(1.6f),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    if (tagImage != null) {
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
                                        //tint = MaterialTheme.colorScheme.inverseOnSurface,
                                    )
                                }
                            }
                        }
                    } else {
                        BadgeIcon()
                    }


                }
                Text(
                    text = tagName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
                )
            } else {
                ReceptionTutorialImage(modifier = Modifier.padding(bottom = 16.dp))
            }

            val instructionMsg = stringResource(
                if (projectionMode)
                    R.string.projection_instructions
                else
                    R.string.reception_instructions
            )

            Text(
                text = instructionMsg,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 64.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceptionTutorialIconPreview() {
    NFCWalletTheme {
        ReceptionTutorialImage()
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectionPreview() {
    NFCWalletTheme {
        CommunicationScreen(projectionMode = true, tagName = "Generic Card")
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectionWithImagePreview() {
    NFCWalletTheme {
        CommunicationScreen(
            projectionMode = true,
            tagName = "Pigeon Card",
            tagImage = ImageBitmap.imageResource(id = R.drawable.pigeon)
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProjectionPreviewNight() {
    NFCWalletTheme {
        CommunicationScreen(projectionMode = true, tagName = "Generic Card")
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProjectionWithImagePreviewNight() {
    NFCWalletTheme {
        CommunicationScreen(
            projectionMode = true,
            tagName = "Pigeon Card",
            tagImage = ImageBitmap.imageResource(R.drawable.pigeon)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReceptionPreview() {
    NFCWalletTheme {
        CommunicationScreen(projectionMode = true)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ReceptionPreviewNight() {
    NFCWalletTheme {
        CommunicationScreen(projectionMode = true)
    }
}