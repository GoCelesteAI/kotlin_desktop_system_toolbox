import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class Tab(val label: String, val icon: ImageVector) {
    Clipboard("Clipboard", Icons.Default.ContentPaste),
    Commands("Commands", Icons.Default.Terminal),
    System("System", Icons.Default.Computer),
}

@Composable
fun AppContent(
    trayState: TrayState,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(Tab.Clipboard) }
    val clipboardEntries = remember { mutableStateListOf<ClipboardEntry>() }
    val commandResults = remember { mutableStateListOf<CommandResult>() }
    var isRunning by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            NavigationRail(
                modifier = Modifier.testTag("nav_rail"),
            ) {
                Spacer(Modifier.height(12.dp))
                Tab.entries.forEach { tab ->
                    NavigationRailItem(
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        modifier = Modifier.testTag("nav_${tab.name.lowercase()}"),
                    )
                }
            }

            when (selectedTab) {
                Tab.Clipboard -> ClipboardTab(
                    entries = clipboardEntries,
                    onCopyBack = { text ->
                        copyToClipboard(text)
                    },
                    onClear = { clipboardEntries.clear() },
                )

                Tab.Commands -> CommandTab(
                    results = commandResults,
                    onExecute = { command ->
                        isRunning = true
                        scope.launch(Dispatchers.IO) {
                            val result = executeCommand(command)
                            commandResults.add(0, result)
                            isRunning = false
                            val type = if (result.exitCode == 0) {
                                Notification.Type.Info
                            } else {
                                Notification.Type.Warning
                            }
                            trayState.sendNotification(
                                Notification(
                                    "Command Done",
                                    "exit ${result.exitCode}: ${result.command.take(50)}",
                                    type,
                                )
                            )
                        }
                    },
                    isRunning = isRunning,
                )

                Tab.System -> SystemTab()
            }
        }
    }

    LaunchedEffect(Unit) {
        startClipboardMonitoring(this) { entry ->
            clipboardEntries.add(0, entry)
            trayState.sendNotification(
                Notification(
                    "Clipboard Captured",
                    entry.text.take(50),
                    Notification.Type.Info,
                )
            )
        }
    }
}
