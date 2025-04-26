/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
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

package com.chunkslab.gestures.playeranimator.nms.v1_19_R2.entity;

import com.chunkslab.gestures.playeranimator.api.nms.ISkinManager;
import com.chunkslab.gestures.playeranimator.api.skin.images.SkinTexture;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.chunkslab.gestures.playeranimator.nms.v1_19_R2.v1_19_R2;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.NoSuchElementException;

public class SkinManager extends v1_19_R2 implements ISkinManager {

    @Override
    public Object getSkinTexture(Player player) {
        BufferedImage image;
        JsonObject jsonObject;
        Property property;
        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
        GameProfile gameProfile = serverPlayer.getGameProfile();
        try {
            property = gameProfile.getProperties().get("textures").iterator().next();
        } catch (NoSuchElementException exception) {
            return null;
        }
        byte[] bytes = Base64.getDecoder().decode(property.getValue());
        try {
            jsonObject = new JsonParser().parse(new String(bytes)).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException exception) {
            return null;
        }
        try {
            URL url = new URL(jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString());
            image = ImageIO.read(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonElement metadata = jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("metadata");
        boolean slimModel = metadata != null && metadata.getAsJsonObject().get("model").getAsString().equals("slim");
        return slimModel ? new SkinTexture(image, true, false) : new SkinTexture(image, false, true);
    }

    @Override
    public boolean getSkinType(Player player) {
        ServerPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        GameProfile gameProfile = entityPlayer.getGameProfile();
        try {
            TextureWrapper textureWrapper = TextureWrapper.fromBase64((gameProfile.getProperties().get("textures").iterator().next()).getValue());
            return textureWrapper.isSlim();
        } catch (NoSuchElementException exception) {
            return false;
        }
    }

    @Override
    public String getSkinTextureUrl(Player player) {
        return TextureWrapper.fromBase64(getTexture(player)).getUrl();
    }

    @Override
    public boolean isSkin(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        GameProfile gameProfile = serverPlayer.getGameProfile();
        try {
            gameProfile.getProperties().get("textures").iterator().next();
            return true;
        } catch (NoSuchElementException exception) {
            return false;
        }
    }
}
