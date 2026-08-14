package dev.rdf453.ApothicAutoEnchant.table;






import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu;

//**씨발 더러운거 ㅈㅈ 클릭 가로채서 토글 버튼 활성화 하고 나머지 버튼 추가


public class EnchTableScreen extends ApothEnchantmentScreen {
	//private record powerButton(int x, int y, int id){};
	private record xpButton(int x, int y , String label, int id){};
	private record onoff(int x, int y, String label , int id){};

	//private static final powerButton[] POWER_BUTTON_LAYOUT = new powerButton[] {
	//	new powerButton()
	//}; 클릭 영역으로 처리하고 토글 코스트로 id 값만 잘 넘길 것

	private static final xpButton[] XP_BUTTON_LAYOUT = new xpButton[] {
		new xpButton(BACKGROUND_TEXTURE_HEIGHT, inventoryLabelX, "+ALL", 6),
		new xpButton(BACKGROUND_TEXTURE_HEIGHT, inventoryLabelX, "+10LV", 7),
		new xpButton(BACKGROUND_TEXTURE_HEIGHT, inventoryLabelX, "-10LV", 8),
		new xpButton(BACKGROUND_TEXTURE_HEIGHT, inventoryLabelX, "-ALL", 9)
	};
	//좌표 세부 지정은 디버그 모드로
	private static final onoff[] ONOFF_LAYOUT = new onoff[] {
		new onoff(0, 0, "AUTO", 10),
	};

	public EnchTableScreen(EnchantmentMenu menu, Inventory inv, Component title){
		super(menu,inv,title);
	}

	//버튼 추가는 여기서
	@Override
	protected void init() {
		super.init();


	}

}
