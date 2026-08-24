package dev.rdf453.ApothicAutoEnchant.table;






import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;




public class EnchTableScreen extends ApothEnchantmentScreen {
	
	
	private record xpButton(int x, int y , String label, int id){};
	private record onoff(int x, int y, String label , int id){};

	private Button activation;

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

	private EnchantMenu autoMenu() {
		return (EnchantMenu) this.menu;
	}

	//버튼 추가는 여기서
	@Override
	protected void init() {
		super.init();

		//xp버튼
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

		//활성화 버튼
		onoff data = ONOFF_LAYOUT[0];
		activation =  
			this.addRenderableWidget(Button.builder(
			Component.literal(autoMenu().getDisplayedActivation() ? "ON":"OFF"),
			button -> {
				this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
					data.id());
			})
			.bounds(data.x(),data.y(),50,20).build()
		);
		
	}
	//선택된 파워레벨만 활성으로 두고 나머지 비활성으로 되게하여 강조로
	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks){
		super.extractBackground(gfx, mouseX, mouseY, partialTicks);

		gfx.text(
			this.font,
			String.valueOf(autoMenu().getDisplayedXpLevel()),
			0,
			0,
			0xFF4CFC12
		);
		activation.setMessage(
			Component.literal(autoMenu().getDisplayedActivation() ? "ON" : "OFF")
		);

		
// Copyright notice for derived work:
// This file includes modified code from Apothic Enchanting.
// Original work: Apothic Enchanting
// Original author: Shadows_of_Fire
// License of original work: MIT
// Changes in this file: adapted and modified for this project by rdf453
//
		int xCenter = (this.width - this.imageWidth) / 2;
        int yCenter = (this.height - this.imageHeight) / 2;

		//스크린 초기화
        //gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, xCenter, yCenter, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        //this.extractBook(gfx, xCenter, yCenter);

        EnchantmentNames.getInstance().initSeed(this.menu.getEnchantmentSeed());
        int lapis = this.menu.getGoldCount();

		for (int slot = 0; slot < 3; ++slot) {
            int j1 = xCenter + 60;
            int k1 = j1 + 20;
            int level = this.menu.costs[slot];
            if (level == 0) {
                gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 218, 108, 19, 256, 256);
            }
            else {
                String s = "" + level;
                int width = 86 - this.font.width(s);
                FormattedText name = EnchantmentNames.getInstance().getRandomName(this.font, width);
                int color = 6839882;
                if ((lapis < slot + 1 || this.minecraft.player.experienceLevel < level) && !this.minecraft.player.getAbilities().instabuild || this.menu.enchantClue[slot] == -1) {
                    gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 218, 108, 19, 256, 256);
                    gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1 + 1, yCenter + 15 + 19 * slot, 16 * slot, 239, 16, 16, 256, 256);
                    gfx.textWithWordWrap(this.font, name, k1, yCenter + 16 + 19 * slot, width, ARGB.opaque((color & 16711422) >> 1), false);
                    color = 4226832;
                }
                else {
                    int hx = mouseX - (xCenter + 60);
                    int hy = mouseY - (yCenter + 14 + 19 * slot);
                    if (hx >= 0 && hy >= 0 && hx < 108 && hy < 19) {
                        gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 237, 108, 19, 256, 256);
                        color = 16777088;
                    }
                    else {
                        gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 199, 108, 19, 256, 256);
                    }
                    gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1 + 1, yCenter + 15 + 19 * slot, 16 * slot, 223, 16, 16, 256, 256);
                    gfx.textWithWordWrap(this.font, name, k1, yCenter + 16 + 19 * slot, width, ARGB.opaque(color), false);
                    color = 8453920;
                }
                gfx.text(this.font, s, k1 + 86 - this.font.width(s), yCenter + 16 + 19 * slot + 7, ARGB.opaque(color));
            }
        }
	}

}
//#4cfc12
//#16777088
//**정리 
// 1.파워 선택이 처음부터 다 뜨게 한다
// 2. 특정 파워 선택시 선택한 파워 제외하고 전부 지운다
// */
