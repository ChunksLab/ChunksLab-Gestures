/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.playeranimator.nms.v1_18_R2.entity;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.model.player.Hand;
import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerBone;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RendererImpl implements IRenderer {

    private final List<Pair<EquipmentSlot, ItemStack>> equipments = new ArrayList<>();
    private final List<Pair<EquipmentSlot, ItemStack>> invisibleEquipments = new ArrayList<>();
    private ArmorStand armorStand;
    private AreaEffectCloud cloud;

    private PlayerBone limb;

    @Override
    public void setLimb(PlayerBone limb) {
        this.limb = limb;
        if (limb.getType().equals(LimbType.EFFECTS) || limb.getType().equals(LimbType.PARTICLE)) {
            return;
        }

        final var nmsWorld = ((CraftWorld) limb.getModel().getBase().getWorld()).getHandle();
        if(armorStand == null)
            armorStand = new ArmorStand(EntityType.ARMOR_STAND, nmsWorld);
        armorStand.setInvisible(true);
        armorStand.setRightArmPose(new Rotations(0, 0, 0));
        armorStand.setLeftArmPose(new Rotations(0, 0, 0));

        if(cloud == null)
            cloud = new AreaEffectCloud(EntityType.AREA_EFFECT_CLOUD, nmsWorld);
        cloud.setRadius(0);
        cloud.setInvisible(true);

        armorStand.startRiding(cloud);

        if(limb.getType().isItem()) {
            if(!(limb.getModel().getBase() instanceof LivingEntity living))
                return;
            if(limb.getType() == LimbType.RIGHT_ITEM) {
                final var item = living.getEquipment().getItemInMainHand();
                equipments.add(Pair.of(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(item)));
            }else {
                final var item = living.getEquipment().getItemInOffHand();
                equipments.add(Pair.of(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(item)));
            }
        }else {
            TextureWrapper textureWrapper = limb.getModel().getTexture().get(limb.getType().name());
            if (textureWrapper == null) {
                textureWrapper = limb.getModel().getTexture().get("HEAD");
            }
            org.bukkit.inventory.ItemStack playerHead = PlayerAnimator.api.getNms().setSkullTexture(null, textureWrapper);
            ItemMeta meta = playerHead.getItemMeta();
            meta.setCustomModelData(textureWrapper.isSlim() ? limb.getType().getSlimId() : limb.getType().getModelId());
            playerHead.setItemMeta(meta);
            equipments.add(Pair.of(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(playerHead)));
            org.bukkit.inventory.ItemStack invisiblePlayerHead = PlayerAnimator.api.getNms().setSkullTexture(null, textureWrapper);
            meta = invisiblePlayerHead.getItemMeta();
            meta.setCustomModelData(LimbType.INVISIBLE_HEAD.getModelId());
            invisiblePlayerHead.setItemMeta(meta);
            invisibleEquipments.add(Pair.of(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(playerHead)));
        }
    }

    @Override
    public void spawn() {
        final var packets = getSpawnPackets();
        for(final var player : limb.getModel().getSeenBy())
            sendPackets(player, packets);
    }

    @Override
    public void spawn(Player player) {
        sendPackets(player, getSpawnPackets());
    }

    @Override
    public void despawn() {
        final var packets = getDespawnPackets();
        for(final var player : limb.getModel().getSeenBy())
            sendPackets(player, packets);
    }

    @Override
    public void despawn(Player player) {
        sendPackets(player, getDespawnPackets());
    }


    @Override
    public void update() {
        final var packets = getMovePackets();
        for(final var player : limb.getModel().getSeenBy())
            sendPackets(player, packets);
    }

    @Override
    public void changeItem(Hand hand, int slot) {
        if (!limb.getType().isItem()) return;
        Entity entity = limb.getModel().getBase();
        if (!(entity instanceof Player player)) return;
        equipments.clear();
        org.bukkit.inventory.ItemStack selectedItem = (slot == -1)
                ? (hand == Hand.MAIN_HAND ? player.getEquipment().getItemInMainHand() : player.getEquipment().getItemInOffHand())
                : player.getInventory().getItem(slot);

        EquipmentSlot enumSlot = (hand == Hand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

        this.equipments.add(Pair.of(enumSlot, CraftItemStack.asNMSCopy(selectedItem)));
    }

    @Override
    public void changeItem(org.bukkit.inventory.ItemStack item) {
        this.equipments.clear();

        EquipmentSlot slot = switch (this.limb.getType()) {
            case LEFT_ITEM -> EquipmentSlot.OFFHAND;
            default -> EquipmentSlot.MAINHAND;
        };

        this.equipments.add(Pair.of(slot, CraftItemStack.asNMSCopy(item)));
    }

    private void sendPackets(Player player, List<Packet<?>> packets) {
        final var pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        for(Packet<?> packet : packets)
            pipeline.write(packet);
        pipeline.flush();
    }

    private List<Packet<?>> getSpawnPackets() {

        final var finalLocation = limb.getPosition().toLocation(limb.getModel().getBase().getWorld());
        cloud.setPos(finalLocation.getX(), finalLocation.getY(), finalLocation.getZ());
        armorStand.moveTo(finalLocation.getX(), finalLocation.getY() + cloud.getPassengersRidingOffset() + armorStand.getMyRidingOffset(), finalLocation.getZ(), limb.getModel().getBaseYaw(), 0);

        ClientboundAddMobPacket asSpawn = new ClientboundAddMobPacket(armorStand);
        ClientboundSetEntityDataPacket asMeta = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true);
        ClientboundSetEquipmentPacket asEquip = limb.isInvisible() && limb.getType().getModelId() != -1 ? new ClientboundSetEquipmentPacket(armorStand.getId(), invisibleEquipments) : new ClientboundSetEquipmentPacket(armorStand.getId(), equipments);
        ClientboundAddEntityPacket aecSpawn = new ClientboundAddEntityPacket(cloud);
        ClientboundSetEntityDataPacket aecMeta = new ClientboundSetEntityDataPacket(cloud.getId(), cloud.getEntityData(), true);
        ClientboundSetPassengersPacket mount = new ClientboundSetPassengersPacket(cloud);

        return Arrays.asList(asSpawn, asMeta, asEquip, aecSpawn, aecMeta, mount);
    }

    private List<Packet<?>> getDespawnPackets() {
        ClientboundRemoveEntitiesPacket remove = new ClientboundRemoveEntitiesPacket(armorStand.getId(), cloud.getId());
        return List.of(remove);
    }

    private List<Packet<?>> getMovePackets() {
        final var finalLocation = limb.getPosition().toLocation(limb.getModel().getBase().getWorld());
        final var finalRotation = limb.getRotation();

        cloud.setPos(finalLocation.getX(), finalLocation.getY(), finalLocation.getZ());
        if(limb.getType() == LimbType.LEFT_ITEM)
            armorStand.setLeftArmPose(toNMS(finalRotation));
        else
            armorStand.setRightArmPose(toNMS(finalRotation));


        ClientboundTeleportEntityPacket teleport = new ClientboundTeleportEntityPacket(cloud);
        //ClientboundMoveEntityPacket.Rot rotate = new ClientboundMoveEntityPacket.Rot(armorStand.getId(), IRenderer.rotByte(limb.getModel().getBaseYaw()), (byte) 0, false);
        ClientboundSetEntityDataPacket meta = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), false);
        ClientboundSetEquipmentPacket asEquip = limb.isInvisible() && limb.getType().getModelId() != -1 ? new ClientboundSetEquipmentPacket(armorStand.getId(), invisibleEquipments) : new ClientboundSetEquipmentPacket(armorStand.getId(), equipments);
        return Arrays.asList(asEquip, teleport, meta);
    }

    private Rotations toNMS(EulerAngle angle) {
        return new Rotations((float)Math.toDegrees(angle.getX()), (float)Math.toDegrees(angle.getY()), (float)Math.toDegrees(angle.getZ()));
    }

}
