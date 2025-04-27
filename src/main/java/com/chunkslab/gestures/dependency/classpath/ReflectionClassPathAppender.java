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

package com.chunkslab.gestures.dependency.classpath;

import com.chunkslab.gestures.GesturesPlugin;

import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Path;

public class ReflectionClassPathAppender implements ClassPathAppender {
    private final URLClassLoaderAccess classLoaderAccess;

    public ReflectionClassPathAppender(ClassLoader classLoader) throws IllegalStateException {
        if (classLoader instanceof URLClassLoader) {
            this.classLoaderAccess = URLClassLoaderAccess.create((URLClassLoader) classLoader);
        } else {
            throw new IllegalStateException("ClassLoader is not instance of URLClassLoader");
        }
    }

    public ReflectionClassPathAppender(GesturesPlugin plugin) throws IllegalStateException {
        this(plugin.getClass().getClassLoader());
    }

    @Override
    public void addJarToClasspath(Path file) {
        try {
            this.classLoaderAccess.addURL(file.toUri().toURL());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}