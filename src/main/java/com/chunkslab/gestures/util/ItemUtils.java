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
import com.chunkslab.gestures.api.config.ConfigFile;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class ItemUtils {
    @NotNull
    public static ItemStack build(ConfigFile config, String path, TagResolver... placeholders) {
        String material = config.getString(path + ".material");
        if (material == null) material = "BARRIER";
        ItemStack itemStack;
        if (!material.startsWith("custom:")) {
            itemStack = new ItemStack(Material.valueOf(material.toUpperCase(Locale.ENGLISH)));
        } else {
            itemStack = GesturesPlugin.getInstance().getItemAPI().getItemHook().getById(material.replace("custom:", ""));
            if (itemStack == null) {
                itemStack = new ItemStack(Material.BARRIER);
            }
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (config.contains(path + ".name"))
            itemMeta.setDisplayName(ChatUtils.fromLegacy(ChatUtils.format(PlaceholderAPI.setPlaceholders(null, config.getString(path + ".name")))));
        if (config.contains(path + ".amount"))
            itemStack.setAmount(config.getInt(path + ".amount"));
        if (config.contains(path + ".damage"))
            itemStack.setDurability((short)config.getInt(path + ".damage"));
        if (config.contains(path + ".custom-model-data"))
            itemMeta.setCustomModelData(Integer.valueOf(config.getInt(path + ".custom-model-data")));
        if (config.contains(path + ".lore"))
            itemMeta.setLore(ChatUtils.fromLegacy(ChatUtils.format(PlaceholderAPI.setPlaceholders(null, config.getStringList(path + ".lore")), placeholders)));
        if (config.contains(path + ".unbreakable"))
            itemMeta.setUnbreakable(config.getBoolean(path + ".unbreakable"));
        if (config.contains(path + ".hide-attributes")) {
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        if (config.contains(path + ".glow")) {
            itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (config.contains(path + ".enchantments")) {
            List<String> enchantments = config.getStringList(path + ".enchantments");
            for (String enchantment : enchantments) {
                String[] enchantmentSplit = enchantment.split(":");
                itemStack.addUnsafeEnchantment(Enchantment.getByKey(NamespacedKey.minecraft(enchantmentSplit[0].toLowerCase(Locale.ENGLISH))), Integer.parseInt(enchantmentSplit[1]));
            }
        }
        if (config.contains(path + ".item-flags")) {
            List<String> itemFlags = config.getStringList(path + ".item-flags");
            for (String flag : itemFlags) {
                itemMeta.addItemFlags(ItemFlag.valueOf(flag));
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}