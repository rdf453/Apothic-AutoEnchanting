package dev.rdf453.ApothicAutoEnchant.table;






import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.EnchantmentMenu;




public class EnchTableScreen extends ApothEnchantmentScreen {
	
	private EnchantMenu autoMenu() {
		return (EnchantMenu) this.menu;
	}

	int costSet = autoMenu().getCostSetter();
	private GridLayout xpLayout;
	private StringWidget levelLabel;

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

	
	//버튼 추가는 여기서
	@Override
	protected void init() {
		super.init();
		xpLayout = new GridLayout();
		xpLayout.spacing(2);

		//xp버튼
		for(int i = 0 ; i< XP_BUTTON_LAYOUT.length;i++) {
			xpButton data = XP_BUTTON_LAYOUT[i];
			this.addRenderableWidget(Button.builder(
				Component.literal(data.label()),
				button -> {
					this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId,
					data.id());
				})
				.bounds(data.x(),data.y(),50,20).build()
		);//아몰랑 영 안되면 위젯창으로 관리하는거 포기하지 뭐
		xpLayout.setPosition(10, 20);
		xpLayout.arrangeElements();
		}
		levelLabel = new StringWidget(
			Component.literal(String.valueOf(autoMenu().getDisplayedXpLevel())).withColor(0x4CFC12),
			this.font
		);
		levelLabel.setWidth(40);
		levelLabel.setHeight(20);

		xpLayout.addChild(levelLabel,1,1);

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
	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
		
        for (int i = 0; i < 3; i++) {
            double xx = event.x() - (xo + 60);
            double yy = event.y() - (yo + 14 + 19 * i);
			double cyy = event.y() -(yo + 14 +19 * costSet);
			//파워 선택 취소
			if(xx >= 0.0 && cyy >= 0.0 && xx < 108.0 && cyy < 19.0 && costSet == i) {
				costSet = -1;
				return true;
			}//여기서의 costSet은 Menu로 보내는 id이다
            if (xx >= 0.0 && yy >= 0.0 && xx < 108.0 && yy < 19.0 && this.menu.clickMenuButton(this.minecraft.player, i+3)) {
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, i+3);
                return true;
            }
        }

		return super.mouseClicked(event , doubleClick);
	}
	
	//선택된 파워레벨만 활성으로 두고 나머지 비활성으로 되게하여 강조로
	@Override
	public void extractBackground(GuiGraphicsExtractor gfx, int mouseX, int mouseY, float partialTicks){
		super.extractBackground(gfx, mouseX, mouseY, partialTicks);
		//레벨 표시
		levelLabel.setMessage(
			Component.literal(String.valueOf(autoMenu().getDisplayedXpLevel())).withColor(0x4CFC12)
		);
		//라벨 변경
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
        
	

		
				for (int slot = 0; slot < 3; ++slot) {
				//레벨표시 시작 위치
				int j1 = xCenter + 60;
				//레벨표시 종료 위치
				int k1 = j1 + 20;
				//표시되는 레벨
				int level = this.menu.costs[slot];
				//테이블이 소지중인 레벨
				int tableLevel = autoMenu().getDisplayedXpLevel();
				
				if (level == 0) {
					//기본 화면
					gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 218, 108, 19, 256, 256);
				}
				else {
					String s = "" + level;
					int width = 86 - this.font.width(s);
					FormattedText name = EnchantmentNames.getInstance().getRandomName(this.font, width);
					int color = 6839882;
					if ((tableLevel < level) && !this.minecraft.player.getAbilities().instabuild || this.menu.enchantClue[slot] == -1) {
						//비활성화 파워 선택 바
						gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 218, 108, 19, 256, 256);
						//비활성화 레벨 그림
						gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1 + 1, yCenter + 15 + 19 * slot, 16 * slot, 239, 16, 16, 256, 256);
						//룬문자
						gfx.textWithWordWrap(this.font, name, k1, yCenter + 16 + 19 * slot, width, ARGB.opaque((color & 16711422) >> 1), false);
						color = 4226832;
					}
					else {
						//이미 파워 선택이 된 경우
						if((costSet >= 3) && (costSet <= 5)) {
							costSet -= 3;

							//마우스 좌표
							int hx = mouseX - (xCenter + 60);
							int hy = mouseY - (yCenter + 14 + 19 * costSet);
							if (hx >= 0 && hy >= 0 && hx < 108 && hy < 19) {
								//마우스가 위에 있는 파워 선택창
								gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * costSet, 148, 237, 108, 19, 256, 256);
								color = 16777088;
							}
							else {
								//파워 선택창
								gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * costSet, 148, 199, 108, 19, 256, 256);

							//레벨 그림
							gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1 + 1, yCenter + 15 + 19 * costSet, 16 * costSet, 223, 16, 16, 256, 256);
							
							//룬문자
							gfx.textWithWordWrap(this.font, name, k1, yCenter + 16 + 19 * costSet, width, ARGB.opaque(color), false);
							color = 8453920;
						}
						}
						//선택된 레벨이 없을 경우
						else {
							//마우스 좌표
							int hx = mouseX - (xCenter + 60);
							int hy = mouseY - (yCenter + 14 + 19 * slot);
							if (hx >= 0 && hy >= 0 && hx < 108 && hy < 19) {
								//마우스가 위에 있는 파워 선택창
								gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 237, 108, 19, 256, 256);
								color = 16777088;
							}

							else {
								//파워 선택창
								gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1, yCenter + 14 + 19 * slot, 148, 199, 108, 19, 256, 256);
							}//레벨 그림
							gfx.blit(RenderPipelines.GUI_TEXTURED, TEXTURES, j1 + 1, yCenter + 15 + 19 * slot, 16 * slot, 223, 16, 16, 256, 256);
							//룬문자
							gfx.textWithWordWrap(this.font, name, k1, yCenter + 16 + 19 * slot, width, ARGB.opaque(color), false);
							color = 8453920;
						}
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
