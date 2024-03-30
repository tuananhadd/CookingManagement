package com.example.cookingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewRecipeActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        databaseHelper = new DatabaseHelper(this);

        // Get the recipe ID from the intent
        long recipeId = getIntent().getLongExtra("RECIPE_ID", -1);

        if (recipeId != -1) {
            // Retrieve the recipe details from the database
            Recipe recipe = databaseHelper.getRecipeById(recipeId);

            // Display the recipe details
            displayRecipeDetails(recipe);
        }
    }

    public void onClickReturnToRecipeList(View view) {
        startActivity(new Intent(this, RecipeListActivity.class));
    }

    private void displayRecipeDetails(Recipe recipe) {
        if (recipe != null) {
            TextView recipeNameTextView = findViewById(R.id.recipeNameTextView);
            TextView courseTextView = findViewById(R.id.courseTextView);
            TextView ingredientsTextView = findViewById(R.id.ingredientsTextView);
            TextView weightTextView = findViewById(R.id.weightTextView);
            TextView cookingTimeTextView = findViewById(R.id.cookingTimeTextView);
            TextView instructionLinkTextView = findViewById(R.id.instructionLinkTextView);

            recipeNameTextView.setText("Recipe Name: " + recipe.getRecipeName());
            courseTextView.setText("Course: " + recipe.getCourse());
            ingredientsTextView.setText("Ingredients: \n" + recipe.getIngredients());
            weightTextView.setText("Weight: " + recipe.getWeight());
            cookingTimeTextView.setText("Cooking Time: " + recipe.getCookingTime());

            // Check if the instruction link is provided
            if (recipe.getInstructionLink() != null && !recipe.getInstructionLink().isEmpty()) {
                instructionLinkTextView.setText("Instruction Link: " + recipe.getInstructionLink());
            } else {
                instructionLinkTextView.setText("Instruction Link: Not Provided");
            }
        }
    }
}
