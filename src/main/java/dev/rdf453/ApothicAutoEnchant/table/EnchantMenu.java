package dev.rdf453.ApothicAutoEnchant.table;



import dev.rdf453.ApothicAutoEnchant.ApothicAutoEnchanting;
import dev.rdf453.ApothicAutoEnchant.util.XpTransfer;
import dev.shadowsoffire.apothic_enchanting.table.ApothEnchantmentMenu;
import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
public class EnchantMenu extends ApothEnchantmentMenu {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, ApothicAutoEnchanting.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<EnchantMenu>> AUTO_ENCHANT_MENU = MENUS.register(
        "auto_enchant_menu",
        () -> IMenuTypeExtension.create((id, inv, data) -> new EnchantMenu(id, inv, data.readBlockPos()))
    );

    private final BlockPos tablePos;
    private TableBlockEntity be;

    private static final int XP_LEVEL_DATA = 0;
    private static final int ACTIVATION_DATA = 1;
    private static final int COST_SETTER = 2;
    private final SimpleContainerData automationData =
        new SimpleContainerData(3);

    public EnchantMenu(int id, Inventory inv, BlockPos pos) {
        super(id, inv, pos);
        this.tablePos = pos;
        // 메뉴 오픈 시점에 위치 기반으로 BE를 해석해 NPE를 사전에 차단한다.
        this.be = resolveTableBe(inv.player, pos);
        this.addDataSlots(this.automationData);
    }

    public EnchantMenu(int id, Inventory inv, ContainerLevelAccess wPos, EnchantmentTableItemHandler teInv, BlockPos pos) {
        super(id, inv, wPos, teInv, pos);
        this.tablePos = pos;
        // 자동화/가짜 플레이어 경로도 동일한 초기화 규칙을 사용한다.
        this.be = resolveTableBe(inv.player, pos);
        this.addDataSlots(this.automationData);
    }

    private static TableBlockEntity resolveTableBe(Player player, BlockPos pos) {
        if (player == null || player.level() == null) return null;

        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        if (blockEntity instanceof TableBlockEntity tableBlockEntity) {
            return tableBlockEntity;
        }
        return null;
    }
//메뉴타입 반환
    @Override
    public MenuType<?> getType() {
        return AUTO_ENCHANT_MENU.get();
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < 0 || id >= 11) {
            Util.logAndPauseIfInIde(player.getName() + " pressed invalid button id: " + id);
            return false;
        }

        //* 버튼 id 3~5 자동부여 레벨 선택
        //       id 6~9 xp 삽입 추출
        //       id 10 자동화 기능 토글   */
        if (id >= 0 && id <= 2) return super.clickMenuButton(player, id);

        if (this.be == null) {
            Util.logAndPauseIfInIde("TableBlockEntity is null at " + this.tablePos + " for player " + player.getName().getString());
            return false;
        }

        switch (id) {
            case 3, 4, 5:
                this.be.costSetter(id);
                break;
            case 6:
                this.be.injectAllLv(player);
                break;

            case 7:
                this.be.inject10Lv(player);
                break;
            case 8:
                this.be.eject10Lv(player);
                break;
            case 9:
                this.be.ejectAllLv(player);
                break;

            case 10:
                this.be.toggleAutoEnabled();
                break;

            default:
                this.be.costSetter(-1);
                return false;

        }
        return true;
    }
    //현재 블럭이 소지중인 레벨 표시
    public int getLevel(){
        if(this.be == null) return 0;

        return XpTransfer.getLevelForExperience(be.xpTank);
    }

    public boolean getOn() {
        if(this.be == null) return false;

        return be.setAutoEnabled;
    }

    //[0] 레벨 [1] 활성화 여부
    @Override
    public void broadcastChanges() {
        if (this.be != null
                && this.be.tableLevel() instanceof ServerLevel) {
            this.automationData.set(
                XP_LEVEL_DATA,
                XpTransfer.getLevelForExperience(this.be.xpTank)
            );
            this.automationData.set(
                ACTIVATION_DATA,
                be.setAutoEnabled ? 1 : 0
            );
            this.automationData.set(
                COST_SETTER,
                be.toggleCost
            );
        }
    
        super.broadcastChanges();
    }

    //containerData로 스크린과 서버 동기화 시도
    public int getDisplayedXpLevel() {
        return this.automationData.get(XP_LEVEL_DATA);
    }

    public boolean getDisplayedActivation() {
        return this.automationData.get(ACTIVATION_DATA) != 0;
    }

    public int getCostSetter() {
        return this.automationData.get(COST_SETTER);
    }

}
