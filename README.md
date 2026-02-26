# System Toolbox

A Compose Desktop system integration app — clipboard management, command execution, and desktop notifications in a tabbed interface.

**Kotlin Desktop Lesson 18: System Integration**

## Features

- **Clipboard Tab** — Read/write system clipboard with `Toolkit.getDefaultToolkit().systemClipboard` and `DataFlavor.stringFlavor`
- **Command Tab** — Execute system commands via `ProcessBuilder` with live stdout/stderr output
- **System Tab** — Desktop notifications using `rememberTrayState()` and `Notification`
- **Tabbed Interface** — Material 3 `TabRow` with icons for each tool

## Tech Stack

- Kotlin 2.1.0
- Compose Multiplatform 1.7.3
- Material 3
- Material Icons Extended
- AWT Toolkit (Clipboard, DataFlavor)
- ProcessBuilder for command execution
- Compose Desktop Tray & Notification API

## Run

```bash
./gradlew run
```

## Taught by CelesteAI
