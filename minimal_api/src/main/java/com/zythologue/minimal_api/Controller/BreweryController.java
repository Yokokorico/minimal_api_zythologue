package com.zythologue.minimal_api.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zythologue.minimal_api.Model.BreweryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class BreweryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("brewery")
    @Operation(summary = "Get All Breweries", description = "Returns all breweries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<List<BreweryDTO>> getAllBrewery() {
        try {
            String sql = "SELECT * FROM Brewery";

            List<BreweryDTO> brewerys = jdbcTemplate.query(sql, (rs, rowNum) -> {
                BreweryDTO brewery = new BreweryDTO();
                brewery.setName(rs.getString("zybr_name"));
                brewery.setCountry(rs.getString("zybr_country"));
                brewery.setWebsite(rs.getString("zybr_website"));

                return brewery;
            });

            if (brewerys.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            else {
                return ResponseEntity.ok().body(brewerys);
            }
        }
        catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("brewery/{id}")
    public ResponseEntity<?> getOneBrewery(@PathVariable
    int id) {
        try {
            String sql = "SELECT zybr_name, zybr_country, zybr_website FROM Brewery WHERE zybr_id = ?";

            List<BreweryDTO> breweries = jdbcTemplate.query(sql, (rs, rowNum) -> {
                BreweryDTO breweryQuery = new BreweryDTO();
                breweryQuery.setName(rs.getString("zybr_name"));
                breweryQuery.setCountry(rs.getString("zybr_country"));
                breweryQuery.setWebsite(rs.getString("zybr_website"));
                return breweryQuery;
            }, id);

            if (breweries.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune brasserie trouvée avec l'ID spécifié.");
            }
            else {
                return ResponseEntity.ok(breweries);
            }
        }
        catch (EmptyResultDataAccessException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune brasserie trouvée avec l'ID spécifié.");
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération des données.");
        }
    }

    @PostMapping("/brewery")
    public ResponseEntity<String> insertBrewery(@RequestBody
    BreweryDTO breweryDTO) {
        try {
            jdbcTemplate.update("INSERT INTO brewery (zybr_name, zybr_country, zybr_website) VALUES (?, ?, ?)",
                    breweryDTO.getName(), breweryDTO.getCountry(), breweryDTO.getWebsite());
            return ResponseEntity.status(HttpStatus.CREATED).body("Brasserie insérée avec succès.");
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'insertion de la brasserie.");
        }
    }

    @PutMapping("brewery/{id}")
    public ResponseEntity<String> updateBrewery(@PathVariable
    int id, @RequestBody
    BreweryDTO breweryDTO) {
        try {
            int rowsUpdated = jdbcTemplate.update(
                    "UPDATE brewery SET zybr_name = ?, zybr_country = ?, zybr_website = ? WHERE zybr_id = ?",
                    breweryDTO.getName(), breweryDTO.getCountry(), breweryDTO.getWebsite(), id);

            if (rowsUpdated > 0) {
                return ResponseEntity.ok("Brasserie mise à jour avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune brasserie trouvée avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la mise à jour de la brasserie.");
        }
    }

    @DeleteMapping("brewery/{id}")
    public ResponseEntity<String> deleteBrewery(@PathVariable
    int id) {
        try {
            int rowsDeleted = jdbcTemplate.update("DELETE FROM brewery WHERE zybr_id = ?", id);

            if (rowsDeleted > 0) {
                return ResponseEntity.ok("Brasserie supprimée avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucune brasserie trouvée avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la suppression de la brasserie.");
        }
    }
}
