package dev.rdf453.ApothicAutoEnchant.table;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.Arrays;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;


public class EnchantmentItemHandler extends SnapshotJournal<TableSnapShot> implements ResourceHandler<ItemResource> {

    public static final AttachmentType<EnchantmentItemHandler> TYPE = AttachmentType.serializable(EnchantmentItemHandler::new).build();

    private static final int IO_SLOT = 0;
    private static final int FUEL_SLOT = 1;
    
    private ItemStack ioStack= ItemStack.EMPTY;
    private ItemStack FuelStack = ItemStack.EMPTY;
    // 슬롯이 몇 개인가
    @Override
    public final int size() {
        return 2;
    }
    // 슬롯에 무엇이 있는가
    @Override
    public ItemResource getResource(int index) {
        
        if(index == IO_SLOT) return ioStack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(ioStack);

        else return FuelStack.isEmpty() ? ItemResource.EMPTY : ItemResource.of(FuelStack);
    }
    // 슬롯에 몇 개 있는가
    @Override
    public long getAmountAsLong(int index) {
        if(index == IO_SLOT) return ioStack.getCount();

        else return FuelStack.getCount();
    }
    // 무엇을 최대 몇 개까지 담는가
    @Override
    public long getCapacityAsLong(int index, ItemResource resource){
        if(index == IO_SLOT) return resource.isEmpty() || (resource.is(Items.BOOK)||resource.is(Items.ENCHANTED_BOOK)) ? 1 : 0;

        else if(index == FUEL_SLOT) return resource.isEmpty() || resource.is(Items.LAPIS_LAZULI) ? 1 : 0;
    }
    // 무엇을 받아들이는가
    @Override
    public boolean isValid(int index, ItemResource resource) {
        if(index == FUEL_SLOT) return resource.is(Items.LAPIS_LAZULI);
        else if (index == IO_SLOT) return resource.is(Items.BOOK) || resource.is(Items.ENCHANTED_BOOK);
    }

    //씨
    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {

    }
    //발
    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        
    }

    @Override
    protected TableSnapShot createSnapshot() {

    }

    @Override
    protected void revertToSnapshot(TableSnapShot snapshot) {
        
    }

    @Override
    protected void onRootCommit(TableSnapShot originalState) {

    }
}

