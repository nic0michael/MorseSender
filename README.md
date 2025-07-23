
# ZS6BVR Morse Sender (Java Project)



This Morse Sender is a powerful and user-friendly Java tool that translates text into authentic Morse code audio.

It supports dynamic speed control, customizable tone frequency, random group generation, lesson playback, and memory macros.

With intuitive commands and repeat functions, it’s perfect for beginners and seasoned operators alike.

---

## Features

- Converts typed text into audible Morse code
- Adjustable speed (WPM - words per minute)
- Special calibration for speed and tone frequency
- Play Generated lesson files
- Generate new lesson files on demand
- Store and reuse text via 9 memories ... CQ DX de ZS6BVR
- Repeating Message in brackets 
- Generate random groups of 5 in square brackets

---

## 📦 Run Without Building
**Rename the file MorseSender-1.0.jarjar to MorseSender.jar**

```bash
chmod 775 *.sh

./install.sh

# Now run this every time to start this
./start.sh


# For Windows users, we have:
install.BAT 

# and
start.BAT

```

---

## 🔧 How to Build and Run from Source Code

### Prerequisites

- Java 17+
- Maven 3+

### Steps to Build

1. Clone or download the project.
2. Open a terminal in the project directory.
3. Run the following:

```bash
mvn clean package
```

You will get the JAR in the `target/` folder:

```
MorseSender-1.0.jar
```

### To Run

```bash
# Copy jar file
cp target/MorseSender-1.0.jar ./MorseSender.jar

# Run the jar program
java -jar MorseSender.jar


```

---

## 🎮 How to Use the Program

When you launch the program, you'll see:

```
ZS6BVR Morse Code Sender
Type message or special key and press ENTER
Special keys: * = quit, + = faster, - = slower, @ = dots (scope), # = continuous tone
```

---

## 🧠 Commands Reference

| Command                | Description                                                                                                          |
| ---------------------- | -------------------------------------------------------------------------------------------------------------------- |
| `*`                    | Exit the program                                                                                                     |
| `+`/'++' /  `-`/'--'   | Increase/decrease speed by 1/5 WPM                                                                                   |
| `@`                    | Send continuous dots (To Oscilloscope for speed calibration)                                                         |
| `#`                    | Send continuous tone (To Frequency Counter for tone calibration)                                                     |
| `#ML`                  | Generate new lessons (lesson1.txt to lesson9.txt)                                                                    |
| `#L1` to `#L9`         | Send contents of lesson1.txt to lesson9.txt as Morse                                                                 |
| `[text]`               | Repeat the text 3 times, with pauses                                                                                 |
| `{N}`                  | Send `N` lines of 5-character groups (random, each with one digit)                                                   |
| `!1 text` to `!6 text` | Store the given text in memory slot mem1 through mem6                                                                |
| `$1` to `$6`           | Insert text from memory slot mem1–mem6 at that position                                                              |
| Any other text         | Will be converted to Morse tone and played                                                                           |

---

## 📚 Lessons

### Predefined Lessons

- You can use `#L1` to `#L9` to play `lesson1.txt` to `lesson9.txt`
- These files contain:
  - `lesson1-3`: random groups of 5 characters (callsigns style)
  - `lesson4-6`: random callsigns
  - `lesson7-9`: random English words

### Regenerate Lessons

To generate fresh content for all 9 lessons:

```
> #ML
✅ New lessons generated (lesson1.txt to lesson9.txt).
```

---

## 🧪 Examples

### Simple word:

```
> hello
H E L L O 
```

### Repeat Continuously:

```
> [sos]
S O S   (pause) S O S   (pause) S O S
```
type s to stop

### Random groups of 5 letters:

```
> {2}
M3YTZ
KQW8Z
```

### Memory:

```
> !1 de zs6bvr k
Mem1 stored.

> $1 $1
de zs6bvr k de zs6bvr k
```

---

## ☕ Install Java 17 (OpenJDK)

### On Linux (Debian/Ubuntu)

```bash
sudo apt update
sudo apt install openjdk-17-jdk
java -version
```

---

### On macOS (with Homebrew)

```bash
brew install openjdk@17
echo 'export PATH="/usr/local/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
java -version
```

---

### On Windows

- Download: [https://adoptium.net/temurin/releases/?version=17](https://adoptium.net/temurin/releases/?version=17)
- OR run in PowerShell:

```powershell
winget install -e --id Eclipse.Temurin.17.JDK
java -version
```

---

## 📜 License

GNU General Public License (GPL v3)

---


