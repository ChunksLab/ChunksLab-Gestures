package com.chunkslab.gestures.item;

import com.chunkslab.gestures.item.api.ItemAPI;
import com.chunkslab.gestures.item.api.ItemHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;

public class ItemHookImpl extends ItemAPI {

    public static ItemAPI initialize(JavaPlugin plugin) {
        if(api == null)
            api = new ItemHookImpl();

        return api;
    }

    private ItemHookImpl() {
        getHookName();
        applyHook();
    }

    protected void getHookName() {
        if (Bukkit.getPluginManager().isPluginEnabled("Nexo"))
            hookName = "Nexo";
        else if (Bukkit.getPluginManager().isPluginEnabled("Oraxen"))
            hookName = "Oraxen";
        else if (Bukkit.getPluginManager().isPluginEnabled("ItemsAdder"))
            hookName = "ItemsAdder";
        else if (Bukkit.getPluginManager().isPluginEnabled("CraftEngine"))
            hookName = "CraftEngine";
        else
            hookName = "Vanilla";
    }

    private void applyHook() {
        String packageName;
        switch (hookName) {
            case "Nexo" -> packageName = "nexo";
            case "Oraxen" -> packageName = "oraxen";
            case "ItemsAdder" -> packageName = "itemsadder";
            case "CraftEngine" -> packageName = "craftengine";
            default -> throw new IllegalStateException("Plugin not supported");
        }
        try {
            // Item Hook
            Class<?> itemHookClazz = Class.forName("com.chunkslab.gestures.item." + packageName + "." + hookName + "Hook");
            Constructor<?> itemHookConstructor = itemHookClazz.getDeclaredConstructor();
            itemHookConstructor.setAccessible(true);
            setItemHook((ItemHook) itemHookConstructor.newInstance());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to initialize item hook", e);
        }
    }

}