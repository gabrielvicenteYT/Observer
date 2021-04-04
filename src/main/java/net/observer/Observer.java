package net.observer;

import net.observer.banwave.BanwaveBlatant;
import net.observer.banwave.BanwaveGhost;
import net.observer.banwave.BanwaveType;
import net.observer.check.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Observer extends JavaPlugin {

    private static Observer instance;

    public BanwaveGhost banwaveGhost;
    public BanwaveBlatant banwaveBlatant;
    public List<String> ghostBanwaveList = new ArrayList<>();
    public List<String> blatantBanwaveList = new ArrayList<>();

    public CheckManager checkManager;

    @Override
    public void onEnable() {

        instance = this;

        banwaveGhost = new BanwaveGhost();
        banwaveBlatant = new BanwaveBlatant();
        checkManager = new CheckManager();

        Bukkit.getPluginManager().registerEvents(checkManager, instance);

    }

    @Override
    public void onDisable() {

    }

    /*
      returns this class
                        */
    public static Observer getInstance() {
        return instance;
    }

    /*
      adds a player to a banwave
                                */
    public void addBanwave(String player, BanwaveType type) {
        switch (type) {
            case BLATANT:
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.getDataFolder() + "\\banwave-blatant.yml"))) {
                    writer.write(player + "\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case GHOST:
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.getDataFolder() + "\\banwave-ghost.yml"))) {
                    writer.write(player + "\n");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }
}
