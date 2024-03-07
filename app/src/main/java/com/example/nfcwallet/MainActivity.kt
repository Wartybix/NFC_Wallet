package com.example.nfcwallet

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nfcwallet.ui.HomeScreen
import com.example.nfcwallet.ui.CommunicationScreen
import com.example.nfcwallet.ui.theme.NFCWalletTheme

enum class WalletScreen {
    Home,
    ProjectionReception
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Menu()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navController: NavHostController = rememberNavController()
) {
    NFCWalletTheme {
        Surface {
            Column {
                NavHost(
                    navController = navController,
                    startDestination = WalletScreen.Home.name,
                ) {
                    composable(
                        route = WalletScreen.Home.name,
                        enterTransition = {
                            slideInHorizontally( initialOffsetX = { -it / 2 } )
                        },
                        exitTransition = {
                            slideOutHorizontally(targetOffsetX = { -it / 2 })
                        }
                    ) {
                        HomeScreen(
                            listData = tagTestData,
                            onTagClicked = {
                                navController.navigate(WalletScreen.ProjectionReception.name)
                            }
                        )
                    }
                    composable(
                        route = WalletScreen.ProjectionReception.name,
                        enterTransition = {
                            slideInHorizontally(
                                initialOffsetX = { it }
                            )
                        },
                        exitTransition = {
                            slideOutHorizontally(
                                targetOffsetX = { it }
                            )
                        }
                    ) {
                        CommunicationScreen(true, onNavigateUp = { navController.navigateUp() })
                    }
                }
            }
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

data class DrawableStringPair(
    @DrawableRes val icon: Int?,
    val name: String
)

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