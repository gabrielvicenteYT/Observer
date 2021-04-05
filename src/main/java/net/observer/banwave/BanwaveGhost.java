package net.observer.banwave;

import com.outil.OUtil;
import com.outil.event.OListener;
import com.outil.event.events.*;

public class BanwaveGhost implements OListener {
    public BanwaveGhost() {
        OUtil.getInstance().addListener(this);
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

    }

    @Override
    public void onMod(ModEvent modEvent) {

    }

    @Override
    public void onMove(MoveEvent moveEvent) {

    }

    @Override
    public void onHit(HitEvent hitEvent) {

    }

    @Override
    public void onChunkLoad(ChunkLoadEvent chunkLoadEvent) {

    }
}