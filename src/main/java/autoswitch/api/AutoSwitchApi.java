package autoswitch.api;

import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import org.apache.commons.lang3.tuple.Pair;

public interface AutoSwitchApi {

    /**
     * <p>Method for adding modded targets and tool groupings to AutoSwitch.
     * Targets are how AutoSwitch parses game data into config information for evaluation,
     * see {@link autoswitch.util.TargetableUtil#getTarget(Object)} for details on this process.</p><br></br>
     *
     * <p>Specific {@link net.minecraft.block.Block}s are checked first,
     * then the {@link net.minecraft.block.Material} is queried.
     * {@link net.minecraft.entity.Entity}s follow a similar process, checking {@link net.minecraft.entity.EntityType}
     * and then {@link net.minecraft.entity.EntityGroup}. This order is to facilitate users adding per block/entity
     * configs that differ from their group's. </p><br></br>
     *
     * <p>Keys should be lowercase.</p>
     *
     * @param targets Map of target name -> object, eg. "stone -> {@link net.minecraft.block.Material#STONE}"
     *                Values can be a Material, Block, EntityGroup, or EntityType.
     *                Do note that AutoSwitch accepts {@link net.minecraft.util.Identifier} as a key for overrides,
     *                so using that format for Materials or EntityGroups is not advised to avoid confusion and/or
     *                conflicts. Using the Identifier for blocks and/or entities is fine.
     *                Default targets can be read from here.
     * @param actionConfig Map of key -> tool selection, eg. "wool -> 'shears'". Use this for specifying
     *               default config values for your targets in the case of "attack" (eg. mining) actions.
     *               Targets do not need to be added to this map.
     *               Value can be empty string. Default target config entries can be read from here. Default config entries
     *               with no tool selections (eg. {@link autoswitch.config.AutoSwitchMaterialConfig#cake()}) will not be passed in this
     *               method, therefore you can add to them - but you will be the only one allowed to do so!
     * @param usableConfig Map of key -> tool selection, eg. "creeper -> 'flint & steel'". Use this for specifying
     *               default config values for your targets in the case of "use" (eg. right click) actions.
     *               Targets do not need to be added to this map.
     *               Value can be empty string. Default target config entries can be read from here. Default config entries
     *               with no tool selections (eg. {@link autoswitch.config.AutoSwitchMaterialConfig#cake()}) will not be passed in this
     *               method, therefore you can add to them - but you will be the only one allowed to do so!
     *
     *
     */
    void moddedTargets(AutoSwitchMap<String, Object> targets,
                               AutoSwitchMap<String, String> actionConfig,
                               AutoSwitchMap<String, String> usableConfig);


    /**
     * Tool groupings allow users to use simple keywords to specify a series of items that should match a config entry,
     * eg. "pickaxe" will match any pickaxe item. By default, groupings exist for all of Fabric's ToolTags.
     * Do note that Tags are synced from server -> client, and therefore if you wish for these groupings to work on
     * vanilla servers without your datapack you will need to provide a class or interface that they all inherit from.
     * See {@link autoswitch.config.ToolHandler#isCorrectTool(String, Item)} for details.
     *
     *
     * @param toolGroupings Map of key -> tool tag and/or class, eg. "pickaxe ->
     * {@link net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags#PICKAXES}". Class is nullable. YOU SHOULD PREFER
     *                      USING {@link net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags} OVER CUSTOM GROUPS
     *                      WHERE POSSIBLE!
     */
    void moddedToolGroups(AutoSwitchMap<String, Pair<Tag<Item>, Class<?>>> toolGroupings);


    /**
     * <p>Function for specifying custom damage systems for items, eg. powered tools a la Tech Reborn's drills.</p></b>
     * <p>This method exists to allow mod devs a way of making their tools with custom damage systems
     * be fully compatible with AutoSwitch without 'including' AutoSwitch-API within their mod, as would be the
     * case with an interface being implemented.</p><b></b>
     *
     * <p>Usage: AutoSwitch, when configured to (default enabled) see {@link autoswitch.config.AutoSwitchConfig#tryPreserveDamagedTools()},
     * will check if an item is about to break (less than 3 durability) and will remove it from further evaluation.
     * It naively assumes that is the number of uses remaining (ie. no check for unbreaking enchantment).</p>
     *
     * <p>This takes in a map of Classes/Interfaces -> lambda. When evaluating tools for damage, AutoSwitch
     * will check if an item is {@link instanceof} one of the classes, then pass the ItemStack to the lambda.
     * Do note it only checks this map if the item returns false for {@link Item#isDamageable()}. The passed lambda
     * should return some {@link Number} (int, float, etc).</p><b></b>
     *
     * <p>Example Usage: damageMap.put(Item.class, ItemStack::getDamage); This will check if the Item is instanceof Item
     * and then call getDamage on the ItemStack, in this case it would match for all passed items.</p><b></b>
     *
     * <p>Contrary to how Item is setup where the durability remaining is {@code getMaxDamage - getDamage}, the
     * passed method should directly return that evaluation, eg. the amount of durability remaining on the item.</p>
     *
     * @param damageMap A map of {@link Class} -> {@link DurabilityGetter}. Value can be a method reference.
     *                  When an item is found to be an instance or implementation of the provided Class,
     *                  AutoSwitch will pass the ItemStack to the lambda to get the amount of durability remaining.
     *                  This could be the (non-decimal form) percentage of energy remaining, or (preferably) the number of uses the
     *                  item has left before being depleted/broken.
     *
     */
    void customDamageSystems(AutoSwitchMap<Class<?>, DurabilityGetter> damageMap);

}
