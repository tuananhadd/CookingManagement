package com.example.cookingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeActivity extends AppCompatActivity {

    private EditText recipeNameEditText;
    private Spinner courseSpinner;
    private EditText ingredientsEditText;
    private EditText weightEditText;
    private EditText cookingTimeEditText;
    private CheckBox provideLinkCheckBox;
    private EditText instructionLinkEditText;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        databaseHelper = new DatabaseHelper(this);

        recipeNameEditText = findViewById(R.id.editTextRecipeName);
        courseSpinner = findViewById(R.id.spinnerCourse);
        ingredientsEditText = findViewById(R.id.editTextIngredients);
        weightEditText = findViewById(R.id.editTextWeight);
        cookingTimeEditText = findViewById(R.id.editTextCookingTime);
        provideLinkCheckBox = findViewById(R.id.checkBoxProvideLink);
        instructionLinkEditText = findViewById(R.id.editTextInstructionLink);

        // Set up listener for checkbox change
        provideLinkCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Show or hide instruction link field based on checkbox state
            instructionLinkEditText.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        setupCourseSpinner();
    }

    private void setupCourseSpinner() {
        // Define the course options
        String[] courseOptions = {"Select Course", "Hors d'oeuvre", "Soup", "Appetizer", "Salad", "Main Course", "Dessert", "Mignardise"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseOptions);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        courseSpinner.setAdapter(adapter);
    }

    public void onClickAddRecipe(View view) {
        String recipeName = recipeNameEditText.getText().toString().trim();
        String course = courseSpinner.getSelectedItem().toString().trim();
        String ingredients = ingredientsEditText.getText().toString().trim();
        String weight = weightEditText.getText().toString().trim();
        String cookingTime = cookingTimeEditText.getText().toString().trim();
        String instructionLink = instructionLinkEditText.getText().toString().trim();

        if (validateRecipeDetails(recipeName, course, ingredients, weight, cookingTime)) {
            if (!course.equals("Select Course")) {
                // Add recipe to the database
                addRecipeToDatabase(recipeName, course, ingredients, weight, cookingTime, instructionLink);
            } else {
                Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClickViewRecipes(View view) {
        // Handle the button click to navigate to RecipeListActivity
        startActivity(new Intent(this, RecipeListActivity.class));
    }

    private boolean validateRecipeDetails(String recipeName, String course, String ingredients, String weight, String cookingTime) {
        if (recipeName.isEmpty() || course.isEmpty() || ingredients.isEmpty() || weight.isEmpty() || cookingTime.isEmpty()) {
            Toast.makeText(this, "Please enter all recipe details", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            // Validate numeric input for weight and cooking time
            Double.parseDouble(weight);
            Integer.parseInt(cookingTime);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values for weight and cooking time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addRecipeToDatabase(String recipeName, String course, String ingredients, String weight, String cookingTime, String instructionLink) {
        // Add recipe to the database using DatabaseHelper
        long result = databaseHelper.addRecipe(recipeName, course, ingredients, weight, cookingTime, instructionLink);

        // Check if the recipe was added successfully
        if (result != -1) {
            Toast.makeText(this, "Recipe added successfully", Toast.LENGTH_SHORT).show();
            // Clear input fields
            clearInputFields();
            startActivity(new Intent(this, RecipeListActivity.class));
        } else {
            Toast.makeText(this, "Failed to add recipe. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearInputFields() {
        // Clear all input fields
        recipeNameEditText.setText("");
        ingredientsEditText.setText("");
        weightEditText.setText("");
        cookingTimeEditText.setText("");
        provideLinkCheckBox.setChecked(false);
        instructionLinkEditText.setText("");
        instructionLinkEditText.setVisibility(View.GONE); // Hide instruction link field
    }
}