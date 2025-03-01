package com.chunkslab.gestures.playeranimator.api.model.player;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimatorPlugin;
import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
import com.chunkslab.gestures.playeranimator.api.animation.time.AnimationProperty;
import com.chunkslab.gestures.playeranimator.api.exceptions.MissingAnimationsException;
import com.chunkslab.gestures.playeranimator.api.exceptions.UnknownAnimationException;
import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class PlayerModel {

	private final Map<LimbType, IRenderer> limbs = new HashMap<>();
	private final Map<LimbType, PlayerBone> bones = new HashMap<>();
	private final Map<String, PlayerBone> children = new HashMap<>();

	@Getter @Setter private Entity base;
	@Getter @Setter private TextureWrapper texture;
	@Getter private AnimationProperty animationProperty;
	@Getter private IRangeManager rangeManager;
	private Location locationBuffer;

	public PlayerModel(Player player) {
		base = player;
		String raw = PlayerAnimator.api.getNms().getTexture(player);
		texture = TextureWrapper.fromBase64(raw);
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		initialize();
	}

	public PlayerModel(Entity base, String url, boolean isSlim) {
		this.base = base;
		this.texture = new TextureWrapper(url, isSlim);
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		initialize();
	}

	public PlayerModel(Entity base, TextureWrapper tex) {
		this.base = base;
		this.texture = tex;
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		initialize();
	}

	public PlayerModel(Entity base, Player player) {
		this.base = base;
		String raw = PlayerAnimator.api.getNms().getTexture(player);
		texture = TextureWrapper.fromBase64(raw);
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		initialize();
	}

	protected void initialize() {
		for(LimbType type : LimbType.values())
			limbs.put(type, PlayerAnimator.api.getNms().createRenderer());
	}

	public void spawn() {
		for(LimbType type : LimbType.values()) {
			if(!limbs.containsKey(type) || !bones.containsKey(type))
				continue;
			limbs.get(type).spawn();
		}
	}

	public void spawn(Player player) {
		for(LimbType type : LimbType.values()) {
			if(!limbs.containsKey(type) || !bones.containsKey(type))
				continue;
			limbs.get(type).spawn(player);
		}
	}

	public void despawn() {
		for(LimbType type : bones.keySet())
			limbs.get(type).despawn();
	}

	public void despawn(Player player) {
		for(LimbType type : bones.keySet())
			limbs.get(type).despawn(player);
	}

	public void playAnimation(String name) {
		String[] keys = name.split("\\.", 3);
		if(keys.length < 3)
			throw new IllegalArgumentException();
		final var animationPack = PlayerAnimator.api.getAnimationManager().getAnimationPack(keys[0] + ":" + keys[1]);
		if(animationPack == null) {
		    PlayerAnimatorPlugin.plugin.getLogger().log(Level.SEVERE, "AnimationPack " + keys[0] + ":" + keys[1] + " not found");
		    throw new MissingAnimationsException();
		}
		
		final var animation = animationPack.getAnimation(keys[2]);
		if(animation == null)
			throw new UnknownAnimationException();

		bones.clear();
		children.clear();
		for(Bone bone : animationPack.getBones())
			children.put(bone.getName(), setBones(bone));

		animationProperty = new AnimationProperty(animation);
		update(false);
		spawn();

		PlayerAnimator.api.getModelManager().registerModel(this);
	}

	private PlayerBone setBones(Bone bone) {
		LimbType type = LimbType.get(bone.getName());
		PlayerBone playerBone;
		if(type != null) {
			playerBone = type.isItem() ? new PlayerItemBone(this, bone, type) : new PlayerBone(this, bone, type);
			limbs.get(type).setLimb(playerBone);
			bones.put(type, playerBone);
		}else {
			playerBone = new PlayerBone(this, bone);
		}

		for(Bone child : bone.getChildren())
			playerBone.addChild(setBones(child));

		return playerBone;
	}

	public boolean update() {
		return update(true);
	}

	protected boolean update(boolean updateTime) {
		locationBuffer = getBase().getLocation();
		for(PlayerBone bone : children.values())
			bone.update();
		for(LimbType type : bones.keySet())
			limbs.get(type).update();
		if(updateTime && !getAnimationProperty().updateTime())
			animationProperty = null;
		return animationProperty != null && !base.isDead();
	}

	public Set<Player> getSeenBy() {
		final var set = getRangeManager().getPlayerInRange();
		if(base instanceof Player player)
			set.add(player);
		return set;
	}

	public Vector getBaseVector() {
		if(locationBuffer != null)
			return locationBuffer.toVector();
		return getBase().getLocation().toVector();
	}

	public float getBaseYaw() {
		if(locationBuffer != null)
			return locationBuffer.getYaw();
		return getBase().getLocation().getYaw();
	}

}
