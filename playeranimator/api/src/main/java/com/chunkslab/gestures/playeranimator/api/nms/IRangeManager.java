package com.chunkslab.gestures.playeranimator.api.nms;

import org.bukkit.entity.Player;

import java.util.Set;

public interface IRangeManager {

	void addPlayer(Player player);
	void removePlayer(Player player);
	void setRenderDistance(int radius);

	Set<Player> getPlayerInRange();

}