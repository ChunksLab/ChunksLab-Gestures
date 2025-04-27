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

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.dependency.classloader.IsolatedClassLoader;
import com.chunkslab.gestures.dependency.classpath.ClassPathAppender;
import com.chunkslab.gestures.dependency.relocation.Relocation;
import com.chunkslab.gestures.dependency.relocation.RelocationHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Loads and manages runtime dependencies for the plugin.
 */
public class DependencyManagerImpl implements DependencyManager {

    /** A registry containing plugin specific behaviour for dependencies. */
    private final DependencyRegistry registry;
    /** The path where library jars are cached. */
    private final Path cacheDirectory;
    /** The classpath appender to preload dependencies into */
    private final ClassPathAppender classPathAppender;
    /** A map of dependencies which have already been loaded. */
    private final EnumMap<Dependency, Path> loaded = new EnumMap<>(Dependency.class);
    /** A map of isolated classloaders which have been created. */
    private final Map<Set<Dependency>, IsolatedClassLoader> loaders = new HashMap<>();
    /** Cached relocation handler instance. */
    private final RelocationHandler relocationHandler;
    private final GesturesPlugin plugin;

    public DependencyManagerImpl(GesturesPlugin plugin) {
        this.plugin = plugin;
        this.registry = new DependencyRegistry();
        this.cacheDirectory = setupCacheDirectory(plugin);
        this.classPathAppender = plugin.getClassPathAppender();
        this.relocationHandler = new RelocationHandler(this);
    }

    @Override
    public ClassLoader obtainClassLoaderWith(Set<Dependency> dependencies) {
        Set<Dependency> set = new HashSet<>(dependencies);

        for (Dependency dependency : dependencies) {
            if (!this.loaded.containsKey(dependency)) {
                throw new IllegalStateException("Dependency " + dependency + " is not loaded.");
            }
        }

        synchronized (this.loaders) {
            IsolatedClassLoader classLoader = this.loaders.get(set);
            if (classLoader != null) {
                return classLoader;
            }

            URL[] urls = set.stream()
                    .map(this.loaded::get)
                    .map(file -> {
                        try {
                            return file.toUri().toURL();
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toArray(URL[]::new);

            classLoader = new IsolatedClassLoader(urls);
            this.loaders.put(set, classLoader);
            return classLoader;
        }
    }

    @Override
    public void loadDependencies(Collection<Dependency> dependencies) {
        CountDownLatch latch = new CountDownLatch(dependencies.size());

        for (Dependency dependency : dependencies) {
            if (this.loaded.containsKey(dependency)) {
                latch.countDown();
                continue;
            }

            try {
                loadDependency(dependency);
            } catch (Throwable e) {
                LogUtils.warn("Unable to load dependency " + dependency.name(), e);
            } finally {
                latch.countDown();
            }
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void loadDependency(Dependency dependency) throws Exception {
        if (this.loaded.containsKey(dependency)) {
            return;
        }

        Path file = remapDependency(dependency, downloadDependency(dependency));

        this.loaded.put(dependency, file);

        if (this.classPathAppender != null && this.registry.shouldAutoLoad(dependency)) {
            this.classPathAppender.addJarToClasspath(file);
        }
    }

    private Path downloadDependency(Dependency dependency) throws DependencyDownloadException {
        String fileName = dependency.getFileName(null);
        Path file = this.cacheDirectory.resolve(fileName);

        // if the file already exists, don't attempt to re-download it.
        if (Files.exists(file)) {
            return file;
        }

        DependencyDownloadException lastError = null;
        String forceRepo = dependency.getRepo();
        List<DependencyRepository> repository = DependencyRepository.getByID(forceRepo);
        if (!repository.isEmpty()) {
            int i = 0;
            while (i < repository.size()) {
                try {
                    LogUtils.info("Downloading dependency(" + fileName + ")[" + repository.get(i).getUrl() + dependency.getMavenRepoPath() + "]");
                    repository.get(i).download(dependency, file);
                    LogUtils.info("Successfully downloaded " + fileName);
                    return file;
                } catch (DependencyDownloadException e) {
                    lastError = e;
                    i++;
                }
            }
        }
        throw Objects.requireNonNull(lastError);
    }

    private Path remapDependency(Dependency dependency, Path normalFile) throws Exception {
        List<Relocation> rules = new ArrayList<>(dependency.getRelocations());
        if (rules.isEmpty()) {
            return normalFile;
        }

        Path remappedFile = this.cacheDirectory.resolve(dependency.getFileName(DependencyRegistry.isGsonRelocated() ? "remapped-legacy" : "remapped"));

        // if the remapped source exists already, just use that.
        if (Files.exists(remappedFile)) {
            return remappedFile;
        }

        LogUtils.info("Remapping " + dependency.getFileName(null));
        relocationHandler.remap(normalFile, remappedFile, rules);
        LogUtils.info("Successfully remapped " + dependency.getFileName(null));
        return remappedFile;
    }

    private static Path setupCacheDirectory(GesturesPlugin plugin) {
        Path cacheDirectory = plugin.getDataFolder().toPath().resolve("libs");
        try {
            if (!Files.exists(cacheDirectory)) {
                Files.createDirectories(cacheDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to create libs directory", e);
        }

        return cacheDirectory;
    }

    @Override
    public void close() {
        IOException firstEx = null;

        for (IsolatedClassLoader loader : this.loaders.values()) {
            try {
                loader.close();
            } catch (IOException ex) {
                if (firstEx == null) {
                    firstEx = ex;
                } else {
                    firstEx.addSuppressed(ex);
                }
            }
        }

        if (firstEx != null) {
            LogUtils.severe(firstEx.getMessage(), firstEx);
        }
    }
}
