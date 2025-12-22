# MinecraftLwjgl3
A little project that is aiming to port Minecraft beta 1.3_01 to LWJGL3 with OpenGL 4.6, including additional features.

## Build and Run Instructions

### Prerequisites
- Java 8 or higher
- Maven 3.6 or higher
- Git (for cloning)

### Quick Start (Windows)
1. Clone the repository:
   ```
   git clone https://github.com/minikikplBLAAA/MinecraftLwjgl3.git
   cd MinecraftLwjgl3
   ```

2. Build the project:
   ```
   build.cmd
   ```

3. Run the game:
   ```
   run.cmd
   ```

### Manual Build and Run
If you prefer to build manually:

1. Clean and compile:
   ```
   mvn clean compile
   ```

2. Package the JAR:
   ```
   mvn package
   ```

3. Run the game:
   ```
   java -jar target/minecraft-1.3.01-ultimate-edition.jar
   ```

### Project Structure
- `minecraft/` - Source code directory
- `resources/` - Game resources (sounds, music, etc.)
- `textures/` - Game textures
- `target/` - Build output directory
- `build.cmd` - Automated build script (Windows)
- `run.cmd` - Automated run script (Windows)

# Information
If someone wants to help, feel free to make a fork and submit a pull request! We welcome contributions of all kinds, from bug fixes to new features.

## How to Contribute (Pull Request)
To contribute to this project, please follow these steps:

Fork the repository: Click the "Fork" button at the top right of this page to create your own copy of the repository.

Clone your fork: Clone the repository to your local machine using Git.

```
git clone https://github.com/minikikplBLAAA/MinecraftLwjgl3.git
```

Create a branch: Create a new branch for your specific feature or fix.

```
git checkout -b feature/amazing-feature
```

Make your changes: Implement your changes in the code.

Commit your changes: Commit your changes with a clear and descriptive message.
```
git commit -m "Add some amazing feature"
```
Push to your fork: Push your branch to your forked repository on GitHub.
```
git push origin feature/amazing-feature
```
Submit a Pull Request: Go to the original repository on GitHub and you will see a prompt to compare and create a Pull Request. Click it, describe your changes, and submit!
