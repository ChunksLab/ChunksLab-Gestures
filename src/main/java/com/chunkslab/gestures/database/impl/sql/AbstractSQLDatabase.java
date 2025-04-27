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

package com.chunkslab.gestures.database.impl.sql;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.player.GesturePlayerImpl;
import com.google.common.collect.Maps;

import java.sql.*;
import java.util.UUID;

public abstract class AbstractSQLDatabase implements Database {

    protected String tablePrefix;
    protected abstract Connection getConnection() throws SQLException;
    protected abstract GesturesPlugin getPlugin();

    @Override
    public void enable() {
        createTableIfNotExist();
    }

    public void createTableIfNotExist() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS " + tablePrefix + "_players (" +
                            "uuid VARCHAR(36) PRIMARY KEY," +
                            "name VARCHAR(32)," +
                            "one VARCHAR(64)," +
                            "two VARCHAR(64)," +
                            "three VARCHAR(64)," +
                            "four VARCHAR(64)," +
                            "five VARCHAR(64)," +
                            "six VARCHAR(64)" +
                            ");"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GesturePlayer loadPlayer(UUID playerUUID) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_players WHERE uuid = ?")) {
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            GesturePlayer player = new GesturePlayerImpl(playerUUID);
            player.setTextures(Maps.newConcurrentMap());

            FavoriteGestures favoriteGestures = new FavoriteGestures(
                    getGesture(rs, "one"),
                    getGesture(rs, "two"),
                    getGesture(rs, "three"),
                    getGesture(rs, "four"),
                    getGesture(rs, "five"),
                    getGesture(rs, "six")
            );
            player.setFavoriteGestures(favoriteGestures);

            getPlugin().getPlayerManager().addPlayer(player);
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public GesturePlayer loadPlayer(String name) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + tablePrefix + "_players WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            UUID uuid = UUID.fromString(rs.getString("uuid"));
            GesturePlayer player = new GesturePlayerImpl(uuid);
            player.setTextures(Maps.newConcurrentMap());

            FavoriteGestures favoriteGestures = new FavoriteGestures(
                    getGesture(rs, "one"),
                    getGesture(rs, "two"),
                    getGesture(rs, "three"),
                    getGesture(rs, "four"),
                    getGesture(rs, "five"),
                    getGesture(rs, "six")
            );
            player.setFavoriteGestures(favoriteGestures);

            getPlugin().getPlayerManager().addPlayer(player);
            return player;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void savePlayer(GesturePlayer player) {
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO " + tablePrefix + "_players (uuid, name, one, two, three, four, five, six) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE " +
                             "name=VALUES(name), one=VALUES(one), two=VALUES(two), three=VALUES(three), " +
                             "four=VALUES(four), five=VALUES(five), six=VALUES(six);"
             )) {
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, player.getName());
            setGesture(ps, 3, player.getFavoriteGestures().getOne());
            setGesture(ps, 4, player.getFavoriteGestures().getTwo());
            setGesture(ps, 5, player.getFavoriteGestures().getThree());
            setGesture(ps, 6, player.getFavoriteGestures().getFour());
            setGesture(ps, 7, player.getFavoriteGestures().getFive());
            setGesture(ps, 8, player.getFavoriteGestures().getSix());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Gesture getGesture(ResultSet rs, String column) throws SQLException {
        String id = rs.getString(column);
        if (id == null) return null;
        return getPlugin().getGestureManager().getGesture(id);
    }

    private void setGesture(PreparedStatement ps, int index, Gesture gesture) throws SQLException {
        if (gesture != null) {
            ps.setString(index, gesture.getId());
        } else {
            ps.setNull(index, Types.VARCHAR);
        }
    }
}