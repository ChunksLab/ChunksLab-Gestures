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
public class MessagesTR extends MessagesEN {

    private String prefix = "<#D0EFB1>Gestures <dark_gray>•";

    private String skinUploaded = "<prefix> <green>Skin başarıyla yüklendi!";

    private String skinNotSet = "<prefix> <red>Skin uygulanamadı!";

    private String skinUploading = "<prefix> <yellow>Skin yüklenmeye başladı!";

    private String invalidArgument = "<prefix> <red>Geçersiz argüman.";

    private String tooManyArguments = "<prefix> <red>Fazla sayıda argüman girdiniz.";

    private String notEnoughArguments = "<prefix> <red>Yetersiz argüman girdiniz.";

    private String unknownCommand = "<prefix> <red>Bilinmeyen komut.";

    private String notEnoughPermission = "<prefix> <red>Bunu yapmak için yetkiniz yok.";

    private String gestureNotExists = "<prefix> <red>Böyle bir animasyon bulunmuyor!";

    private String wardrobeNotExists = "<prefix> <red>Böyle bir gardırop bulunmuyor!";

    private String notInWardrobe = "<prefix> <red>Gardıropta değilsiniz!";

    private String notOnGround = "<prefix> <red>Bunu yapmak için yerde olmalısınız!";

    private String worldGuardRegion = "<prefix> <red>Bu bölgede bunu yapamazsınız!";

}