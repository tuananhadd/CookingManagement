package com.example.cookingmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CookingApp.db";
    private static final String TABLE_USERS = "users";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "USERNAME";
    private static final String COL_3 = "PASSWORD";

    private static final String TABLE_SHOPPING_LIST = "shopping_list";
    private static final String COL_SHOPPING_ID = "SHOPPING_ID";
    private static final String COL_DISH_NAME = "DISH_NAME";
    private static final String COL_INGREDIENTS = "INGREDIENTS";

    private static final String TABLE_RECIPES = "recipes";
    private static final String COL_RECIPE_ID = "RECIPE_ID";
    private static final String COL_RECIPE_NAME = "RECIPE_NAME";
    private static final String COL_COURSE = "COURSE";
    private static final String COL_WEIGHT = "WEIGHT";
    private static final String COL_COOKING_TIME = "COOKING_TIME";
    private static final String COL_INSTRUCTION_LINK = "INSTRUCTION_LINK";

    private static final String TABLE_COOKED_RECIPES = "cooked_recipes";
    private static final String COL_COOKED_RECIPE_ID = "COOKED_RECIPE_ID";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, PASSWORD TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_SHOPPING_LIST + " (SHOPPING_ID INTEGER PRIMARY KEY AUTOINCREMENT, DISH_NAME TEXT, INGREDIENTS TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (RECIPE_ID INTEGER PRIMARY KEY AUTOINCREMENT, RECIPE_NAME TEXT, COURSE TEXT, INGREDIENTS TEXT, WEIGHT TEXT, COOKING_TIME TEXT, INSTRUCTION_LINK TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_COOKED_RECIPES + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, COOKED_RECIPE_ID INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COOKED_RECIPES);
        onCreate(db);
    }

    // Insert user data
    public long addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, username);
        contentValues.put(COL_3, password);
        return db.insert(TABLE_USERS, null, contentValues);
    }

    // Check if username and password match
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {COL_1};
        String selection = COL_2 + " = ?" + " AND " + COL_3 + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_1};
        String selection = COL_2 + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    // Add dish to shopping list
    public long addDishToShoppingList(String dishName, String ingredients) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DISH_NAME, dishName);
        contentValues.put(COL_INGREDIENTS, ingredients);
        return db.insert(TABLE_SHOPPING_LIST, null, contentValues);
    }

    // Remove dish from shopping list
    public void removeDishFromShoppingList(String dishName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("DatabaseHelper", "Removing dish: " + dishName);

        int deletedRows = db.delete(TABLE_SHOPPING_LIST, COL_DISH_NAME + "=?", new String[]{dishName});
        Log.d("DatabaseHelper", "Deleted rows: " + deletedRows);

        db.close();
    }


    // Update dish in shopping list
    public void updateDishInShoppingList(String oldDishName, String updatedDishName, String updatedIngredients) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DISH_NAME, updatedDishName);
        values.put(COL_INGREDIENTS, updatedIngredients);

        db.update(TABLE_SHOPPING_LIST, values, COL_DISH_NAME + "=?", new String[]{oldDishName});
        db.close();
    }


    // Get shopping list items
    public List<String> getShoppingList() {
        List<String> shoppingList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SHOPPING_LIST;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int dishNameIndex = cursor.getColumnIndex(COL_DISH_NAME);
            int ingredientsIndex = cursor.getColumnIndex(COL_INGREDIENTS);

            // Check if the column indices are valid
            if (dishNameIndex != -1 && ingredientsIndex != -1) {
                do {
                    String dishName = cursor.getString(dishNameIndex);
                    String ingredients = cursor.getString(ingredientsIndex);
                    shoppingList.add(dishName + ": " + ingredients);
                } while (cursor.moveToNext());
            } else {
                throw new IllegalStateException("Invalid column index for shopping list");
            }
        }

        cursor.close();
        return shoppingList;
    }

    // Add recipe to the database with new details
    public long addRecipe(String recipeName, String course, String ingredients, String weight, String cookingTime, String instructionLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_RECIPE_NAME, recipeName);
        contentValues.put(COL_COURSE, course);
        contentValues.put(COL_INGREDIENTS, ingredients);
        contentValues.put(COL_WEIGHT, weight);
        contentValues.put(COL_COOKING_TIME, cookingTime);
        contentValues.put(COL_INSTRUCTION_LINK, instructionLink);
        return db.insert(TABLE_RECIPES, null, contentValues);
    }

    // Get all recipes from the database
    public List<Recipe> getRecipes() {
        List<Recipe> recipesList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_RECIPE_ID);
            int recipeNameIndex = cursor.getColumnIndex(COL_RECIPE_NAME);
            int courseIndex = cursor.getColumnIndex(COL_COURSE);
            int ingredientsIndex = cursor.getColumnIndex(COL_INGREDIENTS);
            int weightIndex = cursor.getColumnIndex(COL_WEIGHT);
            int cookingTimeIndex = cursor.getColumnIndex(COL_COOKING_TIME);
            int instructionLinkIndex = cursor.getColumnIndex(COL_INSTRUCTION_LINK);

            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getLong(idIndex));
                recipe.setRecipeName(cursor.getString(recipeNameIndex));
                recipe.setCourse(cursor.getString(courseIndex));
                recipe.setIngredients(cursor.getString(ingredientsIndex));
                recipe.setWeight(cursor.getString(weightIndex));
                recipe.setCookingTime(cursor.getString(cookingTimeIndex));
                recipe.setInstructionLink(cursor.getString(instructionLinkIndex));

                recipesList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return recipesList;
    }

    public void deleteRecipe(long recipeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPES, COL_RECIPE_ID + "=?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public void updateRecipe(Recipe updatedRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RECIPE_NAME, updatedRecipe.getRecipeName());
        values.put(COL_COURSE, updatedRecipe.getCourse());
        values.put(COL_INGREDIENTS, updatedRecipe.getIngredients());
        values.put(COL_WEIGHT, updatedRecipe.getWeight());
        values.put(COL_COOKING_TIME, updatedRecipe.getCookingTime());
        values.put(COL_INSTRUCTION_LINK, updatedRecipe.getInstructionLink());

        db.update(
                TABLE_RECIPES,
                values,
                COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(updatedRecipe.getId())}
        );

        db.close();
    }

    public Recipe getRecipeById(long recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_RECIPES + " WHERE " + COL_RECIPE_ID + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(recipeId)});

        Recipe recipe = null;

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_RECIPE_ID);
            int recipeNameIndex = cursor.getColumnIndex(COL_RECIPE_NAME);
            int courseIndex = cursor.getColumnIndex(COL_COURSE);
            int ingredientsIndex = cursor.getColumnIndex(COL_INGREDIENTS);
            int weightIndex = cursor.getColumnIndex(COL_WEIGHT);
            int cookingTimeIndex = cursor.getColumnIndex(COL_COOKING_TIME);
            int instructionLinkIndex = cursor.getColumnIndex(COL_INSTRUCTION_LINK);

            recipe = new Recipe();
            recipe.setId(cursor.getLong(idIndex));
            recipe.setRecipeName(cursor.getString(recipeNameIndex));
            recipe.setCourse(cursor.getString(courseIndex));
            recipe.setIngredients(cursor.getString(ingredientsIndex));
            recipe.setWeight(cursor.getString(weightIndex));
            recipe.setCookingTime(cursor.getString(cookingTimeIndex));
            recipe.setInstructionLink(cursor.getString(instructionLinkIndex));
        }

        cursor.close();
        return recipe;
    }

}
