---

````markdown
# ZS6BVR Morse Sender (Java Project)

---

This Morse Sender is a powerful and user-friendly Java tool that translates text into authentic Morse code audio.

It supports dynamic speed control, customizable tone frequency, and advanced features like memory storage and random group generation.

With intuitive commands and repeat functions, it’s perfect for beginners and seasoned operators alike.

Experience precise, flexible Morse code sending — the best companion for your Morse communication needs.

---

This Java application allows you to type text and have it translated and sent as Morse code using sound tones.

## Features

- Converts typed text into audible Morse code
- Adjustable speed (WPM - words per minute)
- Special modes for calibration and testing
- Continuous tone for frequency counters
- Continuous dots for oscilloscope calibration

---

## 🔧 How to Build and Run

### Prerequisites

- Java 17+
- Maven 3+

### Steps to Build

1. Clone or download the project.
2. Open a terminal in the project directory.
3. Run the following command:

```bash
mvn clean package
````

This will create a JAR file in the `target/` folder, named:

```
MorseSender-1.0.jar
```

### To Run

```bash
java -jar target/MorseSender-1.0.jar
```

---

## 🎮 How to Use the Program

Once the program starts, you will see a prompt:

```
ZS6BVR Morse Code Sender
Type message or special key and press ENTER
Special keys: * = quit, + = faster, - = slower, @ = dots (scope), # = continuous tone
```

Here’s an updated version of your **Morse Sender Instructions**, including all the new commands and features you’ve added — mem variables, bracket repeats, random groups, and more:

---

# Morse Sender Instructions

### Commands:

| Command                | Description                                                                                                          |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------- |
| `*`                    | Exit the program                                                                                                     |
| `+` or `++`            | Increase speed by 5 WPM                                                                                              |
| `-` or `--`            | Decrease speed by 5 WPM                                                                                              |
| `@`                    | Send continuous dots (for oscilloscope calibration)                                                                  |
| `#`                    | Send a continuous tone (for frequency counter)                                                                       |
| `[text]`               | Repeat the text inside the brackets 3 times, with pauses between repeats                                             |
| `{N}`                  | Send `N` rows of random 5-character groups — each group contains exactly one digit randomly positioned among letters |
| `!1 text` to `!6 text` | Store the following text in memory slots mem1 through mem6 respectively                                              |
| `$1` to `$6`           | Insert stored text from memory slots mem1 through mem6 respectively at the position of these tokens                  |
| Any other text         | Will be translated and sent as Morse code                                                                            |

---

### Notes:

* Speed changes always adjust by 5 WPM increments (no 1 WPM step)
* For `{N}`, the program generates `N` lines of random groups, each with exactly one digit placed randomly among letters
* Using `[text]` repeats `text` exactly 3 times with a longer pause between repeats
* Memories (`mem1`–`mem6`) allow you to store and reuse text snippets dynamically
* The program replaces all occurrences of `$1` to `$6` with their stored text before sending

---

## Example Usage

```
> hello world
```

Outputs Morse for:

```
.... . .-.. .-.. --- / .-- --- .-. .-.. -..
```

---

```
> [sos]
```

Sends:

```
... --- ...   (pause)   ... --- ...   (pause)   ... --- ...
```

---

```
> {3}
```

Generates and sends 3 rows like:

```
A9ZXR
QTX2M
NBV7L
```

---

```
> !1 hello there
Mem1 stored.

> $1 world
Sends Morse for "hello there world"
```

---


Sure! Here’s a concise **step-by-step guide** on how to install **OpenJDK 17 (Java 17)** on **Linux**, **macOS**, and **Windows** — suitable for Java 17 development.

---

# How to Install Java 17 (OpenJDK 17)

---

## On Linux (Ubuntu/Debian)

1. **Update package list:**

```bash
sudo apt update
```

2. **Install OpenJDK 17:**

```bash
sudo apt install openjdk-17-jdk
```

3. **Verify installation:**

```bash
java -version
```

Expected output:

```
openjdk version "17.0.x" 202x-xx-xx
OpenJDK Runtime Environment (build 17.0.x+xx)
OpenJDK 64-Bit Server VM (build 17.0.x+xx, mixed mode)
```

---

## On macOS (using Homebrew)

1. **Install Homebrew** (if not installed):

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

2. **Install OpenJDK 17:**

```bash
brew install openjdk@17
```

3. **Add Java 17 to your PATH:**

For zsh (default shell on newer macOS):

```bash
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

For bash:

```bash
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.bash_profile
source ~/.bash_profile
```

4. **Verify installation:**

```bash
java -version
```

---

## On Windows

### Option 1: Use Official OpenJDK builds (Eclipse Temurin / Adoptium)

1. Go to [Adoptium.net](https://adoptium.net/temurin/releases/?version=17) and download the **Windows x64 MSI installer** for Java 17.

2. Run the installer and follow the instructions.

3. **Verify installation:**

* Open Command Prompt and run:

```cmd
java -version
```

Expected output similar to:

```
openjdk version "17.0.x" 202x-xx-xx
OpenJDK Runtime Environment (build 17.0.x+xx)
OpenJDK 64-Bit Server VM (build 17.0.x+xx, mixed mode)
```

---

### Option 2: Use Windows Package Manager (winget)

1. Open PowerShell as Administrator.

2. Run:

```powershell
winget install -e --id Eclipse.Temurin.17.JDK
```

3. After installation, verify with:

```powershell
java -version
```

---




## License

GNU General Public License (GPL v3) (Copyleft license)

---


