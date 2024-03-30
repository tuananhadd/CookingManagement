
package com.example.cookingmanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecipeListAdapter extends BaseAdapter {
    private Context context;
    private List<Recipe> recipeList;
    private DatabaseHelper dbHelper;

    public RecipeListAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.recipe_list_item, parent, false);
        }

        TextView recipeNameTextView = convertView.findViewById(R.id.recipeNameTextView);
        TextView courseTextView = convertView.findViewById(R.id.courseTextView);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);
        Button updateButton = convertView.findViewById(R.id.updateButton);
        Button viewButton = convertView.findViewById(R.id.viewButton);

        final Recipe recipe = recipeList.get(position);

        recipeNameTextView.setText(recipe.getRecipeName());
        courseTextView.setText(recipe.getCourse());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRecipe(recipe.getId(), recipe);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditRecipeActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getId());
                context.startActivity(intent);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecipe(recipe);
            }
        });

        return convertView;
    }

    private void addRecipe(Recipe recipe) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("SELECTED_RECIPE_ID", recipe.getId());
        ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
        ((Activity) context).finish();
    }

    private void deleteRecipe(long recipeId, Recipe recipe) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        dbHelper.deleteRecipe(recipeId);

        recipeList.remove(recipe);
        notifyDataSetChanged();
        Toast.makeText(context, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
    }

    private void updateRecipe(Recipe recipe) {
        Intent intent = new Intent(context, EditRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        context.startActivity(intent);
    }

    private void viewRecipe(Recipe recipe) {
        Intent intent = new Intent(context, ViewRecipeActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getId());
        context.startActivity(intent);
    }

    public void setData(List<Recipe> data) {
        recipeList = data;
    }
}
