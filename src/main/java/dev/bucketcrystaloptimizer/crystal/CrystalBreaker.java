package dev.bucketcrystaloptimizer.crystal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;

public final class CrystalBreaker {
    private final KeptCrystals keptCrystals;

    public CrystalBreaker(KeptCrystals keptCrystals) {
        this.keptCrystals = keptCrystals;
    }

    public void predictBreak(Minecraft client, EndCrystal crystal) {
        LocalPlayer player = client.player;
        ClientLevel level = client.level;
        if (player == null || level == null) {
            return;
        }

        // Do not predict a break the server will reject at the world border,
        // or one the player's current damage/effects cannot actually cause.
        if (!level.getWorldBorder().isWithinBounds(crystal.blockPosition()) || !AttackDamage.breaksCrystal(player)) {
            return;
        }

        keptCrystals.keep(crystal, ((SequencedLevel) level).bucketCrystalOptimizer$blockSequence());
    }
}
