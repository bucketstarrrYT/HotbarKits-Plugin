<!--
  HotbarKits — repository documentation
  Animated banner and typing subtitle are rendered by external SVG services.
-->

<div align="center">

  <img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=0:101827,45:14532D,100:0EA5E9&height=210&section=header&text=HotbarKits&fontSize=58&fontColor=FFFFFF&fontAlignY=38&desc=SAVE%20IT.%20LOAD%20IT.%20PLAY%20YOUR%20WAY.&descAlignY=61&descSize=15&animation=fadeIn" alt="HotbarKits banner" />

  <a href="https://git.io/typing-svg">
    <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=19&duration=2600&pause=900&color=86EFAC&center=true&vCenter=true&width=620&lines=Your+inventory%2C+saved+as+a+kit.;Save+and+reuse+your+favorite+setups.;Manage+kits+with+simple+commands." alt="Animated HotbarKits tagline" />
  </a>

  <br/>

  ![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-3C8527?style=for-the-badge&logo=minecraft&logoColor=white)
  ![Platform](https://img.shields.io/badge/Platform-Paper-20232A?style=for-the-badge)
  ![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
  ![Version](https://img.shields.io/badge/Plugin_Version-1.0.0-0EA5E9?style=for-the-badge)

  <br/><br/>

  **A lightweight Paper plugin for saving, loading, sharing, listing, and deleting inventory kits.**

  [Features](#-features) · [Installation](#-installation) · [Commands](#-commands) · [Build](#-building-from-source)

</div>

---

## ✨ Features

- **Save kits:** Save your current storage inventory, equipped armor, and extra inventory slots under a name.
- **Give kits:** Load a saved kit onto a specific player who is online.
- **Give to everyone:** Distribute one saved kit to every player currently online.
- **List kits:** Display all kits stored by the plugin.
- **Remove kits:** Delete a saved kit when it is no longer needed.
- **Tab completion:** Suggests subcommands, saved kit names, and online player names where applicable.
- **Persistent YAML storage:** Each kit is stored in its own YAML file inside the plugin data folder.

## 📥 Installation

1. Download the plugin JAR from the repository's [Releases](https://github.com/bucketstarrrYT/HotbarKits-Plugin/releases) page, when a release is available, or build it yourself using the instructions below.
2. Stop your Paper server.
3. Place `HotbarKits-1.0.0.jar` in the server's `plugins/` folder.
4. Start the server and check the console for the plugin load message.
5. Run `/kit` to view the available command syntax.

> **Requirements:** Paper server targeting Minecraft **1.21.11** and **Java 21**. The project compiles against the Paper API `1.21.11-R0.1-SNAPSHOT`.

## 🎮 Commands

| Command | What it does |
|---|---|
| `/kit save <kitname>` | Save your current storage inventory, armor, and extra slots as a kit. Players only. |
| `/kit give <kitname> <player>` | Load a saved kit onto the named online player. |
| `/kit giveall <kitname>` | Load a saved kit onto every online player. |
| `/kit list` | Show the names of all saved kits. |
| `/kit remove <kitname>` | Delete the named saved kit. |

### Examples

```text
/kit save pvp
/kit list
/kit give pvp Steve
/kit giveall pvp
/kit remove pvp
```

Kit names must be **1–32 characters** and may contain only letters (`A–Z`), numbers (`0–9`), underscores (`_`), and hyphens (`-`). Names are stored in lowercase.

## 🔐 Permissions

| Permission | Default | Access |
|---|---|---|
| `hotbarkits.manage` | Operators | Allows use of the `/kit` command and its subcommands. |

The permission is attached to the root `/kit` command, so it controls all listed subcommands together.

## ⚠️ Important behavior

- **Giving a kit replaces inventory contents.** The plugin sets the target's storage, armor, and extra inventory slots to the saved values. It does not merge the kit into the target's current items.
- `/kit give` requires the target player to be online; the name is matched exactly.
- `/kit giveall` affects only players online at the time the command is run.
- `/kit save` can only be run by a player, because it saves the sender's own inventory.
- This version saves inventory contents only. It does not save ender-chest contents, experience, health, hunger, potion effects, or player location.

## 💾 Where kits are stored

After the plugin starts, it creates a `kits` directory in its data folder. Each saved kit is stored as a separate YAML file:

```text
plugins/
└── HotbarKits/
    └── kits/
        ├── pvp.yml
        ├── starter.yml
        └── builder.yml
```

Back up this folder with your server backups to keep your saved kits safe.

## 🧱 Building from source

You need **Java 21** and a local **Gradle** installation.

```bash
git clone https://github.com/bucketstarrrYT/HotbarKits-Plugin.git
cd HotbarKits-Plugin
gradle clean build
```

The plugin JAR is generated in:

```text
build/libs/HotbarKits-1.0.0.jar
```

## 🗂️ Project structure

```text
src/
└── main/
    ├── java/com/kodari/hotbarkits/
    │   ├── HotbarKitsPlugin.java
    │   ├── command/KitCommand.java
    │   └── kit/KitManager.java
    └── resources/
        └── plugin.yml
build.gradle
settings.gradle
```

## 🧰 Built with

- [Java 21](https://adoptium.net/)
- [Paper API](https://papermc.io/)
- [Gradle](https://gradle.org/)

---

<div align="center">

  **Made for Minecraft server owners who want quick, reusable inventory setups.**

  <a href="https://github.com/bucketstarrrYT">
    <img src="https://img.shields.io/badge/Developer-DeepuRana-181717?style=for-the-badge&logo=github&logoColor=white" alt="DeepuRana on GitHub" />
  </a>

  <br/><br/>

  <sub>HotbarKits · Save it once. Use it whenever you need it.</sub>

</div>
