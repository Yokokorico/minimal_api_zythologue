package com.zythologue.minimal_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<IngredientDTO> getAllIngredients() {
        String sql = "SELECT * FROM ingredient ";

        List<IngredientDTO> ingredients = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    IngredientDTO ingredient = new IngredientDTO();
                    ingredient.setName(rs.getString("zyi_name"));
                    ingredient.setType(rs.getString("zyi_type"));
                    return ingredient;
                });

        return ingredients;
    }

    @GetMapping("/ingredient/{id}")
    public List<IngredientDTO> getingredientById(@PathVariable int id) {
        String sql = "SELECT zyi_name, zyi_type FROM ingredient WHERE zyi_id = ? ";

        List<IngredientDTO> ingredients = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    IngredientDTO IngredientDTO = new IngredientDTO();
                    IngredientDTO.setName(rs.getString("zyi_name"));
                    IngredientDTO.setType(rs.getString("zyi_type"));
                    return IngredientDTO;
                },
                id
                );

        return ingredients;
    }

    @PostMapping("/ingredient")
    public void addingredient(@RequestBody IngredientDTO ingredient) {
        jdbcTemplate.update(
            "INSERT INTO ingredient (zyi_name, zyi_type) VALUES (?, ?)",
            ingredient.getName(),
            ingredient.getType()
        );
    }

    @PutMapping("ingredient/{id}")
    public void updateingredient(@PathVariable int id, @RequestBody IngredientDTO ingredient) {
        jdbcTemplate.update(
            "UPDATE ingredient SET zyi_name = ?, zyi_type = ? WHERE zyi_id = ?",
            ingredient.getName(),
            ingredient.getType(),
            id
        );
    }
    
    @DeleteMapping("ingredient/{id}")
    public void deleteingredient(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM ingredient WHERE zyi_id = ?", id);
    }
}
