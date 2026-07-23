package dev.rdf453.ApothicAutoEnchant.table;

import javax.annotation.Nullable;

//TODO:블럭의 속성 설정

import dev.shadowsoffire.apothic_enchanting.table.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) ApothEnchantingTableBlock 상속 기반으로 커스텀 블록 엔트리만 정리되어 있다.
 *   2) 서버 tick 연결과 테이블 BE 생성 경로를 붙이면 자동 인챈트 루프가 이어진다.
 * - 다음 작업:
 *   1) newBlockEntity/getTicker를 TableBlockEntity와 연결한다.
 *   2) use 상호작용과 크리에이티브 탭 노출 정책을 분리해 정리한다.
 *   3) 필요 시 블록 상태 프로퍼티나 드롭 정책을 추가한다.
 * - 리스크/주의:
 *   1) BlockEntityType 등록 여부에 따라 getTicker의 비교 대상이 달라질 수 있다.
 */
public class AutoEnchantingTableBlock extends ApothEnchantingTableBlock {

    public AutoEnchantingTableBlock(Block.Properties prop) {
        super(prop);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TableBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type ) {
        return level.isClientSide() ? null : createTickerHelper(type, BlockEntityType.ENCHANTING_TABLE, (tickerLevel, tickerPos, tickerState, tickerBlockEntity) -> {
            if (tickerBlockEntity instanceof TableBlockEntity tableBlockEntity) {
                TableBlockEntity.serverTick(tickerLevel, tickerPos, tickerState, tableBlockEntity);
            }
        });
    }

    
    
}