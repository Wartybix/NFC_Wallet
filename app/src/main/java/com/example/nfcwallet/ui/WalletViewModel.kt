package com.example.nfcwallet.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.example.nfcwallet.data.WalletUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WalletViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()

    fun setTagName(newTagName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                tagName = newTagName
            )
        }
    }

    fun setTagImage(newTagImage: ImageBitmap?) {
        _uiState.update { currentState ->
            currentState.copy(
                tagImage = newTagImage
            )
        }
    }
}