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

import com.chunkslab.gestures.api.GesturesAPI;
import com.chunkslab.gestures.api.scheduler.CancellableTask;
import com.chunkslab.gestures.api.scheduler.SyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class FoliaSchedulerImpl implements SyncScheduler {
    private final GesturesAPI plugin;

    public FoliaSchedulerImpl(GesturesAPI plugin) {
        this.plugin = plugin;
    }

    public void runSyncTask(Runnable runnable, Location location) {
        if (location == null) {
            Bukkit.getGlobalRegionScheduler().execute(this.plugin, runnable);
        } else {
            Bukkit.getRegionScheduler().execute(this.plugin, location, runnable);
        }
    }

    public void runSyncTask(Runnable runnable, World world, int x, int z) {
        Bukkit.getRegionScheduler().execute(this.plugin, world, x, z, runnable);
    }

    public CancellableTask runTaskSyncTimer(Runnable runnable, Location location, long delay, long period) {
        if (location == null)
            return new FoliaCancellableTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(this.plugin, scheduledTask -> runnable.run(), delay, period));
        return new FoliaCancellableTask(Bukkit.getRegionScheduler().runAtFixedRate(this.plugin, location, scheduledTask -> runnable.run(), delay, period));
    }

    public CancellableTask runTaskSyncLater(Runnable runnable, Location location, long delay) {
        if (delay == 0L) {
            runSyncTask(runnable, location);
            return new FoliaCancellableTask(null);
        }
        if (location == null)
            return new FoliaCancellableTask(Bukkit.getGlobalRegionScheduler().runDelayed(this.plugin, scheduledTask -> runnable.run(), delay));
        return new FoliaCancellableTask(Bukkit.getRegionScheduler().runDelayed(this.plugin, location, scheduledTask -> runnable.run(), delay));
    }

    public static class FoliaCancellableTask implements CancellableTask {
        private final ScheduledTask scheduledTask;

        public FoliaCancellableTask(ScheduledTask scheduledTask) {
            this.scheduledTask = scheduledTask;
        }

        public void cancel() {
            this.scheduledTask.cancel();
        }

        public boolean isCancelled() {
            return this.scheduledTask.isCancelled();
        }
    }
}