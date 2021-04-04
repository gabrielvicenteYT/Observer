package net.observer.check;

import net.observer.Observer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CheckManager implements Listener {
    public CheckManager() {
        Bukkit.getPluginManager().registerEvents(this, Observer.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        
    }
}
