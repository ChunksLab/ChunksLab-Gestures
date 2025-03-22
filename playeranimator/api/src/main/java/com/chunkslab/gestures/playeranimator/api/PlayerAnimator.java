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
