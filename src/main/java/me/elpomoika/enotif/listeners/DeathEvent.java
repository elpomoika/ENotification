package me.elpomoika.enotif.listeners;

import me.elpomoika.enotif.utils.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathEvent implements Listener {
    private Config config;

    public DeathEvent(Config config) {
        this.config = config;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer != null) {
            config.sendDeathMessage(player, killer);
        } else {
            player.sendMessage(ChatColor.BLACK + "Вы скончались при неизвестных обстоятельствах");
        }
    }
}
