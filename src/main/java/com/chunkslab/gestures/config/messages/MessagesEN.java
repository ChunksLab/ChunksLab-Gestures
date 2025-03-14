package com.chunkslab.gestures.config.messages;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.Getter;
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
public class MessagesEN extends OkaeriConfig {

    private String prefix = "<#D0EFB1>Gestures <dark_gray>•";

    private String skinUploaded = "<prefix> <green>Skin successfully uploaded!";
    private String skinUploading = "<prefix> <yellow>Skin started uploading!";

    private String invalidArgument = "<prefix> <red>Invalid argument.";
    private String tooManyArguments = "<prefix> <red>Too many arguments.";
    private String notEnoughArguments = "<prefix> <red>Not enough arguments.";
    private String unknownCommand = "<prefix> <red>Unknown command.";
    private String notEnoughPermission = "<prefix> <red>You don't have enough permission to do this.";
    private String gestureNotExists = "<prefix> <red>Gesture not exists!";
    private String wardrobeNotExists = "<prefix> <red>Wardrobe not exists!";
    private String notInWardrobe = "<prefix> <red>You're not in the wardrobe!";
}
