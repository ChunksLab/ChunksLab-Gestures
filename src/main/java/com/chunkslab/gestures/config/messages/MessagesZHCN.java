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
public class MessagesZHCN extends MessagesEN {

    private String prefix = "<#D0EFB1>Gestures <dark_gray>•";

    private String skinUploaded = "<prefix> <green>皮肤上传成功！";

    private String skinNotSet = "<prefix> <red>皮肤未应用！";

    private String skinUploading = "<prefix> <yellow>皮肤开始上传！";

    private String invalidArgument = "<prefix> <red>无效的参数。";

    private String tooManyArguments = "<prefix> <red>参数太多。";

    private String notEnoughArguments = "<prefix> <red>参数不足。";

    private String unknownCommand = "<prefix> <red>未知指令。";

    private String notEnoughPermission = "<prefix> <red>你没有足够的权限执行此操作。";

    private String gestureNotExists = "<prefix> <red>该动作不存在！";

    private String wardrobeNotExists = "<prefix> <red>该衣柜不存在！";

    private String notInWardrobe = "<prefix> <red>你不在衣柜中！";

    private String notOnGround = "<prefix> <red>你必须站在地面上才能执行此操作！";

    private String worldGuardRegion = "<prefix> <red>你无法在此区域执行此操作！";

}