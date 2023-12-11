
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.moontech.GenericUiTest
import com.example.moontech.data.dataclasses.ObjectWithRoomCode
import com.example.moontech.ui.components.DismissableRoomTile
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class DismissableRoomTileTest: GenericUiTest() {
    @Test
    fun dismissableRoomTile_Dismiss() {
        // Mock data
        val mockRoom = mockk<ObjectWithRoomCode>()
        every { mockRoom.code } returns "Test code"
        val onDismissMock = mockk<(ObjectWithRoomCode) -> Unit>(relaxed = true)

        // Set up the UI with the DismissableRoomTile
        composeTestRule.setContent {
            DismissableRoomTile(room = mockRoom, onDismiss = onDismissMock)
        }

        // Find the dismiss icon by content description and perform a click action
        composeTestRule.onRoot().printToLog("TAG")
        composeTestRule
            .onNodeWithContentDescription("Delete")
            .assertExists()
        composeTestRule
            .onNodeWithText("Test code")
            .assertExists()
    }
}