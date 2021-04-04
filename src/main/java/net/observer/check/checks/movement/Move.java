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

    // LAST TICK'S DATA //
    double lastDeltaX = 0;
    double lastDeltaY = 0;
    double lastDeltaZ = 0;
    double lastDelta = 0;
    float lastYaw = 0;
    // PLAYER DATA //
    double playerX = 0;
    double playerY = 0;
    double playerZ = 0;
    double deltaX = 0;
    double deltaY = 0;
    double deltaZ = 0;
    double delta = 0;
    float yaw = 0;
    boolean onGround = false;
    // CHECK DATA //
    double x = 0;
    double y = 0;
    double z = 0;
    double motionX = 0;
    double motionY = 0;
    double motionZ = 0;
    boolean serverGround = false;
    boolean failed = false;

    @Override
    public void onMove(MoveEvent e) {
        // SETTING UP LAST VARIABLES //
        lastDeltaX = deltaX;
        lastDeltaY = deltaY;
        lastDeltaZ = deltaZ;
        lastDelta = delta;
        // SETTING UP CURRENT VARIABLES //
        failed = false;
        playerX = e.getTo().getX();
        playerY = e.getTo().getY();
        playerZ = e.getTo().getZ();
        deltaX = e.getTo().getX() - e.getFrom().getX();
        deltaY = e.getTo().getY() - e.getFrom().getY();
        deltaZ = e.getTo().getZ() - e.getFrom().getZ();
        delta = Math.hypot(deltaX, deltaZ);
        yaw = e.getTo().getYaw();
        // RUNNING CHECKS //
        prediction(e);
        speed(e);
        step(e);
        groundspoof(e);
        // REDUCING VIOLATION LEVEL //
        if (!failed) {
            pass();
        }
    }

    public void prediction(MoveEvent e) {

    }

    public void speed(MoveEvent e) {
        double predictionX = 0;
        double predictionZ = 0;
    }

    public void step(MoveEvent e) {

    }

    public void groundspoof(MoveEvent e) {

    }
}
