package com.example.nfcwallet.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfcwallet.SerializableTag
import com.example.nfcwallet.Tag
import com.example.nfcwallet.data.WalletUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val USER_STORAGE_FILE_PATH = "tag_data"

private fun loadTagsFromStorage(context: Context) : SnapshotStateList<Tag> {
    var tags: SnapshotStateList<Tag>
    try {
        val fis = context.openFileInput(USER_STORAGE_FILE_PATH)
        val ois = ObjectInputStream(fis)

        val serializableTags = ois.readObject() as List<SerializableTag>
        tags = serializableTags.map { serializableTag -> Tag(serializableTag.name, serializableTag.getImage()) }.toMutableStateList()

        ois.close()
        fis.close()
    } catch (e: Exception) { // This is normal, e.g. if the user has opened the app for the first time
        e.printStackTrace()
        tags = SnapshotStateList()
    }

    return tags
}

private suspend fun saveTagsToStorage(context: Context, tags: SnapshotStateList<Tag>) {
    withContext(Dispatchers.IO) {
        val serializableTags: List<SerializableTag> = tags.map {
                tag -> SerializableTag(tag.name, tag.image)
        }

        try {
            val fos: FileOutputStream = context.openFileOutput(USER_STORAGE_FILE_PATH, Context.MODE_PRIVATE)
            val oos = ObjectOutputStream(fos)

            oos.writeObject(serializableTags)

            oos.close()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(WalletUiState())

    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()
    val tags = loadTagsFromStorage(getApplication<Application>().applicationContext)

    fun anyTagsPresent() : Boolean {
        return tags.any()
    }

    fun enableReceiver() {
        _uiState.update { currentState ->
            currentState.copy(
                projectionMode = false
            )
        }
    }

    fun setTag(tag: Tag) {
        _uiState.update { currentState ->
            currentState.copy(
                projectionMode = true,
                selectedTag = tag,
            )
        }
    }

    fun saveTags(onSave: () -> Unit = {}) {
        viewModelScope.launch {
            saveTagsToStorage(getApplication<Application>().applicationContext, tags)
            onSave()
        }
    }

    fun addTag(tag: Tag, onSave: () -> Unit = {}) {
        tags.add(tag)
        saveTags(onSave = onSave)
    }

    fun removeTag(tag: Tag, onSave: () -> Unit = {}) {
        tags.remove(tag)
        saveTags(onSave = onSave)
    }
}