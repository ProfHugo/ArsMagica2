package am2.api;

import java.util.Map;

import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.api.items.armor.ArmorImbuement;
import am2.skill.Skill;
import am2.spell.AbstractSpellPart;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

public class ArsMagicaAPI {
	
	private static final FMLControlledNamespacedRegistry<Affinity> AFFINITY_REGISTRY;
	private static final FMLControlledNamespacedRegistry<AbstractAffinityAbility> ABILITY_REGISTRY;
	private static final FMLControlledNamespacedRegistry<ArmorImbuement> IMBUEMENTS_REGISTRY;
	private static final FMLControlledNamespacedRegistry<AbstractSpellPart> SPELL_REGISTRY;
	private static final FMLControlledNamespacedRegistry<Skill> SKILL_REGISTRY;

	static {
		ABILITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinityabilities"), AbstractAffinityAbility.class, null, 0, Short.MAX_VALUE, false, ObjectCallbacks.ABILITY, ObjectCallbacks.ABILITY, ObjectCallbacks.ABILITY);
		AFFINITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinities"), Affinity.class, new ResourceLocation("arsmagica2", "none"), 0, Short.MAX_VALUE, false, ObjectCallbacks.AFFINITY, ObjectCallbacks.AFFINITY, ObjectCallbacks.AFFINITY);
		IMBUEMENTS_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "armorimbuments"), ArmorImbuement.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.IMBUEMENT, ObjectCallbacks.IMBUEMENT, ObjectCallbacks.IMBUEMENT);
		SPELL_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "spells"), AbstractSpellPart.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.SPELL, ObjectCallbacks.SPELL, ObjectCallbacks.SPELL);
		SKILL_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "skills"), Skill.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.SKILL, ObjectCallbacks.SKILL, ObjectCallbacks.SKILL);
	}
	
	public static FMLControlledNamespacedRegistry<Affinity> getAffinityRegistry() {return AFFINITY_REGISTRY;};
	public static FMLControlledNamespacedRegistry<AbstractAffinityAbility> getAffinityAbilityRegistry() {return ABILITY_REGISTRY;}
	public static FMLControlledNamespacedRegistry<ArmorImbuement> getArmorImbuementRegistry() {return IMBUEMENTS_REGISTRY;}
	public static FMLControlledNamespacedRegistry<AbstractSpellPart> getSpellRegistry() {return SPELL_REGISTRY;}
	public static FMLControlledNamespacedRegistry<Skill> getSkillRegistry() {return SKILL_REGISTRY;}
	
	public static String getCurrentModId () {
		ModContainer current = Loader.instance().activeModContainer();
		String modid = "arsmagica2";
		if (current != null)
			modid = current.getModId();
		return modid;
	}
	
    private static class ObjectCallbacks<T> implements IForgeRegistry.AddCallback<T>,IForgeRegistry.ClearCallback<T>,IForgeRegistry.CreateCallback<T>
	{
		static final ObjectCallbacks<AbstractSpellPart> SPELL = new SpellCallbacks();
		static final ObjectCallbacks<AbstractAffinityAbility> ABILITY = new ObjectCallbacks<>();
		static final ObjectCallbacks<Affinity> AFFINITY = new ObjectCallbacks<>();
		static final ObjectCallbacks<ArmorImbuement> IMBUEMENT = new ObjectCallbacks<>();
		static final ObjectCallbacks<Skill> SKILL = new ObjectCallbacks<>();

		@Override
		public void onAdd(T ability, int id, Map<ResourceLocation, ?> slaves) {}

		@Override
		public void onClear(Map<ResourceLocation, ?> slaveset) {}

		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {}
	}
    
    private static class SpellCallbacks extends ObjectCallbacks<AbstractSpellPart> {

		@Override
		public void onAdd(AbstractSpellPart ability, int id, Map<ResourceLocation, ?> slaves) {
		}
		
	}
}
