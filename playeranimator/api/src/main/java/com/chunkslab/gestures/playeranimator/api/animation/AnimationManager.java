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

package com.chunkslab.gestures.playeranimator.api.animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimatorPlugin;
import com.chunkslab.gestures.playeranimator.api.animation.pack.AnimationPack;
import com.chunkslab.gestures.playeranimator.api.animation.pack.AnimationPackDeserializer;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class AnimationManager {

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(AnimationPack.class, new AnimationPackDeserializer())
			.create();
	@Getter private final Map<String, AnimationPack> registry = new HashMap<>();
	private final File packsFolder;

	public AnimationManager() {
		if(PlayerAnimatorPlugin.plugin == null) {
			packsFolder = null;
			return;
		}

		File root = PlayerAnimatorPlugin.plugin.getDataFolder();
		packsFolder = new File(root, "gestures");
		if(!packsFolder.exists() && !packsFolder.mkdirs())
			PlayerAnimatorPlugin.plugin.getLogger().log(Level.WARNING, "Unable to create gestures folder.");
	}

	public void clearRegistry() {
		registry.clear();
	}

	public void importPacks() {
		if(packsFolder == null)
			throw new RuntimeException("Player Animator is not used as a standalone plugin. Please use AnimationManager.importAnimations() to import animations individually.");
		try {
			clearRegistry();
			File[] packs = packsFolder.listFiles();
			if (packs == null)
				return;
			for (File pack : packs) {
				if (!pack.isDirectory())
					continue;
				File[] animations = pack.listFiles();
				if(animations == null)
					continue;
				for(File animation : animations) {
					if (!animation.isFile())
						continue;
					String animationName = FilenameUtils.removeExtension(animation.getName());
					FileReader reader = new FileReader(animation);
					AnimationPack animationPack = gson.fromJson(reader, AnimationPack.class);
					registerPack(pack.getName() + ":" + animationName, animationPack);
					reader.close();
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importAnimations(String key, File animation) {
		try {
			if (!animation.isFile())
				throw new IllegalArgumentException("Provided path to animation file does not point to a file.");
			if(!"bbmodel".equalsIgnoreCase(FilenameUtils.getExtension(animation.getName())))
				throw new IllegalArgumentException("File is not a bbmodel file.");

			String animationName = FilenameUtils.removeExtension(animation.getName());
			FileReader reader = new FileReader(animation);
			AnimationPack animationPack = gson.fromJson(reader, AnimationPack.class);
			registerPack(key + ":" + animationName, animationPack);
			reader.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerPack(String name, AnimationPack pack) {
		registry.put(name, pack);
	}

	public AnimationPack getAnimationPack(String name) {
		return registry.get(name);
	}

}
