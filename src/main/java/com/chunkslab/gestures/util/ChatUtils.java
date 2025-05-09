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

package com.chunkslab.gestures.util;

import com.chunkslab.gestures.GesturesPlugin;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import xyz.xenondevs.inventoryaccess.component.AdventureComponentWrapper;
import xyz.xenondevs.inventoryaccess.component.ComponentWrapper;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .tags(TagResolver.standard())
            .preProcessor(s -> s.replace("<prefix>", GesturesPlugin.getInstance().getPluginMessages().getPrefix()))
            .postProcessor(component -> component.decoration(TextDecoration.ITALIC, false))
            .build();

    public static final DecimalFormat FORMATTER = (DecimalFormat) NumberFormat.getNumberInstance();

    public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.builder().character('&')
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    public static final LegacyComponentSerializer LEGACY_AMPERSAND = LegacyComponentSerializer.builder()
            .hexColors().useUnusualXRepeatedCharacterHexFormat().build();

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Setter private static NumberFormat compactNumberFormat;

    static {
        FORMATTER.setMinimumIntegerDigits(1);
        FORMATTER.setMaximumIntegerDigits(20);
        FORMATTER.setMaximumFractionDigits(2);
        FORMATTER.setGroupingSize(3);
    }

    public static String formatNumber(Number number) {
        return compactNumberFormat.format(number);
    }

    public static String fromLegacy(Component component) {
        return LEGACY_AMPERSAND.serialize(component);
    }

    public static List<String> fromLegacy(List<Component> components) {
        return components.stream().map(ChatUtils::fromLegacy).collect(Collectors.toList());
    }

    public static Component colorLegacyString(String string) {
        return LEGACY.deserialize(string);
    }

    public static String toLegacy(String string, TagResolver... placeholders) {
        Component component = ChatUtils.format(string, placeholders);
        return LEGACY.serialize(component);
    }

    public static List<String> toLegacy(List<String> list, TagResolver... placeholders) {
        return list.stream().map(s -> toLegacy(s, placeholders)).collect(Collectors.toList());
    }

    public static ComponentWrapper formatForGui(String string, TagResolver... placeholders) {
        return new AdventureComponentWrapper(format(string, placeholders));
    }

    public static List<ComponentWrapper> formatForGui(List<String> list, TagResolver... placeholders) {
        return list.stream().map(s -> formatForGui(s, placeholders)).collect(Collectors.toList());
    }

    public static Component format(String string, TagResolver... placeholders) {
        return MINI_MESSAGE.deserialize(string, placeholders);
    }

    public static List<Component> format(List<String> list, TagResolver... placeholders) {
        return list.stream().map(s -> MINI_MESSAGE.deserialize(s, placeholders)).collect(Collectors.toList());
    }

    public static void sendMessage(CommandSender player, Component component) {
        GesturesPlugin.getInstance().getAdventure().sender(player).sendMessage(component);
    }

    public static void sendTitle(CommandSender player, Component title, Component subtitle) {
        GesturesPlugin.getInstance().getAdventure().sender(player).showTitle(Title.title(title, subtitle));
    }

    public static void sendMessage(CommandSender player, List<Component> components) {
        components.forEach(s -> ChatUtils.sendMessage(player, s));
    }
}