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

package com.chunkslab.gestures.playeranimator.api.model.player.bones;

import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.Effects;
import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.ParticleEffect;
import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.SoundEffect;
import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import com.chunkslab.gestures.playeranimator.api.utils.math.Offset;
import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class PlayerEffectsBone extends PlayerBone {

    protected Effects effects;
    private static final Vector locale = new Vector(-0.3, 1.8, 0.0);

    public PlayerEffectsBone(PlayerModel model, Bone bone, boolean particles) {
        super(model, bone, particles ? LimbType.PARTICLE : LimbType.EFFECTS);
    }

    public void update() {
        effects = model.getAnimationProperty().getEffectsFrame(bone.getName());
        Vector fPosition = model.getAnimationProperty().getPositionFrame(bone.getName());
        EulerAngle fRotation = model.getAnimationProperty().getRotationFrame(bone.getName());

        Vector pPosition = parent == null ? getModel().getBaseVector() : parent.getPosition();
        EulerAngle pRotation = parent == null ? EulerAngle.ZERO : parent.getRotation();

        fPosition = Offset.getRelativeLocation(pRotation, fPosition.add(getBone().getOrigin()));
        fRotation = TMath.globalRotate(getBone().getRotation(), fRotation);
        if (type == LimbType.PARTICLE) {
            fPosition.add(locale);
        }

        fPosition = Offset.rotateYaw(fPosition, Math.toRadians(getModel().getBaseYaw()));

        position = pPosition.add(fPosition);
        rotation = TMath.localRotate(pRotation, fRotation);

        for (PlayerBone bone : children.values()) {
            bone.update();
        }
    }

    public Effects getEffects() {
        return effects;
    }

    public ParticleEffect getParticle() {
        if (effects == null) {
            return null;
        }
        if (effects.getParticle() == null) {
            return null;
        }
        return effects.getParticle();
    }

    public SoundEffect getSound() {
        if (effects == null) {
            return null;
        }
        if (effects.getSound() == null) {
            return null;
        }
        return effects.getSound();
    }

    public String getInstructions() {
        if (effects == null) {
            return null;
        }
        if (effects.getInstructions().isEmpty()) {
            return null;
        }
        return effects.getInstructions();
    }
}
