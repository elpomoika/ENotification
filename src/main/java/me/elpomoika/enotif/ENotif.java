package me.elpomoika.enotif;

import me.elpomoika.enotif.listeners.DeathEvent;
import me.elpomoika.enotif.listeners.PlayerJoinListener;
import me.elpomoika.enotif.listeners.PlayerLeaveListener;
import me.elpomoika.enotif.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class ENotif extends JavaPlugin implements Listener {

    private static ENotif instance;

    public static ENotif getInstance() {
        return instance;
    }

    private Config config = new Config(this);

    private boolean soundEnabled;

    private String title;

    private String subtitle;

    private Sound sound;

    private float volume;

    private float pitch;

    private int fadein;

    private int stay;

    private int fadeout;

    @Override
    public void onEnable() {
	// load config
        loadConfig();
	// register listeners
        Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
        Bukkit.getPluginManager().registerEvents(new DeathEvent(config), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerLeaveListener(), this);
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendTitle(this.title, this.subtitle, this.fadein, this.stay, this.fadeout);
        if (this.soundEnabled)
            event.getPlayer().playSound(event.getPlayer().getLocation(), this.sound, this.volume, this.pitch);
    }

    private void loadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists())
            saveResource("config.yml", false);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(configFile);
        this.title = ChatColor.translateAlternateColorCodes('&', yamlConfiguration.getString("title", ""));
        this.subtitle = ChatColor.translateAlternateColorCodes('&', yamlConfiguration.getString("subtitle", ""));
        this.soundEnabled = yamlConfiguration.getBoolean("sound", true);
        if (this.soundEnabled) {
            this.sound = Sound.valueOf(yamlConfiguration.getString("soundType", Sound.ENTITY_PLAYER_LEVELUP.name()));
            this.volume = (float)yamlConfiguration.getDouble("volume", 1.0D);
            this.pitch = (float)yamlConfiguration.getDouble("pitch", 1.0D);
        }
        this.fadein = yamlConfiguration.getInt("fadein", 20);
        this.stay = yamlConfiguration.getInt("stay", 40);
        this.fadeout = yamlConfiguration.getInt("fadeout", 20);
    }

    public void saveConfig() {
        FileConfiguration config = getConfig();
        config.set("title", this.title);
        config.set("subtitle", this.subtitle);
        config.set("sound", Boolean.valueOf(this.soundEnabled));
        if (this.soundEnabled) {
            config.set("soundType", this.sound.name());
            config.set("volume", Float.valueOf(this.volume));
            config.set("pitch", Float.valueOf(this.pitch));
        }
        config.set("fadein", Integer.valueOf(this.fadein));
        config.set("stay", Integer.valueOf(this.stay));
        config.set("fadeout", Integer.valueOf(this.fadeout));
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
