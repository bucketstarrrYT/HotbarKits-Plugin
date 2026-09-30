package com.kodari.hotbarkits;

import com.kodari.hotbarkits.command.KitCommand;
import com.kodari.hotbarkits.kit.KitManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class HotbarKitsPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        try {
            KitManager kitManager = new KitManager(this);
            PluginCommand kitCommand = getCommand("kit");
            if (kitCommand == null) {
                throw new IllegalStateException("The kit command is missing from plugin.yml");
            }
            KitCommand executor = new KitCommand(kitManager);
            kitCommand.setExecutor(executor);
            kitCommand.setTabCompleter(executor);
        } catch (IOException exception) {
            getLogger().severe("Could not create the kits storage directory: " + exception.getMessage());
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().isOp()) {
            event.getPlayer().sendMessage("&eThe Mod HotBarKit Is Made By BucketStar");
        }
    }
}