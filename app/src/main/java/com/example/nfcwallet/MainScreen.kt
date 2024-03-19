package com.example.nfcwallet

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.nfcwallet.ui.WalletViewModel
import com.example.nfcwallet.ui.theme.NFCWalletTheme
import androidx.lifecycle.viewmodel.compose.viewModel

enum class WalletScreen {
    Home,
    CommunicationScreen
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfcWalletAppBar(
    currentScreen: WalletScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var dropDownVisible by remember { mutableStateOf(false) }

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
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        },
        actions = {
            if (currentScreen == WalletScreen.CommunicationScreen) {
                IconButton(onClick = { dropDownVisible = !dropDownVisible }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.tag_options)
                    )
                }
                DropdownMenu(
                    expanded = dropDownVisible,
                    onDismissRequest = { dropDownVisible = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.edit_tag)) },
                        onClick = { /*TODO*/ }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete_tag)) },
                        onClick = { /*TODO*/ }
                    )
                }
            }
        },
        modifier = modifier,
    )
}

/**
 * Returns whether the lazy list is currently scrolling up
 * (from https://developer.android.com/codelabs/jetpack-compose-animation)
 * Thank you Yuichi Araki and Rebecca Franks for the following function:
 */
@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) {
        mutableIntStateOf(firstVisibleItemScrollOffset)
    }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun NewTagFAB(
    onClick: () -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = { Icon(Icons.Default.Add, null) },
        text = { Text(stringResource(id = R.string.new_tag)) },
        expanded = expanded,
        modifier = modifier
    )
}

@Composable
fun Menu(
    navController: NavHostController = rememberNavController(),
    viewModel: WalletViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WalletScreen.valueOf(
        backStackEntry?.destination?.route ?: WalletScreen.Home.name
    )
    val lazyListState = rememberLazyListState() // Saves state of the lazy column in the home page.

    Scaffold(
        topBar = {
            NfcWalletAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        },
        floatingActionButton = {
            Column {
                if (currentScreen == WalletScreen.Home) {
                    NewTagFAB(onClick = { /*TODO*/ }, expanded = lazyListState.isScrollingUp())
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
                    listData = viewModel._tags,
                    systemPadding = innerPadding,
                    onTagClicked = {
                        navController.navigate(WalletScreen.CommunicationScreen.name)
                    },
                    lazyListState = lazyListState
                )
            }
            composable(
                route = WalletScreen.CommunicationScreen.name,
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


data class DrawableStringPair(
    val icon: Bitmap?,
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
            currentScreen = WalletScreen.CommunicationScreen,
            canNavigateBack = true,
            navigateUp = {}
        )
    }
}

@Preview
@Composable
fun NewTagFabExpandedPreview() {
    NFCWalletTheme {
        NewTagFAB(onClick = {}, expanded = true)
    }
}

@Preview
@Composable
fun NewTagFabShrunkPreview() {
    NFCWalletTheme {
        NewTagFAB(onClick = {}, expanded = false)
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