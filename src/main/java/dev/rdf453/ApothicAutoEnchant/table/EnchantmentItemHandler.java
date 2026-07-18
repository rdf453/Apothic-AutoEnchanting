package dev.rdf453.ApothicAutoEnchant.table;



import dev.shadowsoffire.apothic_enchanting.table.EnchantmentTableItemHandler;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;

public class EnchantmentItemHandler extends ItemStackResourceHandler {
    
    public static final AttachmentType<EnchantmentItemHandler> TYPE = AttachmentType.serializable(EnchantmentItemHandler::new).build();

    public EnchantmentItemHandler() {
        super(2);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        if(index == 0)
        return   resource.is(Tags.Items.ENCHANTING_FUELS);

        else if(index == 1)
        return resource.is(net.minecraft.world.item.Items.Book) || resource.is(net.minecraft.world.item.Items.ENCHANTED_BOOK);

        else return false;
    }
    public int getSlotLimit (int index) 
}
