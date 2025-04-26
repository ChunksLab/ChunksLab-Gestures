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

package com.chunkslab.gestures.gesture;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.gesture.GestureEquip;
import com.chunkslab.gestures.api.gesture.IGestureManager;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LogUtils;
import com.chunkslab.gestures.nms.api.MountNMS;
import com.chunkslab.gestures.player.gesture.CustomPlayerModel;
import com.chunkslab.gestures.util.ChatUtils;
import com.chunkslab.gestures.util.ItemUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class GestureManager implements IGestureManager {

    private final GesturesPlugin plugin;

    private final Map<String, Gesture> gestureMap = new ConcurrentHashMap<>();
    private final Map<GesturePlayer, CustomPlayerModel> ticking = Maps.newConcurrentMap();

    @Override
    public void enable() {
        gestureMap.clear();
        for (String id : plugin.getGesturesFile().getKeys(false)) {
            ConfigurationSection section = plugin.getGesturesFile().getConfigurationSection(id);
            Component name = ChatUtils.format(section.getString("name"));
            String permission = section.getString("permission");
            String font = section.getString("font");
            boolean movement = section.getBoolean("movement");
            double moveSpeed = section.getDouble("moveSpeed");
            String animationStart = section.getString("animations.start");
            String animationIdle = section.getString("animations.idle");
            String animationEnd = section.getString("animations.end");
            ItemStack rightHand = null;
            ItemStack leftHand = null;
            ItemStack head = null;
            if (animationStart != null) {
                String[] keys = animationStart.split("\\.", 3);
                if (!plugin.getPlayerAnimator().getAnimationManager().getRegistry().containsKey(keys[0] + ":" + keys[1])) {
                    LogUtils.warn("The gesture('" + id + "') has an start animation that does not exist in your registered animations.");
                    continue;
                }
            }
            if (animationIdle != null) {
                String[] keys = animationIdle.split("\\.", 3);
                if (!this.plugin.getPlayerAnimator().getAnimationManager().getRegistry().containsKey(keys[0] + ":" + keys[1])) {
                    LogUtils.warn("The gesture('" + id + "') has an idle animation that does not exist in your registered animations.");
                    continue;
                }
            }
            if (animationEnd != null) {
                String[] keys = animationStart.split("\\.", 3);
                if (!plugin.getPlayerAnimator().getAnimationManager().getRegistry().containsKey(keys[0] + ":" + keys[1])) {
                    LogUtils.warn("The gesture('" + id + "') has an start animation that does not exist in your registered animations.");
                    continue;
                }
            }
            if (section.contains("equipment")) {
                if (section.contains("equipment.rightHand"))
                    rightHand = ItemUtils.build(plugin.getGesturesFile(), section.getString("equipment.rightHand"));
                if (section.contains("equipment.leftHand"))
                    leftHand = ItemUtils.build(plugin.getGesturesFile(), section.getString("equipment.leftHand"));
                if (section.contains("equipment.head"))
                    head = ItemUtils.build(plugin.getGesturesFile(), section.getString("equipment.head"));
            }
            GestureEquip gestureEquip = new GestureEquip(rightHand, leftHand, head);
            gestureMap.put(id, new Gesture(id, name, permission, font, animationStart, animationIdle, animationEnd, movement, moveSpeed, gestureEquip));
        }
    }

    @Override
    public Gesture getGesture(String id) {
        return gestureMap.get(id);
    }

    @Override
    public Collection<Gesture> getGestures() {
        return gestureMap.values();
    }

    @Override
    public void playGesture(GesturePlayer player, Gesture gesture) {
        player.setGesture(gesture);
        if(!ticking.containsKey(player)) {
            CustomPlayerModel model = new CustomPlayerModel(player, gesture, 1);
            model.playAnimation();
            model.spawn();
            ticking.put(player, model);
            return;
        }
        CustomPlayerModel model = ticking.get(player);
        model.changeGestureAnimation(gesture);
    }

    @Override
    public void stopGesture(GesturePlayer player) {
        CustomPlayerModel model = ticking.remove(player);
        if(model == null) return;
        model.destroy();
        player.setGesture(null);
        MountNMS mountNMS = plugin.getGestureNMS().getMountNMS();
        mountNMS.destroy(player.getPlayer());
    }

    @Override
    public void stopGesture(GesturePlayer player, boolean wardrobe) {
        CustomPlayerModel model = ticking.remove(player);
        if(model == null) return;
        player.setGesture(null);
        model.instantDestroy();
    }
}