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
