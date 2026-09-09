package dev.rdf453.ApothicAutoEnchant;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.rdf453.ApothicAutoEnchant.table.AutoEnchantingTableBlock;
import dev.rdf453.ApothicAutoEnchant.table.EnchTableScreen;
import dev.rdf453.ApothicAutoEnchant.table.EnchantMenu;
import dev.rdf453.ApothicAutoEnchant.table.TableBlockEntity;
import dev.rdf453.ApothicAutoEnchant.table.EnchantmentItemHandler;

@EventBusSubscriber(modid =  ApothicAutoEnchanting.MODID)
@Mod(ApothicAutoEnchanting.MODID)
public class ApothicAutoEnchanting {

    public static final String MODID = "apothic_auto_enchanting";
    public static final Logger LOGGER = LoggerFactory.getLogger("Apothesis : Auto Enchanting");

    
    


    public ApothicAutoEnchanting(IEventBus bus) {
        AutoEnchantingTableBlock.BLOCKS.register(bus);
        AutoEnchantingTableBlock.ITEMS.register(bus);
        EnchantMenu.MENUS.register(bus);
        TableBlockEntity.BLOCK_ENTITIES.register(bus);
        
        // 1. 클라이언트 전용 화면 등록 리스너 연결
        bus.addListener(EnchTableScreen::registerScreens);
        bus.addListener(ApothicAutoEnchanting::registerCapabilities);
        
    }

    

    
    
    @SubscribeEvent
    public static void BuildContent(BuildCreativeModeTabContentsEvent e) {
        if(e.getTabKey()==CreativeModeTabs.FUNCTIONAL_BLOCKS) e.accept(AutoEnchantingTableBlock.BLOCK_ITEM.get());
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

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.Item.BLOCK,
            TableBlockEntity.BLOCK_ENTITY_TYPE_HOLDER.get(),
            (blockEntity,direction) -> 
                (blockEntity.getData(EnchantmentItemHandler.TYPE))
        );
    }    
}
