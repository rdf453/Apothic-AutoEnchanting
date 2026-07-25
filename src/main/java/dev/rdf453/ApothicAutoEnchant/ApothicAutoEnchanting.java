package dev.rdf453.ApothicAutoEnchant;

import net.minecraft.world.level.block.entity.BlockEntityType;
import dev.rdf453.ApothicAutoEnchant.table.AutoEnchantingTableBlock;
import dev.rdf453.ApothicAutoEnchant.table.EnchTableScreen;
import dev.rdf453.ApothicAutoEnchant.table.EnchantMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.resources.Identifier;
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

    public static final String MODID = "apothic_auto_enchanting";

    public ApothicAutoEnchanting(IEventBus modEventBus) {
        modEventBus.addListener(EnchTableScreen::registerScreens);
    }


    @SubscribeEvent
    public void addBlockEntityVaildBlocks(BlockEntityTypeAddBlocksEvent e) {
        e.modify(BlockEntityType.ENCHANTING_TABLE, 
            Auto.Blocks.AUTO_ENCHANT_TABLE.value());
    }

    @SubscribeEvent
    public void addCreativeContents(BuildCreativeModeTabContentsEvent event) {
    // 💡 ResourceLocation 대신 최신 'Identifier'를 사용합니다!
    ResourceKey<CreativeModeTab> apothicEnchantTab = ResourceKey.create(
        Registries.CREATIVE_MODE_TAB, 
        Identifier.fromNamespaceAndPath("apothic_enchanting", "enchanting") // ◀ 여기도 Identifier로 교체
    );

    if (event.getTabKey().equals(apothicEnchantTab)) {
        event.accept(Auto.Items.AUTO_ENCHANT_TABLE.value()); 
    }
    }


    @EventBusSubscriber(modid = ApothicAutoEnchanting.MODID)
    public static class InteractionEvents {
        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (event.getLevel().isClientSide()) return;
            if (event.getHand() != InteractionHand.MAIN_HAND) return;

            if (!(event.getLevel().getBlockState(event.getPos()).getBlock() instanceof AutoEnchantingTableBlock)) return;

            // 웅크린 우클릭은 원본 블록 동작(= Apoth 메뉴)으로 넘기고,
            // 일반 우클릭은 자동화 메뉴를 직접 열어 커스텀 스크린을 사용한다.
            if (event.getEntity().isCrouching()) return;

            BlockPos pos = event.getPos();
            event.getEntity().openMenu(
                new SimpleMenuProvider((id, inv, player) -> new EnchantMenu(id, inv, pos), event.getLevel().getBlockState(pos).getBlock().getName()),
                pos
            );

            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
