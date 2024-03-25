package com.example.nfcwallet.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import com.example.nfcwallet.Tag
import com.example.nfcwallet.data.WalletUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

private const val USER_STORAGE_FILE_PATH = "tag_data"

private fun loadTagsFromStorage(context: Context) : SnapshotStateList<Tag> {
    var tags: SnapshotStateList<Tag>
    try {
        val fis = context.openFileInput(USER_STORAGE_FILE_PATH)
        val ois = ObjectInputStream(fis)

        tags = (ois.readObject() as List<Tag>).toMutableStateList()

        ois.close()
        fis.close()
    } catch (e: Exception) { // This is normal, e.g. if the user has opened the app for the 1st time
        e.printStackTrace()
        tags = SnapshotStateList()
    }
    Log.d("Loading tags", "Loading tags right now")

    return tags
}

private fun saveTagsToStorage(context: Context, tags: SnapshotStateList<Tag>) {
    Log.d("Saving tag", tags.toString())

    val serializableTags = tags.toList<Tag>()

    try {
        val fos: FileOutputStream = context.openFileOutput(USER_STORAGE_FILE_PATH, Context.MODE_PRIVATE)
        val oos = ObjectOutputStream(fos)

        oos.writeObject(serializableTags)

        oos.close()
        fos.close()

        Log.d("Tag saved", "successful")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

class WalletViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(WalletUiState())

    val uiState: StateFlow<WalletUiState> = _uiState.asStateFlow()
    val tags = loadTagsFromStorage(getApplication<Application>().applicationContext)


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

    fun addTag(tag: Tag) {
        tags.add(tag)
        saveTagsToStorage(getApplication<Application>().applicationContext, tags)
    }

    fun removeTag(tag: Tag) {
        tags.remove(tag)
        saveTagsToStorage(getApplication<Application>().applicationContext, tags)
    }
}