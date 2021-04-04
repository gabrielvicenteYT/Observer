package net.observer.check;

import net.observer.check.checks.client.*;
import net.observer.check.checks.combat.*;
import net.observer.check.checks.movement.*;
import net.observer.check.checks.other.*;
import net.observer.check.checks.packet.*;
import net.observer.check.checks.world.*;
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

        // COMBAT
        type = CheckType.COMBAT;

        // MOVEMENT
        type = CheckType.MOVEMENT;

        // OTHER
        type = CheckType.OTHER;
        name = "Move"; checks.add(new Move(player, name, getBooleans(name, type)[0], getBooleans(name, type)[1], getBooleans(name, type)[2], getIntegers(name, type)[0], getIntegers(name, type)[1], getDoubles(name, type)[0], getDoubles(name, type)[1], getDoubles(name, type)[2], getDoubles(name, type)[3], getDoubles(name, type)[4], getDoubles(name, type)[5], getDoubles(name, type)[6]));
        // PACKET
        type = CheckType.PACKET;

        // WORLD
        type = CheckType.WORLD;

    }

    public Boolean[] getBooleans(String name, CheckType checkType) {
        Boolean[] values = new Boolean[12];
        String check = name.split(" ")[0];
        String path;
        if (name.split(" ").length > 1) {
            String type = name.split(" ")[1].replace("(", "").replace(")", "");
            path = "checks." + checkType.name().toLowerCase() + "." + check + "." + type + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + check + ".";
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
            path = "checks." + checkType.name().toLowerCase() + "." + check + "." + type + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + check + ".";
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
            path = "checks." + checkType.name().toLowerCase() + "." + check + "." + type + ".";
        } else {
            path = "checks." + checkType.name().toLowerCase() + "." + check + ".";
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
}
