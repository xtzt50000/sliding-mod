# 🏃 sliding-mod (Minecraft Fabric 1.20.1)

[🇰🇷 한국어 설명 (Korean Guide)](#-한국어-가이드) | [English Documentation](#-english-features--building)

A smooth, fast-paced sliding and slide-jump mod for Minecraft Fabric 1.20.1.

Featuring authentic 3rd-person knee-slide animations, immersive 1st-person camera roll and dynamic FOV, smooth physics deceleration curves, and multiplayer synchronization.

---

## 📦 모드 다운로드 및 설치 방법 (Download & Install)

### 📥 1. 다운로드 (Download)
- [👉 GitHub Releases에서 최신 버전 다운로드](https://github.com/xtzt50000/sliding-mod/releases)
- **`sliding-mod-1.1.0.jar`** 파일을 다운로드합니다.

### ⚙️ 2. 마인크래프트에 설치하기 (Installation)
1. **필수 모드 및 런처 준비**:
   - 마인크래프트 **1.20.1**
   - [Fabric Loader 1.20.1](https://fabricmc.net/use/installer/) (0.14.21 이상) 설치
   - [Fabric API 1.20.1](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?page=1&pageSize=20&version=1.20.1) 다운로드
2. **모드 파일 넣기**:
   - 키보드의 `Win + R` 키를 누르고 `%appdata%\.minecraft` 입력 후 엔터
   - `mods` 폴더 안에 다운로드한 **`sliding-mod-1.1.0.jar`** 와 **`Fabric API`** 파일을 함께 넣습니다.
3. 마인크래프트 런처를 켜고 **Fabric-Loader-1.20.1** 프로필로 게임을 시작합니다.

---

## 🇰🇷 한국어 가이드

### 🎮 인게임 조작법 (Controls)

| 동작 | 조작 방법 | 설명 |
|---|---|---|
| **슬라이딩** | **달리기** (`Ctrl` 또는 `W` 2회 연타) 중 지면에서 **앉기 (`Shift`)** | 시선 및 이동 방향으로 가속하며 미끄러집니다. |
| **슬라이드 점프** | 슬라이딩 도중 **점프 (`Space`)** | 추진력을 받아 전방으로 강하게 도약합니다 (+15% 가속). |
| **슬라이딩 멈추기** | **앉기 (`Shift`)** 키 떼기 | 일어서며 슬라이딩을 종료합니다. (단, 1.5블록 이하 틈새에서는 기어가기 유지) |

---

### ✨ 주요 기능 및 특징 (Features)

1. **부드러운 슬라이딩 물리 및 감속 곡선**:
   - 달리기 중 앉기 시 시선과 관성을 결합한 초기 가속 부스트 부여.
   - 바닐라 마인크래프트의 틱당 45% 급격한 마찰 감속 대신, `0.975 ~ 0.955`의 부드러운 감속 곡선을 적용하여 약 10~12블록 거리를 매끄럽게 미끄러집니다.
   - 내리막길(경사면)을 내려갈 때 중력 가속도가 반영되어 속도가 유지되거나 더 빨라집니다.
2. **제3자 시점 자연스러운 모션**:
   - 기존의 바닥에 엎드려 허우적대는 수영/기어가기 회전을 완전히 제거했습니다.
   - 상체를 약 18° 뒤로 젖히고, 앞다리는 앞으로 쭉 뻗고, 뒷다리는 무릎을 꿇는 실제 액션 게임 스타일의 **택티컬 니슬라이딩(Knee-slide)** 포즈가 적용됩니다.
   - 무기나 도구를 손에 쥐었을 때 전방을 조준하는 자세를 지원하며, 겉옷 레이어(자켓, 바지 등)가 완벽하게 일치합니다.
3. **1인칭 시점 연출**:
   - 시선 높이가 툭 끊기지 않고 `0.85F` 높이로 부드럽게 낮아집니다.
   - 슬라이딩 시 카메라가 약 `-3.5°` 기울어지는 액션 카메라 롤(Roll) 및 피치 틸트 연출.
   - 속도에 비례하여 시야각(FOV)이 시원하게 넓어지는 다이내믹 FOV (최대 +12%) 효과.
   - 발밑 바닥 재질에 맞는 먼지 파티클과 미끄러지는 마찰음 재생.
4. **전투 및 PvP 밸런스 히트박스 (v1.1.0)**:
   - 슬라이딩 중 히트박스 높이를 `1.2`로 넉넉하게 설정하여, 상대방이 활이나 검으로 상체 및 머리를 정상적으로 맞출 수 있습니다.
   - 1.5블록(반블록/트랩도어) 틈새는 쉽게 통과하면서도 전투 공정성을 유지합니다.
5. **낭떠러지 방지 해제 & 1블록 구멍 통과**:
   - 쉬프트 키의 가장자리 걸침(낭떠러지 방지) 판정을 슬라이딩 중 해제하여, 1칸짜리 바닥 구멍이나 틈새를 만나도 멈추지 않고 **스윽 미끄러져 통과**합니다.
   - 절벽에서 슬라이딩할 때는 수직 중력이 그대로 작용하여 앞으로 미끄러지며 자연스럽게 낙하합니다.
6. **완벽한 멀티플레이어 동기화**:
   - C2S 및 S2C 패킷 통신을 통해 다른 플레이어의 화면에서도 내 슬라이딩 모션이 끊김(러버밴딩) 없이 실시간으로 보입니다.

---

## 📜 버전 변경 내역 (Version History)

### `v1.1.0` (최신 버전)
- **전투/PvP 히트박스 밸런스**: 히트박스 높이를 2배 상향(`0.6` ➡️ `1.2`)하여 머리 및 상체 피격 판정 추가.
- **쉬프트 낭떠러지 방지 해제**: 1블록 구멍을 스윽 미끄러져 통과하도록 개선, 낙하 시 중력 가속도 자연스럽게 유지.
- 프로젝트 및 모드 ID를 `sliding-mod`로 정식 리팩토링.

### `v1.0.0`
- 최초 릴리즈: 부드러운 슬라이딩 물리, 3인칭 니슬라이딩 모션, 1인칭 카메라 틸트 및 다이내믹 FOV, 슬라이드 점프 부스트, 멀티플레이 동기화.

---

## 🇺🇸 English Features & Building

### Features
- **Smooth Sliding Physics**: Forward impulse boost, custom decay curve (`0.975 ~ 0.955`) for ~10-12 blocks slide distance, downhill momentum boost.
- **Slide Jump Boost**: Forward leap with `+15%` velocity boost.
- **3rd-Person Knee-Slide Pose**: Tactical action slide pose with torso lean back and forward extended leg.
- **1st-Person Camera Effects**: Smooth eye height (`0.85F`), camera roll (`-3.5°`), and dynamic FOV boost (`+12%`).
- **Combat Hitbox (v1.1.0)**: Height 1.2 for fair PvP hit detection.
- **Gap Sliding (v1.1.0)**: Bypassed sneak edge-lock; glide over 1-block floor gaps smoothly.
- **Multiplayer Synchronized**: Real-time packet sync for all spectators.

### Building
```bash
./gradlew build
```
Built jar: `build/libs/sliding-mod-1.1.0.jar`

### Development Test Client
```bash
./gradlew runClient
# Or on Windows, double-click run_game.bat
```

---

## 📄 License
This project is licensed under CC0-1.0.
