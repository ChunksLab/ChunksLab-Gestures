/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
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

import com.chunkslab.gestures.dependency.properties.GesturesDependencyProperties;
import com.chunkslab.gestures.dependency.relocation.Relocation;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The dependencies used by ChunksLab-Gestures.
 */
public enum Dependency {

    ASM(
            "org.ow2.asm",
            "asm",
            "maven",
            "asm"
    ),
    ASM_COMMONS(
            "org.ow2.asm",
            "asm-commons",
            "maven",
            "asm-commons"
    ),
    JAR_RELOCATOR(
            "me.lucko",
            "jar-relocator",
            "maven",
            "jar-relocator"
    ),
    COMMONS_POOL_2(
            "org{}apache{}commons",
            "commons-pool2",
            "maven",
            "commons-pool",
            Relocation.of("commonspool2", "org{}apache{}commons{}pool2")
    ),
    INVUI_CORE(
            "xyz{}xenondevs{}invui",
            "invui-core",
            "xenondevs",
            "invui-core"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS(
            "xyz{}xenondevs{}invui",
            "inventory-access",
            "xenondevs",
            "inventory-access"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R1(
            "xyz{}xenondevs{}invui",
            "inventory-access-r1",
            "xenondevs",
            "inventory-access-r1"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R2(
            "xyz{}xenondevs{}invui",
            "inventory-access-r2",
            "xenondevs",
            "inventory-access-r2"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R3(
            "xyz{}xenondevs{}invui",
            "inventory-access-r3",
            "xenondevs",
            "inventory-access-r3"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R4(
            "xyz{}xenondevs{}invui",
            "inventory-access-r4",
            "xenondevs",
            "inventory-access-r4"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R5(
            "xyz{}xenondevs{}invui",
            "inventory-access-r5",
            "xenondevs",
            "inventory-access-r5"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R6(
            "xyz{}xenondevs{}invui",
            "inventory-access-r6",
            "xenondevs",
            "inventory-access-r6"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R7(
            "xyz{}xenondevs{}invui",
            "inventory-access-r7",
            "xenondevs",
            "inventory-access-r7"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R8(
            "xyz{}xenondevs{}invui",
            "inventory-access-r8",
            "xenondevs",
            "inventory-access-r8"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R9(
            "xyz{}xenondevs{}invui",
            "inventory-access-r9",
            "xenondevs",
            "inventory-access-r9"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R10(
            "xyz{}xenondevs{}invui",
            "inventory-access-r10",
            "xenondevs",
            "inventory-access-r10"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R11(
            "xyz{}xenondevs{}invui",
            "inventory-access-r11",
            "xenondevs",
            "inventory-access-r11"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R12(
            "xyz{}xenondevs{}invui",
            "inventory-access-r12",
            "xenondevs",
            "inventory-access-r12"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R13(
            "xyz{}xenondevs{}invui",
            "inventory-access-r13",
            "xenondevs",
            "inventory-access-r13"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R14(
            "xyz{}xenondevs{}invui",
            "inventory-access-r14",
            "xenondevs",
            "inventory-access-r14"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R15(
            "xyz{}xenondevs{}invui",
            "inventory-access-r15",
            "xenondevs",
            "inventory-access-r15"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R16(
            "xyz{}xenondevs{}invui",
            "inventory-access-r16",
            "xenondevs",
            "inventory-access-r16"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R17(
            "xyz{}xenondevs{}invui",
            "inventory-access-r17",
            "xenondevs",
            "inventory-access-r17"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R18(
            "xyz{}xenondevs{}invui",
            "inventory-access-r18",
            "xenondevs",
            "inventory-access-r18"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R19(
            "xyz{}xenondevs{}invui",
            "inventory-access-r19",
            "xenondevs",
            "inventory-access-r19"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R20(
            "xyz{}xenondevs{}invui",
            "inventory-access-r20",
            "xenondevs",
            "inventory-access-r20"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R21(
            "xyz{}xenondevs{}invui",
            "inventory-access-r21",
            "xenondevs",
            "inventory-access-r21"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R22(
            "xyz{}xenondevs{}invui",
            "inventory-access-r22",
            "xenondevs",
            "inventory-access-r22"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    INVUI_INVENTORY_ACCESS_R23(
            "xyz{}xenondevs{}invui",
            "inventory-access-r23",
            "xenondevs",
            "inventory-access-r23"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("invui");
        }
    },
    ADVENTURE_API(
            "net{}kyori",
            "adventure-api",
            "maven",
            "adventure-api"
    ) {
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("adventure-bundle");
        }
    },
    ADVENTURE_TEXT_MINIMESSAGE(
            "net{}kyori",
            "adventure-text-minimessage",
            "maven",
            "adventure-text-minimessage"
    ){
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("adventure-bundle");
        }
    },
    ADVENTURE_TEXT_SERIALIZER_LEGACY(
            "net{}kyori",
            "adventure-text-serializer-legacy",
            "maven",
            "adventure-text-serializer-legacy"
    ){
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("adventure-bundle");
        }
    },
    ADVENTURE_PLATFORM_BUKKIT(
            "net{}kyori",
            "adventure-platform-bukkit",
            "maven",
            "adventure-platform-bukkit"
    ){
        @Override
        public String getVersion() {
            return GesturesDependencyProperties.getValue("adventure-platform");
        }
    },
    GSON(
            "com.google.code.gson",
            "gson",
            "maven",
            "gson"
    ),
    SQLITE_DRIVER(
            "org.xerial",
            "sqlite-jdbc",
            "maven",
            "sqlite-driver"
    ),
    H2_DRIVER(
            "com.h2database",
            "h2",
            "maven",
            "h2-driver",
            Relocation.of("h2", "com{}h2")
    ),
    MONGODB_DRIVER_CORE(
            "org{}mongodb",
            "mongodb-driver-core",
            "maven",
            "mongodb-driver-core",
            Relocation.of("mongodb", "com{}mongodb"),
            Relocation.of("bson", "org{}bson")
    ),
    MONGODB_DRIVER_SYNC(
            "org{}mongodb",
            "mongodb-driver-sync",
            "maven",
            "mongodb-driver-sync",
            Relocation.of("mongodb", "com{}mongodb"),
            Relocation.of("bson", "org{}bson")
    ) {
        @Override
        public String getVersion() {
            return Dependency.MONGODB_DRIVER_CORE.getVersion();
        }
    },
    MONGODB_DRIVER_BSON(
            "org{}mongodb",
            "bson",
            "maven",
            "mongodb-bson",
            Relocation.of("mongodb", "com{}mongodb"),
            Relocation.of("bson", "org{}bson")
    ) {
        @Override
        public String getVersion() {
            return Dependency.MONGODB_DRIVER_CORE.getVersion();
        }
    },
    HIKARI_CP(
            "com{}zaxxer",
            "HikariCP",
            "maven",
            "hikari-cp",
            Relocation.of("hikari", "com{}zaxxer{}hikari")
    ),
    MARIADB_DRIVER(
            "org{}mariadb{}jdbc",
            "mariadb-java-client",
            "maven",
            "mariadb-java-client",
            Relocation.of("mariadb", "org{}mariadb")
    ),
    MYSQL_DRIVER(
            "com{}mysql",
            "mysql-connector-j",
            "maven",
            "mysql-connector-j",
            Relocation.of("mysql", "com{}mysql")
    ),
    JEDIS(
            "redis{}clients",
            "jedis",
            "maven",
            "jedis",
            Relocation.of("jedis", "redis{}clients{}jedis"),
            Relocation.of("commonspool2", "org{}apache{}commons{}pool2")
    ),
    ;

    @Getter
    private final List<Relocation> relocations;
    private final String repo;
    private final String groupId;
    private String rawArtifactId;
    private String customArtifactID;

    private static final String MAVEN_FORMAT = "%s/%s/%s/%s-%s.jar";

    Dependency(String groupId, String rawArtifactId, String repo, String customArtifactID) {
        this(groupId, rawArtifactId, repo, customArtifactID, new Relocation[0]);
    }

    Dependency(String groupId, String rawArtifactId, String repo, String customArtifactID, Relocation... relocations) {
        this.rawArtifactId = rawArtifactId;
        this.groupId = groupId;
        this.relocations = new ArrayList<>(Arrays.stream(relocations).toList());
        this.repo = repo;
        this.customArtifactID = customArtifactID;
    }

    public Dependency setCustomArtifactID(String customArtifactID) {
        this.customArtifactID = customArtifactID;
        return this;
    }

    public Dependency setRawArtifactID(String artifactId) {
        this.rawArtifactId = artifactId;
        return this;
    }

    public String getVersion() {
        return GesturesDependencyProperties.getValue(customArtifactID);
    }

    private static String rewriteEscaping(String s) {
        return s.replace("{}", ".");
    }

    public String getFileName(String classifier) {
        String name = customArtifactID.toLowerCase(Locale.ROOT).replace('_', '-');
        String extra = classifier == null || classifier.isEmpty()
                ? ""
                : "-" + classifier;
        return name + "-" + this.getVersion() + extra + ".jar";
    }

    String getMavenRepoPath() {
        return String.format(MAVEN_FORMAT,
                rewriteEscaping(groupId).replace(".", "/"),
                rewriteEscaping(rawArtifactId),
                getVersion(),
                rewriteEscaping(rawArtifactId),
                getVersion()
        );
    }

    /**
     * Creates a {@link MessageDigest} suitable for computing the checksums
     * of dependencies.
     *
     * @return the digest
     */
    public static MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public String getRepo() {
        return repo;
    }
}
