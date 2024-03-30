package com.example.cookingmanagement;

public class Recipe {
    private long id;
    private String recipeName;
    private String course;
    private String ingredients;
    private String weight;
    private String cookingTime;
    private String instructionLink;

    public Recipe() {
        // Default constructor
    }

    public Recipe(String recipeName, String course, String ingredients, String weight, String cookingTime, String instructionLink) {
        this.recipeName = recipeName;
        this.course = course;
        this.ingredients = ingredients;
        this.weight = weight;
        this.cookingTime = cookingTime;
        this.instructionLink = instructionLink;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getInstructionLink() {
        return instructionLink;
    }

    public void setInstructionLink(String instructionLink) {
        this.instructionLink = instructionLink;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", recipeName='" + recipeName + '\'' +
                ", course='" + course + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", weight='" + weight + '\'' +
                ", cookingTime='" + cookingTime + '\'' +
                ", instructionLink='" + instructionLink + '\'' +
                '}';
    }
}
