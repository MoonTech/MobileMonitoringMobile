
import android.util.Log
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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