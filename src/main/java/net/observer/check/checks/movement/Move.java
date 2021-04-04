package net.observer.check.checks.movement;

import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;
import com.outil.packet.type.C03PacketPlayer;
import com.outil.util.MathUtil;
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
    int air = 0;
    int ground = 0;
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
    int serverAir = 0;
    int serverGround = 0;
    double x = 0;
    double y = 0;
    double z = 0;
    double motionX = 0;
    double motionY = 0;
    double motionZ = 0;
    boolean serverOnGround = false;
    float landMovementFactor = 0;
    float jumpMovementFactor = 0;
    float airSpeed = 0.02F;
    double speedBuffer = 0;

    @Override
    public void onMove(MoveEvent e) {
        // SETTING UP LAST VARIABLES //
        lastDeltaX = deltaX;
        lastDeltaY = deltaY;
        lastDeltaZ = deltaZ;
        lastDelta = delta;
        lastYaw = yaw;
        // SETTING UP CURRENT VARIABLES //
        playerX = e.getTo().getX();
        playerY = e.getTo().getY();
        playerZ = e.getTo().getZ();
        deltaX = e.getTo().getX() - e.getFrom().getX();
        deltaY = e.getTo().getY() - e.getFrom().getY();
        deltaZ = e.getTo().getZ() - e.getFrom().getZ();
        delta = Math.hypot(deltaX, deltaZ);
        yaw = e.getTo().getYaw();
        // UPDATING THE PLAYER'S CONDITION //
        updateCondition(e);
        // RUNNING CHECKS //
        prediction(e);
        speed(e);
        step(e);
        groundspoof(e);
        // REDUCING VIOLATION LEVEL //
        pass();
    }

    public void updateCondition(MoveEvent e) {
        air = e.isOnGround() ? 0 : Math.min(air + 1, 100);
        ground = e.isOnGround() ? Math.min(ground + 1, 100) : 0;
        landMovementFactor = e.getPlayer().isSprinting() ? 0.1F : 0.13F;
    }

    public void prediction(MoveEvent e) {

    }

    public void speed(MoveEvent e) {
        /*
          basic horizontal prediction checks
                                            */
        if (ground > 2) {
            double predictionX = lastDeltaX * 0.6F * 0.91F;
            double predictionZ = lastDeltaZ * 0.6F * 0.91F;
            double diff = Math.hypot(deltaX - predictionX, deltaZ - predictionZ);
            if (diff > 0.13F) {
                if (diff > 0.14F || ++speedBuffer > 2) {
                    fail("moved unexpectedly (Speed)", "diff: " + diff);
                }
            } else {
                speedBuffer = MathUtil.rt(speedBuffer, 0.25);
            }
        }

        if (air > 2) {
            double predictionX = lastDeltaX * 0.91F;
            double predictionZ = lastDeltaZ * 0.91F;
            double diff = Math.hypot(deltaX - predictionX, deltaZ - predictionZ);
            if (diff > 0.026F) {
                if (diff > 0.0325F || ++speedBuffer > 2) {
                    fail("moved unexpectedly (Speed)", "diff: " + diff);
                }
            } else {
                speedBuffer = MathUtil.rt(speedBuffer, 0.25);
            }
        }
    }

    public void step(MoveEvent e) {

    }

    public void groundspoof(MoveEvent e) {

    }
}
