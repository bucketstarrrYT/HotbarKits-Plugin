package dev.bucketcrystaloptimizer;

import net.fabricmc.api.ClientModInitializer;

public final class BucketCrystalOptimizerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BucketCrystalOptimizer.initialize();
    }
}
