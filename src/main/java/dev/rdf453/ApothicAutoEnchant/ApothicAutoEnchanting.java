package dev.rdf453.ApothicAutoEnchant;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) @Mod 진입점만 담당하는 최소 부트스트랩 클래스다.
 *   2) 실제 블록/BE/메뉴 등록은 Auto 쪽으로 분리할 수 있게 구조를 비워 두었다.
 * - 다음 작업:
 *   1) 공용 레지스트리 초기화가 필요하면 생성자에서 이벤트 버스 연결만 추가한다.
 *   2) 클라이언트 전용 등록은 별도 이벤트 헬퍼로 분리한다.
 * - 리스크/주의:
 *   1) modid 문자열은 리소스 경로와 일치하도록 유지한다.
 */

@Mod(ApothicAutoEnchanting.MODID)
public class ApothicAutoEnchanting {

    public static final String MODID = "Apothic_Auto_Enchanting";


    @SubscribeEvent
    public void addBlockEntityVaildBlocks(BlockEntityTypeAddBlocksEvent e) {
        e.modify(BlockEntityType.ENCHANTING_TABLE, 
            Auto.Blocks.AUTO_ENCHANT_TABLE.value());
    }
}
