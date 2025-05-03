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
public class MessagesES extends MessagesEN {

    private String prefix = "<#D0EFB1>Gestures <dark_gray>•";

    private String skinUploaded = "<prefix> <green>¡La skin se ha subido correctamente!";

    private String skinNotSet = "<prefix> <red>¡No se aplicó la skin!";

    private String skinUploading = "<prefix> <yellow>¡La skin está comenzando a subirse!";

    private String invalidArgument = "<prefix> <red>Argumento inválido.";

    private String tooManyArguments = "<prefix> <red>Demasiados argumentos.";

    private String notEnoughArguments = "<prefix> <red>No hay suficientes argumentos.";

    private String unknownCommand = "<prefix> <red>Comando desconocido.";

    private String notEnoughPermission = "<prefix> <red>No tienes permiso suficiente para hacer esto.";

    private String gestureNotExists = "<prefix> <red>¡El gesto no existe!";

    private String wardrobeNotExists = "<prefix> <red>¡El armario no existe!";

    private String notInWardrobe = "<prefix> <red>¡No estás en el armario!";

    private String notOnGround = "<prefix> <red>¡Debes estar en el suelo para hacer esto!";

    private String worldGuardRegion = "<prefix> <red>¡No puedes hacer esto en esta región!";

}