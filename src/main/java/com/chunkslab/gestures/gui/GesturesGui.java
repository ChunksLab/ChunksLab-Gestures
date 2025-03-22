package com.chunkslab.gestures.gui;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.gui.item.BackItem;
import com.chunkslab.gestures.gui.item.ForwardItem;
import com.chunkslab.gestures.gui.item.UpdatingItem;
import com.chunkslab.gestures.util.ChatUtils;
import com.chunkslab.gestures.util.ItemUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GesturesGui {

    public static void open(GesturePlayer player, GesturesPlugin plugin) {
        ConfigFile config = plugin.getGesturesMenuConfig();
        config.reload();
        StringBuilder title = new StringBuilder(config.getString("title"));

        List<Item> gestures = new ArrayList<>();
        int index = 0;

        for (int i = 1; i <= 5; i++) {
            if (config.getBoolean("rows." + i + ".enabled")) {
                int amount = config.getInt("rows." + i + ".amount");

                title.append("<font:gesture_row_").append(i).append(">");

                int row = i;
                List<Item> rowGestures = plugin.getGestureManager().getGestures()
                        .stream()
                        .skip(index)
                        .limit(amount)
                        .map(gesture -> {
                            if (player.getPlayer().hasPermission(gesture.getPermission()))
                                title.append(config.getString("green-color")).append(config.getString("slot-background-unicode")).append("<font:default>").append(config.getString("slot-gap"));
                            else
                                title.append(config.getString("red-color")).append(config.getString("slot-background-unicode")).append("<font:default>").append(config.getString("slot-gap"));
                            title.append(config.getString("gesture-color")).append("<font:gesture_row_").append(row).append(">").append(gesture.getFont());

                            ItemBuilder gestureItem = new ItemBuilder(ItemUtils.build(config, "items.x"));
                            gestureItem.setDisplayName(ChatUtils.formatForGui(ChatUtils.fromLegacy(gesture.getName())));

                            return new UpdatingItem(20, () -> gestureItem, event -> {
                                plugin.getGestureManager().playGesture(player, gesture);
                            });
                        })
                        .collect(Collectors.toList());

                title.append(config.getString("rows." + i + ".offset"));

                gestures.addAll(rowGestures);
                index += amount;
            }
        }

        Item close = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.c")), event -> player.getPlayer().closeInventory());

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('c', close)
                .addIngredient('<', new BackItem(config))
                .addIngredient('>', new ForwardItem(config))
                .setContent(gestures)
                .build();

        Window window = Window.single()
                .setViewer(player.getPlayer())
                .setGui(gui)
                .setTitle(ChatUtils.formatForGui(PlaceholderAPI.setPlaceholders(null, title.toString())))
                .build();

        window.open();
    }
}