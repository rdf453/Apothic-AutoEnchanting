package dev.rdf453.ApothicAutoEnchant.table;

import javax.annotation.Nullable;

import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantingTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.registries.DeferredItem;


public class AutoEnchantingTableBlock extends ApothEnchantingTableBlock {

    public AutoEnchantingTableBlock(Block.Properties prop) {
        super(prop);
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("apothic_auto_enchanting");
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("apothic_auto_enchanting");

    public static final DeferredBlock<AutoEnchantingTableBlock> BLOCK_HOLDER = BLOCKS.registerBlock(
        "auto_enchant_table",
        AutoEnchantingTableBlock::new,
        properties -> properties.destroyTime(2.5f)
    );

    public static final DeferredItem<BlockItem> BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "auto_enchant_table", 
            BLOCK_HOLDER
    );

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type ) {
        if (level.isClientSide()) return null;  
            
            return createTickerHelper(type, TableBlockEntity.BLOCK_ENTITY_TYPE_HOLDER.get(), (tickerLevel, tickerPos, tickerState, tickerBlockEntity) -> {
            if (tickerBlockEntity instanceof TableBlockEntity tableBlockEntity) {
                TableBlockEntity.serverTick(tickerLevel, tickerPos, tickerState, tableBlockEntity);
            }
        });
    }

    public static ResourceHandler<ItemResource> getItemHandler(EnchantingTableBlockEntity be, Direction dir) {
        return (ResourceHandler)be.getData(EnchantmentItemHandler.TYPE);
    }
}