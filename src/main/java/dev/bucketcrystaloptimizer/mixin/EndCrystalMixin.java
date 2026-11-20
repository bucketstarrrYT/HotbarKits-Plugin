package dev.bucketcrystaloptimizer.mixin;

import dev.bucketcrystaloptimizer.crystal.KeptCrystal;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EndCrystal.class)
public class EndCrystalMixin implements KeptCrystal {
    @Unique
    private boolean bucketCrystalOptimizer$kept;

    @Unique
    private long bucketCrystalOptimizer$keptAt;

    @Unique
    private int bucketCrystalOptimizer$sequence;

    @Override
    public void bucketCrystalOptimizer$keep(long tick, int sequence) {
        bucketCrystalOptimizer$kept = true;
        bucketCrystalOptimizer$keptAt = tick;
        bucketCrystalOptimizer$sequence = sequence;
    }

    @Override
    public void bucketCrystalOptimizer$release() {
        bucketCrystalOptimizer$kept = false;
    }

    @Override
    public boolean bucketCrystalOptimizer$isKept() {
        return bucketCrystalOptimizer$kept;
    }

    @Override
    public long bucketCrystalOptimizer$keptAt() {
        return bucketCrystalOptimizer$keptAt;
    }

    @Override
    public int bucketCrystalOptimizer$sequence() {
        return bucketCrystalOptimizer$sequence;
    }
}
