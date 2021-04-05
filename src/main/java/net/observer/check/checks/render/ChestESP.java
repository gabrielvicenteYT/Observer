package net.observer.check.checks.render;

import com.outil.OUtil;
import com.outil.event.events.ChunkLoadEvent;
import com.outil.event.events.MoveEvent;
import com.outil.location.Loc;
import com.outil.util.PlayerUtil;
import net.observer.check.Check;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class ChestESP extends Check {
    public ChestESP(Player player, String name, boolean enabled, boolean experimental, boolean punishable, int max, int min, double reduce, double bufferCeil, double bufferMax, double bufferReduce, double preVlCeil, double preVlMax, double preVlReduce, int chunkLoad) {
        super(player, name, enabled, experimental, punishable, max, min, reduce, bufferCeil, bufferMax, bufferReduce, preVlCeil, preVlMax, preVlReduce);
        this.chunkLoad = chunkLoad;
    }

    final int chunkLoad;
    int lastCheck = 0;
    long chunk = System.currentTimeMillis();

    @Override
    public void onMove(MoveEvent e) {
        if (!enabled)
            return;
        if (System.currentTimeMillis() - chunk < 1250) {
            lastCheck = 0;
            return;
        }
        if (++lastCheck > 15) {
            List<Block> chests = PlayerUtil.getChests(player, e.getTo(), chunkLoad);
            for (Block chest : chests) {
                if (PlayerUtil.canSeeBlock(player, e.getTo(), new Loc(chest.getLocation()))) {
                    OUtil.getInstance().resetBlock(player, chest.getLocation());
                } else {
                    OUtil.getInstance().removeBlock(player, chest.getLocation());
                }
            }
            lastCheck = 0;
        }
    }

    @Override
    public void onChunkLoad(ChunkLoadEvent e) {
        chunk = System.currentTimeMillis();
    }
}
