Here’s an improved and expanded version of your `README.md`, with clearly structured sections for building, running, and using the program:

---


# ZS6BVR Morse Sender (Java Project)

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

### Commands:

| Command     | Description                                         |
| ----------- | --------------------------------------------------- |
| `*`         | Exit the program                                    |
| `+` or `++` | Increase speed by 1 WPM or 5 WPM                             |
| `-` or `--` | Decrease speed by 1 WPM or 5 WPM                             |
| `@`         | Send continuous dots (for oscilloscope calibration) |
| `#`         | Send a continuous tone (for frequency counter)      |
| Any Text    | Will be translated and sent as Morse code           |

---

## Example

```
> hello world
```

Will be converted to:

```
.... . .-.. .-.. --- / .-- --- .-. .-.. -..
```

---

## License

GNU General Public License (GPL v3) (Copyleft license)

---


