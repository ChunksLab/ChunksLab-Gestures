package com.chunkslab.gestures.item.nexo;

import com.chunkslab.gestures.item.api.ItemHook;
import com.nexomc.nexo.api.NexoItems;
import org.bukkit.inventory.ItemStack;

public class NexoHook implements ItemHook {
    @Override
    public ItemStack getById(String id) {
        if (!NexoItems.exists(id))
            return null;
        return NexoItems.itemFromId(id).build();
    }

    @Override
    public String getId(ItemStack itemStack) {
        if (!NexoItems.exists(itemStack))
            return null;
        return NexoItems.idFromItem(itemStack);
    }
}
