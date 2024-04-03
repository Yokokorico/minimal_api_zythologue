package com.zythologue.minimal_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Model.IngredientDTO;

@RestController
public class IngredientController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/ingredients")
    public ResponseEntity<?> getAllIngredients() {
        try {
            String sql = "SELECT * FROM ingredient";

            List<IngredientDTO> ingredients = jdbcTemplate.query(sql, (rs, rowNum) -> {
                IngredientDTO ingredient = new IngredientDTO();
                ingredient.setName(rs.getString("zyi_name"));
                ingredient.setType(rs.getString("zyi_type"));
                return ingredient;
            });

            return ResponseEntity.ok(ingredients);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération des ingrédients.");
        }
    }

    @GetMapping("/ingredient/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable
    int id) {
        try {
            String sql = "SELECT zyi_name, zyi_type FROM ingredient WHERE zyi_id = ?";

            List<IngredientDTO> ingredients = jdbcTemplate.query(sql, (rs, rowNum) -> {
                IngredientDTO ingredientDTO = new IngredientDTO();
                ingredientDTO.setName(rs.getString("zyi_name"));
                ingredientDTO.setType(rs.getString("zyi_type"));
                return ingredientDTO;
            }, id);

            if (ingredients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun ingrédient trouvé avec l'ID spécifié.");
            }

            return ResponseEntity.ok(ingredients);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération de l'ingrédient.");
        }
    }

    @PostMapping("/ingredient")
    public ResponseEntity<String> addIngredient(@RequestBody
    IngredientDTO ingredient) {
        try {
            jdbcTemplate.update("INSERT INTO ingredient (zyi_name, zyi_type) VALUES (?, ?)", ingredient.getName(),
                    ingredient.getType());
            return ResponseEntity.status(HttpStatus.CREATED).body("Ingrédient ajouté avec succès.");
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'ingrédient.");
        }
    }

    @PutMapping("ingredient/{id}")
    public ResponseEntity<String> updateIngredient(@PathVariable
    int id, @RequestBody
    IngredientDTO ingredient) {
        try {
            int rowsUpdated = jdbcTemplate.update("UPDATE ingredient SET zyi_name = ?, zyi_type = ? WHERE zyi_id = ?",
                    ingredient.getName(), ingredient.getType(), id);

            if (rowsUpdated > 0) {
                return ResponseEntity.ok("Ingrédient mis à jour avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun ingrédient trouvé avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la mise à jour de l'ingrédient.");
        }
    }

    @DeleteMapping("ingredient/{id}")
    public ResponseEntity<String> deleteIngredient(@PathVariable
    int id) {
        try {
            int rowsDeleted = jdbcTemplate.update("DELETE FROM ingredient WHERE zyi_id = ?", id);

            if (rowsDeleted > 0) {
                return ResponseEntity.ok("Ingrédient supprimé avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun ingrédient trouvé avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la suppression de l'ingrédient.");
        }
    }

}
