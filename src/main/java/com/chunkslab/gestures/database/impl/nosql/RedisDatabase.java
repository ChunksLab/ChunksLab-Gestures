package com.chunkslab.gestures.database.impl.nosql;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.player.GesturePlayerImpl;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class RedisDatabase implements Database {

    private static final String PLAYER_PREFIX = "gestures:player:";

    private final GesturesPlugin plugin;
    private final ConfigurationSection section;

    private JedisPool jedisPool;
    private String password;
    private int port;
    private String host;
    private boolean useSSL;

    @Override
    public void enable() {
        if (section == null) {
            LogUtils.severe("Redis configuration section is null. Please check your database config.");
            return;
        }

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));
        poolConfig.setNumTestsPerEvictionRun(-1);
        poolConfig.setMinEvictableIdleDuration(Duration.ofMillis(section.getInt("MinEvictableIdleTimeMillis", 1800000)));
        poolConfig.setMaxTotal(section.getInt("MaxTotal",8));
        poolConfig.setMaxIdle(section.getInt("MaxIdle",8));
        poolConfig.setMinIdle(section.getInt("MinIdle",1));
        poolConfig.setMaxWait(Duration.ofMillis(section.getInt("MaxWaitMillis", 3000)));

        password = section.getString("password", "");
        port = section.getInt("port", 6379);
        host = section.getString("host", "localhost");
        useSSL = section.getBoolean("use-ssl", false);

        if (password == null || password.isBlank()) {
            jedisPool = new JedisPool(poolConfig, host, port, 0, useSSL);
        } else {
            jedisPool = new JedisPool(poolConfig, host, port, 0, password, useSSL);
        }

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.ping();
            LogUtils.info("Redis server connected.");
        } catch (Exception e) {
            LogUtils.warn("Redis server could not be connected. Please check your database config.", e);
        }

        plugin.getServerManager().getAllOnlinePlayers().forEach(this::loadPlayer);
    }

    @Override
    public void disable() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    @Override
    public GesturePlayer loadPlayer(UUID playerUUID) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> data = jedis.hgetAll(PLAYER_PREFIX + playerUUID);
            if (data.isEmpty()) return null;

            GesturePlayer player = buildPlayerFromMap(playerUUID, data);
            plugin.getPlayerManager().addPlayer(player);
            return player;
        }
    }

    @Override
    public GesturePlayer loadPlayer(String name) {
        try (Jedis jedis = jedisPool.getResource()) {
            for (String key : jedis.keys(PLAYER_PREFIX + "*")) {
                Map<String, String> data = jedis.hgetAll(key);
                if (name.equalsIgnoreCase(data.get("name"))) {
                    UUID uuid = UUID.fromString(key.replace(PLAYER_PREFIX, ""));
                    GesturePlayer player = buildPlayerFromMap(uuid, data);
                    plugin.getPlayerManager().addPlayer(player);
                    return player;
                }
            }
        }
        return null;
    }

    @Override
    public void savePlayer(GesturePlayer player) {
        try (Jedis jedis = jedisPool.getResource()) {
            Map<String, String> data = new HashMap<>();
            data.put("name", player.getName());

            if (player.getFavoriteGestures().getOne() != null)
                data.put("one", player.getFavoriteGestures().getOne().getId());
            if (player.getFavoriteGestures().getTwo() != null)
                data.put("two", player.getFavoriteGestures().getTwo().getId());
            if (player.getFavoriteGestures().getThree() != null)
                data.put("three", player.getFavoriteGestures().getThree().getId());
            if (player.getFavoriteGestures().getFour() != null)
                data.put("four", player.getFavoriteGestures().getFour().getId());
            if (player.getFavoriteGestures().getFive() != null)
                data.put("five", player.getFavoriteGestures().getFive().getId());
            if (player.getFavoriteGestures().getSix() != null)
                data.put("six", player.getFavoriteGestures().getSix().getId());

            String key = PLAYER_PREFIX + player.getUniqueId();
            jedis.del(key);
            jedis.hset(key, data);
        }
    }

    private GesturePlayer buildPlayerFromMap(UUID uuid, Map<String, String> data) {
        GesturePlayer player = new GesturePlayerImpl(uuid);
        player.setTextures(Maps.newConcurrentMap());

        FavoriteGestures favoriteGestures = new FavoriteGestures(
                getGesture(data, "one"),
                getGesture(data, "two"),
                getGesture(data, "three"),
                getGesture(data, "four"),
                getGesture(data, "five"),
                getGesture(data, "six")
        );
        player.setFavoriteGestures(favoriteGestures);
        return player;
    }

    private Gesture getGesture(Map<String, String> data, String key) {
        if (!data.containsKey(key)) return null;
        return plugin.getGestureManager().getGesture(data.get(key));
    }
}