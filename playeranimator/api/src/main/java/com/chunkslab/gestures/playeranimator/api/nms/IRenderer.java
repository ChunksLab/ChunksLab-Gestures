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

package com.chunkslab.gestures.playeranimator.api.nms;

import com.chunkslab.gestures.playeranimator.api.model.player.Hand;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerBone;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IRenderer {

	void setLimb(PlayerBone bone);
	void spawn();
	void spawn(Player player);
	void despawn();
	void despawn(Player player);
	void update();
	void changeItem(Hand hand, int slot);
	void changeItem(ItemStack item);

	static byte rotByte(float rot) {
		return (byte) (rot * 256 / 360);
	}

}
