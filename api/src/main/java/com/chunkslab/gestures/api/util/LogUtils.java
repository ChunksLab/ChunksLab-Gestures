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

package com.chunkslab.gestures.api.util;

import com.chunkslab.gestures.api.GesturesAPI;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class LogUtils {

    /**
     * Log an informational message.
     *
     * @param message The message to log.
     */
    public static void info(@NotNull String message) {
        GesturesAPI.getInstance().getLogger().info(message);
    }

    /**
     * Log a warning message.
     *
     * @param message The message to log.
     */
    public static void warn(@NotNull String message) {
        GesturesAPI.getInstance().getLogger().warning(message);
    }

    /**
     * Log a severe error message.
     *
     * @param message The message to log.
     */
    public static void severe(@NotNull String message) {
        GesturesAPI.getInstance().getLogger().severe(message);
    }

    /**
     * Log a debug message.
     *
     * @param message The message to log.
     */
    public static void debug(@NotNull String message) {
        if (!GesturesAPI.isDebugMode()) return;
        GesturesAPI.getInstance().getLogger().log(Level.INFO, "[DEBUG] " + message);
    }

    /**
     * Log a warning message with a throwable exception.
     *
     * @param message    The message to log.
     * @param throwable  The throwable exception to log.
     */
    public static void warn(@NotNull String message, Throwable throwable) {
        GesturesAPI.getInstance().getLogger().log(Level.WARNING, message, throwable);
    }

    /**
     * Log a severe error message with a throwable exception.
     *
     * @param message    The message to log.
     * @param throwable  The throwable exception to log.
     */
    public static void severe(@NotNull String message, Throwable throwable) {
        GesturesAPI.getInstance().getLogger().log(Level.SEVERE, message, throwable);
    }
}