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
