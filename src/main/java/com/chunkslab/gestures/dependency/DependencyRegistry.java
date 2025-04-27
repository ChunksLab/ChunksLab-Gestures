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

package com.chunkslab.gestures.dependency;

import com.google.gson.JsonElement;

/**
 * Applies ChunksLab-Gestures specific behaviour for {@link Dependency}s.
 */
public class DependencyRegistry {

    public boolean shouldAutoLoad(Dependency dependency) {
        return switch (dependency) {
            // all used within 'isolated' classloaders, and are therefore not
            // relocated.
            case ASM, ASM_COMMONS, JAR_RELOCATOR, H2_DRIVER, SQLITE_DRIVER -> false;
            default -> true;
        };
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isGsonRelocated() {
        return JsonElement.class.getName().startsWith("com.chunkslab");
    }

}
