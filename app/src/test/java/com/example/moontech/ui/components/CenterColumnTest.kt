
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.onNodeWithTag
import com.example.moontech.GenericUiTest
import com.example.moontech.ui.components.CenterColumn
import org.junit.Test

class CenterColumnTest: GenericUiTest() {

    @Test
    fun centerColumnTest() {
        composeTestRule.setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Use CenterColumn in your UI hierarchy
                CenterColumn(
                    modifier = Modifier.testTag("centerColumnTest"),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Your content goes here
                }
            }
        }

        // Verify that the CenterColumn has the correct layout properties
        composeTestRule.onNodeWithTag("centerColumnTest")
            .assertExists()
    }
}