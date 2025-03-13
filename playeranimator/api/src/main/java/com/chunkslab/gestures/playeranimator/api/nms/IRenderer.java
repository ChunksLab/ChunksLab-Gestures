package com.chunkslab.gestures.playeranimator.api.nms;

import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerBone;
import org.bukkit.entity.Player;

public interface IRenderer {

	void setLimb(PlayerBone bone);
	void spawn();
	void spawn(Player player);
	void despawn();
	void despawn(Player player);
	void update();

	static byte rotByte(float rot) {
		return (byte) (rot * 256 / 360);
	}

}
