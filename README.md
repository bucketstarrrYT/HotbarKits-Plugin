# BucketCrystalOptimizer

A minimal client-side Fabric mod for Minecraft **1.21.11**.

## Scope

BucketCrystalOptimizer deliberately leaves Minecraft's normal End Crystal interaction path unchanged. It does not add a HUD, menus, profiles, render changes, FPS tweaks, automated clicks, packet changes, cooldown bypasses, reach changes, or combat automation.

**Important:** This build does not claim to increase the server-accepted crystal placement rate. Vanilla already processes the player's normal interaction input, and the requested safety restrictions rule out modifying that timing or sending extra interactions. The mod is intentionally passive and adds no placement delay.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.18.0 or newer
- Java 21

No Fabric API dependency is required.

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Put `bucket-crystal-optimizer-1.0.0.jar` in the instance's `mods` folder.
3. Launch the Fabric profile.

## Build from source

With JDK 21 and Gradle 9.7.1 installed:

```sh
gradle clean build
```

The remapped mod jar is written to `build/libs/bucket-crystal-optimizer-1.0.0.jar`.

## Safety and server compatibility

The mod does not change reach, attack speed, item-use cooldowns, interaction packets, server validation, or player input. No mod can guarantee that every server will allow it; always follow the server's mod rules.
