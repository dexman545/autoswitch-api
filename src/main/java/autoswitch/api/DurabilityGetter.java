package autoswitch.api;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface DurabilityGetter {
    Number getDurability(ItemStack stack);
}
