package net.observer.check.checks.other;

import com.outil.event.events.PacketEvent;
import com.outil.packet.type.C03PacketPlayer;
import net.observer.check.Check;
import org.bukkit.entity.Player;

public class Test extends Check {
    public Test(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
    }

    @Override
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            debug("test");
        }
    }
}
