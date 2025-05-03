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

package com.chunkslab.gestures.hook.impl.worldguard;

import com.chunkslab.gestures.hook.manager.WorldGuardManager;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class WorldGuard implements WorldGuardManager {

    private StateFlag flag;

    public WorldGuard() {
        registerFlag();
    }

    @Override
    public void registerFlag() {
        try {
            FlagRegistry registry = com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry();
            StateFlag flag = new StateFlag("gesture", true);
            registry.register(flag);
            this.flag = flag;
        } catch (FlagConflictException e) {
            // Handle the case where the flag is already registered
            this.flag = (StateFlag) com.sk89q.worldguard.WorldGuard.getInstance().getFlagRegistry().get("gesture");
        }
    }

    @Override
    public StateFlag getFlag() {
        return flag;
    }

    @Override
    public String getName() {
        return "WorldGuard";
    }
}
