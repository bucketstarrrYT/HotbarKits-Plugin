package dev.bucketcrystaloptimizer;

import dev.bucketcrystaloptimizer.crystal.CrystalBreaker;
import dev.bucketcrystaloptimizer.crystal.KeptCrystals;
import dev.bucketcrystaloptimizer.crystal.PastKeptCrystal;

/**
 * Holds the client-side crystal prediction state.
 *
 * The optimizer does not create, duplicate, or alter interaction packets. It
 * locally predicts a successful crystal break so the next genuine click can
 * use vanilla's normal crystal-placement path without waiting for the visual
 * entity-removal update from the server.
 */
public final class BucketCrystalOptimizer {
    private static final BucketCrystalOptimizer INSTANCE = new BucketCrystalOptimizer();

    private final KeptCrystals keptCrystals = new KeptCrystals();
    private final CrystalBreaker crystalBreaker = new CrystalBreaker(keptCrystals);
    private final PastKeptCrystal pastKeptCrystal = new PastKeptCrystal(keptCrystals);

    private BucketCrystalOptimizer() {
    }

    public static BucketCrystalOptimizer get() {
        return INSTANCE;
    }

    public static void initialize() {
        System.getLogger("BucketCrystalOptimizer").log(
                System.Logger.Level.INFO,
                "Loaded. Crystal prediction is client-side; vanilla interaction packets remain unchanged."
        );
    }

    public KeptCrystals keptCrystals() {
        return keptCrystals;
    }

    public CrystalBreaker crystalBreaker() {
        return crystalBreaker;
    }

    public PastKeptCrystal pastKeptCrystal() {
        return pastKeptCrystal;
    }
}
