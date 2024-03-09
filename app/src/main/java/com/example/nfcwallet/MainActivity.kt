package com.example.nfcwallet

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
fun NfcWalletAppBar(
    currentScreen: WalletScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            if (currentScreen == WalletScreen.Home) {
                Text(stringResource(id = R.string.app_name))
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
fun NewTagFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.Add, null) },
        text = { Text(stringResource(id = R.string.new_tag)) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WalletScreen.valueOf(
        backStackEntry?.destination?.route ?: WalletScreen.Home.name
    )

    Scaffold(
        topBar = {
            NfcWalletAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        },
        floatingActionButton = {
            Column {
                AnimatedVisibility(
                    visible = currentScreen == WalletScreen.Home,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    NewTagFAB(onClick = { /*TODO*/ })
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WalletScreen.Home.name
        ) {
            composable(
                route = WalletScreen.Home.name,
                enterTransition = {
                    slideInHorizontally( initialOffsetX = { -it / 4 } )
                },
                exitTransition = {
                    slideOutHorizontally(targetOffsetX = { -it / 4 })
                }
            ) {
                HomeScreen(
                    listData = tagTestData,
                    systemPadding = innerPadding,
                    onTagClicked = {
                        navController.navigate(WalletScreen.ProjectionReception.name)
                    }
                )
            }
            composable(
                route = WalletScreen.ProjectionReception.name,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                CommunicationScreen(
                    projectionMode = true,
                    modifier = Modifier.padding(innerPadding)
                )
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

@Preview
@Composable
fun HomeAppBarPreview() {
    NFCWalletTheme {
        NfcWalletAppBar(
            currentScreen = WalletScreen.Home, canNavigateBack = false, navigateUp = {}
        )
    }
}
@Preview
@Composable
fun CommunicationScreenAppBarPreview() {
    NFCWalletTheme {
        NfcWalletAppBar(
            currentScreen = WalletScreen.ProjectionReception,
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}

@Preview
@Composable
fun NewTagFabPreview() {
    NFCWalletTheme {
        NewTagFAB(onClick = {})
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