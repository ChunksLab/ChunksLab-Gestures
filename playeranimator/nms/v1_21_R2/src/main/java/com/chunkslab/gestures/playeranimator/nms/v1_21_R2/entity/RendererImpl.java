package com.chunkslab.gestures.playeranimator.nms.v1_21_R2.entity;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerBone;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class RendererImpl implements IRenderer {

    private final List<Pair<EquipmentSlot, ItemStack>> equipments = new ArrayList<>();
    private final List<Pair<EquipmentSlot, ItemStack>> invisibleEquipments = new ArrayList<>();
    private ArmorStand armorStand;
    private AreaEffectCloud cloud;

    private PlayerBone limb;

    @Override
    public void setLimb(PlayerBone limb) {
        this.limb = limb;

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

    private void sendPackets(Player player, List<Packet<?>> packets) {
        ServerGamePacketListenerImpl packetListener = ((CraftPlayer) player).getHandle().connection;
        //final var pipeline = ((CraftPlayer) player).getHandle().connection.connection.channel.pipeline();
        for(Packet<?> packet : packets)
            packetListener.send(packet);
        //pipeline.flush();
    }

    private List<Packet<?>> getSpawnPackets() {

        final var finalLocation = limb.getPosition().toLocation(limb.getModel().getBase().getWorld());
        cloud.setPos(finalLocation.getX(), finalLocation.getY(), finalLocation.getZ());
        Vec3 ridingPosition = this.armorStand.getPassengerRidingPosition(cloud);
        Vec3 vehicleAttachmentPoint = this.cloud.getVehicleAttachmentPoint(armorStand);
        armorStand.moveTo(finalLocation.getX(), finalLocation.getY() + (ridingPosition.y - vehicleAttachmentPoint.y), finalLocation.getZ(), limb.getModel().getBaseYaw(), 0);

        ClientboundAddEntityPacket asSpawn = new ClientboundAddEntityPacket(armorStand, 0, armorStand.blockPosition());
        ClientboundSetEntityDataPacket asMeta = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData().getNonDefaultValues());
        ClientboundSetEquipmentPacket asEquip = limb.isInvisible() && limb.getType().getModelId() != -1 ? new ClientboundSetEquipmentPacket(armorStand.getId(), invisibleEquipments) : new ClientboundSetEquipmentPacket(armorStand.getId(), equipments);
        ClientboundAddEntityPacket aecSpawn = new ClientboundAddEntityPacket(cloud, 0, cloud.blockPosition());
        ClientboundSetEntityDataPacket aecMeta = new ClientboundSetEntityDataPacket(cloud.getId(), cloud.getEntityData().getNonDefaultValues());
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


        ClientboundTeleportEntityPacket teleport = new ClientboundTeleportEntityPacket(
                cloud.getId(),
                new PositionMoveRotation(
                        new Vec3(armorStand.getX(), armorStand.getY(), armorStand.getZ()),
                        Vec3.ZERO,
                        armorStand.getBukkitYaw(),
                        armorStand.getXRot()
                ),
                Set.of(),
                false
                );
        ClientboundMoveEntityPacket.Rot rotate = new ClientboundMoveEntityPacket.Rot(armorStand.getId(), IRenderer.rotByte(limb.getModel().getBaseYaw()), (byte) 0, false);
        ClientboundSetEntityDataPacket meta = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData().getNonDefaultValues());
        ClientboundSetEquipmentPacket asEquip = limb.isInvisible() && limb.getType().getModelId() != -1 ? new ClientboundSetEquipmentPacket(armorStand.getId(), invisibleEquipments) : new ClientboundSetEquipmentPacket(armorStand.getId(), equipments);
        return Arrays.asList(asEquip, teleport, rotate, meta);
    }

    private Rotations toNMS(EulerAngle angle) {
        return new Rotations((float)Math.toDegrees(angle.getX()), (float)Math.toDegrees(angle.getY()), (float)Math.toDegrees(angle.getZ()));
    }

}
