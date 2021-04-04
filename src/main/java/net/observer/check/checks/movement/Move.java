package net.observer.check.checks.movement;

import com.outil.event.events.ChunkLoadEvent;
import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;
import com.outil.location.Vec2D;
import com.outil.packet.type.C08PacketPlayerBlockPlacement;
import com.outil.packet.type.C13PacketPlayerAbilities;
import com.outil.util.MathUtil;
import com.outil.util.PlayerUtil;
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
    long lastChunkLoad = System.currentTimeMillis();
    boolean placed = false;
    int groundSpoofBuffer = 0;
    long balance = -200;
    long lastFlying = System.currentTimeMillis();
    double timerVar = 0;
    double timerVar2 = 0;

    @Override
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C13PacketPlayerAbilities) {
            if (player.getAllowFlight() && ((C13PacketPlayerAbilities) e.getPacket()).isFlying()) {
                creativeFly = true;
            }
        }
        if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) e.getPacket();
            placed = true;
        }
    }

    @Override
    public void onMove(MoveEvent e) {
        /*
          SETTING UP LAST TICK'S VARIABLES
                                          */
        lastDeltaX = deltaX;
        lastDeltaY = deltaY;
        lastDeltaZ = deltaZ;
        lastDelta = delta;
        lastYaw = yaw;
        /*
          SETTING UP CURRENT TICK'S VARIABLES
                                             */
        playerX = e.getTo().getX();
        playerY = e.getTo().getY();
        playerZ = e.getTo().getZ();
        deltaX = e.getTo().getX() - e.getFrom().getX();
        deltaY = e.getTo().getY() - e.getFrom().getY();
        deltaZ = e.getTo().getZ() - e.getFrom().getZ();
        delta = Math.hypot(deltaX, deltaZ);
        yaw = e.getTo().getYaw();
        /*
          UPDATING THE PLAYER'S CONDITION
                                         */
        updateCondition(e);
        /*
          RUNNING CHECKS
                        */
        timer(e);
        prediction(e);
        speed(e);
        step(e);
        groundspoof(e);
        /*
          REDUCING VIOLATION LEVEL
                                  */
        pass();
        /*
          RESETTING PACKET VARIABLES
                                    */
        creativeFly = false;
        placed = false;
        lastFlying = System.currentTimeMillis();
    }

    public void updateCondition(MoveEvent e) {
        /*
          to-do: add a proper block wrapper to OUtils, make the predictions account
                 for slipperiness, block bounding boxes, etc.
                                                                                   */
        air = e.isOnGround() ? 0 : Math.min(air + 1, 100);
        ground = e.isOnGround() ? Math.min(ground + 1, 100) : 0;
        landMovementFactor = e.getPlayer().isSprinting() ? 0.1F : 0.13F;
    }

    public void timer(MoveEvent e) {
        /*
          multi-version balance timer check
                                           */
        long rate = System.currentTimeMillis() - lastFlying;
        timerVar = ((timerVar * 14) + rate) / 15;
        if (Math.abs(timerVar - 50) < 30) {
            timerVar2 = Math.min(timerVar2 + 1, 30);
            if (timerVar2 == 10) {
                balance = -250;
            }
            if (timerVar2 == 20) {
                balance = -50;
            }
        } else {
            timerVar2 = MathUtil.rt(timerVar2, 1);
        }
        balance += 50;
        balance -= rate;
        if (balance > 10) {
            buffer = Math.min(buffer + 1, 5);
            if (buffer > 1) {
                fail("sent too many flying packets", "balance: " + balance);
            }
            balance = 0;
        }
    }

    public void prediction(MoveEvent e) {
        /*
          to-do: add a full prediction for Y
                                            */
    }

    public void speed(MoveEvent e) {
        /*
          basic horizontal movement prediction check
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
                    fail("moved unexpectedly (Strafe/Speed)", "diff: " + diff);
                }
            } else {
                speedBuffer = MathUtil.rt(speedBuffer, 0.25);
            }
            if (delta > prediction && delta > 0.2) {
                fail("moved unexpectedly (Speed)", "delta: " + delta, "prediction: " + prediction);
            }
        }
    }

    public void step(MoveEvent e) {
        /*
          detecting stepHeight changes
                                      */
        if (deltaY > stepHeight && e.isOnGround()) {
            fail("moved unexpectedly (Step)", "deltaY: " + deltaY, "stepHeight: " + stepHeight);
        }
        /*
          detecting phase/noclip/whatever module might do something as dumb as this
                                                                                   */
        if (ground > 25 && deltaY < -0.05) {
            fail("moved unexpectedly (Other)", "deltaY: " + deltaY, "ground: " + ground);
        }
        /*
          to-do: add an onSteppable exemption
                                             */
        if (playerY % 1.0 == 0 && deltaY > 0) {
            fail("moved unexpectedly (Step)", "deltaY: " + deltaY);
        }
    }

    public void groundspoof(MoveEvent e) {
        if (System.currentTimeMillis() - lastChunkLoad > 1000) {
            boolean nearGround = PlayerUtil.nearGround(player, e.getTo());
            boolean dumbGround = e.getTo().getY() % 1/64 == 0;
            boolean onGround = e.isOnGround();
            if (onGround) {
                if (!nearGround) {
                    if (!dumbGround) {
                        fail("moved unexpectedly (GroundSpoof)", "onGround: true", "1/64: false");
                    } else {
                        if (++groundSpoofBuffer > 1) {
                            fail("moved unexpectedly (Fly)", "onGround: true", "1/64: true");
                        }
                    }
                } else {
                    groundSpoofBuffer = 0;
                }
            } else {
                groundSpoofBuffer = 0;
            }
        }
    }

    @Override
    public void onChunkLoad(ChunkLoadEvent e) {
        double dist = Math.hypot(playerX - e.x, playerZ - e.z);
        if (dist < 32) {
            lastChunkLoad = System.currentTimeMillis();
        }
    }
}
