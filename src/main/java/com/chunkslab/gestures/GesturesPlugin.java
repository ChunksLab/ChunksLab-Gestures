package com.chunkslab.gestures;

import com.chunkslab.gestures.api.GesturesAPI;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.database.Database;
import com.chunkslab.gestures.api.listener.IListenerManager;
import com.chunkslab.gestures.api.module.ModuleManager;
import com.chunkslab.gestures.api.scheduler.IScheduler;
import com.chunkslab.gestures.api.server.IServerManager;
import com.chunkslab.gestures.config.Config;
import com.chunkslab.gestures.config.messages.MessagesEN;
import com.chunkslab.gestures.listener.ListenerManager;
import com.chunkslab.gestures.playeranimator.PlayerAnimatorImpl;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.scheduler.Scheduler;
import com.chunkslab.gestures.server.ServerManager;
import com.chunkslab.gestures.util.ChatUtils;
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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

@Getter
public final class GesturesPlugin extends GesturesAPI {

    @Getter private static GesturesPlugin instance;

    private BukkitAudiences adventure;
    private Config pluginConfig;
    private MessagesEN pluginMessages;
    private BukkitCommandManager<CommandSender> commandManager;
    private PlayerAnimator playerAnimator;

    // config
    private final ConfigFile wardrobesFile = new ConfigFile(this, "wardrobes.yml", true);
    private final ConfigFile gesturesFile = new ConfigFile(this, "gestures.yml", true);

    // database
    @Setter private Database database;

    // managers
    @Setter private IListenerManager listenerManager = new ListenerManager(this);
    @Setter private IServerManager serverManager = new ServerManager();
    //@Setter private IPlayerManager playerManager = new PlayerManager();
    @Setter private IScheduler scheduler = new Scheduler(this);
    @Setter private ModuleManager moduleManager = new ModuleManager(this);

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
        playerAnimator = PlayerAnimatorImpl.initialize(this);
        this.playerAnimator.getAnimationManager().importPacks();

        registerCommands();
        createConfig();

        this.getModuleManager().enableModules();
        database.enable();
    }

    @Override
    public void onDisable() {
        this.getModuleManager().disableModules();

        listenerManager.disable();
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

        // Player Commands

        // Admin Commands

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
