package com.chunkslab.gestures.command;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.WardrobeNMS;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import dev.triumphteam.cmd.core.annotation.Suggestion;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

@Command(value = "wardrobe")
@RequiredArgsConstructor
public class WardrobeCommand extends BaseCommand {

    private final GesturesPlugin plugin;

    @SubCommand("create")
    @Permission("chunkslab.gestures.wardrobe.create")
    public void create(Player player, String id) {
        plugin.getWardrobeManager().addWithoutSave(new Wardrobe(id, null, null, null, false));
    }

    @SubCommand("npc-location")
    @Permission("chunkslab.gestures.wardrobe.npc-location")
    public void npcLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setNpcLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("player-location")
    @Permission("chunkslab.gestures.wardrobe.player-location")
    public void playerLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setPlayerLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("exit-location")
    @Permission("chunkslab.gestures.wardrobe.exit-location")
    public void exitLocation(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setExitLocation(player.getLocation());
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("status")
    @Permission("chunkslab.gestures.wardrobe.status")
    public void status(Player player, @Suggestion("wardrobes") String id, @Suggestion("wardrobeStatus") String status) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        wardrobe.setStatus("enable".equalsIgnoreCase(status));
        plugin.getWardrobeManager().addWithoutSave(wardrobe);
    }

    @SubCommand("save")
    @Permission("chunkslab.gestures.wardrobe.save")
    public void save(Player player, @Suggestion("wardrobes") String id) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(id);
        plugin.getWardrobeManager().addWardrobe(wardrobe);
    }

    @SubCommand("join")
    @Permission("chunkslab.gestures.wardrobe.join")
    public void join(Player player, @Suggestion("wardrobes") String wardrobeId) {
        Wardrobe wardrobe = plugin.getWardrobeManager().getWardrobe(wardrobeId);
        if (wardrobe == null) {
            //TODO: add wardrobe is non exist message
            return;
        }
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.spawn(player, wardrobe.getNpcLocation());
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.spawn(player, wardrobe.getPlayerLocation());
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        gesturePlayer.setWardrobe(wardrobe);
    }

    @SubCommand("leave")
    @Permission("chunkslab.gestures.wardrobe.leave")
    public void leave(Player player) {
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.inWardrobe()) {
            //TODO: add player not in the wardrobe message
            return;
        }
        Wardrobe wardrobe = gesturePlayer.getWardrobe();
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.destroy(player);
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.destroy(player);
        player.teleport(wardrobe.getExitLocation());
        gesturePlayer.setWardrobe(null);
    }

}