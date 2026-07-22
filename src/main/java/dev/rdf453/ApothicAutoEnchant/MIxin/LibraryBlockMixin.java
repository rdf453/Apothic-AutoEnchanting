package dev.rdf453.ApothicAutoEnchant.MIxin;

//TODO:플레이어의 상호작용에 반응하여 ui의 입력부에 넣기
import dev.rdf453.ApothicAutoEnchant.util.LibraryTransfer;
import dev.shadowsoffire.apothic_enchanting.library.EnchLibraryBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchLibraryBlock.class)
/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) 서버 우클릭 시 LibraryTransfer.AutoEnch_flushBufferToInput()를 호출하는 입력 전송 경로가 있다.
 *   2) 블럭 상호작용을 도서관 버퍼 비우기 트리거로 삼는 최소 동작은 마련되어 있다.
 * - 다음 작업:
 *   1) 도서관 좌표 캐시가 생기면 재탐색 비용을 줄여 바로 flush하도록 묶는다.
 *   2) Mixin과 이벤트 등록의 중복 책임을 정리해 한 가지 진입점으로 통일한다.
 * - 리스크/주의:
 *   1) 플레이어 상호작용과 자동화 틱이 같은 버퍼를 건드릴 수 있으므로 호출 정책을 분리해야 한다.
 */
public class LibraryBlockMixin {

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event) {
        // 서버에서만 버퍼 비우기를 수행한다.
        if (event.getLevel().isClientSide()) {
            return;
        }

        BlockEntity blockEntity = event.getLevel().getBlockEntity(event.getPos());
        if (blockEntity instanceof LibraryTransfer transfer) {
            // 플레이어 상호작용 시점에 버퍼 아이템을 입력 슬롯으로 최대한 밀어넣는다.
            transfer.AutoEnch_flushBufferToInput();
        }
    }
}
