package com.chunkslab.gestures.item.craftengine;

import com.chunkslab.gestures.item.api.ItemHook;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.inventory.ItemStack;

public class CraftEngineHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        return CraftEngineItems.byId(Key.withDefaultNamespace(id)).buildItemStack();
    }

    @Override
    public String getId(ItemStack itemStack) {
        return CraftEngineItems.getCustomItemId(itemStack).value();
    }
}
