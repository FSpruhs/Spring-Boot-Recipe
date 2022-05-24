package com.example.recipe.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.recipe.businesslayer.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository  extends CrudRepository<Recipe, Long> {

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Recipe> findAllByNameContainsIgnoreCaseOrderByDateDesc(String name);
}
