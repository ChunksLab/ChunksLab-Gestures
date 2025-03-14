package com.chunkslab.gestures.task;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
public class SkinTask extends BukkitRunnable {

    private final GesturesPlugin plugin;

    @Getter private final Queue<GesturePlayer> skinQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        if (skinQueue.isEmpty()) return;
        GesturePlayer gesturePlayer = skinQueue.poll();
        if (gesturePlayer == null) return;
        plugin.getGesturePlayerSkin().loadOrUploadSkin(gesturePlayer, null);
    }
}