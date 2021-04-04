package net.observer.banwave;

import com.outil.OUtil;
import com.outil.event.OListener;
import com.outil.event.events.ModEvent;
import com.outil.event.events.PacketEvent;

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
}