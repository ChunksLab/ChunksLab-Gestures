package com.chunkslab.gestures.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Header("################################################################")
@Header("#                                                              #")
@Header("#                      ChunksLab Gestures                      #")
@Header("#                                                              #")
@Header("#     Need help configuring the plugin? Join our discord!      #")
@Header("#                https://discord.chunkslab.com                 #")
@Header("#                                                              #")
@Header("################################################################")
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
@Getter @Setter
public class Config extends OkaeriConfig {

    @Comment("Currently supports: EN, DE, TR")
    private Language language = Language.EN;

    @Comment("Plugin settings")
    private Settings settings = new Settings();

    @Comment("These settings are used to configure the thread pool for the plugin")
    @Comment("Please be careful when changing these values")
    @Comment("If you are unsure, please leave them as they are")
    private ThreadSettings threadSettings = new ThreadSettings();

    @RequiredArgsConstructor
    @Getter
    public static enum Language {
        EN("messages_en.yml"),
        DE("messages_de.yml"),
        TR("messages_tr.yml"),
        ;

        private final String fileName;

    }

    @Getter @Setter
    public static class Settings extends OkaeriConfig {

    }

    @Getter @Setter
    public static class ThreadSettings extends OkaeriConfig {
        private int maximumPoolSize = 10;
        private int corePoolSize = 10;
        private int keepAliveTime = 30;
    }
}
