package net.observer.check.checks.world;

import com.outil.enums.EnumFacing;
import com.outil.event.events.HitEvent;
import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;
import com.outil.location.Loc;
import com.outil.packet.type.C08PacketPlayerBlockPlacement;
import com.outil.util.PlayerUtil;
import net.observer.check.Check;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ScaffoldA extends Check {
    public ScaffoldA(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
    }

    boolean check = false;
    Loc blockLoc = new Loc();
    Loc playerLoc = new Loc();

    @Override
    public void onMove(MoveEvent e) {
        playerLoc = e.getTo();
        if (check) {
            if (!PlayerUtil.lookingAtBlock(playerLoc, blockLoc)) fail("tried to place block out of line of sight");
            check = false;
        }
    }

    @Override
    public void onPacket(PacketEvent e) {
        if (e.getPacket() instanceof C08PacketPlayerBlockPlacement) {
            C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement) e.getPacket();
            if (packet.getEnumFacing() != EnumFacing.NONE && e.getPlayer().getItemInHand().getType().isBlock()) {
                blockLoc = new Loc(packet.getBlockPosition());
                if (!PlayerUtil.lookingAtBlock(playerLoc, blockLoc)) check = true;
            }
        }
    }
}
