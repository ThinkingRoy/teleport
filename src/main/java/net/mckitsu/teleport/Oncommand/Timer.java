package net.mckitsu.teleport.Oncommand;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;


public class Timer extends BukkitRunnable {

    Map<Player, PlayerParameter> teleportMap;

    public Timer (Map<Player, PlayerParameter> map){
        this.teleportMap = map;
    }

    @Override
    public void run() {
        teleportMap.forEach((key, value)->{
           value.count+=1;
           if(value.count>=15){
               key.sendMessage("傳送請求已超時");
               teleportMap.remove(key);
           }
        });
    }
}
