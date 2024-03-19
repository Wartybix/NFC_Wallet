package com.example.nfcwallet.ui

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.example.nfcwallet.DrawableStringPair
import com.example.nfcwallet.data.WalletUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private fun getExampleData() = listOf(
    null to "Pigeon Card",
    null to "Passport",
    null to "Shopping Card",
    null to "Other Card",
    null to "Cardigan Card",
    null to "Student Card",
    null to "Staff Card",
    null to "Cardboard card"
).map { DrawableStringPair(it.first, it.second) }

class WalletViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WalletUiState())
    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()
    val _tags = getExampleData()

    fun setTag(newTagName: String, newTagImage: ImageBitmap?) {
        _uiState.update {currentState ->
            currentState.copy(
                tagName = newTagName,
                tagImage = newTagImage
            )
        }
    }
}