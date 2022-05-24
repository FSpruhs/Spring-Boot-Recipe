package com.example.recipe.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.recipe.businesslayer.Recipe;
import com.example.recipe.businesslayer.RecipeService;
import com.example.recipe.businesslayer.UserDetailsImpl;

import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipe")
public class RecipesController {
    private final RecipeService recipeService;

    public RecipesController (RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("new")
    public ResponseEntity<Recipe> setRecipe(@Valid @RequestBody Recipe recipe) {
        recipe.setAuthor(getCurrentUserName());
        recipeService.save(recipe);
        return new ResponseEntity(Map.of("id", recipe.getId()), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        Optional<Recipe> optionalRecipe = recipeService.findById(id);
        if (optionalRecipe.isPresent()) {
            return new ResponseEntity(optionalRecipe.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteRecipe(@PathVariable long id) {
        return recipeService.delete(id, getCurrentUserName());
    }

    @PutMapping("{id}")
    public ResponseEntity updateRecipe(@PathVariable long id, @Valid @RequestBody Recipe recipe) {
        try {
            return new ResponseEntity(recipeService.updateRecipe(recipe, id, getCurrentUserName()), HttpStatus.NO_CONTENT);
        } catch (IllegalAccessException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("search")
    public ResponseEntity searchRecipe(@RequestParam(required = false) String category,
                                       @RequestParam(required = false) String name) {
        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else if (name != null) {
            return new ResponseEntity(recipeService.searchByName(name), HttpStatus.OK);
        } else {
            return new ResponseEntity(recipeService.searchByCategory(category), HttpStatus.OK);
        }
    }


    private String getCurrentUserName() {
        return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUsername();
    }

}