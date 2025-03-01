package com.chunkslab.gestures.playeranimator.api.nms;

import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface INMSHandler {

	void injectPlayer(Player player);
	void removePlayer(Player player);

	IRangeManager createRangeManager(Entity entity);
	IRenderer createRenderer();
	String getTexture(Player player);
	ItemStack setSkullTexture(ItemStack skull, TextureWrapper texture);

}
