package com.fusionflux.originsumbrellas.mixin;

import com.fusionflux.originsumbrellas.state.property.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FlowerBlock.class)
public class FlowerBlockMixin extends Block {
    private static final IntProperty FLOWERS = Properties.FLOWERS;

    public FlowerBlockMixin(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(FLOWERS, 1));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FLOWERS);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(FLOWERS, Math.min(4, blockState.get(FLOWERS) + 1));
        }
        return super.getPlacementState(ctx);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        if (!ctx.shouldCancelInteraction() && ctx.getStack().isOf(this.asItem()) && state.get(FLOWERS) < 4) {
            return true;
        }
        return super.canReplace(state, ctx);
    }
}
