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

package com.chunkslab.gestures.dependency.relocation;

import java.util.Objects;

public final class Relocation {
    private static final String RELOCATION_PREFIX = "com.chunkslab.gestures.libraries.";

    public static Relocation of(String id, String pattern) {
        return new Relocation(pattern.replace("{}", "."), RELOCATION_PREFIX + id);
    }

    private final String pattern;
    private final String relocatedPattern;

    private Relocation(String pattern, String relocatedPattern) {
        this.pattern = pattern;
        this.relocatedPattern = relocatedPattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getRelocatedPattern() {
        return this.relocatedPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relocation that = (Relocation) o;
        return Objects.equals(this.pattern, that.pattern) &&
                Objects.equals(this.relocatedPattern, that.relocatedPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pattern, this.relocatedPattern);
    }
}
