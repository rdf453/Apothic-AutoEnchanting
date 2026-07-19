package dev.rdf453.ApothicAutoEnchant.table;
//블럭 엔티티 설정
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
public class TableBlockEntity extends EnchantingTableBlockEntity {
    //바닐라 인첸트 테이블 블럭엔티티 불러오기
    public TableBlockEntity(BlockPos Pos, BlockState State) {
        super(Pos,State);
    }
    //데이터를 NBT로 저장
    @Override
    protected void saveAdditional(ValueOutput output){
        super.saveAdditional(output);
        //커스텀 이름 설정
        if (this.hasCustomName()) {
            output.storeNullable("CustomName", ComponentSerialization.CODEC, this.getCustomName());
        }
    }
    //NBT데이터를 불러오기
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        Component loadedName = input.read("CustomName", ComponentSerialization.CODEC).orElse(null);

        if (loadedName != null) {
            // 부모 클래스에 이름 데이터 세팅
            this.setCustomName(loadedName);
        }
    }
}