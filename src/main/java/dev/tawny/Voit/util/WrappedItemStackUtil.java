package dev.tawny.Voit.util;

import dev.tawny.Voit.util.type.WrappedItemStack;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class WrappedItemStackUtil {

    public static WrappedItemStack getWrappedItemStack(ItemStack obiItemStack) {
        return new WrappedItemStack(obiItemStack);
    }

    public abstract float getDestroySpeed(Block obbBlock);

    public abstract boolean canDestroySpecialBlock(Block obbBlock);
}
