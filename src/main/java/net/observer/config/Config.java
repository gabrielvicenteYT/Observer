package net.observer.config;

import net.observer.Observer;
import org.bukkit.Bukkit;

import java.util.List;

public class Config {
    public static String PUNISHMENT_TYPE;
    public static String BAN_CMD;
    public static String LICENSE;

    public static void updateConfig() {
        try {
            PUNISHMENT_TYPE = getStringFromConfig("punishment-type");
            BAN_CMD = getStringFromConfig("ban-cmd");
            LICENSE = getStringFromConfig("license");
        } catch (Exception exception) {
            Bukkit.getLogger().severe("Could not properly load config.");
            exception.printStackTrace();
        }
    }

    public static boolean getBooleanFromConfig(String string) {
        return Observer.getInstance().getConfig().getBoolean(string);
    }

    public static String getStringFromConfig(String string) {
        return Observer.getInstance().getConfig().getString(string);
    }

    public static int getIntegerFromConfig(String string) {
        return Observer.getInstance().getConfig().getInt(string);
    }

    public static double getDoubleFromConfig(String string) {
        return Observer.getInstance().getConfig().getDouble(string);
    }

    private static List<String> getStringListFromConfig(String string) {
        return Observer.getInstance().getConfig().getStringList(string);
    }

    private static long getLongFromConfig(String string) {
        return Observer.getInstance().getConfig().getLong(string);
    }
}
