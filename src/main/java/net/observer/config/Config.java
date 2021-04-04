package net.observer.config;

import net.observer.Observer;
import org.bukkit.Bukkit;

import java.util.List;

public class Config {
    public static String PUNISHMENT_TYPE;
    public static String BAN_CMD;

    public static void updateConfig() {
        try {
            PUNISHMENT_TYPE = getStringFromConfig("punishment-type");
            BAN_CMD = getStringFromConfig("ban-cmd");
        } catch (Exception exception) {
            Bukkit.getLogger().severe("Could not properly load config.");
            exception.printStackTrace();
        }
    }

    public static boolean getBooleanFromConfig(String getBooleanFromConfig) {
        return Observer.getInstance().getConfig().getBoolean(getBooleanFromConfig);
    }

    public static String getStringFromConfig(String getStringFromConfig) {
        return Observer.getInstance().getConfig().getString(getStringFromConfig);
    }

    public static int getIntegerFromConfig(String getIntegerFromConfig) {
        return Observer.getInstance().getConfig().getInt(getIntegerFromConfig);
    }

    public static double getDoubleFromConfig(String getDoubleFromConfig) {
        return Observer.getInstance().getConfig().getDouble(getDoubleFromConfig);
    }

    private static List<String> getStringListFromConfig(String string) {
        return Observer.getInstance().getConfig().getStringList(string);
    }

    private static long getLongFromConfig(String string) {
        return Observer.getInstance().getConfig().getLong(string);
    }
}
