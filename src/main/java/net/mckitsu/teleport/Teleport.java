package net.mckitsu.teleport;

import net.mckitsu.teleport.Oncommand.CommandExecutor;
import net.mckitsu.teleport.Oncommand.PlayerParameter;
import net.mckitsu.teleport.Oncommand.Timer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Teleport extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        CommandExecutor cx = new CommandExecutor();
        getServer().getPluginManager().registerEvents(cx, this);
        this.getCommand("tpa").setExecutor(cx);
        this.getCommand("tok").setExecutor(cx);
        this.getCommand("tpaccept").setExecutor(cx);
        this.getCommand("tno").setExecutor(cx);
        this.getCommand("tpdeny").setExecutor(cx);
        this.getCommand("tpahere").setExecutor(cx);
        this.getCommand("tpcancel").setExecutor(cx);
        BukkitTask task = new Timer(cx.teleportMap).runTaskTimer(this, 0, 20);
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
