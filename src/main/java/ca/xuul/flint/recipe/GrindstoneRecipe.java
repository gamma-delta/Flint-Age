package ca.xuul.flint.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import ca.xuul.flint.Flint;
import ca.xuul.flint.init.ModBlocks;


public class GrindstoneRecipe implements Recipe<Container> {

    private final String group;
    private final ResourceLocation id;
    @Nullable
    private final Ingredient ingredient;
    private final ItemStack result;

    public static RecipeSerializer<?> SERIALIZER = new Serializer();



    public GrindstoneRecipe(ResourceLocation id, String group, @Nullable Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.group = group;
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return getResultItem().copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> allIngredients = NonNullList.create();
        allIngredients.add(ingredient != null ? ingredient : Ingredient.EMPTY);
        return allIngredients;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }


    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.GRINDSTONE.get());
    }

    public Ingredient getPattern() {
        return ingredient != null ? ingredient : Ingredient.EMPTY;
    }

    @Override
    public RecipeType<?> getType() {
        return FoundryRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<GrindstoneRecipe> {
        private Type() { }
        public static final GrindstoneRecipe.Type INSTANCE = new GrindstoneRecipe.Type();
        public static final String ID = "grindstone";
    }


    public static class Serializer implements RecipeSerializer<GrindstoneRecipe> {

        public static final ToolUseRecipe.Serializer INSTANCE = new ToolUseRecipe.Serializer();
        public static final ResourceLocation ID =new ResourceLocation(Flint.MOD_ID, "grindstone");


        protected GrindstoneRecipe createRecipe(ResourceLocation recipeId, String group, Ingredient ingredient, ItemStack result) {
            return new GrindstoneRecipe(recipeId, group, ingredient, result);
        }


        @Override
        public GrindstoneRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient;
            if (GsonHelper.isArrayNode(json, "ingredient")) {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredient"));
            } else {
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "ingredient"));
            }

            String s1 = GsonHelper.getAsString(json, "result");
            int i = GsonHelper.getAsInt(json, "count");
            ItemStack result = new ItemStack(Registry.ITEM.get(new ResourceLocation(s1)), i);
            System.out.println(result);
            return createRecipe(recipeId, group, ingredient, result);


        }

        @Nullable
        @Override
        public GrindstoneRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(32767);
            boolean hasInput = buffer.readBoolean();
            Ingredient input = hasInput ? Ingredient.fromNetwork(buffer) : null;
            ItemStack result = buffer.readItem();
            return createRecipe(recipeId, group, input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GrindstoneRecipe recipe) {

            buffer.writeUtf(recipe.group);
            boolean hasInput = recipe.ingredient != null;
            buffer.writeBoolean(hasInput);
            if (hasInput) recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}


