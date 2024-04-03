package com.zythologue.minimal_api.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Model.BeerDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Tag(name = "Beer", description = "Beer Controller")
public class BeerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/beers")
    @Operation(summary = "Get All Beers", description = "Returns a list of all beers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<List<BeerDTO>> getAllBeers() {
        try {
            String sql = "SELECT * FROM beer";

            List<BeerDTO> beers = jdbcTemplate.query(sql, (rs, rowNum) -> {
                BeerDTO beer = new BeerDTO();
                beer.setName(rs.getString("zyb_name"));
                beer.setDescription(rs.getString("zyb_description")); // Assuming typo in original code
                beer.setCategory(rs.getInt("zyb_category"));
                beer.setAbv(rs.getFloat("zyb_abv"));
                beer.setBrewery(rs.getInt("zyb_brewery"));
                return beer;
            });

            if (beers.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            else {
                return ResponseEntity.ok().body(beers);
            }
        }
        catch (DataAccessException e) {
            // Handle database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/beers/{id}")
    @Operation(summary = "Get Beer By Id", description = "Returns a single beer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "404", content = {
                    @Content(schema = @Schema())
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable
    int id) {
        try {
            String sql = "SELECT zyb_name, zyb_description, zyb_category, zyb_abv, zyb_brewery FROM beer WHERE zyb_id = ?";

            List<BeerDTO> beers = jdbcTemplate.query(sql, (rs, rowNum) -> {
                BeerDTO beerDTO = new BeerDTO();
                beerDTO.setName(rs.getString("zyb_name"));
                beerDTO.setDescription(rs.getString("zyb_description")); // Assuming typo in original code
                beerDTO.setCategory(rs.getInt("zyb_category"));
                beerDTO.setAbv(rs.getFloat("zyb_abv"));
                beerDTO.setBrewery(rs.getInt("zyb_brewery"));
                return beerDTO;
            }, id);

            if (beers.size() == 0) {
                return ResponseEntity.notFound().build();
            }
            else {
                return ResponseEntity.ok().body(beers.get(0));
            }
        }
        catch (DataAccessException e) {
            // Handle database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (Exception e) {
            // Handle other unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/beers")
    @Operation(summary = "Create Beer", description = "Creates a new beer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<Void> createBeer(@RequestBody
    BeerDTO beer) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO beer (zyb_name, zyb_description, zyb_abv, zyb_brewery, zyb_category) VALUES (?, ?, ?, ?, ?)",
                    beer.getName(), beer.getDescription(), beer.getAbv(), beer.getBrewery(), beer.getCategory());

            return ResponseEntity.ok().build(); // Beer successfully created
        }
        catch (DataAccessException e) {
            // Handle database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("beers/{id}")
    @Operation(summary = "Update Beer", description = "Updates an existing beer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<Void> updateBeer(@PathVariable
    int id, @RequestBody
    BeerDTO beer) {
        try {
            int rowsUpdated = jdbcTemplate.update(
                    "UPDATE beer SET zyb_name = ?, zyb_description = ?, zyb_abv = ?, zyb_brewery = ?, zyb_category = ? WHERE zyb_id = ?",
                    beer.getName(), beer.getDescription(), beer.getAbv(), beer.getBrewery(), beer.getCategory(), id);

            if (rowsUpdated > 0) {
                return ResponseEntity.ok().build(); // Beer successfully updated
            }
            else {
                return ResponseEntity.notFound().build(); // Beer with specified ID not found
            }
        }
        catch (DataAccessException e) {
            // Handle database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("beers/{id}")
    @Operation(summary = "Delete Beer", description = "Deletes an existing beer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(), mediaType = "application/json")
            }), @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<?> deleteBeer(@PathVariable
    int id) {
        try {
            int rowsDeleted = jdbcTemplate.update("DELETE FROM beer WHERE zyb_id = ?", id);

            if (rowsDeleted > 0) {
                return ResponseEntity.ok().build(); // Beer successfully deleted
            }
            else {
                return ResponseEntity.notFound().build(); // Beer with specified ID not found
            }
        }
        catch (DataAccessException e) {
            // Handle database errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la suppression de la bière");
        }
    }

}
