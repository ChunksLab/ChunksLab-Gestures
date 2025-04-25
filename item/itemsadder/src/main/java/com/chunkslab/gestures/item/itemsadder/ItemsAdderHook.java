package com.chunkslab.gestures.item.itemsadder;

import com.chunkslab.gestures.item.api.ItemHook;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        return CustomStack.getInstance(id).getItemStack();
    }

    @Override
    public String getId(ItemStack itemStack) {
        return CustomStack.byItemStack(itemStack).getId();
    }
}