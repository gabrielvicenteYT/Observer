package net.observer.check;

import net.observer.check.checks.client.*;
import net.observer.check.checks.combat.*;
import net.observer.check.checks.movement.*;
import net.observer.check.checks.render.*;
import net.observer.check.checks.world.ScaffoldA;
import net.observer.config.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;

public class CheckManager implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        List<Check> checks = new ArrayList<>();
        Player player = e.getPlayer();
        CheckType type = null;
        String name = null;
        // CLIENT
        type = CheckType.CLIENT;
        name = "Client (A)"; checks.add(new ClientA(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        name = "Client (B)"; checks.add(new ClientB(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        name = "Client (C)"; checks.add(new ClientC(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        name = "Client (D)"; checks.add(new ClientD(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        name = "Client (E)"; checks.add(new ClientE(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        // COMBAT
        type = CheckType.COMBAT;
        name = "HitBox (A)"; checks.add(new HitBoxA(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        name = "HitBox (B)"; checks.add(new HitBoxB(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        // MOVEMENT
        type = CheckType.MOVEMENT;
        name = "Move"; checks.add(new Move(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        // OTHER
        type = CheckType.OTHER;

        // PACKET
        type = CheckType.PACKET;

        // PACKET
        type = CheckType.RENDER;
        name = "ChestESP"; checks.add(new ChestESP(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6], getInteger(name, type, "chunkLoad")));
        // WORLD
        type = CheckType.WORLD;
        name = "Scaffold (A)"; checks.add(new ScaffoldA(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
    }

    public Boolean[] getBooleans(String name, CheckType checkType) {
        Boolean[] values = new Boolean[12];
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + ".";
        }
        values[0] = Config.getBooleanFromConfig(path + "enabled");
        values[1] = Config.getBooleanFromConfig(path + "experimental");
        values[2] = Config.getBooleanFromConfig(path + "punishable");
        return values;
    }

    public Integer[] getIntegers(String name, CheckType checkType) {
        Integer[] values = new Integer[12];
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + ".";
        }
        values[0] = Config.getIntegerFromConfig(path + "max");
        values[1] = Config.getIntegerFromConfig(path + "min");
        return values;
    }

    public Double[] getDoubles(String name, CheckType checkType) {
        Double[] values = new Double[12];
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + ".";
        }
        values[0] = Config.getDoubleFromConfig(path + "reduce");
        values[1] = Config.getDoubleFromConfig(path + "bufferCeil");
        values[2] = Config.getDoubleFromConfig(path + "bufferMax");
        values[3] = Config.getDoubleFromConfig(path + "bufferReduce");
        values[4] = Config.getDoubleFromConfig(path + "preVlCeil");
        values[5] = Config.getDoubleFromConfig(path + "preVlMax");
        values[6] = Config.getDoubleFromConfig(path + "preVlReduce");
        return values;
    }

    public boolean getBoolean(String name, CheckType checkType, String key) {
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + "." + key;
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + "." + key;
        }
        return Config.getBooleanFromConfig(path);
    }

    public int getInteger(String name, CheckType checkType, String key) {
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + "." + key;
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + "." + key;
        }
        return Config.getIntegerFromConfig(path);
    }

    public double getDouble(String name, CheckType checkType, String key) {
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check.toLowerCase() + "." + type.toLowerCase() + "." + key;
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + name.toLowerCase() + "." + key;
        }
        return Config.getDoubleFromConfig(path);
    }
}
