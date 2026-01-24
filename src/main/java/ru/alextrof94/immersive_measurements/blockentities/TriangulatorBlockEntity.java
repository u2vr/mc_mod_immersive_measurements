package ru.alextrof94.immersive_measurements.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.alextrof94.immersive_measurements.ModBlockEntities;
import ru.alextrof94.immersive_measurements.ModDataComponents;

import java.util.ArrayList;
import java.util.List;

public class TriangulatorBlockEntity extends BlockEntity {
    private GlobalPos triangulatedPos;
    private List<GlobalPos> lodestonePositions = new ArrayList<>();
    private int ledsCount = 0;

    public TriangulatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TRIANGULATOR.get(), pos, blockState);
    }

    public void setTriangulatedPos(GlobalPos pos) {
        this.triangulatedPos = pos;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public GlobalPos getTriangulatedPos() {
        return triangulatedPos;
    }

    public void setLodestonePositions(List<GlobalPos> positions) {
        this.lodestonePositions = positions;
        this.ledsCount = positions.size();
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public List<GlobalPos> getLodestonePositions() {
        return lodestonePositions;
    }

    public int getLedsCount() {
        return ledsCount;
    }

    public void setLedsCount(int count) {
        this.ledsCount = count;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (triangulatedPos != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, triangulatedPos).resultOrPartial()
                    .ifPresent(t -> tag.put("triangulatedPos", t));
        }
        if (lodestonePositions != null && !lodestonePositions.isEmpty()) {
            GlobalPos.CODEC.listOf().encodeStart(NbtOps.INSTANCE, lodestonePositions).resultOrPartial()
                    .ifPresent(t -> tag.put("lodestonePositions", t));
        }
        tag.putInt("ledsCount", ledsCount);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("triangulatedPos")) {
            GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("triangulatedPos")).resultOrPartial()
                    .ifPresent(p -> triangulatedPos = p);
        }
        if (tag.contains("lodestonePositions")) {
            GlobalPos.CODEC.listOf().parse(NbtOps.INSTANCE, tag.get("lodestonePositions")).resultOrPartial()
                    .ifPresent(p -> lodestonePositions = new ArrayList<>(p));
        }
        if (tag.contains("ledsCount")) {
            tag.getInt("ledsCount").ifPresent(v -> ledsCount = v);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
