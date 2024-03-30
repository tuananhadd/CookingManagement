package com.example.cookingmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText editRecipeName;
    private Spinner editCourseSpinner;
    private EditText editIngredients;
    private EditText editWeight;
    private EditText editCookingTime;
    private CheckBox editProvideLinkCheckBox;
    private EditText editInstructionLink;
    private Button btnSaveChanges;
    private Button btnBackToRecipeList;

    private DatabaseHelper databaseHelper;
    private long recipeId;
    private int selectedCoursePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        databaseHelper = new DatabaseHelper(this);

        editRecipeName = findViewById(R.id.editTextEditRecipeName);
        editCourseSpinner = findViewById(R.id.spinnerEditCourse);
        editIngredients = findViewById(R.id.editTextEditIngredients);
        editWeight = findViewById(R.id.editTextEditWeight);
        editCookingTime = findViewById(R.id.editTextEditCookingTime);
        editProvideLinkCheckBox = findViewById(R.id.checkBoxEditProvideLink);
        editInstructionLink = findViewById(R.id.editTextEditInstructionLink);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnBackToRecipeList = findViewById(R.id.btnBackToRecipeList);

        recipeId = getIntent().getLongExtra("RECIPE_ID", -1);

        loadRecipeDetails();

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        setupCourseSpinner(selectedCoursePosition);
    }

    public void onClickViewRecipes(View view) {
        // Handle the button click to navigate to RecipeListActivity
        startActivity(new Intent(this, RecipeListActivity.class));
    }

    private void setupCourseSpinner(int selectedPosition) {
        String[] courseOptions = {"Hors d'oeuvre", "Soup", "Appetizer", "Salad", "Main Course", "Dessert", "Mignardise"};

        // Check if the adapter is already set
        if (editCourseSpinner.getAdapter() == null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, courseOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editCourseSpinner.setAdapter(adapter);
        }

        // Set the selected position
        editCourseSpinner.setSelection(selectedPosition);
    }

    private void loadRecipeDetails() {
        if (recipeId != -1) {
            Recipe recipe = databaseHelper.getRecipeById(recipeId);

            if (recipe != null) {
                // Get the selected course from the intent
                String selectedCourse = getIntent().getStringExtra("SELECTED_COURSE");

                editRecipeName.setText(recipe.getRecipeName());
                editIngredients.setText(recipe.getIngredients());
                editWeight.setText(recipe.getWeight());
                editCookingTime.setText(recipe.getCookingTime());

                editProvideLinkCheckBox.setChecked(!recipe.getInstructionLink().isEmpty());
                editInstructionLink.setText(recipe.getInstructionLink());
                editInstructionLink.setVisibility(editProvideLinkCheckBox.isChecked() ? View.VISIBLE : View.GONE);

                editProvideLinkCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    editInstructionLink.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                });

                // Directly set the selected item in the spinner
                setSpinnerSelection(editCourseSpinner, selectedCourse);
            } else {
                Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    private int getSpinnerPosition(String course) {
        String[] courseOptions = {"Hors d'oeuvre", "Soup", "Appetizer", "Salad", "Main Course", "Dessert", "Mignardise"};
        for (int i = 0; i < courseOptions.length; i++) {
            if (courseOptions[i].equals(course)) {
                return i;
            }
        }
        return 0; // Default to the first position if not found
    }

    private void saveChanges() {
        String editedRecipeName = editRecipeName.getText().toString().trim();
        String editedCourse = editCourseSpinner.getSelectedItem().toString().trim();
        String editedIngredients = editIngredients.getText().toString().trim();
        String editedWeight = editWeight.getText().toString().trim();
        String editedCookingTime = editCookingTime.getText().toString().trim();
        String editedInstructionLink = editInstructionLink.getText().toString().trim();

        if (!editedRecipeName.isEmpty() && !editedCourse.isEmpty() && !editedIngredients.isEmpty() && !editedWeight.isEmpty() && !editedCookingTime.isEmpty()) {
            Recipe updatedRecipe = new Recipe();
            updatedRecipe.setId(recipeId);
            updatedRecipe.setRecipeName(editedRecipeName);
            updatedRecipe.setCourse(editedCourse);
            updatedRecipe.setIngredients(editedIngredients);
            updatedRecipe.setWeight(editedWeight);
            updatedRecipe.setCookingTime(editedCookingTime);
            updatedRecipe.setInstructionLink(editedInstructionLink);

            databaseHelper.updateRecipe(updatedRecipe);

            Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Please enter all recipe details", Toast.LENGTH_SHORT).show();
        }
    }
}
