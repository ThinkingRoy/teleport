package net.mckitsu.teleport.Oncommand;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.time.Instant;

import javax.swing.*;
import java.util.UUID;

public class PlayerParameter {
    public UUID uuid;

    public int count;

    public Location location;

    public Action action;

    public PlayerParameter (UUID uuid, Location location, Action action){
        this.uuid = uuid;
        this.count = 0;
        this.location = location;
        this.action = action;
    }
}
