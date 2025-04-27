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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Represents a repository which contains {@link Dependency}s.
 */
public enum DependencyRepository {

    /**
     * Maven Central
     */
    MAVEN_CENTRAL("maven", "https://repo1.maven.org/maven2/") {
        @Override
        protected URLConnection openConnection(Dependency dependency) throws IOException {
            URLConnection connection = super.openConnection(dependency);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection;
        }
    },
    /**
     * Maven Central Mirror
     */
    MAVEN_CENTRAL_MIRROR("maven", "https://maven.aliyun.com/repository/public/"),

    XENONDEVS("xenondevs", "https://repo.xenondevs.xyz/releases/"),

    ;

    private final String url;
    private final String id;

    DependencyRepository(String id, String url) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public static List<DependencyRepository> getByID(String id) {
        ArrayList<DependencyRepository> repositories = new ArrayList<>();
        for (DependencyRepository repository : values()) {
            if (id.equals(repository.id)) {
                repositories.add(repository);
            }
        }
        // 中国大陆优先使用阿里云镜像
        if (Locale.getDefault() == Locale.SIMPLIFIED_CHINESE) {
            Collections.reverse(repositories);
        }
        return repositories;
    }

    /**
     * Opens a connection to the given {@code dependency}.
     *
     * @param dependency the dependency to download
     * @return the connection
     * @throws IOException if unable to open a connection
     */
    protected URLConnection openConnection(Dependency dependency) throws IOException {
        URL dependencyUrl = new URL(this.url + dependency.getMavenRepoPath());
        return dependencyUrl.openConnection();
    }

    /**
     * Downloads the raw bytes of the {@code dependency}.
     *
     * @param dependency the dependency to download
     * @return the downloaded bytes
     * @throws DependencyDownloadException if unable to download
     */
    public byte[] downloadRaw(Dependency dependency) throws DependencyDownloadException {
        try {
            URLConnection connection = openConnection(dependency);
            try (InputStream in = connection.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                if (bytes.length == 0) {
                    throw new DependencyDownloadException("Empty stream");
                }
                return bytes;
            }
        } catch (Exception e) {
            throw new DependencyDownloadException(e);
        }
    }

    /**
     * @param dependency the dependency to download
     * @return the downloaded bytes
     * @throws DependencyDownloadException if unable to download
     */
    public byte[] download(Dependency dependency) throws DependencyDownloadException {
        return downloadRaw(dependency);
    }

    /**
     * Downloads the the {@code dependency} to the {@code file}, ensuring the
     * downloaded bytes match the checksum.
     *
     * @param dependency the dependency to download
     * @param file the file to write to
     * @throws DependencyDownloadException if unable to download
     */
    public void download(Dependency dependency, Path file) throws DependencyDownloadException {
        try {
            Files.write(file, download(dependency));
        } catch (IOException e) {
            throw new DependencyDownloadException(e);
        }
    }

    public String getId() {
        return id;
    }
}
