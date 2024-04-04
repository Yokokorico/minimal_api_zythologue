package com.zythologue.minimal_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Model.Favorite;
import com.zythologue.minimal_api.Model.FavoriteDTO;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@Tag(name = "Favorite", description = "Favorite management endpoints")
public class FavoriteController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/favorites")
    @Operation(summary = "Get all favorites", description = "Retrieves all favorites")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllFavorites() {
        try {
            String sql = "SELECT * FROM favorite";

            List<Favorite> favorites = jdbcTemplate.query(sql, (rs, rowNum) -> {
                Favorite favorite = new Favorite();
                favorite.setUserId(rs.getInt("zyf_user"));
                favorite.setBeerId(rs.getInt("zyf_beer"));
                favorite.setCreated(rs.getTimestamp("zyf_created_at"));
                favorite.setUpdated(rs.getTimestamp("zyf_updated_at"));
                return favorite;
            });

            return ResponseEntity.ok(favorites);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération des favoris.");
        }
    }

    @GetMapping("/favorites/{id}")
    @Operation(summary = "Get favorite by user id", description = "Retrieves favorite by user id", parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", required = true, description = "User ID")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Favorite not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getFavoriteByUserId(@PathVariable
    int id) {
        try {
            String sql = "SELECT zyf_user, zyf_beer FROM favorite WHERE zyf_user = ?";

            List<FavoriteDTO> favorites = jdbcTemplate.query(sql, (rs, rowNum) -> {
                FavoriteDTO favoriteDTO = new FavoriteDTO();
                favoriteDTO.setUserId(rs.getInt("zyf_user"));
                favoriteDTO.setBeerId(rs.getInt("zyf_beer"));
                return favoriteDTO;
            }, id);

            if (favorites.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(favorites);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération des favoris.");
        }
    }

    @PostMapping("/favorites")
    @Operation(summary = "Insert Favorite", description = "Inserts a new favorite")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Favorite inserted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> insertFavorite(@RequestBody
    FavoriteDTO favorite) {
        try {
            jdbcTemplate.update("INSERT INTO favorite (zyf_user, zyf_beer) VALUES (?, ?)", favorite.getUserId(),
                    favorite.getBeerId());
            return ResponseEntity.status(HttpStatus.CREATED).body("Favori inséré avec succès.");
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'insertion du favori.");
        }
    }

    @DeleteMapping("favorites/{id}")
    @Operation(summary = "Delete Favorite", description = "Deletes a favorite", parameters = {
            @Parameter(in = ParameterIn.PATH, name = "id", description = "The id of the user", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Favorite not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> deleteFavoritesByUserId(@PathVariable
    int id) {
        try {
            int rowsDeleted = jdbcTemplate.update("DELETE FROM favorite WHERE zyf_user = ?", id);

            if (rowsDeleted > 0) {
                return ResponseEntity.ok("Favoris de l'utilisateur supprimés avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Aucun favori trouvé pour l'utilisateur avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la suppression des favoris de l'utilisateur.");
        }
    }

}
