package ca.xuul.flint.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.alchemy.Potion;

public class ModFoods {


    public static final FoodProperties SALVE = new FoodProperties.Builder().nutrition(0).saturationMod(0).alwaysEat().fast()
            .effect(()-> new MobEffectInstance(MobEffects.REGENERATION, 40, 1,false,false, false), 1).build();

}
