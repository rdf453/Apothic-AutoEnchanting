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




public class EnchTableScreen extends ApothEnchantmentScreen {
	protected final EnchantMenu amenu;
	
	private record xpButton(int x, int y , String label, int id){};
	private record onoff(int x, int y, String label , int id){};

	//private static final powerButton[] POWER_BUTTON_LAYOUT = new powerButton[] {
	//	new powerButton()
	//}; 클릭 영역으로 처리하고 토글 코스트로 id 값만 잘 넘길 것

	private static final xpButton[] XP_BUTTON_LAYOUT = new xpButton[] {
		new xpButton(0, 0, "+ALL", 6),
		new xpButton(0, 0, "+10LV", 7),
		new xpButton(0, 0, "-10LV", 8),
		new xpButton(0, 0, "-ALL", 9)
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

		for(xpButton data:XP_BUTTON_LAYOUT) {
			this.addRenderableWidget(Button.builder(
				Component.literal(data.label()),
				button -> {
					this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
					data.id());
				})
				.bounds(data.x(),data.y,50,20).build()
		);
		}


	}
	//선택된 파워레벨만 활성으로 두고 나머지 비활성으로 되게하여 강조로
	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks){
		super.extractBackground(gfx, mouseX, mouseY, partialTicks);


	}

}
