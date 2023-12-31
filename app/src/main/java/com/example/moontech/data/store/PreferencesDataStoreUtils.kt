import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.IOException

private const val TAG = "PreferencesDataStore"

fun <T> Flow<T>.catching(): Flow<T> {
    return catch {
        if (it is IOException) {
            Log.e(TAG, "Error reading preferences", it)
            emptyPreferences()
        }
        throw it
    }
}

inline fun <reified T> Set<String>.editDecodedValues(block: MutableSet<T>.() -> Unit): Set<String> {
    return decode<T>()
        .toMutableSet().apply { block() }.encodeToString()
}

inline fun <reified T> Set<String>.decode(): Set<T> {
    return map { Json.decodeFromString<T>(it) }.toSet()
}

inline fun <reified T> Set<T>.encodeToString(): Set<String> {
    return map { Json.encodeToString(it) }.toSet()
}