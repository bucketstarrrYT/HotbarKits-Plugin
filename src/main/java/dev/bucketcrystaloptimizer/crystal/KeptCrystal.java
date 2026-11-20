package dev.bucketcrystaloptimizer.crystal;

public interface KeptCrystal {
    void bucketCrystalOptimizer$keep(long tick, int sequence);
    void bucketCrystalOptimizer$release();
    boolean bucketCrystalOptimizer$isKept();
    long bucketCrystalOptimizer$keptAt();
    int bucketCrystalOptimizer$sequence();
}
