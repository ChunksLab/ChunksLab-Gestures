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

package com.chunkslab.gestures.scheduler;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.scheduler.CancellableTask;
import com.chunkslab.gestures.api.scheduler.SyncScheduler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitTask;

public class BukkitSchedulerImpl implements SyncScheduler {

    private final GesturesPlugin plugin;

    public BukkitSchedulerImpl(GesturesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void runSyncTask(Runnable runnable, Location location) {
        if (Bukkit.isPrimaryThread())
            runnable.run();
        else
            Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runSyncTask(Runnable runnable, World world, int x, int z) {
        runSyncTask(runnable, null);
    }

    @Override
    public CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delay, long period) {
        return new BukkitCancellableTask(Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, period));
    }

    @Override
    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delay) {
        if (delay == 0) {
            if (Bukkit.isPrimaryThread()) runnable.run();
            else Bukkit.getScheduler().runTask(plugin, runnable);
            return new BukkitCancellableTask(null);
        }
        return new BukkitCancellableTask(Bukkit.getScheduler().runTaskLater(plugin, runnable, delay));
    }

    public static class BukkitCancellableTask implements CancellableTask {

        private final BukkitTask bukkitTask;

        public BukkitCancellableTask(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        @Override
        public void cancel() {
            if (this.bukkitTask != null)
                this.bukkitTask.cancel();
        }

        @Override
        public boolean isCancelled() {
            if (this.bukkitTask == null) return true;
            return this.bukkitTask.isCancelled();
        }
    }
}