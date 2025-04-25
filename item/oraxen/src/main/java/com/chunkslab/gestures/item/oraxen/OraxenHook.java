package com.chunkslab.gestures.item.oraxen;

import com.chunkslab.gestures.item.api.ItemHook;
import io.th0rgal.oraxen.api.OraxenItems;
import org.bukkit.inventory.ItemStack;

public class OraxenHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        if (!OraxenItems.exists(id))
            return null;
        return OraxenItems.getItemById(id).build();
    }

    @Override
    public String getId(ItemStack itemStack) {
        if (!OraxenItems.exists(itemStack))
            return null;
        return OraxenItems.getIdByItem(itemStack);
    }
}
