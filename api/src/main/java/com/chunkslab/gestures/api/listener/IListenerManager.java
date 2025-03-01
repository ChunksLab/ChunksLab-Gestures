package com.chunkslab.gestures.api.listener;

import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface IListenerManager {

    void enable();

    void disable();

    void register(@NotNull Listener listener);

    Optional<Listener> getListener(@NotNull Class<? extends Listener> clazz);

}