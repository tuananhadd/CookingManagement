package com.example.cookingmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private EditText dishNameEditText;
    private EditText ingredientsEditText;
    private ListView shoppingListView;

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        databaseHelper = new DatabaseHelper(this);

        dishNameEditText = findViewById(R.id.editTextDishName);
        ingredientsEditText = findViewById(R.id.editTextIngredients);
        shoppingListView = findViewById(R.id.listViewShoppingList);

        // Populate shopping list
        populateShoppingList();

        // Set up item click listener for editing/removing items
        shoppingListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDish = shoppingListAdapter.getItem(position);
            editOrRemoveDish(selectedDish);
        });
    }

    public void onClickAddToShoppingList(View view) {
        String dishName = dishNameEditText.getText().toString().trim();
        String ingredients = ingredientsEditText.getText().toString().trim();

        if (!dishName.isEmpty() && !ingredients.isEmpty()) {
            // Add dish to the shopping list
            addDishToShoppingList(dishName, ingredients);

            // Clear input fields
            dishNameEditText.setText("");
            ingredientsEditText.setText("");

            // Update the displayed shopping list
            populateShoppingList();
        } else {
            Toast.makeText(this, "Please enter dish name and ingredients", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateShoppingList() {
        // Retrieve shopping list items from the database
        List<String> shoppingList = getShoppingList();

        // Update the adapter
        shoppingListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingList);
        shoppingListView.setAdapter(shoppingListAdapter);
    }

    private void addDishToShoppingList(String dishName, String ingredients) {
        // Add dish to the database
        databaseHelper.addDishToShoppingList(dishName, ingredients);
    }

    private void editOrRemoveDish(String selectedDish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit or Remove Dish");
        builder.setMessage("Choose an action:");

        builder.setPositiveButton("Edit", (dialog, which) -> {
            // Implement logic for editing a dish
            showEditDishDialog(selectedDish);
        });

        builder.setNegativeButton("Remove", (dialog, which) -> {
            // Implement logic for removing a dish
            removeDishFromShoppingList(selectedDish);
            // Refresh the displayed shopping list
            populateShoppingList();
        });

        builder.setNeutralButton("Cancel", (dialog, which) -> {
            // Do nothing, just close the dialog
            dialog.dismiss();
        });

        builder.show();
    }

    private void showEditDishDialog(String selectedDish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Dish");

        // Split the selectedDish into dish name and ingredients
        String[] dishInfo = selectedDish.split(":");
        String existingDishName = dishInfo[0].trim();
        String existingIngredients = dishInfo[1].trim();

        // Create layout to hold the two EditText fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditTexts to input updated dish information and set existing values
        final EditText editDishName = new EditText(this);
        editDishName.setText(existingDishName);

        final EditText editIngredients = new EditText(this);
        editIngredients.setText(existingIngredients);

        layout.addView(editDishName);
        layout.addView(editIngredients);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String updatedDishName = editDishName.getText().toString().trim();
            String updatedIngredients = editIngredients.getText().toString().trim();

            // Implement logic for updating the dish
            updateDishInShoppingList(existingDishName, updatedDishName, updatedIngredients);

            // Refresh the displayed shopping list
            populateShoppingList();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing, just close the dialog
            dialog.dismiss();
        });

        builder.show();
    }

    private void removeDishFromShoppingList(String selectedDish) {
        // Split the selectedDish into dish name and ingredients
        String[] dishInfo = selectedDish.split(":");
        String dishNameToRemove = dishInfo[0].trim();

        // Implement logic to remove the selected dish from the shopping list
        databaseHelper.removeDishFromShoppingList(dishNameToRemove);

        // Refresh the displayed shopping list
        populateShoppingList();
    }

    private void updateDishInShoppingList(String oldDishName, String updatedDishName, String updatedIngredients) {
        // Implement logic to update the selected dish in the shopping list
        databaseHelper.updateDishInShoppingList(oldDishName, updatedDishName, updatedIngredients);
    }

    private List<String> getShoppingList() {
        // Retrieve shopping list items from the database
        return databaseHelper.getShoppingList();
    }
}
