package dev.bucketcrystaloptimizer.mixin;

import dev.bucketcrystaloptimizer.BucketCrystalOptimizer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.EndCrystalItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(EndCrystalItem.class)
public class EndCrystalItemMixin {
    @ModifyVariable(method = "useOn", at = @At("STORE"), ordinal = 0)
    private List<Entity> bucketCrystalOptimizer$skipLocallyPredictedCrystals(List<Entity> obstructing) {
        return BucketCrystalOptimizer.get().keptCrystals().withoutHidden(obstructing);
    }
}
