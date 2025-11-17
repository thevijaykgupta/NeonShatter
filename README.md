# Neon Shatter

A modern take on the classic Brick Breaker game, featuring a neon glow aesthetic with smooth animations and particle effects.

![Neon Shatter](https://img.shields.io/badge/Java-8+-orange) ![License](https://img.shields.io/badge/License-MIT-blue)

## 🎮 Features

- **Classic Gameplay**: Traditional Breakout mechanics with smooth paddle movement and ball physics
- **Neon Aesthetic**: Glowing visuals with bright colors (cyan, pink, green, yellow)
- **4 Unique Layouts**: 
  - Grid Layout (classic)
  - Wave Layout (sinusoidal pattern)
  - Circle Layout (rings of bricks)
  - ZigZag Layout (diagonal pattern)
- **Particle Effects**: Lightweight particle system when bricks are destroyed
- **Progressive Difficulty**: Complete all 4 layouts to win

## 🎯 Controls

- **Arrow Keys (Left/Right)**: Move paddle
- **ENTER or SPACE**: Start/Restart game

## 🚀 Quick Start

### Windows
```bash
run.bat
```

### Linux/Mac
```bash
chmod +x run.sh
./run.sh
```

### Manual Compilation
```bash
# Compile
javac -source 8 -target 8 -d bin -sourcepath src src/*.java

# Run
java -cp bin NeonShatter
```

## 📁 Project Structure

```
Neon_Shatter/
├── src/
│   ├── NeonShatter.java      # Main entry point
│   ├── GamePanel.java        # Main game logic and rendering
│   ├── GameState.java        # Game state enumeration
│   ├── Ball.java            # Ball entity with physics
│   ├── Paddle.java          # Paddle entity with smooth movement
│   ├── Brick.java           # Brick entity with neon rendering
│   ├── Particle.java        # Particle effect system
│   ├── BrickLayout.java     # Interface for brick layouts
│   ├── GridBrickLayout.java # Classic grid layout
│   ├── WaveLayout.java      # Wave pattern layout
│   ├── CircleLayout.java    # Circular ring layout
│   └── ZigZagLayout.java    # Zigzag pattern layout
├── bin/                      # Compiled classes (gitignored)
├── run.bat                   # Windows run script
├── run.sh                    # Linux/Mac run script
├── .gitignore               # Git ignore file
└── README.md                # This file
```

## 🎮 Game Mechanics

- **Paddle Physics**: Smooth acceleration and friction-based movement
- **Ball Physics**: Realistic bouncing with angle adjustment based on paddle hit position
- **Collision Detection**: Precise rectangle-based collision detection
- **Score System**: 10 points per brick destroyed
- **Win Condition**: Complete all 4 brick layouts
- **Game Over**: Ball falls below the screen

## 🛠 Technical Details

- Built with Java Swing/AWT
- 60 FPS game loop
- Anti-aliased rendering for smooth graphics
- Layered glow effects using alpha transparency
- Lightweight particle system (5-12 particles per brick)
- Java 8+ compatible

## 📋 Requirements

- Java 8 or higher
- Java Development Kit (JDK) for compilation

## 🎨 Visual Features

- Neon glow effects on all game elements
- Smooth 60 FPS animations
- Particle effects when bricks break
- Rounded corners on bricks and paddle
- Anti-aliased rendering
- Dark theme background

## 📝 License

This project is open source and available under the MIT License.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## 👨‍💻 Author

Created with ❤️ for classic arcade game enthusiasts.

---

**Enjoy playing Neon Shatter!** 🎮✨
