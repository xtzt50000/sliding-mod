# 🏃 sliding-mod (Minecraft Fabric 1.20.1)

| English | 한국어 |
|---|---|
| A smooth, fast-paced sliding and slide-jump mod for Minecraft Fabric 1.20.1. | 마인크래프트 패브릭 1.20.1을 위한 부드럽고 빠른 슬라이딩 & 슬라이드 점프 모드입니다. |
| Featuring authentic 3rd-person knee-slide animations, immersive 1st-person camera roll and dynamic FOV, smooth physics deceleration curves, and multiplayer synchronization. | 자연스러운 3인칭 택티컬 니슬라이딩 모션, 1인칭 카메라 기울임(Roll) 및 다이내믹 FOV, 부드러운 감속 곡선, 멀티플레이어 동기화를 완벽하게 지원합니다. |

---

## 📦 Download & Installation / 다운로드 및 간편 설치

| Step / 단계 | English | 한국어 |
|:---:|---|---|
| **1. Download<br>다운로드** | Download the latest [sliding-mod-1.1.0.jar](https://github.com/xtzt50000/sliding-mod/releases) from GitHub Releases. | GitHub Releases에서 최신 버전의 [sliding-mod-1.1.0.jar](https://github.com/xtzt50000/sliding-mod/releases) 파일을 다운로드합니다. |
| **2. Requirements<br>필수 준비** | Requires **Minecraft 1.20.1**, [Fabric Loader 1.20.1](https://fabricmc.net/use/installer/) (0.14.21+), and [Fabric API 1.20.1](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?page=1&pageSize=20&version=1.20.1). | **마인크래프트 1.20.1**, [Fabric Loader 1.20.1](https://fabricmc.net/use/installer/) (0.14.21 이상), [Fabric API 1.20.1](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files/all?page=1&pageSize=20&version=1.20.1)이 필요합니다. |
| **3. Install<br>모드 넣기** | Press `Win + R`, type `%appdata%\.minecraft`, open the `mods` folder, and place **`sliding-mod-1.1.0.jar`** and **`Fabric API`** inside. | `Win + R` 키를 누르고 `%appdata%\.minecraft` 입력 후 엔터, `mods` 폴더 안에 **`sliding-mod-1.1.0.jar`** 와 **`Fabric API`** 파일을 넣습니다. |
| **4. Launch<br>게임 실행** | Select the **Fabric-Loader-1.20.1** profile in your Minecraft Launcher and start the game! | 마인크래프트 런처에서 **Fabric-Loader-1.20.1** 프로필을 선택하고 게임을 시작합니다! |

---

## 🎮 Controls / 조작법

| Action / 동작 | Key / 조작 키 | English | 한국어 |
|---|:---:|---|---|
| **Slide<br>슬라이딩** | **Sprint** + **Sneak** (`Shift`) | Press `Shift` while sprinting on the ground to slide in your looking and moving direction. | 지면에서 달리기(`Ctrl` 또는 `W` 2회 연타) 중 `Shift`를 눌러 시선 및 이동 방향으로 미끄러집니다. |
| **Slide-Jump<br>슬라이드 점프** | **Jump** (`Space`) | Press `Space` during a slide to convert slide momentum into a powerful forward leap (+15% boost). | 슬라이딩 도중 `Space`를 누르면 추진력을 받아 전방으로 강하게 도약합니다 (+15% 추가 가속). |
| **Cancel Slide<br>슬라이딩 정지** | Release **Sneak** (`Shift`) | Release `Shift` to stand back up. (Crawling is automatically kept under low <1.5 block ceilings) | `Shift` 키를 떼면 즉시 일어섭니다. (단, 1.5블록 이하 틈새에서는 끼이지 않도록 기어가기 유지) |

---

## ✨ Key Features / 주요 기능

| Feature / 기능 | English | 한국어 |
|---|---|---|
| **Smooth Physics<br>부드러운 슬라이딩 물리** | Initial impulse boost when sliding out of a sprint. Custom decay curve (`0.975 ~ 0.955`) allows ~10-12 blocks slide distance without vanilla friction cutoffs. Sliding down slopes/hills gains downhill gravity boost. | 달리기 중 앉기 시 초기 가속 부스트 부여. 바닐라의 틱당 45% 급감속 대신 `0.975 ~ 0.955` 부드러운 감속 곡선을 적용하여 약 10~12블록 슬라이딩. 내리막길 경사면 가속 지원. |
| **3rd-Person Motion<br>3인칭 자연스러운 모션** | Replaces awkward swimming/crawling animations with a tactical knee-slide pose: torso leaned back 18°, front leg extended forward, rear knee bent. Supports weapon aiming and outer skin layers. | 바닥에 엎드려 허우적대는 수영/기어가기를 제거하고, 상체를 18° 뒤로 젖히고 다리를 뻗는 액션 게임 니슬라이딩 포즈 적용. 무기 조준 모션 및 겉옷 레이어 완벽 일치. |
| **1st-Person Camera<br>1인칭 시점 연출** | Eye height smoothly drops to `0.85F`. Features action camera roll (`-3.5°`) and pitch tilt, dynamic FOV widening (up to `+12%`), block step dust particles, and sliding sound effects. | 시선 높이가 `0.85F`로 부드럽게 낮아지며, 액션 카메라 롤(`-3.5°`) 및 피치 틸트, 속도 비례 다이내믹 FOV(최대 `+12%`), 마찰음 및 바닥 먼지 파티클 연출. |
| **Combat Hitbox (v1.1.0)<br>전투/PvP 밸런스 히트박스** | Hitbox height doubled (`0.6` ➡️ `1.2`) to cover head and upper body for fair PvP/combat hit detection. Slides through 1.5-block openings (slabs/trapdoors). | 전투 및 PvP 공정성을 위해 히트박스 높이를 2배 상향(`0.6` ➡️ `1.2`)하여 머리와 상체 피격 판정 추가. 1.5블록 틈새(반블록/트랩도어)는 여유 있게 통과. |
| **Gap Sliding (v1.1.0)<br>1블록 구멍 통과** | Bypasses vanilla sneaking edge-protection (`maybeBackOffFromEdge`). Glides smoothly over 1-block floor gaps and holes without getting snagged. Natural vertical gravity is maintained. | 바닐라 쉬프트 낭떠러지 방지 판정을 해제하여 1칸짜리 바닥 구멍이나 틈새를 만나도 멈추지 않고 스윽 미끄러져 통과. 절벽에서 떨어질 때는 수직 중력이 정상 작동하여 자연스럽게 낙하. |
| **Multiplayer Sync<br>완벽한 멀티플레이 동기화** | Custom C2S and S2C network packets synchronize sliding state across all tracking players in real time with zero rubberbanding. | C2S / S2C 네트워크 패킷 통신을 통해 다른 플레이어의 화면에서도 내 슬라이딩 모션이 끊김(러버밴딩) 없이 실시간 동기화. |

---

## 📜 Version History / 버전 히스토리

| Version / 버전 | English | 한국어 |
|:---:|---|---|
| **`v1.1.0`** *(Latest)* | Combat hitbox height doubled to `1.2`, sneak edge-protection bypassed for 1-block gap sliding, project renamed to `sliding-mod`. | 전투/PvP 히트박스 높이 2배 상향(`1.2`), 1블록 구멍 통과를 위한 낭떠러지 방지 해제, `sliding-mod`로 정식 리팩토링. |
| **`v1.0.0`** | Initial release: Smooth physics, 3rd-person knee-slide animation, 1st-person camera tilt & dynamic FOV, slide-jump boost, multiplayer sync. | 최초 릴리즈: 부드러운 슬라이딩 물리, 3인칭 니슬라이딩 모션, 1인칭 카메라 틸트 및 다이내믹 FOV, 슬라이드 점프, 멀티플레이 동기화. |

---

## 🛠️ Building & Setup / 빌드 가이드

```bash
# Build mod jar file / 모드 jar 파일 빌드
./gradlew build

# Run test Minecraft client / 테스트 클라이언트 실행
./gradlew runClient
# (Windows: run_game.bat)
```
- Built jar file located at / 빌드 파일 위치: `build/libs/sliding-mod-1.1.0.jar`

---

## 📄 License / 라이선스
This project is licensed under **CC0-1.0** (이 프로젝트는 CC0-1.0 라이선스를 따릅니다).
