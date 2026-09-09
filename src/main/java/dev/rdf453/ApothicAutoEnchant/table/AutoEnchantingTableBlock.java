package dev.rdf453.ApothicAutoEnchant.table;

import javax.annotation.Nullable;

import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantingTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

public class AutoEnchantingTableBlock extends ApothEnchantingTableBlock {

    public AutoEnchantingTableBlock(Block.Properties prop) {
        super(prop);
    }
    //파괴시 아이템/xp 떨구기
    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos,
            boolean movedByPiston) {
        if (level.getBlockEntity(pos) instanceof TableBlockEntity be) {
            EnchantmentItemHandler handler = be.getData(EnchantmentItemHandler.TYPE);
            for (int index = 0; index < handler.size(); index++) {
                ItemResource resource = handler.getResource(index);
                int amount = handler.getAmountAsInt(index);

                if (!resource.isEmpty() && amount > 0) {
                    Block.popResource(level, pos, resource.toStack(amount));
                }
            }
            if (be.getxpTank() > 0) {

                ExperienceOrb.award(
                        level,
                        Vec3.atCenterOf(pos),
                        be.xpTank);
            }
        }
    }
    //레지스터 등록
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("apothic_auto_enchanting");
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("apothic_auto_enchanting");

    public static final DeferredBlock<AutoEnchantingTableBlock> BLOCK_HOLDER = BLOCKS.registerBlock(
            "auto_enchant_table",
            AutoEnchantingTableBlock::new,
            properties -> properties.destroyTime(2.5f));

    public static final DeferredItem<BlockItem> BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "auto_enchant_table",
            BLOCK_HOLDER);
    //블럭엔티티 연결
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }
    //틱 이벤트 연결
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        if (level.isClientSide())
            return null;

        return createTickerHelper(type, TableBlockEntity.BLOCK_ENTITY_TYPE_HOLDER.get(),
                (tickerLevel, tickerPos, tickerState, tickerBlockEntity) -> {
                    if (tickerBlockEntity instanceof TableBlockEntity tableBlockEntity) {
                        TableBlockEntity.serverTick(tickerLevel, tickerPos, tickerState, tableBlockEntity);
                    }
                });
    }
    //아이템 핸들러 반환
    public static ResourceHandler<ItemResource> getItemHandler(TableBlockEntity be, Direction dir) {
        return be.getData(EnchantmentItemHandler.TYPE);
    }
}