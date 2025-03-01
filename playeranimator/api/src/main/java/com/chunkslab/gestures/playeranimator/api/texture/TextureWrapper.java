package com.chunkslab.gestures.playeranimator.api.texture;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TextureWrapper {

	private static final Gson gson = new Gson();
	private static final String jsonTemplate = "{\"textures\":{\"SKIN\":{\"url\":\"\",\"metadata\":{}}}}";

	private final SimpleProfileTexture payload;
	@Getter @Setter private String url;
	@Getter @Setter private boolean isSlim;

	public static TextureWrapper fromBase64(String base64) {
		String decoded = new String(Base64.getDecoder().decode(base64));
		final var temp = gson.fromJson(decoded, SimpleProfileTexture.class);

		String url = temp.getTextures().get(MinecraftProfileTexture.Type.SKIN).getUrl();
		boolean isSlim = "slim".equals(temp.getTextures().get(MinecraftProfileTexture.Type.SKIN).getMetadata("model"));

		return new TextureWrapper(url, isSlim);
	}

	public TextureWrapper(String url, boolean isSlim) {
		payload = gson.fromJson(jsonTemplate, SimpleProfileTexture.class);
		setUrl(url);
		setSlim(isSlim);
	}

	public String toBase64() {
		updatePayload();
		String json = gson.toJson(payload);
		return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
	}

	private void updatePayload() {
		Map<String, String> meta = new HashMap<>();
		if(isSlim)
			meta.put("model", "slim");
		payload.getTextures().put(MinecraftProfileTexture.Type.SKIN, new MinecraftProfileTexture(url, meta));
	}

	private static class SimpleProfileTexture {
		@Getter private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;
	}

}
