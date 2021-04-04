package net.observer.check.checks.movement;

import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;
import com.outil.packet.type.C13PacketPlayerAbilities;
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
    float stepHeight = 0.6F;
    double speedBuffer = 0;
    boolean creativeFly = false;

    @Override
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C13PacketPlayerAbilities) {
            if (player.getAllowFlight() && ((C13PacketPlayerAbilities) e.getPacket()).isFlying()) {
                creativeFly = true;
            }
        }
    }

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
        // RESETTING THE CREATIVEFLY VARIABLE //
        creativeFly = false;
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
          basic horizontal prediction check
                                           */
        if (air > 2 || ground > 2) {
            float add = e.isOnGround() ? 0.13F : 0.026F;
            if (player.isFlying() || creativeFly) {
                add += 0.098F;
            }
            double predictionX = e.isOnGround() ? lastDeltaX * 0.6F * 0.91F : lastDeltaX * 0.91F;
            double predictionZ = e.isOnGround() ? lastDeltaZ * 0.6F * 0.91F : lastDeltaZ * 0.91F;
            double prediction = Math.hypot(predictionX, predictionZ) + add;
            double diff = deltaX == 0 ? deltaZ == 0 ? 0 : Math.abs(deltaZ - predictionZ) : deltaZ == 0 ? Math.abs(deltaX - predictionX) : Math.hypot(deltaX - predictionX, deltaZ - predictionZ);
            if (diff > add) {
                if (++speedBuffer > 2) {
                    speedBuffer = 2;
                    fail("moved unexpectedly (MotionH)", "diff: " + diff);
                }
            } else {
                speedBuffer = MathUtil.rt(speedBuffer, 0.25);
            }
            if (delta > prediction && delta > 0.16) {
                fail("moved unexpectedly (Speed)", "delta: " + delta, "prediction: " + prediction);
            }
        }
    }

    public void step(MoveEvent e) {
        if (deltaY > stepHeight && e.isOnGround()) {
            fail("moved unexpectedly (Step)", "deltaY: " + deltaY, "stepHeight: " + stepHeight);
        }
        if (ground > 25 && deltaY < -0.05) {
            fail("moved unexpectedly", "deltaY: " + deltaY, "ground: " + ground);
        }
    }

    public void groundspoof(MoveEvent e) {

    }
}
