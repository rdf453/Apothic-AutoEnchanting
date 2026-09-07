package dev.rdf453.ApothicAutoEnchant.table;

import dev.rdf453.ApothicAutoEnchant.table.EnchantmentItemHandler;
import dev.rdf453.ApothicAutoEnchant.util.XpTransfer;
import dev.shadowsoffire.apothic_enchanting.library.EnchLibraryTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

// 블럭 엔티티에 남겨둔 상태를 받아 실제 동작만 수행한다.
public final class AutomationUtils {
    private static final int IO_SLOT = 0;
    private static final int FUEL_SLOT = 1;

    private AutomationUtils() {
    }

    // id 매핑
    public static void costSetter(TableBlockEntity tbe, int id) {
        tbe.toggleCost = id - 3;
        tbe.setChanged();
    }

    // 자동화 토글
    public static void toggleAutoEnabled(TableBlockEntity tbe) {
        tbe.setAutoEnabled = !tbe.setAutoEnabled;
        tbe.setChanged();
    }

    // 10레벨 주입
    public static void inject10Lv(TableBlockEntity tbe, Player player) {
        int needed = XpTransfer.getXpNeedPoint(player, Math.max(0, player.experienceLevel - 10));

        tbe.xpTank -= needed;
        player.giveExperiencePoints(needed);
        tbe.setChanged();
    }

    // 모든 레벨 주입
    public static void injectAllLv(TableBlockEntity tbe, Player player) {
        if (player.experienceLevel <= 0)
            return;
        int needed = XpTransfer.getXpNeedPoint(player, 0);

        tbe.xpTank -= needed;
        player.giveExperiencePoints(needed);
        tbe.setChanged();
    }

    // 10레벨 회수
    public static void eject10Lv(TableBlockEntity tbe, Player player) {
        if (tbe.xpTank < 0)
            return;
        int needed = XpTransfer.getXpNeedPoint(player, player.experienceLevel + 10);

        if (tbe.xpTank < needed)
            return;
        tbe.xpTank -= needed;
        player.giveExperiencePoints(needed);

        tbe.setChanged();
    }

    // 레벨 전부 회수
    public static void ejectAllLv(TableBlockEntity tbe, Player player) {
        player.giveExperiencePoints((int) tbe.xpTank);
        tbe.xpTank = 0;
        tbe.setChanged();
    }

    // 도서관으로 결과물 배출
    public static void doTransfer(TableBlockEntity tbe, EnchantMenu em) {
        if (tbe.libraryPos.isEmpty() || tbe.tableLevel() == null)
            return;

        BlockEntity blockEntity = tbe.tableLevel().getBlockEntity(tbe.libraryPos.get());
        if (blockEntity instanceof EnchLibraryTile lib) {
            // 버퍼네 뭐네 하지말고 도서관 NBT로 직송
            if (em.getSlot(0).getItem().is(Items.ENCHANTED_BOOK)) {
                lib.depositBook(em.getSlot(IO_SLOT).getItem());
                em.getSlot(IO_SLOT).set(ItemStack.EMPTY);
            }
        }

    }

    // 인첸트 연료 가져오기
    public static boolean bringFuel(TableBlockEntity tbe, EnchantMenu em) {
        if (tbe.chestPos.isEmpty() || tbe.tableLevel() == null)
            return false;

        BlockPos pos = tbe.chestPos.get();
        Level level = tbe.tableLevel();

        ResourceHandler<ItemResource> chestHandler = level.getCapability(Capabilities.Item.BLOCK, pos, Direction.DOWN);
        EnchantmentItemHandler tableHandler = tbe.getData(EnchantmentItemHandler.TYPE);
        if (chestHandler == null)
            return false;
        //상자 슬롯 탐색
        for (int i = 0; i < chestHandler.size(); i++) {
            ItemResource fuel = chestHandler.getResource(i);
            if (!fuel.isEmpty()) {
                if (fuel.is(Items.LAPIS_LAZULI)) {

                    try(Transaction tx1 = Transaction.openRoot()) {
                        
                        int buffer;
                        try(Transaction tx2 = Transaction.open(tx1)) {
                            //추출 가능한 청금석 갯수
                            buffer = tableHandler.insert(FUEL_SLOT,fuel, 64, tx2);
                            if (buffer <= 0) return false;
                        }
                        int extracted = chestHandler.extract(i,fuel, buffer, tx1);
                        int inserted = tableHandler.insert(FUEL_SLOT,fuel, extracted, tx1);

                        if(extracted == inserted) {
                            tx1.commit();
                            tbe.setChanged();
                            return true;
                        }
                    }                    
                }
            }         
        }
        return false;
    }

    // 책가져오기
    public static boolean bringBook(TableBlockEntity tbe, EnchantMenu em) {
        if (tbe.chestPos.isEmpty() || tbe.tableLevel() == null)
            return false;

        BlockPos pos = tbe.chestPos.get();
        Level level = tbe.tableLevel();

        ResourceHandler<ItemResource> chestHandler = level.getCapability(Capabilities.Item.BLOCK, pos, Direction.DOWN);
        EnchantmentItemHandler tableHandler = tbe.getData(EnchantmentItemHandler.TYPE);
        if (chestHandler == null)
            return false;

        for (int i = 0; i < chestHandler.size(); i++) {
            ItemResource book = chestHandler.getResource(i);
            if (!book.isEmpty()) {
                if (book.is(Items.BOOK)) {

                    try (Transaction tx = Transaction.openRoot()) {
                        int extract = chestHandler.extract(i, book, 1, tx);
                        int insert = tableHandler.insert(IO_SLOT, book, extract, tx);

                        if (extract == insert) {
                            tx.commit();
                            tbe.setChanged();
                            return true;
                        }
                    }
                }
            }            
        }
        return false;
    }
}