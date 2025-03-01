package com.chunkslab.gestures.server;

import com.chunkslab.gestures.api.server.IServerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;

import java.util.Collection;
import java.util.stream.Collectors;

public class ServerManager implements IServerManager {
    @Override
    public Collection<String> getAllOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toSet());
    }
}