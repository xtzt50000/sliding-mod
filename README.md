# sliding-mod (Minecraft Fabric 1.20.1)

A smooth, fast-paced sliding and slide-jump mod for Minecraft Fabric 1.20.1.

Featuring authentic 3rd-person knee-slide animations, immersive 1st-person camera roll and dynamic FOV, smooth physics deceleration curves, and multiplayer synchronization.

---

## ✨ Features

- **Smooth Sliding Physics**:
  - Initial forward impulse boost when sliding out of a sprint.
  - Custom decay curve (`0.975 ~ 0.955`) allowing ~10-12 blocks of buttery-smooth slide distance without abrupt vanilla ground friction stops.
  - Downhill gravity boost: sliding down slopes or hills increases and maintains momentum.
- **Slide Jump Boost**:
  - Pressing Jump (`Space`) during a slide converts horizontal momentum into a powerful forward slide-jump (`+15%` velocity boost).
- **Authentic 3rd-Person Motion**:
  - Replaces awkward crawling/swimming animations with a tactical knee-slide pose.
  - Upper body leaned back ~18°, front leg extended forward along the ground, rear knee bent underneath, and arms posed for balance or aiming held weapons.
  - Outer skin layers (jacket, pants, sleeves) fully synchronized.
- **Immersive 1st-Person Camera Effects**:
  - Smooth eye-height transition (drops smoothly to `0.85F`).
  - Action camera roll (`-3.5°`) and pitch tilt when sliding.
  - Dynamic Field of View (FOV) widening (up to `+12%`) scaling with slide velocity.
  - Block step particles and sliding sweep sounds.
- **Multiplayer Synchronized**:
  - Custom C2S and S2C networking packets synchronize sliding state across all players with zero rubberbanding.

---

## 🎮 Controls

| Action | Control |
|---|---|
| **Slide** | **Sprint** (`Ctrl` or double-tap `W`) + **Sneak** (`Shift`) while on the ground |
| **Slide-Jump** | Press **Jump** (`Space`) during a slide |
| **Cancel Slide** | Release **Sneak** (`Shift`) |

---

## 📜 Version History

### `v1.1.0` (Latest)
- **Combat & PvP Hitbox Balance**:
  - Hitbox height doubled (`0.6` ➡️ `1.2`) to cover the player's head and upper body during slide, allowing enemies and arrows to hit the player fairly.
  - Fits through 1.5-block openings (slabs/trapdoors).
- **Edge-Protection Bypass & Gap Sliding**:
  - Bypassed vanilla sneaking ledge-hold (`maybeBackOffFromEdge` & `isStayingOnGroundSurface`) during sliding.
  - Players now smoothly glide across 1-block floor gaps and holes without getting snagged on the edge.
  - Natural gravity is maintained when sliding off edges or cliffs.
- Renamed project to `sliding-mod`.

### `v1.0.0`
- Initial release featuring smooth sliding physics, tactical knee-slide model pose, 1st-person camera tilt & dynamic FOV, slide jump boost, and multiplayer sync.

---

## 🛠️ Building & Setup

### Requirements
- Java 17 or higher
- Minecraft 1.20.1
- Fabric Loader `0.14.21` or higher
- Fabric API `0.92.12+1.20.1`

### Build Jar
```bash
./gradlew build
```
Built jar files will be located in `build/libs/`:
- `sliding-mod-1.1.0.jar`

### Test Client
```bash
./gradlew runClient
# Or on Windows, double-click run_game.bat
```

---

## 📄 License
This project is licensed under CC0-1.0.
