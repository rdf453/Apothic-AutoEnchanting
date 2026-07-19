package dev.rdf453.ApothicAutoEnchant.MIxin;

import dev.rdf453.ApothicAutoEnchant.util.LibraryTransfer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import dev.shadowsoffire.apothic_enchanting.library.EnchLibraryTile;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Mixin(EnchLibraryTile.class)
public class LibraryTileMixin extends BlockEntity implements LibraryTransfer {
    
    @Unique
    private final ItemStack ItemStackResourceHandler = new ItemStackResourceHandler(Items.ENCHANTED_BOOK,432);
    @Unique
    public boolean ApothicEnch$insertArray(ItemStack Stack) {
        return Stack.is(Items.ENCHANTED_BOOK)&& <=ItemStack.max
    }
}
