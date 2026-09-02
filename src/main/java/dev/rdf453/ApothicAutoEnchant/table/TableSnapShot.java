package dev.rdf453.ApothicAutoEnchant.table;


import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.transfer.item.ItemResource;

public record TableSnapShot(Object2IntOpenHashMap<Holder<ItemResource>> IO, Object2IntOpenHashMap<Holder<ItemResource>> Fuel) {}
