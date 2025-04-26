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

package com.chunkslab.gestures.database.impl.nosql;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.player.GesturePlayerImpl;
import com.google.common.collect.Maps;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collections;
import java.util.UUID;

public class MongoDBDatabase implements Database {

    private final GesturesPlugin plugin;
    private final ConfigurationSection section;

    public MongoDBDatabase(GesturesPlugin plugin, ConfigurationSection section) {
        this.plugin = plugin;
        this.section = section;
    }

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @Override
    public void enable() {
        if (section == null) {
            LogUtils.severe("MongoDB configuration section is null. Please check your database config.");
            return;
        }
        var settings = MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD);
        if (!section.getString("connection-uri", "").equals("")) {
            settings.applyConnectionString(new ConnectionString(section.getString("connection-uri", "")));
            this.mongoClient = MongoClients.create(settings.build());
            this.database = mongoClient.getDatabase(section.getString("database", "gesture"));
            return;
        }

        if (section.contains("user")) {
            MongoCredential credential = MongoCredential.createCredential(
                    section.getString("user", "root"),
                    section.getString("database", "gesture"),
                    section.getString("password", "password").toCharArray()
            );
            settings.credential(credential);
        }

        settings.applyToClusterSettings(builder -> builder.hosts(Collections.singletonList(new ServerAddress(
                section.getString("host", "localhost"),
                section.getInt("port", 27017)
        ))));
        this.mongoClient = MongoClients.create(settings.build());
        this.database = mongoClient.getDatabase(section.getString("database", "gesture"));
        this.collection = database.getCollection(section.getString("collection", "chunkslab-gestures"));

        plugin.getServerManager().getAllOnlinePlayers().forEach(this::loadPlayer);
    }

    @Override
    public void disable() {
        if (this.mongoClient != null) {
            this.mongoClient.close();
        }
    }

    @Override
    public GesturePlayer loadPlayer(UUID playerUUID) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(playerUUID);
        if (player != null) return player;

        Document doc = collection.find(Filters.eq("_id", playerUUID.toString())).first();
        if (doc == null) return null;

        player = buildPlayerFromDocument(playerUUID, doc);

        plugin.getPlayerManager().addPlayer(player);
        return player;
    }

    @Override
    public GesturePlayer loadPlayer(String name) {
        GesturePlayer player = plugin.getPlayerManager().getPlayer(name);
        if (player != null) return player;

        Document doc = collection.find(Filters.eq("name", name)).first();
        if (doc == null) return null;

        UUID uuid = UUID.fromString(doc.getString("_id"));
        player = buildPlayerFromDocument(uuid, doc);

        plugin.getPlayerManager().addPlayer(player);
        return player;
    }

    @Override
    public void savePlayer(GesturePlayer player) {
        Document doc = new Document("_id", player.getUniqueId().toString())
                .append("name", player.getName());

        if (player.getFavoriteGestures().getOne() != null)
            doc.append("one", player.getFavoriteGestures().getOne().getId());
        if (player.getFavoriteGestures().getTwo() != null)
            doc.append("two", player.getFavoriteGestures().getTwo().getId());
        if (player.getFavoriteGestures().getThree() != null)
            doc.append("three", player.getFavoriteGestures().getThree().getId());
        if (player.getFavoriteGestures().getFour() != null)
            doc.append("four", player.getFavoriteGestures().getFour().getId());
        if (player.getFavoriteGestures().getFive() != null)
            doc.append("five", player.getFavoriteGestures().getFive().getId());
        if (player.getFavoriteGestures().getSix() != null)
            doc.append("six", player.getFavoriteGestures().getSix().getId());

        collection.replaceOne(Filters.eq("_id", player.getUniqueId().toString()), doc, new ReplaceOptions().upsert(true));
    }

    private GesturePlayer buildPlayerFromDocument(UUID uuid, Document doc) {
        GesturePlayer player = new GesturePlayerImpl(uuid);
        player.setTextures(Maps.newConcurrentMap());

        FavoriteGestures favoriteGestures = new FavoriteGestures(
                getGesture(doc, "one"),
                getGesture(doc, "two"),
                getGesture(doc, "three"),
                getGesture(doc, "four"),
                getGesture(doc, "five"),
                getGesture(doc, "six")
        );
        player.setFavoriteGestures(favoriteGestures);

        return player;
    }

    private Gesture getGesture(Document doc, String key) {
        if (!doc.containsKey(key)) return null;
        return plugin.getGestureManager().getGesture(doc.getString(key));
    }
}