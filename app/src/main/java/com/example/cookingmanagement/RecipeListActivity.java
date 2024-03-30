package com.example.cookingmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private RecipeListAdapter recipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        databaseHelper = new DatabaseHelper(this);

        ListView recipeListView = findViewById(R.id.listViewRecipesList);
        populateRecipesList(recipeListView);

        Button returnToRecipeButton = findViewById(R.id.btnReturnToRecipe);
        returnToRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click to return to RecipeActivity
                finish(); // Close the RecipeListActivity and go back to the previous activity (RecipeActivity)
            }
        });
    }

    private void populateRecipesList(ListView recipesListView) {
        // Retrieve recipes from the database
        List<Recipe> recipesList = databaseHelper.getRecipes();

        // Initialize the custom adapter
        recipeListAdapter = new RecipeListAdapter(this, recipesList);

        // Update the adapter
        recipesListView.setAdapter(recipeListAdapter);
    }
}
