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

package com.chunkslab.gestures.config.messages;

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
public class MessagesFR extends MessagesEN {

    private String prefix = "<#D0EFB1>Gestures <dark_gray>•";

    private String skinUploaded = "<prefix> <green>Skin téléchargée avec succès !";

    private String skinNotSet = "<prefix> <red>La skin n'a pas été appliquée !";

    private String skinUploading = "<prefix> <yellow>Téléchargement de la skin en cours !";

    private String invalidArgument = "<prefix> <red>Argument invalide.";

    private String tooManyArguments = "<prefix> <red>Trop d'arguments.";

    private String notEnoughArguments = "<prefix> <red>Pas assez d'arguments.";

    private String unknownCommand = "<prefix> <red>Commande inconnue.";

    private String notEnoughPermission = "<prefix> <red>Vous n'avez pas la permission nécessaire pour faire cela.";

    private String gestureNotExists = "<prefix> <red>Le geste n'existe pas !";

    private String wardrobeNotExists = "<prefix> <red>La garde-robe n'existe pas !";

    private String notInWardrobe = "<prefix> <red>Vous n'êtes pas dans la garde-robe !";

    private String notOnGround = "<prefix> <red>Vous devez être au sol pour faire cela !";

    private String worldGuardRegion = "<prefix> <red>Vous ne pouvez pas faire cela dans cette région !";

}