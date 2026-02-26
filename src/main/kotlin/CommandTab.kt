import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
fun CommandTab(
    results: List<CommandResult>,
    onExecute: (String) -> Unit,
    isRunning: Boolean,
    modifier: Modifier = Modifier,
) {
    var commandText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Command Runner",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.testTag("command_title"),
        )

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            OutlinedTextField(
                value = commandText,
                onValueChange = { commandText = it },
                label = { Text("Enter command") },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("command_input")
                    .onPreviewKeyEvent { event ->
                        if (event.key == Key.Enter && event.type == KeyEventType.KeyDown && !isRunning && commandText.isNotBlank()) {
                            onExecute(commandText)
                            commandText = ""
                            true
                        } else {
                            false
                        }
                    },
                textStyle = TextStyle(fontFamily = FontFamily.Monospace),
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    if (commandText.isNotBlank()) {
                        onExecute(commandText)
                        commandText = ""
                    }
                },
                enabled = !isRunning && commandText.isNotBlank(),
                modifier = Modifier.testTag("run_button"),
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Run")
                Spacer(Modifier.width(4.dp))
                Text("Run")
            }
        }

        if (isRunning) {
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("results_list"),
        ) {
            items(results) { result ->
                CommandResultCard(result)
            }
        }
    }
}

@Composable
private fun CommandResultCard(result: CommandResult) {
    val isSuccess = result.exitCode == 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSuccess) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.errorContainer
            },
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "$ ${result.command}",
                    style = MaterialTheme.typography.labelLarge,
                    fontFamily = FontFamily.Monospace,
                )
                Text(
                    "exit ${result.exitCode} (${result.durationMs}ms)",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSuccess) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                )
            }
            if (result.stdout.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    result.stdout.trimEnd(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                )
            }
            if (result.stderr.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    result.stderr.trimEnd(),
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
