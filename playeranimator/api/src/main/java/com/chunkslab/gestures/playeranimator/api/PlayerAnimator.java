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

package com.chunkslab.gestures.playeranimator.api;

import com.chunkslab.gestures.playeranimator.api.animation.AnimationManager;
import com.chunkslab.gestures.playeranimator.api.model.ModelManager;
import com.chunkslab.gestures.playeranimator.api.nms.INMSHandler;
import com.chunkslab.gestures.playeranimator.api.nms.ISkinManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public abstract class PlayerAnimator {

	public static PlayerAnimator api;

	@Getter @Setter
	protected AnimationManager animationManager;
	@Getter @Setter
	protected ModelManager modelManager;
	@Getter @Setter
	protected ISkinManager skinManager;
	@Getter @Setter
	protected INMSHandler nms;
	protected String version;

	public abstract void injectPlayer(Player player);
	public abstract void removePlayer(Player player);

}
