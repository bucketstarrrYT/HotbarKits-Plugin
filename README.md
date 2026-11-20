# BucketCrystalOptimizer

A minimal, client-side Fabric mod for **Minecraft 1.21.11**.

## What it does

When you manually attack an End Crystal and the client can predict that the hit will break it, the mod immediately hides that crystal from the local render and placement-obstruction checks. Your next genuine right-click can then follow vanilla's normal End Crystal placement path without waiting for the server's crystal-removal update to arrive at the client.

The mod keeps the crystal tracked locally until the server's normal block-state acknowledgement advances past the attack sequence. If no acknowledgement arrives (for example, a rejected or cancelled hit), the local prediction expires after a short tick-based timeout and the crystal becomes visible again.

## Packet and interaction behavior

- No automated clicks or repeated actions.
- No extra, duplicated, replayed, or accelerated interaction packets.
- No custom combat packets or packet spoofing.
- No reach, attack speed, hitbox, cooldown, or item-use timing changes.
- The client only changes its local prediction of a crystal that the player has already manually attacked.
- Placement still uses Minecraft's normal End Crystal item-use method; the server remains authoritative.

This can reduce the client-side wait between breaking one crystal and making the next manual placement attempt, especially on higher latency connections. Actual placement timing still depends on server processing, network latency, and server rules.

## Important server warning

No client mod can guarantee that every server will permit it or that you cannot be banned. This mod does not try to bypass anti-cheat, but some servers prohibit crystal optimizers or client-side entity prediction. Use it only on servers where such mods are explicitly allowed.

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.17.3 or newer
- Java 21
- No Fabric API dependency

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Put `bucket-crystal-optimizer-1.0.0.jar` in your Minecraft instance's `mods` folder.
3. Launch the Fabric profile.

## Build

With JDK 21 and Gradle 9.9.1 installed:

```sh
gradle clean build
```

The remapped mod jar is created at:

```
build/libs/bucket-crystal-optimizer-1.0.0.jar
```

The source bundle includes the MIT license and attribution for the adapted crystal-prediction code.
