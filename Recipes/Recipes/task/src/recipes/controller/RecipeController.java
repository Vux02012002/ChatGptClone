package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.entity.Recipe;
import recipes.service.RecipeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    RecipeService recipeService;
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> getRecipe(@PathVariable long id) {
        return recipeService.getRecipe(id);
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRecipe(@RequestBody Recipe recipe) {
        return recipeService.addRecipe(recipe);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Recipe> updateRecipe(@PathVariable long id, @RequestBody Recipe newRecipe) {
        return recipeService.modifyRecipe(id, newRecipe);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Recipe> removeRecipe(@PathVariable long id) {
        return recipeService.deleteRecipe(id);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Recipe>> searchRecipes(@RequestParam Map<String, String> param) {
        if (param.containsKey("category")) return recipeService.searchRecipesByCategory(param.get("category"));
        else if (param.containsKey("name")) return recipeService.searchRecipesByName(param.get("name"));
        return ResponseEntity.badRequest().build();
    }
}
