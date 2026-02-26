import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.image.BufferedImage

val TrayIcon = run {
    val img = BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)
    val g = img.createGraphics()
    g.color = java.awt.Color(103, 80, 164)
    g.fillRoundRect(0, 0, 32, 32, 8, 8)
    g.color = java.awt.Color.WHITE
    g.font = java.awt.Font("SansSerif", java.awt.Font.BOLD, 20)
    val fm = g.fontMetrics
    g.drawString("S", (32 - fm.stringWidth("S")) / 2, (32 + fm.ascent - fm.descent) / 2)
    g.dispose()
    img.toComposeImageBitmap()
}

fun main() = application {
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = BitmapPainter(TrayIcon),
        tooltip = "System Toolbox",
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "System Toolbox",
        state = rememberWindowState(size = DpSize(1100.dp, 700.dp)),
    ) {
        MaterialTheme(colorScheme = darkColorScheme()) {
            AppContent(trayState = trayState)
        }
    }
}
