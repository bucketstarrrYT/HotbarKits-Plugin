package dev.bucketcrystaloptimizer.crystal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * Preserves normal aim behavior while a locally predicted-broken crystal is
 * still present in the server's world state. Only a normal player click is
 * allowed to act on the next crystal or the valid crystal base behind it.
 */
public final class PastKeptCrystal {
    private final KeptCrystals keptCrystals;
    private HitResult originalHit;
    private EntityHitResult nextCrystal;
    private BlockHitResult crystalBase;

    public PastKeptCrystal(KeptCrystals keptCrystals) {
        this.keptCrystals = keptCrystals;
    }

    private static boolean isCrystal(ItemStack stack) {
        return stack.is(Items.END_CRYSTAL);
    }

    public void attackStarted(Minecraft client) {
        HitResult behind = behindKeptCrystal(client);
        if (behind instanceof EntityHitResult target && target.getEntity() instanceof EndCrystal) {
            originalHit = client.hitResult;
            nextCrystal = target;
            client.hitResult = target;
        }
    }

    public void attackEnded(Minecraft client) {
        if (nextCrystal != null) {
            client.hitResult = originalHit;
        }
        originalHit = null;
        nextCrystal = null;
    }

    public void useStarted(Minecraft client) {
        LocalPlayer player = client.player;
        if (player == null || (!isCrystal(player.getMainHandItem()) && !isCrystal(player.getOffhandItem()))) {
            return;
        }

        HitResult behind = behindKeptCrystal(client);
        if (!(behind instanceof BlockHitResult block) || block.getType() != HitResult.Type.BLOCK) {
            return;
        }

        ClientLevel level = client.level;
        BlockPos pos = block.getBlockPos();
        BlockState state = level.getBlockState(pos);
        if ((!state.is(Blocks.OBSIDIAN) && !state.is(Blocks.BEDROCK)) || !level.isEmptyBlock(pos.above())) {
            return;
        }

        originalHit = client.hitResult;
        crystalBase = block;
    }

    public void handTried(Minecraft client, ItemStack stack) {
        if (crystalBase != null) {
            client.hitResult = isCrystal(stack) ? crystalBase : originalHit;
        }
    }

    public void useEnded(Minecraft client) {
        if (crystalBase != null) {
            client.hitResult = originalHit;
        }
        originalHit = null;
        crystalBase = null;
    }

    private HitResult behindKeptCrystal(Minecraft client) {
        LocalPlayer player = client.player;
        ClientLevel level = client.level;
        if (player == null || level == null || player.isSpectator()) {
            return null;
        }
        if (!(client.hitResult instanceof EntityHitResult target) || !keptCrystals.isHidden(target.getEntity())) {
            return null;
        }
        return keptCrystals.seePast(() -> pick(client, player));
    }

    private static HitResult pick(Minecraft client, LocalPlayer player) {
        HitResult savedHit = client.hitResult;
        Entity savedCrosshairEntity = client.crosshairPickEntity;
        client.gameRenderer.pick(1.0F);
        HitResult behind = client.hitResult;
        client.hitResult = savedHit;
        client.crosshairPickEntity = savedCrosshairEntity;
        return behind;
    }
}
