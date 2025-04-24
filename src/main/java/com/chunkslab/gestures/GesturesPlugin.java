package com.chunkslab.gestures;

import com.chunkslab.gestures.api.GesturesAPI;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.gesture.IGestureManager;
import com.chunkslab.gestures.api.listener.IListenerManager;
import com.chunkslab.gestures.api.module.ModuleManager;
import com.chunkslab.gestures.api.player.IPlayerManager;
import com.chunkslab.gestures.api.scheduler.IScheduler;
import com.chunkslab.gestures.api.server.IServerManager;
import com.chunkslab.gestures.api.wardrobe.IWardrobeManager;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.api.web.IWebManager;
import com.chunkslab.gestures.command.GestureCommand;
import com.chunkslab.gestures.command.WardrobeCommand;
import com.chunkslab.gestures.config.Config;
import com.chunkslab.gestures.config.messages.MessagesEN;
import com.chunkslab.gestures.database.impl.yaml.YamlDatabase;
import com.chunkslab.gestures.gesture.GestureManager;
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
public final class GesturesPlugin extends GesturesAPI {

    @Getter private static GesturesPlugin instance;

    private BukkitAudiences adventure;
    private Config pluginConfig;
    private MessagesEN pluginMessages;
    private BukkitCommandManager<CommandSender> commandManager;
    private GestureNMS gestureNMS;
    private PlayerAnimator playerAnimator;
    private MineskinClient mineSkinClient;

    // tasks
    private final SkinTask skinTask = new SkinTask(this);

    // config
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

        this.moduleManager.loadModules();
    }

    @Override
    public void onEnable() {
        adventure = BukkitAudiences.create(this);

        gestureNMS = GestureNMSImpl.initialize(this);

        playerAnimator = PlayerAnimatorImpl.initialize(this);
        playerAnimator.getAnimationManager().importPacks();


        registerCommands();
        createConfig();

        mineSkinClient = new MineskinClient("ChunksLab-Gestures", pluginConfig.getSettings().getMineSkinSecret());
        webManager = new WebManager(this, pluginConfig.getSettings().getWebUrl());

        wardrobesFile.create();
        gesturesFile.create();
        favoritesMenuConfig.create();
        gesturesMenuConfig.create();

        listenerManager.enable();
        scheduler.enable();
        gestureManager.enable();
        wardrobeManager.enable();


        skinTask.runTaskTimerAsynchronously(this, 0L, 20L);

        database = new YamlDatabase(this);

        this.getModuleManager().enableModules();
        database.enable();
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
