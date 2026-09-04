package dev.rdf453.ApothicAutoEnchant.table;
import java.util.Optional;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dev.rdf453.ApothicAutoEnchant.util.FindBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;



public class TableBlockEntity extends EnchantingTableBlockEntity {
    
    
    static final GameProfile gp = new GameProfile(UUID.fromString("eab7b8eb-83a5-eb85-b8ec-9888ec9e8400"), "춘식이");
    boolean setAutoEnabled = false;
    int toggleCost = -1;
    long xpTank = 0;
    Optional<BlockPos> libraryPos = Optional.empty();
    Optional<BlockPos> chestPos = Optional.empty();

    
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
    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Component loadedName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);

        if (loadedName != null) {
            // 부모 클래스에 이름 데이터 세팅
            this.setCustomName(loadedName);
        }
        this.setAutoEnabled = input.getBooleanOr("SetAutoEnabled", false);
        this.toggleCost = input.getIntOr("ToggleCost",0);
        this.xpTank = input.getLongOr("XpTank", 0L);
        this.libraryPos = input.read("LibraryPos", BlockPos.CODEC);
    }


//아
    public void costSetter(int id) {
        AutomationUtils.costSetter(this, id);
    }
//몰
    public void toggleAutoEnabled() {
        AutomationUtils.toggleAutoEnabled(this);
    }
//라
    public void inject10Lv(net.minecraft.world.entity.player.Player player) {
        AutomationUtils.inject10Lv(this, player);
    }
//써
    public void injectAllLv(net.minecraft.world.entity.player.Player player) {
        AutomationUtils.injectAllLv(this, player);
    }
//글
    public void eject10Lv(net.minecraft.world.entity.player.Player player) {
        AutomationUtils.eject10Lv(this, player);
    }
//것
    public void ejectAllLv(net.minecraft.world.entity.player.Player player) {
        AutomationUtils.ejectAllLv(this, player);
    }
//들
    public Level tableLevel() {
        return this.level;
    }

    //인첸트 실시
    private void doEnchant() {
        if(!this.setAutoEnabled ||this.toggleCost == -1 ) return;
        if(this.libraryPos.isEmpty()&&this.level != null) this.libraryPos = FindBlock.findLibraryPos(this.getBlockPos(),this.level);
        if(this.chestPos.isEmpty()&&this.level != null) this.chestPos = FindBlock.findChestPos(this.getBlockPos(), this.level);

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

            EnchantmentItemHandler handler = this.getData(EnchantmentItemHandler.TYPE);

            //임시 메뉴 생성
            EnchantMenu Em = new EnchantMenu(
                0,
                fp.getInventory(),
                ContainerLevelAccess.create(serverLevel,this.getBlockPos()),
                handler,
                this.getBlockPos());
            
            //청금석이 없을때
            if(Em.getSlot(1).getItem().getCount()<3) 
                if (!AutomationUtils.bringFuel(this, Em)) {
                    this.setAutoEnabled = false;
                    this.setChanged();
                    return;
                }
            //책이 없을때
            if(!Em.getSlot(0).hasItem()) 
                if (!AutomationUtils.bringBook(this,Em)) {
                    this.setAutoEnabled = false;
                    this.setChanged();
                    return;
                }
            fp.giveExperiencePoints((int) this.xpTank);
            //인첸트 진행
            boolean success = Em.clickMenuButton(fp, toggleCost);

            if(success){
                //춘식이 xp 반환
                this.xpTank = fp.totalExperience;

                
                AutomationUtils.doTransfer(this, Em);
                this.setChanged();
            }
            else {
                this.xpTank = fp.totalExperience;
                this.setChanged();
            };
        }
    }
    //틱이벤트 수행
    public static void serverTick(Level level, BlockPos pos, BlockState state, EnchantingTableBlockEntity blockEntity) {
        if (blockEntity != null) {
            if (blockEntity instanceof TableBlockEntity tableBlockEntity) {
                tableBlockEntity.doEnchant();
            }
        }
    }
    //레지스터 생성
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "apothic_auto_enchanting");
    //블럭엔티티 홀더에 추가
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TableBlockEntity>> BLOCK_ENTITY_TYPE_HOLDER =
        BLOCK_ENTITIES.register("table_block_entity", () ->
            new BlockEntityType<>(
                TableBlockEntity::new,
                    AutoEnchantingTableBlock.BLOCK_HOLDER.get()
                )
        );    
}
