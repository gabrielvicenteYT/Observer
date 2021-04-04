package net.observer.check.checks.movement;

import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;
import com.outil.packet.type.C03PacketPlayer;
import net.observer.check.Check;
import org.bukkit.entity.Player;

public class Move extends Check {
    public Move(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
    }

    @Override
    public void onMove(MoveEvent e) {

    }

    public void prediction(MoveEvent e) {

    }

    public void speed(MoveEvent e) {

    }

    public void step(MoveEvent e) {

    }

    public void groundspoof(MoveEvent e) {

    }
}
