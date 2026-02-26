import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

fun startClipboardMonitoring(
    scope: CoroutineScope,
    intervalMs: Long = 1000L,
    onNewEntry: (ClipboardEntry) -> Unit,
) {
    scope.launch(Dispatchers.IO) {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        var lastText = try {
            clipboard.getData(DataFlavor.stringFlavor) as? String
        } catch (_: Exception) {
            null
        }

        while (true) {
            delay(intervalMs)
            try {
                val current = clipboard.getData(DataFlavor.stringFlavor) as? String
                if (current != null && current != lastText) {
                    lastText = current
                    onNewEntry(ClipboardEntry(current))
                }
            } catch (_: Exception) {
                // Clipboard may be unavailable momentarily
            }
        }
    }
}

fun copyToClipboard(text: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(StringSelection(text), null)
}
