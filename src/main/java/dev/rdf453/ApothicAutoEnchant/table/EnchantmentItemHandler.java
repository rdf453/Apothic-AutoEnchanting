package dev.rdf453.ApothicAutoEnchant.table;

import net.minecraft.world.item.Items;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;



public class EnchantmentItemHandler extends ItemStacksResourceHandler {

    private static final int IO_SLOT = 0;
    private static final int FUEL_SLOT = 1;

    public static final AttachmentType<EnchantmentItemHandler> TYPE = AttachmentType
            .serializable(EnchantmentItemHandler::new).build();

    public EnchantmentItemHandler() {
        super(2);
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return switch (index) {
            case IO_SLOT -> resource.is(Items.BOOK)
                    || resource.is(Items.ENCHANTED_BOOK);
            case FUEL_SLOT -> resource.is(Items.LAPIS_LAZULI);
            default -> false;
        };

    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return switch (index) {
            case IO_SLOT -> 1;
            case FUEL_SLOT -> 64;
            default -> 0;
        };
    }

}
