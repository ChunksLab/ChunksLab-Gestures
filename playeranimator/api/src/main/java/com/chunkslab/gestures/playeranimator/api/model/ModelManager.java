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

package com.chunkslab.gestures.playeranimator.api.model;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimatorPlugin;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class ModelManager {

	private final Map<Entity, PlayerModel> models = new HashMap<>();
	private BukkitRunnable ticker;

	public void activate() {
		if(ticker != null)
			ticker.cancel();
		ticker = new BukkitRunnable() {
			@Override
			public void run() {
				for (var i = models.keySet().iterator(); i.hasNext(); ) {
					var model = models.get(i.next());
					if (!model.update()) {
						i.remove();
						model.despawn();
					}
				}
			}
		};
		ticker.runTaskTimerAsynchronously(PlayerAnimatorPlugin.plugin, 0, 1);
	}

	public void registerModel(PlayerModel model) {
		models.put(model.getBase(), model);
	}

	public void unregisterModel(PlayerModel model) {
		unregisterModel(model.getBase());
	}

	public void unregisterModel(Entity entity) {
		models.remove(entity);
	}

	public PlayerModel getPlayerModel(Entity entity) {
		return models.get(entity);
	}

}
