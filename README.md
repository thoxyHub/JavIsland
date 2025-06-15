# JavIsland

**JavIsland** is a wave-based 2D survival game developed in Java. It was created as a group project of five people for the Agile Object-Oriented Software Development course (02160) at the Technical University of Denmark (DTU). The project showcases a modular architecture, clean coding practices, and iterative development.

<img width=auto alt="Screenshot 2025-06-14 at 20 49 12" src="https://github.com/user-attachments/assets/eba849b2-f541-43d1-acfb-dd5dd381c7aa" />

## 🎮 Game Overview

In *JavIsland*, the player must survive successive waves of enemies on a procedurally generated island. You can gather resources, build defensive blocks, and engage in both melee and ranged combat.

---

## 📂 Project Structure

The project follows a clear **Model-View-Controller (MVC)** architecture:
- **Model**: Game logic and state (entities, map, items, etc.)
- **View**: Renders game and UI using observer pattern
- **Controller**: Handles player inputs and game updates

---

## 🚀 Getting Started

### Requirements
- Java-compatible IDE (e.g., IntelliJ, Eclipse)
- Java 17+ recommended

### Building & Running
1. Clone the repository:
   ```bash
   git clone https://github.com/thoxyHub/JavIsland.git
   ```
2. Open the project in your IDE.
3. Run `Play.java` in `src/main/java/com/group16`.

---

## 🎮 Controls

| Action               | Key(s)          |
|----------------------|-----------------|
| Move                 | Arrow keys      |
| Attack (melee)       | `S`             |
| Shoot (ranged)       | `G`             |
| Build wood block     | `B`             |
| Build stone block    | `N`             |
| Open/close inventory | `E`             |
| Restart game         | `R`             |
| Title screen         | `Esc`           |
| Start game           | `Enter`         |

---

## 🧠 Gameplay

1. **Choose a Map**: Use arrow keys to select, press Enter.
2. **Prepare**: You have 30 seconds to collect wood and stone.
3. **Build Defenses**: Use blocks to protect yourself.
4. **Combat**: Survive waves of skeleton mobs.
5. **Objective**: Stay alive as long as possible.

---

## 🧰 Troubleshooting

- **Missing maps**: Ensure `MapDesign1.txt` and `MapDesign2.txt` are in the working directory.
- **Sprites not showing**: Confirm `resources/` is included in the classpath.
- **IDE issues**: Refresh source paths and rebuild the project.

---

## ✅ Agile Methodology


The project was developed using agile practices:
- **User Stories & Iterations**
- **Behavior-Driven Development (BDD) with Cucumber**
- **Regular demos, reviews, and refactoring**
- **85% test coverage**

---

## 👾 Sprites

- Player, mobs and environment: https://kenmi-art.itch.io/
- Inventory: https://bragorn.itch.io/modular-inventory-sprites 


