package net.observer.banwave;

import com.outil.OUtil;
import com.outil.event.OListener;
import com.outil.event.events.ChunkLoadEvent;
import com.outil.event.events.ModEvent;
import com.outil.event.events.MoveEvent;
import com.outil.event.events.PacketEvent;

public class BanwaveBlatant implements OListener {
    public BanwaveBlatant() {
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
    public void onChunkLoad(ChunkLoadEvent chunkLoadEvent) {

    }
}
