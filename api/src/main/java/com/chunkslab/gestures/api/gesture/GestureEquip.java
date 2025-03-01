package com.chunkslab.gestures.api.gesture;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class GestureEquip {
    private ItemStack rightHand;
    private ItemStack leftHand;
    private ItemStack head;
}