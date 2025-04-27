/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.listener;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.listener.IListenerManager;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ListenerManager implements IListenerManager {

    private final Set<Listener> listeners = new HashSet<>();
    private final GesturesPlugin plugin;

    @Override
    public void enable() {
        register(new WardrobeListener(plugin));
        register(new PlayerConnectionListener(plugin));
        register(new GestureListener(plugin));
        register(new PlayerListener(plugin));
    }

    @Override
    public void disable() {
        for (Listener listener : listeners)
            HandlerList.unregisterAll(listener);
        listeners.clear();
    }

    @Override
    public void register(@NotNull Listener listener) {
        Preconditions.checkNotNull(listener, "Could not register a null listener");
        if (getListener(listener.getClass()).isPresent())
            throw new IllegalStateException(String.format("A listener \"%s\" already has been registered!", listener.getClass().getSimpleName()));
        listeners.add(listener);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public Optional<Listener> getListener(@NotNull Class<? extends Listener> clazz) {
        return listeners.stream().filter(listener -> listener.getClass() == clazz).findFirst();
    }
}