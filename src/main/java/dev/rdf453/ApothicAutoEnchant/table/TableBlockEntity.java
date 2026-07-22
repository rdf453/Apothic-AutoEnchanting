package dev.rdf453.ApothicAutoEnchant.table;
import java.util.Optional;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dev.rdf453.ApothicAutoEnchant.util.FindLibrary;
import dev.rdf453.ApothicAutoEnchant.util.LibraryTransfer;
import dev.rdf453.ApothicAutoEnchant.util.XpTransfer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;


/*
 * 설계 메모 (2026-07-22 기준)
 * - 현재 상태:
 *   1) 자동화 상태, 비용 선택, XP 탱크, 도서관 좌표 캐시 저장/복원 골격이 구현되어 있다.
 *   2) doEnchant 본체와 버퍼 전송 분리가 되어 있어 FakePlayer 경유 자동 인챈트 흐름을 잡아둘 수 있다.
 * - 다음 작업:
 *   1) static serverTick 연결과 블록 getTicker 연결을 마무리한다.
 *   2) 슬롯 1 결과물 전송과 비우기 경로를 유지하면서 실패 시 재시도 정책을 확정한다.
 *   3) 화면 표시 동기화용 상태값을 Screen 쪽으로 노출한다.
 * - 리스크/주의:
 *   1) XpTank 저장 타입과 loadAdditional 읽기 타입의 일관성이 필요하다.
 *   2) libraryPos는 Optional.empty() 초기화를 유지해야 탐색 재시도가 가능하다.
 */
public class TableBlockEntity extends EnchantingTableBlockEntity {
    public boolean setAutoEnabled = false; 
    private int toggleCost=3;
    private int xpTank=0;
    private Optional<BlockPos> libraryPos = Optional.empty();
    
    static final GameProfile gp = new GameProfile(UUID.fromString("eab7b8eb-83a5-eb85-b8ec-9888ec9e8400"), "춘식이");
    
    //바닐라 인첸트 테이블 블럭엔티티 불러오기
    public TableBlockEntity(BlockPos Pos, BlockState State) {
        super(Pos, State);
    }
    //데이터를 NBT로 저장
    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        //커스텀 이름 설정
        if (this.hasCustomName()) {
            output.storeNullable("CustomName", ComponentSerialization.CODEC, this.getCustomName());
        }
        output.putInt("ToggleCost", this.toggleCost);
        output.putBoolean("SetAutoEnabled", this.setAutoEnabled);
        output.putLong("XpTank", this.xpTank);
        output.storeNullable("LibraryPos", BlockPos.CODEC, this.libraryPos.orElse(null));

        
    }
    //NBT데이터를 불러오기
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Component loadedName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);

        if (loadedName != null) {
            // 부모 클래스에 이름 데이터 세팅
            this.setCustomName(loadedName);
        }
        this.setAutoEnabled = input.getBooleanOr("SetAutoEnabled", false);
        this.toggleCost = input.getIntOr("ToggleCost",0);
        this.xpTank = input.getIntOr("XpTank", 0);
        this.libraryPos = input.read("LibraryPos", BlockPos.CODEC);
    }

    //id 매핑
    public void costSetter(int id) {
        this.toggleCost=id-3;
        setChanged();
    }
    //자동화 토글
    public void toggleAutoEnabled() {
        this.setAutoEnabled = !this.setAutoEnabled;
        setChanged();
        }
    //10레벨 주입
    public void inject10Lv(Player player) {

        this.xpTank += -XpTransfer.getXpNeedPoint(player, Math.max(0,player.experienceLevel-10));

        player.giveExperiencePoints((int)XpTransfer.getXpNeedPoint(player, Math.max(0,player.experienceLevel-10)));
        setChanged();
    }    
    //모든 레벨 주입
    public void injectAllLv(Player player) {
        if(player.experienceLevel <= 0) return;
        this.xpTank -= XpTransfer.getXpNeedPoint(player, 0);

        player.giveExperiencePoints((int)XpTransfer.getXpNeedPoint(player, 0));
        setChanged();
    }
    //10레벨 회수
    public void eject10Lv(Player player) {
        if(this.xpTank < 0) return;
        int Tlqkf = (int)XpTransfer.getXpNeedPoint(player, player.experienceLevel+10);

        if(this.xpTank < Tlqkf) return;
        this.xpTank -= Tlqkf;
        player.giveExperiencePoints(Tlqkf);
        
        setChanged();
    }
    //레벨 전부 회수
    public void ejectAllLv(Player player) {
        player.giveExperiencePoints((int)this.xpTank);
        this.xpTank = 0;
        setChanged();
        
    }

    private ItemStack copyResult(EnchantMenu em){
        return em.getSlot(1).getItem().copy();
    }
    private void ClearSlot(EnchantMenu em) {
        em.getSlot(1).set(ItemStack.EMPTY);
    }

    private void doTransfer(EnchantMenu em)    {
        if(!this.libraryPos.isEmpty()) {
        BlockEntity LE = this.level.getBlockEntity(libraryPos.get());
        if(LE instanceof LibraryTransfer transfer) {            
        ItemStack copy = copyResult(em);            
        if(transfer.AutoEnch_insertList(copy)) ClearSlot(em);}
        }
    }
    private void doEnchant() {
        if(!this.setAutoEnabled) return;
        if(this.libraryPos.isEmpty()&&this.level != null) this.libraryPos = FindLibrary.findLibraryPos(this.getBlockPos(),this.level);

        //서버레벨로 캐스팅
        if(this.level instanceof ServerLevel serverLevel){
            //춘식이 소환
            FakePlayer fp = FakePlayerFactory.get(serverLevel,gp);
            //춘식이 고정
            fp.setPosRaw(
                this.worldPosition.getX(),
                this.worldPosition.getY(),
                this.worldPosition.getZ()
            );
            //임시 메뉴 생성
            EnchantMenu Em = new EnchantMenu(0,fp.getInventory() , this.getBlockPos());
            fp.giveExperienceLevels(this.xpTank); 

            doTransfer(Em);

            //인첸트 진행
            boolean success = Em.clickMenuButton(fp, toggleCost);

            if(success){
                //춘식이 xp 반환
                this.xpTank = fp.totalExperience;

                
                doTransfer(Em);
                this.setChanged();
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EnchantingTableBlockEntity blockEntity) {
        if (blockEntity != null) {
            if (blockEntity instanceof TableBlockEntity tableBlockEntity) {
                tableBlockEntity.doEnchant();
            }
        }
    }
}