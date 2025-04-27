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

package com.chunkslab.gestures.playeranimator.api.animation.pack;

import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.ParticleEffect;
import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.SoundEffect;
import com.google.gson.*;
import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import com.chunkslab.gestures.playeranimator.api.animation.animation.LoopMode;
import com.chunkslab.gestures.playeranimator.api.animation.animation.Timeline;
import com.chunkslab.gestures.playeranimator.api.animation.keyframe.KeyframeType;
import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;
import java.util.Locale;

public class AnimationPackDeserializer implements JsonDeserializer<AnimationPack> {

	@Override
	public AnimationPack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

		JsonObject jObj = json.getAsJsonObject();
		AnimationPack pack = new AnimationPack();

		if(jObj.has("outliner")) {
			for (JsonElement element : jObj.getAsJsonArray("outliner")) {
				if (!element.isJsonObject())
					continue;
				Bone bone = getBone(null, element);
				pack.getBones().add(bone);
			}
		}

		if(jObj.has("animations")) {
			for (JsonElement element : jObj.getAsJsonArray("animations")) {
				Animation animation = getAnimation(element);
				pack.registerAnimation(animation);
			}
		}

		return pack;
	}

	private Bone getBone(Bone parent, JsonElement json) {
		JsonObject jObj = json.getAsJsonObject();

		String name = jObj.get("name").getAsString();
		Vector origin;
		EulerAngle rotation;
		if(jObj.has("origin")) {
			JsonArray array = jObj.get("origin").getAsJsonArray();
			origin = new Vector(-array.get(0).getAsDouble(), array.get(1).getAsDouble(), -array.get(2).getAsDouble()).multiply(0.0625);
		}else {
			origin = new Vector();
		}
		if(jObj.has("rotation")) {
			JsonArray array = jObj.get("rotation").getAsJsonArray();
			rotation = TMath.makeAngle(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
		}else {
			rotation = EulerAngle.ZERO;
		}

		Bone bone = new Bone(name, origin, rotation);

		for(JsonElement element : jObj.getAsJsonArray("children")) {
			if(!element.isJsonObject())
				continue;
			Bone child = getBone(bone, element);
			bone.getChildren().add(child);
		}

		if(parent != null)
			bone.getOrigin().subtract(parent.getOrigin());

		return bone;
	}

	private Animation getAnimation(JsonElement element) {

		JsonObject jObj = element.getAsJsonObject();
		String name = jObj.get("name").getAsString();
		Animation animation = new Animation(name);

		if(jObj.has("loop"))
			animation.setLoopMode(LoopMode.get(jObj.get("loop").getAsString()));
		if(jObj.has("override"))
			animation.setOverride(jObj.get("override").getAsBoolean());
		if(jObj.has("length"))
			animation.setLength(jObj.get("length").getAsFloat());

		if(jObj.has("animators")) {
			JsonObject animators = jObj.get("animators").getAsJsonObject();
			for(final var entry : animators.entrySet()) {
				JsonObject bone = entry.getValue().getAsJsonObject();
				String type = bone.get("type").getAsString();
				if(!"bone".equals(type))
					continue;
				String boneName = bone.get("name").getAsString();
				Timeline timeline = animation.getOrCreateTimeline(boneName);
				configureTimeline(boneName, timeline, bone.get("keyframes"));
			}
		}

		return animation;
	}

	private void configureTimeline(String boneName, Timeline timeline, JsonElement element) {
		JsonArray keyframes = element.getAsJsonArray();
		for(JsonElement ele : keyframes) {
			JsonObject key = ele.getAsJsonObject();
			if(!(key.has("channel") && key.has("data_points") && key.has("time")))
				continue;
			String channel = key.get("channel").getAsString();
			JsonObject data = key.get("data_points").getAsJsonArray().get(0).getAsJsonObject();
			double[] values = new double[] {
					Double.parseDouble(data.get("x").getAsString()),
					Double.parseDouble(data.get("y").getAsString()),
					Double.parseDouble(data.get("z").getAsString())
			};
			double time = key.get("time").getAsDouble();
			KeyframeType type = getType(key.get("interpolation").getAsString());
			switch (channel) {
				case "rotation" -> timeline.addRotationFrame(time, TMath.makeAngle(values[0], values[1], values[2]), type);
				case "position" -> timeline.addPositionFrame(time, new Vector(values[0], values[1], -values[2]).multiply(0.0625), type);
				case "scale" -> {
					String value;
					if (boneName.startsWith("particle")) {
						value = data.get("x").getAsString();
						if (value.isEmpty()) {
							timeline.addOrGetEffectFrame(time).getValue().setParticle(null);
							break;
						}
						String[] separatorValues = value.split(";");
						if (separatorValues.length >= 6) {
							String effect = separatorValues[0];
							float xDist = Float.parseFloat(separatorValues[1]);
							float yDist = Float.parseFloat(separatorValues[2]);
							float zDist = Float.parseFloat(separatorValues[3]);
							float maxSpeed = Float.parseFloat(separatorValues[4]);
							int count = Integer.parseInt(separatorValues[5]);
							timeline.addOrGetEffectFrame(time).getValue().setParticle(new ParticleEffect(effect, xDist, yDist, zDist, maxSpeed, count));
							break;
						}
						timeline.addOrGetEffectFrame(time).getValue().setParticle(new ParticleEffect(separatorValues[0]));
						break;
					}
					if (boneName.equalsIgnoreCase("effects")) {
						value = data.get("x").getAsString();
						String instructions = data.get("y").getAsString();
						if (!instructions.isEmpty()) {
							timeline.addOrGetEffectFrame(time).getValue().setInstructions(instructions);
						}
						if (value.isEmpty()) {
							timeline.addOrGetEffectFrame(time).getValue().setSound(null);
							break;
						}
						String[] separatorValues = value.split(";");
						if (separatorValues.length >= 3) {
							String sound = separatorValues[0];
							float volume = Float.parseFloat(separatorValues[1]);
							float pitch = Float.parseFloat(separatorValues[2]);
							timeline.addOrGetEffectFrame(time).getValue()
									.setSound(new SoundEffect(sound, volume, pitch));
							break;
						}
						timeline.addOrGetEffectFrame(time).getValue().setSound(new SoundEffect(separatorValues[0]));
						break;
					}
					timeline.addScaleFrame(time, new Vector(values[0], values[1], values[2]), type);
				}
			}
		}
	}

	private KeyframeType getType(String type) {
		switch (type.toLowerCase(Locale.ENGLISH)) {
			case "linear" -> {
				return KeyframeType.LINEAR;
			}
			case "catmullrom" -> {
				return KeyframeType.SMOOTH;
			}
			case "step" -> {
				return KeyframeType.STEP;
			}
		}
		return KeyframeType.LINEAR;
	}

}
