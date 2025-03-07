package com.chunkslab.gestures.command;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.gui.GesturesGui;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Optional;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(value = "gesture", alias = "gestures")
@RequiredArgsConstructor
public class GestureCommand extends BaseCommand {

    private final GesturesPlugin plugin;

    @Default
    public void gesture(Player player, @Optional @Suggestion("gestures") String gestureId) {
        plugin.getGesturesMenuConfig().reload();
        if (gestureId == null)
            GesturesGui.open(plugin.getPlayerManager().getPlayer(player), plugin);
        else {
            Gesture gesture = plugin.getGestureManager().getGesture(gestureId);
            if (gesture == null) {
                //TODO: add gesture not found message
                return;
            }
            //TODO: play gesture method
        }
    }

}