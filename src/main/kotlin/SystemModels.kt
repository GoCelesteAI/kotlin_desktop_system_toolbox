data class ClipboardEntry(
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
)

data class CommandResult(
    val command: String,
    val stdout: String,
    val stderr: String,
    val exitCode: Int,
    val durationMs: Long,
)

data class SystemInfo(
    val osName: String,
    val osVersion: String,
    val osArch: String,
    val javaVersion: String,
    val javaVendor: String,
    val userName: String,
    val userHome: String,
    val shell: String,
) {
    companion object {
        fun gather(): SystemInfo = SystemInfo(
            osName = System.getProperty("os.name", "Unknown"),
            osVersion = System.getProperty("os.version", "Unknown"),
            osArch = System.getProperty("os.arch", "Unknown"),
            javaVersion = System.getProperty("java.version", "Unknown"),
            javaVendor = System.getProperty("java.vendor", "Unknown"),
            userName = System.getProperty("user.name", "Unknown"),
            userHome = System.getProperty("user.home", "Unknown"),
            shell = System.getenv("SHELL") ?: "Unknown",
        )
    }
}
