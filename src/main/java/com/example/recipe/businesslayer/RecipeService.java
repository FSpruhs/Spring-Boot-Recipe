package com.example.recipe.businesslayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.recipe.persistence.RecipeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public Optional<Recipe> findById(long id) {
        return recipeRepository.findById(id);
    }

    public void deleteById(long id) {
        recipeRepository.deleteById(id);
    }

    public Recipe updateRecipe(Recipe recipe, long id, String currentUserName) throws Exception {
        Recipe toUpdateRecipe = recipeRepository.findById(id).orElseThrow(() -> new Exception());
        if (!Objects.equals(toUpdateRecipe.getAuthor(), currentUserName)) {
            throw new IllegalAccessException();
        }
        toUpdateRecipe.setName(recipe.getName());
        toUpdateRecipe.setCategory(recipe.getCategory());
        toUpdateRecipe.setDate(LocalDateTime.now());
        toUpdateRecipe.setDescription(recipe.getDescription());
        toUpdateRecipe.setIngredients(recipe.getIngredients());
        toUpdateRecipe.setDirections(recipe.getDirections());
        return recipeRepository.save(toUpdateRecipe);
    }

    public List<Recipe> searchByCategory(String category) {
        return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }

    public List<Recipe> searchByName(String name) {
        return recipeRepository.findAllByNameContainsIgnoreCaseOrderByDateDesc(name);
    }

    public ResponseEntity delete(long id, String userName) {
        Optional<Recipe> optionalRecipe = findById(id);
        if (optionalRecipe.isPresent() && !Objects.equals(userName, optionalRecipe.get().getAuthor())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        if (optionalRecipe.isPresent()) {
            deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }
}
