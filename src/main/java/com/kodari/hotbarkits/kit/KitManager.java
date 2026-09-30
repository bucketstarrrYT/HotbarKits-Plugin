package com.kodari.hotbarkits.kit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class KitManager {
    private static final Pattern VALID_NAME = Pattern.compile("[A-Za-z0-9_-]{1,32}");
    private final File kitsDirectory;

    public KitManager(JavaPlugin plugin) throws IOException {
        this.kitsDirectory = new File(plugin.getDataFolder(), "kits");
        Files.createDirectories(kitsDirectory.toPath());
    }

    public boolean isValidKitName(String name) {
        return name != null && VALID_NAME.matcher(name).matches();
    }

    public void saveKit(String name, Player player) throws IOException {
        YamlConfiguration data = new YamlConfiguration();
        for (int slot = 0; slot < player.getInventory().getStorageContents().length; slot++) {
            data.set("storage." + slot, player.getInventory().getStorageContents()[slot]);
        }
        for (int slot = 0; slot < player.getInventory().getArmorContents().length; slot++) {
            data.set("armor." + slot, player.getInventory().getArmorContents()[slot]);
        }
        for (int slot = 0; slot < player.getInventory().getExtraContents().length; slot++) {
            data.set("extra." + slot, player.getInventory().getExtraContents()[slot]);
        }
        data.save(getKitFile(name));
    }

    public boolean giveKit(String name, Player player) {
        File file = getKitFile(name);
        if (!file.isFile()) {
            return false;
        }

        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        ItemStack[] storage = new ItemStack[player.getInventory().getStorageContents().length];
        for (int slot = 0; slot < storage.length; slot++) {
            storage[slot] = data.getItemStack("storage." + slot);
        }
        player.getInventory().setStorageContents(storage);

        ItemStack[] armor = new ItemStack[player.getInventory().getArmorContents().length];
        for (int slot = 0; slot < armor.length; slot++) {
            armor[slot] = data.getItemStack("armor." + slot);
        }
        player.getInventory().setArmorContents(armor);

        ItemStack[] extra = new ItemStack[player.getInventory().getExtraContents().length];
        for (int slot = 0; slot < extra.length; slot++) {
            extra[slot] = data.getItemStack("extra." + slot);
        }
        player.getInventory().setExtraContents(extra);
        return true;
    }

    public List<String> listKits() {
        File[] files = kitsDirectory.listFiles((directory, filename) -> filename.endsWith(".yml"));
        List<String> kits = new ArrayList<>();
        if (files == null) {
            return kits;
        }

        for (File file : files) {
            String filename = file.getName();
            kits.add(filename.substring(0, filename.length() - 4));
        }
        kits.sort(Comparator.naturalOrder());
        return kits;
    }

    public boolean removeKit(String name) throws IOException {
        return Files.deleteIfExists(getKitFile(name).toPath());
    }

    private File getKitFile(String name) {
        if (!isValidKitName(name)) {
            throw new IllegalArgumentException("Invalid kit name");
        }
        return new File(kitsDirectory, name.toLowerCase(Locale.ROOT) + ".yml");
    }
}