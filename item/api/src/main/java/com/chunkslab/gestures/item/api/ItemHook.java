package com.chunkslab.gestures.item.api;

import org.bukkit.inventory.ItemStack;

public interface ItemHook {

    ItemStack getById(String id);

    String getId(ItemStack itemStack);

}