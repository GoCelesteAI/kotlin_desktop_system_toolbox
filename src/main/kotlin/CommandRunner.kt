import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

suspend fun executeCommand(
    command: String,
    timeoutSeconds: Long = 30,
): CommandResult = withContext(Dispatchers.IO) {
    val startTime = System.currentTimeMillis()
    val process = ProcessBuilder("/bin/sh", "-c", command)
        .redirectErrorStream(false)
        .start()

    val completed = process.waitFor(timeoutSeconds, TimeUnit.SECONDS)
    val durationMs = System.currentTimeMillis() - startTime

    if (!completed) {
        process.destroyForcibly()
        CommandResult(
            command = command,
            stdout = "",
            stderr = "Command timed out after ${timeoutSeconds}s",
            exitCode = -1,
            durationMs = durationMs,
        )
    } else {
        CommandResult(
            command = command,
            stdout = process.inputStream.bufferedReader().readText(),
            stderr = process.errorStream.bufferedReader().readText(),
            exitCode = process.exitValue(),
            durationMs = durationMs,
        )
    }
}
