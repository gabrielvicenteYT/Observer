package net.observer.check.checks.client;

import com.outil.event.events.ModEvent;
import net.observer.check.Check;
import org.bukkit.entity.Player;

public class ClientE extends Check {
    public ClientE(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
    }

    String mod = "autogg";
    String version = "1.0";
    String client = "Skilled Client";

    @Override
    public void onMod(ModEvent e) {
        for (String key : e.getMods().getModsMap().keySet()) {
            if (key.equals(mod) && e.getMods().getModsMap().get(key).equals(version)) {
                fail("joined with a ghost client (" + client + ")");
            }
        }
    }
}
