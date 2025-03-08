package com.chunkslab.gestures.gui;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.config.ConfigFile;
import com.chunkslab.gestures.api.player.GesturePlayer;
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

public class FavoritesGui {

    public static void open(GesturePlayer player, GesturesPlugin plugin) {
        ConfigFile config = plugin.getFavoritesMenuConfig();
        StringBuilder title = new StringBuilder(config.getString("title"));

        List<Item> gestures = new ArrayList<>();
        int index = 0;

        for (int i = 1; i <= 2; i++) {
            if (config.getBoolean("rows." + i + ".enabled")) {
                int amount = config.getInt("rows." + i + ".amount");

                title.append("<font:gesture_favorite_row_").append(i).append(">");

                List<Item> rowGestures = plugin.getGestureManager().getGestures()
                        .stream()
                        .skip(index)
                        .limit(amount)
                        .flatMap(gesture -> {
                            title.append(gesture.getFont()).append(config.getString("gap-shift"));
                            List<Item> repeatedItems = new ArrayList<>();
                            for (int j = 1; j <= 3; j++) {
                                repeatedItems.add(new UpdatingItem(20,
                                        () -> new ItemBuilder(ItemUtils.build(config, "items.x")),
                                        event -> {

                                        }));
                            }
                            return repeatedItems.stream();
                        })
                        .collect(Collectors.toList());

                title.append(config.getString("rows." + i + ".offset"));

                gestures.addAll(rowGestures);
                index += amount;
            }
        }

        Item close = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.c")), event -> player.getPlayer().closeInventory());

        Item allGestures = new UpdatingItem(20, () -> new ItemBuilder(ItemUtils.build(config, "items.a")), event -> {
            player.getPlayer().closeInventory();
            GesturesGui.open(player, plugin);
        });

        Gui gui = PagedGui.items()
                .setStructure(config.getStringList("structure").toArray(new String[0]))
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('c', close)
                .addIngredient('a', allGestures)
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