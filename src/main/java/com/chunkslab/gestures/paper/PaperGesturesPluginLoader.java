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

package com.chunkslab.gestures.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class PaperGesturesPluginLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        final MavenLibraryResolver resolver = new MavenLibraryResolver();

        resolveLibraries(classpathBuilder).stream()
                .map(DefaultArtifact::new)
                .forEach(artifact -> resolver.addDependency(new Dependency(artifact, null)));

        resolver.addRepository(new RemoteRepository.Builder(
                "xenondevs", "default", "https://repo.xenondevs.xyz/releases/"
        ).build());

        classpathBuilder.addLibrary(resolver);
    }

    @NotNull
    private static List<String> resolveLibraries(@NotNull PluginClasspathBuilder classpathBuilder) {
        try (InputStream input = getLibraryListFile()) {
            if (input == null) {
                throw new IllegalStateException("Failed to read libraries file");
            }

            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Object librariesObj = data.get("libraries");
            if (librariesObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> libraries = (List<String>) librariesObj;
                return libraries;
            }
        } catch (Throwable e) {
            classpathBuilder.getContext().getLogger().error("Failed to resolve libraries", e);
        }
        return List.of();
    }

    @Nullable
    private static InputStream getLibraryListFile() {
        return PaperGesturesPlugin.class.getClassLoader().getResourceAsStream("paper-libraries.yml");
    }
}