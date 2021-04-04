package net.observer.check;

import com.outil.OUtil;
import com.outil.chat.Chat;
import com.outil.event.PlayerListener;
import com.outil.util.MathUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.observer.Observer;
import net.observer.banwave.BanwaveType;
import net.observer.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;

public class Check extends PlayerListener {
    public Player player;
    public String name;
    public boolean enabled;
    public boolean experimental;
    public boolean punishable;
    public int max;
    public int min;
    public double reduce;
    public double buffer;
    public double bufferCeil;
    public double bufferMax;
    public double bufferReduce;
    public double preVl;
    public double preVlCeil;
    public double preVlMax;
    public double preVlReduce;
    
    double vl = 0;

    public Check(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min,
                 double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax,
                 double preVlReduce) {
        super(player);
        this.player = player;
        this.name = name;
        this.enabled = enabled;
        this.experimental = experimental;
        this.punishable = punishable;
        this.max = max;
        this.min = min;
        this.reduce = reduce;
        this.bufferCeil = bufferCeil;
        this.bufferMax = bufferMax;
        this.bufferReduce = bufferReduce;
        this.preVlCeil = preVlCeil;
        this.preVlMax = preVlMax;
        this.preVlReduce = preVlReduce;
        OUtil.getInstance().addPlayerListener(player, this);
    }

    public void debug(String debug) {
        player.sendMessage(Chat.getMessage("&bDebug &8» &7" + debug));
    }

    public void fail() {
        if (!enabled)
            return;
        int ping = 0;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException ignored) {

        }
        vl++;
        vl = MathUtil.truncate(vl);
        String cleanMessage = Chat.getMessage("&bObserver &8» &b" + player.getName() + " &7failed &b" + name + " &8(&bx" + Math.round(vl) + "&8)");
        if (experimental) {
            cleanMessage += Chat.getMessage(" &b&o(EXPERIMENTAL)");
        }
        String GameMode = player.getGameMode().name();
        TextComponent message = new TextComponent();
        message.setText(cleanMessage);
        String hoverMessage = "§7Ping: §c" + ping + "\n§7GameMode: §c" + GameMode + "\n\n§7Click to teleport...";
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
        if (vl >= min) {
            for (Player staff : Bukkit.getOnlinePlayers()) {
                if (staff.hasPermission("Observer.alerts")) {
                    staff.spigot().sendMessage(message);
                }
            }
        }
        if (vl >= max) {
            punishPlayer();
        }
    }

    public void fail(String... fail) {
        if (!enabled)
            return;
        int ping = 0;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException ignored) {

        }
        vl++;
        vl = MathUtil.truncate(vl);
        String cleanMessage = Chat.getMessage("&bObserver &8» &b" + player.getName() + " &7failed &b" + name + " &8(&bx" + Math.round(vl) + "&8)");
        if (experimental) {
            cleanMessage += Chat.getMessage(" &b&o(EXPERIMENTAL)");
        }
        String GameMode = player.getGameMode().name();
        TextComponent message = new TextComponent();
        message.setText(cleanMessage);
        StringBuilder hoverMessage = new StringBuilder("§7Ping: §c" + ping + "\n§7GameMode: §c" + GameMode + "\n" + "\n");
        for (String s : fail) {
            hoverMessage.append("§7").append(s.replace('&', '§').replace(": ", ": §b").replace("(", "(§b").replace(")", "§7)")).append("\n");
        }
        hoverMessage.append("\n§7Click to teleport...");
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMessage.toString()).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()));
        if (vl >= min) {
            for (Player staff : Bukkit.getOnlinePlayers()) {
                staff.spigot().sendMessage(message);
            }
        }
        if (vl >= max) {
            punishPlayer();
        }
    }

    public void pass() {
        vl = MathUtil.rt(vl, reduce);
        buffer = MathUtil.rt(buffer, bufferReduce);
        preVl = MathUtil.rt(preVl, preVlReduce);
    }

    public void punishPlayer() {
        if (Config.PUNISHMENT_TYPE.equalsIgnoreCase("kick")) {
            vl = 0;
            kickPlayer();
        } else if (Config.PUNISHMENT_TYPE.equalsIgnoreCase("ban")) {
            if (name.contains("Aim") || name.contains("AutoClicker") || name.contains("HitBox") || name.contains("Reach")
                    || name.contains("Interact") || name.contains("Client") || name.contains("PingSpoof")) {
                if (vl > max * 100) {
                    banPlayer();
                }
                if (!Observer.getInstance().ghostBanwaveList.contains(player.getName())) {
                    Observer.getInstance().ghostBanwaveList.add(player.getName());
                    Observer.getInstance().addBanwave(player.getName(), BanwaveType.GHOST);
                    for (Player staff : Bukkit.getOnlinePlayers()) {
                        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bObserver &8» &b" + player.getName() + " &7was added to &bGHOST &7banwave."));
                    }
                }
            } else {
                if (vl > max * 20) {
                    banPlayer();
                }
                if (!Observer.getInstance().blatantBanwaveList.contains(player.getName())) {
                    Observer.getInstance().blatantBanwaveList.add(player.getName());
                    Observer.getInstance().addBanwave(player.getName(), BanwaveType.BLATANT);
                    for (Player staff : Bukkit.getOnlinePlayers()) {
                        staff.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bObserver &8» &b" + player.getName() + " &7was added to &bBLATANT &7banwave."));
                    }
                }
            }
        } else {
            vl = 0;
        }
    }

    @SuppressWarnings("deprecation")
    public void kickPlayer() {
        Bukkit.getScheduler().runTask(Observer.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                vl = 0;
                player.kickPlayer(Chat.getMessage("&bObserver &8» &bUnfair Advantage"));
                Bukkit.broadcastMessage(Chat.getMessage("&bObserver &8» &b" + player.getName() + " &7was kicked for &bUnfair Advantage"));
            }
        });
    }

    public void banPlayer() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.BAN_CMD.replace("%player%", player.getName()));
        Bukkit.broadcastMessage(Chat.getMessage("&bObserver &8» &b" + player.getName() + " &7was banned for &bUnfair Advantage"));
        Bukkit.getConsoleSender().sendMessage(Chat.getMessage("&bObserver &8> &b" + player.getName() + " &7was punished by check &b" + name));
    }
}
