package net.observer.check.checks.combat;

import com.outil.event.events.HitEvent;
import com.outil.event.events.MoveEvent;
import com.outil.location.Loc;
import com.outil.util.MathUtil;
import com.outil.util.PlayerUtil;
import net.observer.check.Check;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HitBoxB extends Check {
    public HitBoxB(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
    }

    boolean check = false;
    Loc entityLoc = new Loc();

    @Override
    public void onMove(MoveEvent e) {
        if (check) {
            if (!PlayerUtil.lookingAtEntity(e.getTo(), entityLoc)) {
                if (++buffer > 1.4) {
                    fail("tried to hit outside an entity's hitbox");
                }
            } else {
                buffer = MathUtil.rt(buffer, 0.6);
            }
            check = false;
        }
    }

    @Override
    public void onHit(HitEvent e) {
        check = true;
        Location loc = e.getEntity().getLocation();
        entityLoc = new Loc(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }
}
