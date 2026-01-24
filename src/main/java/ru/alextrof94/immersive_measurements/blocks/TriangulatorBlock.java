package ru.alextrof94.immersive_measurements.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ru.alextrof94.immersive_measurements.ModDataComponents;
import ru.alextrof94.immersive_measurements.ModItems;
import ru.alextrof94.immersive_measurements.blockentities.TriangulatorBlockEntity;

import java.util.List;

public class TriangulatorBlock extends Block implements EntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    // 8.275 to 10.275 on X (Width 2), full Z, Y up to ~13.
    private static final VoxelShape SHAPE_EW = Block.box(8.275, 0, 0, 10.275, 13, 16);
    private static final VoxelShape SHAPE_NS = Block.box(0, 0, 8.275, 16, 13, 10.275);

    public TriangulatorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            return SHAPE_NS;
        } else {
            return SHAPE_EW;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock,
            @Nullable Orientation orientation, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, orientation, movedByPiston);
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TriangulatorBlockEntity(pos, state);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer,
            ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TriangulatorBlockEntity triangulator) {
                if (stack.has(ModDataComponents.TRIANGULATED_POS.get())) {
                    triangulator.setTriangulatedPos(stack.get(ModDataComponents.TRIANGULATED_POS.get()));
                }
                if (stack.has(ModDataComponents.LODESTONE_POSITIONS.get())) {
                    triangulator.setLodestonePositions(stack.get(ModDataComponents.LODESTONE_POSITIONS.get()));
                }
                if (stack.has(ModDataComponents.LEDS_COUNT.get())) {
                    triangulator.setLedsCount(stack.get(ModDataComponents.LEDS_COUNT.get()));
                }
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack stack = new ItemStack(this);
        if (be instanceof TriangulatorBlockEntity triangulator) {
            if (triangulator.getTriangulatedPos() != null) {
                stack.set(ModDataComponents.TRIANGULATED_POS.get(), triangulator.getTriangulatedPos());
            }
            if (!triangulator.getLodestonePositions().isEmpty()) {
                stack.set(ModDataComponents.LODESTONE_POSITIONS.get(), triangulator.getLodestonePositions());
            }
            stack.set(ModDataComponents.LEDS_COUNT.get(), triangulator.getLedsCount());
            ModItems.updateItemModelFromLeds(stack, "triangulator");
        }
        return List.of(stack);
    }
}
