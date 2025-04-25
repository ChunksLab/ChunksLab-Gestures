package com.chunkslab.gestures.item.vanilla;

import com.chunkslab.gestures.item.api.ItemHook;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VanillaHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        return new ItemStack(Material.getMaterial(id));
    }

    @Override
    public String getId(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return "AIR";
        }
        return itemStack.getType().name();
    }
}