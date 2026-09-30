package com.kodari.hotbarkits.command;

import com.kodari.hotbarkits.kit.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class KitCommand implements CommandExecutor, TabCompleter {
    private final KitManager kitManager;

    public KitCommand(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender, label);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "save" -> save(sender, label, args);
            case "give" -> give(sender, label, args);
            case "giveall" -> giveAll(sender, label, args);
            case "list" -> list(sender, args);
            case "remove" -> remove(sender, label, args);
            default -> sendUsage(sender, label);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions;
        if (args.length == 1) {
            suggestions = List.of("save", "give", "giveall", "list", "remove");
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("give")
                || args[0].equalsIgnoreCase("giveall") || args[0].equalsIgnoreCase("remove"))) {
            suggestions = kitManager.listKits();
        } else if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            suggestions = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        } else {
            return List.of();
        }

        String prefix = args[args.length - 1].toLowerCase(Locale.ROOT);
        return suggestions.stream()
                .filter(suggestion -> suggestion.toLowerCase(Locale.ROOT).startsWith(prefix))
                .collect(Collectors.toList());
    }

    private void save(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /" + label + " save <kitname>");
            return;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can save a kit.");
            return;
        }
        if (!isValidName(sender, args[1])) {
            return;
        }

        try {
            kitManager.saveKit(args[1], player);
            sender.sendMessage("§aSaved your inventory and equipped armor as kit '" + args[1].toLowerCase(Locale.ROOT) + "'.");
        } catch (IOException exception) {
            sender.sendMessage("§cCould not save that kit.");
            exception.printStackTrace();
        }
    }

    private void give(CommandSender sender, String label, String[] args) {
        if (args.length != 3) {
            sender.sendMessage("§cUsage: /" + label + " give <kitname> <player>");
            return;
        }
        if (!isValidName(sender, args[1])) {
            return;
        }

        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage("§cThat player is not online.");
            return;
        }
        if (!kitManager.giveKit(args[1], target)) {
            sender.sendMessage("§cKit '" + args[1] + "' does not exist.");
            return;
        }

        sender.sendMessage("§aGave kit '" + args[1].toLowerCase(Locale.ROOT) + "' to " + target.getName() + ".");
        if (!target.equals(sender)) {
            target.sendMessage("§aYou received kit '" + args[1].toLowerCase(Locale.ROOT) + "'.");
        }
    }

    private void giveAll(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /" + label + " giveall <kitname>");
            return;
        }
        if (!isValidName(sender, args[1])) {
            return;
        }
        if (kitManager.listKits().stream().noneMatch(kit -> kit.equalsIgnoreCase(args[1]))) {
            sender.sendMessage("§cKit '" + args[1] + "' does not exist.");
            return;
        }

        int given = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (kitManager.giveKit(args[1], player)) {
                player.sendMessage("§aYou received kit '" + args[1].toLowerCase(Locale.ROOT) + "'.");
                given++;
            }
        }
        sender.sendMessage("§aGave kit '" + args[1].toLowerCase(Locale.ROOT) + "' to " + given + " online player(s).");
    }

    private void list(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§cUsage: /kit list");
            return;
        }
        List<String> kits = kitManager.listKits();
        if (kits.isEmpty()) {
            sender.sendMessage("§eNo kits have been saved.");
            return;
        }
        sender.sendMessage("§aSaved kits: §f" + kits.stream().collect(Collectors.joining(", ")));
    }

    private void remove(CommandSender sender, String label, String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /" + label + " remove <kitname>");
            return;
        }
        if (!isValidName(sender, args[1])) {
            return;
        }

        try {
            if (kitManager.removeKit(args[1])) {
                sender.sendMessage("§aRemoved kit '" + args[1].toLowerCase(Locale.ROOT) + "'.");
            } else {
                sender.sendMessage("§cKit '" + args[1] + "' does not exist.");
            }
        } catch (IOException exception) {
            sender.sendMessage("§cCould not remove that kit.");
            exception.printStackTrace();
        }
    }

    private boolean isValidName(CommandSender sender, String name) {
        if (kitManager.isValidKitName(name)) {
            return true;
        }
        sender.sendMessage("§cKit names must be 1-32 characters and contain only letters, numbers, '_' or '-'.");
        return false;
    }

    private void sendUsage(CommandSender sender, String label) {
        sender.sendMessage("§e/" + label + " save <kitname>");
        sender.sendMessage("§e/" + label + " give <kitname> <player>");
        sender.sendMessage("§e/" + label + " giveall <kitname>");
        sender.sendMessage("§e/" + label + " list");
        sender.sendMessage("§e/" + label + " remove <kitname>");
    }
}