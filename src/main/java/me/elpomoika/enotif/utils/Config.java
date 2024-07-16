package me.elpomoika.enotif.utils;

import me.elpomoika.enotif.ENotif;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;

public class Config {
    private FileConfiguration config;

    public Config(Plugin plugin) {
        // Загрузка конфига
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if(!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void sendDeathMessage(Player player, Player killer) {
        String deathMessage = config.getString("deathMessage")
                .replace("%player%", player.getName())
                .replace("%killer%", killer.getName());

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', deathMessage));
    }
}
