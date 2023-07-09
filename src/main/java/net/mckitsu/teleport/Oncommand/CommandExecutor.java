package net.mckitsu.teleport.Oncommand;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CommandExecutor implements Listener, org.bukkit.command.CommandExecutor {
    public final Map<Player, PlayerParameter> teleportMap;
    public CommandExecutor() {
        this.teleportMap = new ConcurrentHashMap<>();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equals("tpa")) {
            if (args.length == 0) {
                return false;
            }
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p1 = (Player) sender;
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 == null) {
                p1.sendMessage("§c§l找不到該玩家!");
                return true;
            }
            if (teleportMap.containsKey(p1) ) {
                p1.sendMessage("§c§l你不能同時送出多個邀請!");
                return true;
            }
            if (teleportMap.containsKey(p2)) {
                p1.sendMessage("§c§l該玩家忙碌中");
                return true;
            }
            if (p1 == p2) {
                p1.sendMessage("§c§l你不可以傳送自己!");
                return true;
            }
            teleportMap.put(p1, new PlayerParameter(p2.getUniqueId(), p1.getLocation(), Action.TPA));
            teleportMap.put(p2, new PlayerParameter(p1.getUniqueId(), p2.getLocation(), Action.GET_TPA));
            p1.sendMessage("§e§l已傳送邀請給" + p2.getName());
            p2.sendMessage("§b§l" + p1.getName() + "§e§l想要傳送到你的位置 輸入§5§l/tpaccept §e§l或 §5§l/tok §e§l接受");
            p2.sendMessage("§e§l輸入§5§l/tpdeny §e§l或 §5§l/tno §e§l拒絕");
            return true;
        }

        if (command.getName().equals("tpahere")) {
            if (args.length == 0) {
                return false;
            }
            if (!(sender instanceof Player)) {
                return true;
            }
            Player p1 = (Player) sender;
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 == null) {
                p1.sendMessage("§c§l找不到該玩家");
                return true;
            }
            if (teleportMap.containsKey(p1) ) {
                p1.sendMessage("§c§l你不能同時送出多個邀請!");
                return true;
            }
            if (teleportMap.containsKey(p2)) {
                p1.sendMessage("§c§l該玩家忙碌中");
                return true;
            }
            if (p1 == p2) {
                p1.sendMessage("§c§l你不可以傳送自己!");
                return true;
            }
            teleportMap.put(p1, new PlayerParameter(p2.getUniqueId(), p2.getLocation(), Action.TPAHERE));
            teleportMap.put(p2, new PlayerParameter(p1.getUniqueId(), p1.getLocation(), Action.GET_TPAHERE));
            p1.sendMessage("§e§l已傳送邀請給" +  "§b§l" + p2.getName());
            p2.sendMessage( "§b§l" + p1.getName() + "§e§l想要你傳送到他的位置 輸入§5§l/tpaccept §e§l或 §5§l/tok §e§l接受");
            p2.sendMessage( "§e§l輸入§5§l/tpdeny §e§l或 §5§l/tno §e§l拒絕");
            return true;
        }

        if (command.getName().equals("tpaccept") || command.getName().equals("tok")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player p1 = (Player) sender;
            if (teleportMap.containsKey(p1)) {
                PlayerParameter para1 = teleportMap.get(p1);
                if (Action.GET_TPA == para1.action) {
                    Player p2 = Bukkit.getPlayer(para1.uuid);
                    p1.sendMessage("§7§l已接受玩家請求，準備傳送...");
                    p2.sendMessage("§e§l玩家" + "§b§l" + p1.getName() + "§e§l玩家已接受傳送請求，準備傳送...");
                    p2.teleport(p1.getLocation());
                    teleportMap.remove(p1);
                    teleportMap.remove(p2);
                    return true;
                }
                if (Action.GET_TPAHERE == para1.action) {
                    Player p2 = Bukkit.getPlayer(para1.uuid);
                    p1.sendMessage("§7§l已接受玩家請求，準備傳送...");
                    p2.sendMessage("§e§l玩家" + "§b§l" + p1.getName() + "§e§l已接受傳送請求，準備傳送...");
                    p1.teleport(p2.getLocation());
                    teleportMap.remove(p1);
                    teleportMap.remove(p2);
                    return true;
                }
            }
            return true;
        }

        if (command.getName().equals("tpdeny") || command.getName().equals("tno")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            Player p1 = (Player) sender;
            if (teleportMap.containsKey(p1)) {
                PlayerParameter para1 = teleportMap.get(p1);
                if ((Action.GET_TPA != para1.action) && (Action.GET_TPAHERE != para1.action)) {
                    return true;
                }
                Player p2 = Bukkit.getPlayer(para1.uuid);
                p1.sendMessage("§e§l已拒絕玩家請求");
                p2.sendMessage("§e§l玩家" +  "§b§l" + p1.getName() + "§e§l已拒絕傳送請求");
                teleportMap.remove(p1);
                teleportMap.remove(p2);
            }
        }

        if(command.getName().equals("tpcancel")){
            if (!(sender instanceof Player)) {
                return false;
            }
            Player p1 = (Player) sender;
            PlayerParameter para1 = teleportMap.get(p1);
            if(para1 == null){
                p1.sendMessage("§e§l你沒有送出中的邀請!");
                return true;
            }
            Player p2 = Bukkit.getPlayer(para1.uuid);
            PlayerParameter para2 = teleportMap.get(p2);
            if (teleportMap.containsKey(p2)){
                if (!((Action.GET_TPA != para2.action) && (Action.GET_TPAHERE != para2.action))) {
                    p1.sendMessage("§e§l已取消傳送請求");
                    p2.sendMessage("§e§l玩家" +  "§b§l" + p2.getName() + "§e§l已取消傳送請求");
                    teleportMap.remove(p1);
                    teleportMap.remove(p2);
                    return true;
                }
            }
        }
        return true;
    }
}
