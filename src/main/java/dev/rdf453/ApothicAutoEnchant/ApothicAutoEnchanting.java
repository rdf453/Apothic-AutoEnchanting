package dev.rdf453.ApothicAutoEnchant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier; 
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge; // ◀ 네오포지 버스 임포트 추가
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.rdf453.ApothicAutoEnchant.table.AutoEnchantingTableBlock;
import dev.rdf453.ApothicAutoEnchant.table.EnchTableScreen;
import dev.rdf453.ApothicAutoEnchant.table.EnchantMenu;
import dev.shadowsoffire.apothic_enchanting.Ench;
import dev.shadowsoffire.placebo.tabs.TabFillingRegistry;

@Mod(ApothicAutoEnchanting.MODID)
public class ApothicAutoEnchanting {

    public static final String MODID = "apothic_auto_enchanting";
    public static final Logger LOGGER = LoggerFactory.getLogger("Apothesis : Auto Enchanting");

    public ApothicAutoEnchanting(IEventBus bus) {
        // 1. 클라이언트 전용 화면 등록 리스너 연결
        bus.addListener(EnchTableScreen::registerScreens);
        
        // 2. 모드 로딩 단계 리스너들 명시적으로 바인딩
        bus.addListener(this::init);
        bus.addListener(this::addBlockEntityVaildBlocks);
        
        // 3. 포지 이벤트 버스(NeoForge.EVENT_BUS)에 이 클래스를 등록하여 크리에이티브 탭 이벤트를 정상 수신하게 만듦
        NeoForge.EVENT_BUS.register(this);
        
        // 4. 플라시보 최신 등록 사양에 맞춰 RegisterEvent 연동
        bus.addListener(net.neoforged.neoforge.registries.RegisterEvent.class, event -> {
            Auto.R.register(event);
        });
    }

    public void init(FMLCommonSetupEvent e) {
        e.enqueueWork(() -> {
            TabFillingRegistry.register(Ench.Tabs.ENCH.getKey(), Auto.Items.AUTO_ENCHANT_TABLE);
        });
    }

    public void addBlockEntityVaildBlocks(BlockEntityTypeAddBlocksEvent e) {
        e.modify(BlockEntityType.ENCHANTING_TABLE, Auto.Blocks.AUTO_ENCHANT_TABLE.value());
    }

    // ★ Forge_BUS 이벤트를 받기 위해 @SubscribeEvent 애노테이션 부착
    @SubscribeEvent
    public void addCreativeContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> apothicEnchantTab = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, 
            Identifier.fromNamespaceAndPath("apothic_enchanting", "enchanting")
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
