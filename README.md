# ChunksLab-Gestures ✋

![Code Quality](https://img.shields.io/codefactor/grade/github/ChunksLab/ChunksLab-Gestures)
<a href="https://wiki.chunkslab.com" alt="GitBook">
<img src="https://img.shields.io/badge/docs-gitbook-brightgreen" alt="Gitbook"/>
</a>
![Code Size](https://img.shields.io/github/languages/code-size/ChunksLab/ChunksLab-Gestures)
![bStats Servers](https://img.shields.io/bstats/servers/25019)
![bStats Players](https://img.shields.io/bstats/players/25019)
![GitHub License](https://img.shields.io/github/license/ChunksLab/ChunksLab-Gestures)

## 📌 Overview

ChunksLab-Gestures is a **high-performance Paper plugin** designed to enhance **player emotes** on Minecraft servers. It prioritizes **customization**, **efficiency**, and **flexibility**. 🏃‍♂️

### 🔥 Key Features

- **☕ Zstd Compression**: Efficient data processing similar to Minecraft's native methods.
- **⚡ Optimized Gesture Detection**: Recognizes gestures with minimal server performance impact.
- **🛠️ Developer API**: Allows creating custom gestures and animations.

---
## 🔧 Building the Project

### 💻 Command Line Usage

1. Install **JDK 17 & 21**.
2. Open a terminal and navigate to the project directory.
3. Run:
   ```sh
   ./gradlew build
   ```
4. The compiled artifact can be found in the **/build/libs** folder.

### 🛠️ Using an IDE

1. Import the project into your preferred **IDE**.
2. Run the **Gradle build** process.
3. Locate the compiled artifact in the **/build/libs** folder.

---
## 🤝 Contributing

### 🌍 Translations

1. Clone the repository.
2. Create a new language file in:
   ```
   /src/main/java/com/chunkslab/gestures/config/messages
   ```
3. Submit a **pull request** with your changes. Your contributions are appreciated! 💖

### 🚀 Areas for Improvement

- Optimize the **gesture recognition engine** for lower computational overhead.
- Enhance **memory management** and implement **compressed gesture data storage**.
- Ensure **thread-safe operations** for gesture processing.
- Optimize **packet transmission** for reduced network load.
- Improve **entity movement packets** to enhance smoothness and reduce latency.

---
## 💖 Support the Developer

If you enjoy using **ChunksLab-Gestures**, consider supporting the developer! 🥰

- [Polymart](https://polymart.org/resource/chunkslab-gestures/)
- [BuiltByBit](https://builtbybit.com/resources/chunkslab-gestures/)
- [ChunksLab](https://chunkslab.com/chunkslab-gestures/)
- [PayPal](https://paypal.me/tamergumus)

---
## 📚 ChunksLab-Gestures API

### 📌 Repository
```kotlin
repositories {
    maven("https://repo.voxelarc.net/releases/")
}
```

### 📌 Dependency
```kotlin
dependencies {
    compileOnly("com.chunkslab.gestures:api:1.0.0")
}
```
