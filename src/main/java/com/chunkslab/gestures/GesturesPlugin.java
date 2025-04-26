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

package com.chunkslab.gestures;

import com.chunkslab.gestures.api.GesturesAPI;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.database.DatabaseType;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.gesture.IGestureManager;
import com.chunkslab.gestures.api.listener.IListenerManager;
import com.chunkslab.gestures.api.module.ModuleManager;
import com.chunkslab.gestures.api.player.IPlayerManager;
import com.chunkslab.gestures.api.scheduler.IScheduler;
import com.chunkslab.gestures.api.server.IServerManager;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.api.wardrobe.IWardrobeManager;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.api.web.IWebManager;
import com.chunkslab.gestures.command.GestureCommand;
import com.chunkslab.gestures.command.WardrobeCommand;
import com.chunkslab.gestures.config.Config;
import com.chunkslab.gestures.config.messages.MessagesEN;
import com.chunkslab.gestures.database.DatabaseFactory;
import com.chunkslab.gestures.dependency.Dependency;
import com.chunkslab.gestures.dependency.DependencyManager;
import com.chunkslab.gestures.dependency.DependencyManagerImpl;
import com.chunkslab.gestures.dependency.classpath.ClassPathAppender;
import com.chunkslab.gestures.dependency.classpath.ReflectionClassPathAppender;
import com.chunkslab.gestures.gesture.GestureManager;
import com.chunkslab.gestures.item.ItemHookImpl;
import com.chunkslab.gestures.item.api.ItemAPI;
import com.chunkslab.gestures.listener.ListenerManager;
import com.chunkslab.gestures.nms.GestureNMSImpl;
import com.chunkslab.gestures.nms.api.GestureNMS;
import com.chunkslab.gestures.player.GesturePlayerSkin;
import com.chunkslab.gestures.player.PlayerManager;
import com.chunkslab.gestures.playeranimator.PlayerAnimatorImpl;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.skin.SkinManager;
import com.chunkslab.gestures.scheduler.Scheduler;
import com.chunkslab.gestures.server.ServerManager;
import com.chunkslab.gestures.task.SkinTask;
import com.chunkslab.gestures.util.ChatUtils;
import com.chunkslab.gestures.util.UpdateUtil;
import com.chunkslab.gestures.wardrobe.WardrobeManager;
import com.chunkslab.gestures.web.WebManager;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.suggestion.SuggestionKey;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.jetbrains.annotations.NotNull;
import org.mineskin.MineskinClient;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class GesturesPlugin extends GesturesAPI {

    @Getter private static GesturesPlugin instance;

    private BukkitAudiences adventure;
    private Config pluginConfig;
    private MessagesEN pluginMessages;
    private BukkitCommandManager<CommandSender> commandManager;
    private GestureNMS gestureNMS;
    private PlayerAnimator playerAnimator;
    private ItemAPI itemAPI;
    private MineskinClient mineSkinClient;

    // tasks
    private final SkinTask skinTask = new SkinTask(this);

    // dependencies
    private ClassPathAppender classPathAppender;
    private DependencyManager dependencyManager;

    // license
    private final String buildByBit = "%%__BUILTBYBIT__%%";
    private final String polymart = "%%__POLYMART__%%";
    private String username = "%%__USERNAME__%%";
    private String user = "%%__USER__%%";
    private String time = "%%__TIMESTAMP__%%";
    private final String nonce = "%%__NONCE__%%";

    // config
    private final ConfigFile databaseFile = new ConfigFile(this, "database.yml", true);
    private final ConfigFile wardrobesFile = new ConfigFile(this, "wardrobes.yml", true);
    private final ConfigFile gesturesFile = new ConfigFile(this, "gestures.yml", true);
    private final ConfigFile favoritesMenuConfig = new ConfigFile(this, "menus", "favorites-menu.yml");
    private final ConfigFile gesturesMenuConfig = new ConfigFile(this, "menus", "gestures-menu.yml");

    // database
    @Setter private Database database;

    // managers
    @Setter private IListenerManager listenerManager = new ListenerManager(this);
    @Setter private IServerManager serverManager = new ServerManager();
    @Setter private IPlayerManager playerManager = new PlayerManager();
    @Setter private IScheduler scheduler = new Scheduler(this);
    @Setter private ModuleManager moduleManager = new ModuleManager(this);
    @Setter private IGestureManager gestureManager = new GestureManager(this);
    @Setter private IWardrobeManager wardrobeManager = new WardrobeManager(this);
    @Setter private IWebManager webManager;
    @Setter private SkinManager skinManager = new SkinManager();
    @Setter private GesturePlayerSkin gesturePlayerSkin = new GesturePlayerSkin(this);

    @Override
    public void onLoad() {
        instance = this;
        GesturesAPI.setInstance(this);
        GesturesAPI.setDebugMode(true);

        this.classPathAppender = new ReflectionClassPathAppender(this);
        this.dependencyManager = new DependencyManagerImpl(this);

        this.dependencyManager.loadDependencies(
                List.of(
                        Dependency.COMMONS_POOL_2,
                        Dependency.INVUI_CORE,
                        Dependency.INVUI_INVENTORY_ACCESS,
                        Dependency.INVUI_INVENTORY_ACCESS_R1,
                        Dependency.INVUI_INVENTORY_ACCESS_R2,
                        Dependency.INVUI_INVENTORY_ACCESS_R3,
                        Dependency.INVUI_INVENTORY_ACCESS_R4,
                        Dependency.INVUI_INVENTORY_ACCESS_R5,
                        Dependency.INVUI_INVENTORY_ACCESS_R6,
                        Dependency.INVUI_INVENTORY_ACCESS_R7,
                        Dependency.INVUI_INVENTORY_ACCESS_R8,
                        Dependency.INVUI_INVENTORY_ACCESS_R9,
                        Dependency.INVUI_INVENTORY_ACCESS_R10,
                        Dependency.INVUI_INVENTORY_ACCESS_R11,
                        Dependency.INVUI_INVENTORY_ACCESS_R12,
                        Dependency.INVUI_INVENTORY_ACCESS_R13,
                        Dependency.INVUI_INVENTORY_ACCESS_R14,
                        Dependency.INVUI_INVENTORY_ACCESS_R15,
                        Dependency.INVUI_INVENTORY_ACCESS_R16,
                        Dependency.INVUI_INVENTORY_ACCESS_R17,
                        Dependency.INVUI_INVENTORY_ACCESS_R18,
                        Dependency.INVUI_INVENTORY_ACCESS_R19,
                        Dependency.INVUI_INVENTORY_ACCESS_R20,
                        Dependency.INVUI_INVENTORY_ACCESS_R21,
                        Dependency.INVUI_INVENTORY_ACCESS_R22,
                        Dependency.INVUI_INVENTORY_ACCESS_R23,
                        Dependency.ADVENTURE_API,
                        Dependency.ADVENTURE_TEXT_MINIMESSAGE,
                        Dependency.ADVENTURE_TEXT_SERIALIZER_LEGACY,
                        Dependency.ADVENTURE_PLATFORM_BUKKIT,
                        Dependency.GSON,
                        Dependency.SQLITE_DRIVER,
                        Dependency.H2_DRIVER,
                        Dependency.MONGODB_DRIVER_CORE,
                        Dependency.MONGODB_DRIVER_SYNC,
                        Dependency.MONGODB_DRIVER_BSON,
                        Dependency.HIKARI_CP,
                        Dependency.MARIADB_DRIVER,
                        Dependency.MYSQL_DRIVER,
                        Dependency.JEDIS
                )
        );

        this.moduleManager.loadModules();
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        gestureNMS = GestureNMSImpl.initialize(this);

        playerAnimator = PlayerAnimatorImpl.initialize(this);
        playerAnimator.getAnimationManager().importPacks();

        itemAPI = ItemHookImpl.initialize(this);

        registerCommands();
        createConfig();

        mineSkinClient = new MineskinClient("ChunksLab-Gestures", pluginConfig.getSettings().getMineSkinSecret());
        webManager = new WebManager(this, pluginConfig.getSettings().getWebUrl());

        databaseFile.create();
        wardrobesFile.create();
        gesturesFile.create();
        favoritesMenuConfig.create();
        gesturesMenuConfig.create();

        listenerManager.enable();
        scheduler.enable();
        gestureManager.enable();
        wardrobeManager.enable();

        skinTask.runTaskTimerAsynchronously(this, 0L, 20L);

        database = DatabaseFactory.createDatabase(this, DatabaseType.get(databaseFile.getString("data-storage-method")), databaseFile.getConfigurationSection(databaseFile.getString("data-storage-method")));

        this.getModuleManager().enableModules();
        database.enable();

        boolean downloadFromPolymart = polymart.equals("1");
        boolean downloadFromBBB = buildByBit.equals("true");

        UpdateUtil.UPDATE_CHECKER.apply(this).thenAccept(result -> {
            String link;
            if (downloadFromPolymart) {
                link = "https://polymart.org/resource/PRODUCT_ID/";
            } else if (downloadFromBBB) {
                link = "https://builtbybit.com/resources/PRODUCT_ID/";
            } else {
                link = "https://github.com/ChunksLab/ChunksLab-Gestures/";
            }
            if (!result) {
                LogUtils.info("You are using the latest version.");
            } else {
                LogUtils.warn("Update is available: " + link);
            }
        });
        if (!downloadFromPolymart && !downloadFromBBB) {
            username = "Github User";
            user = "0";
            time = "0";
        }

        LogUtils.info("GesturesPlugin | Welcome " + username + " (" + user + ") - " + time);

        new Metrics(this, 25019);
    }

    @Override
    public void onDisable() {
        this.getModuleManager().disableModules();

        listenerManager.disable();
        skinTask.cancel();
        wardrobeManager.kickAllPlayer();
        if (adventure != null) {
            adventure.close();
            adventure = null;
        }
        if (database != null) {
            database.disable();
            database = null;
        }
        if (scheduler != null) {
            scheduler.disable();
            scheduler = null;
        }

        getBukkitCommands(getCommandMap()).remove("gesture");
        getBukkitCommands(getCommandMap()).remove("gestures");
        getBukkitCommands(getCommandMap()).remove("wardrobe");
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private CommandMap getCommandMap() {
        try {
            final Server server = Bukkit.getServer();
            final Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
            getCommandMap.setAccessible(true);

            return (CommandMap) getCommandMap.invoke(server);
        } catch (final Exception ignored) {
            throw new CommandRegistrationException("Unable get Command Map. Commands will not be registered!");
        }
    }

    // copied from triumph-cmd, credit goes to triumph-team
    @NotNull
    private Map<String, Command> getBukkitCommands(@NotNull final CommandMap commandMap) {
        try {
            final Field bukkitCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");
            bukkitCommands.setAccessible(true);
            //noinspection unchecked
            return (Map<String, Command>) bukkitCommands.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new CommandRegistrationException("Unable get Bukkit commands. Commands might not be registered correctly!");
        }
    }

    private void registerCommands() {
        commandManager = BukkitCommandManager.create(this);

        commandManager.registerSuggestion(SuggestionKey.of("players"), (sender, context) ->
                new ArrayList<>(this.serverManager.getAllOnlinePlayers())
        );
        commandManager.registerSuggestion(SuggestionKey.of("gestures"), (sender, context) ->
                this.getGestureManager().getGestures().stream().map(Gesture::getId).toList()
        );
        commandManager.registerSuggestion(SuggestionKey.of("wardrobes"), (sender, context) ->
                this.getWardrobeManager().getWardrobes().stream().map(Wardrobe::getId).toList()
        );
        commandManager.registerSuggestion(SuggestionKey.of("wardrobeStatus"), (sender, context) ->
            List.of("enable", "disable")
        );

        commandManager.registerCommand(new GestureCommand(this), new WardrobeCommand(this));

        commandManager.registerMessage(MessageKey.INVALID_ARGUMENT, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getInvalidArgument())));
        commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getUnknownCommand())));
        commandManager.registerMessage(MessageKey.NOT_ENOUGH_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getNotEnoughArguments())));
        commandManager.registerMessage(MessageKey.TOO_MANY_ARGUMENTS, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getTooManyArguments())));
        commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, invalidArgumentContext) ->
                ChatUtils.sendMessage(sender, ChatUtils.format(pluginMessages.getNotEnoughPermission())));
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    private void createConfig() {
        pluginConfig = ConfigManager.create(Config.class, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(this.getDataFolder(), "config.yml"));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });

        Class<MessagesEN> langClass = (Class<MessagesEN>) Class.forName("com.chunkslab.gestures.config.messages.Messages" + pluginConfig.getLanguage().name());
        pluginMessages = ConfigManager.create(langClass, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer());
            it.withBindFile(new File(this.getDataFolder(), pluginConfig.getLanguage().getFileName()));
            it.withRemoveOrphans(true);
            it.saveDefaults();
            it.load(true);
        });
    }
}
