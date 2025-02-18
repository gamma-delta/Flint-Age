package ca.xuul.flint.init;

import ca.xuul.flint.Flint;
import ca.xuul.flint.block.entity.FoundryBlockEntity;
import ca.xuul.flint.block.entity.KilnBlockEntity;
import ca.xuul.flint.block.entity.ModCampfireBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
        ForgeRegistries.BLOCK_ENTITY_TYPES, Flint.MOD_ID);



    public static final RegistryObject<BlockEntityType<FoundryBlockEntity>> FOUNDRY = BLOCK_ENTITIES.register("foundry",
        () -> BlockEntityType.Builder.of(FoundryBlockEntity::new, ModBlocks.FOUNDRY.get()).build(null));


    public static final RegistryObject<BlockEntityType<ModCampfireBlockEntity>> CAMPFIRE = BLOCK_ENTITIES.register("campfire",
            () -> BlockEntityType.Builder.of(ModCampfireBlockEntity::new, ModBlocks.CAMPFIRE.get()).build(null));

    public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN = BLOCK_ENTITIES.register("kiln",
            () -> BlockEntityType.Builder.of(KilnBlockEntity::new, ModBlocks.KILN.get()).build(null));


}
