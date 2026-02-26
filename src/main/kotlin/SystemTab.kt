import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun SystemTab(modifier: Modifier = Modifier) {
    val info = remember { SystemInfo.gather() }
    var envFilter by remember { mutableStateOf("") }
    val envVars = remember {
        System.getenv().entries.sortedBy { it.key }
    }
    val filteredEnvVars by remember(envFilter) {
        derivedStateOf {
            if (envFilter.isBlank()) envVars
            else envVars.filter {
                it.key.contains(envFilter, ignoreCase = true) ||
                    it.value.contains(envFilter, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "System Information",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.testTag("system_title"),
        )

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth().testTag("platform_card"),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Computer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Platform", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(12.dp))
                InfoRow("OS", "${info.osName} ${info.osVersion}")
                InfoRow("Architecture", info.osArch)
                InfoRow("Java", "${info.javaVersion} (${info.javaVendor})")
                InfoRow("User", info.userName)
                InfoRow("Home", info.userHome)
                InfoRow("Shell", info.shell)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Environment Variables (${filteredEnvVars.size})",
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = envFilter,
            onValueChange = { envFilter = it },
            label = { Text("Filter variables") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("env_filter"),
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.testTag("env_list"),
        ) {
            items(filteredEnvVars) { (key, value) ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            key,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace,
                        )
                        Text(
                            value,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 2,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
        )
    }
}
