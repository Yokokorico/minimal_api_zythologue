package com.zythologue.minimal_api.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Model.Beer;
import com.zythologue.minimal_api.Model.BeerDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
public class BeerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/beers")
    public List<Beer> getAllBeers() {
        String sql = "SELECT * FROM beer";

        List<Beer> beers = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    Beer beer = new Beer();
                    beer.setId((rs.getInt("zyb_id")));
                    beer.setName(rs.getString("zyb_name"));
                    beer.setDescription(rs.getString("zyb_description")); // Assuming typo in original code
                    beer.setCategory(rs.getInt("zyb_category"));
                    beer.setAbv(rs.getFloat("zyb_abv"));
                    beer.setBrewery(rs.getInt("zyb_brewery"));
                    beer.setCreated(rs.getTimestamp("zyb_created_at"));
                    beer.setUpdated(rs.getTimestamp("zyb_updated_at"));
                    return beer;
                });

        return beers;
    }

    @GetMapping("/beer/{id}")
    public List<BeerDTO> getBeerById(@PathVariable int id) {
        String sql = "SELECT zyb_name, zyb_description, zyb_category, zyb_abv, zyb_brewery FROM beer WHERE zyb_id = ?";

        List<BeerDTO> beers = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    BeerDTO beerDTO = new BeerDTO();
                    beerDTO.setName(rs.getString("zyb_name"));
                    beerDTO.setDescription(rs.getString("zyb_description")); // Assuming typo in original code
                    beerDTO.setCategory(rs.getInt("zyb_category"));
                    beerDTO.setAbv(rs.getFloat("zyb_abv"));
                    beerDTO.setBrewery(rs.getInt("zyb_brewery"));
                    return beerDTO;
                },
                id
                );

        return beers;
    }

    @PostMapping("/beer")
    public void updateBeer(@RequestBody BeerDTO beer) {
        jdbcTemplate.update(
            "INSERT INTO beer (zyb_name, zyb_description, zyb_abv, zyb_brewery, zyb_category) VALUES (?, ?, ?, ?, ?)",
            beer.getName(),
            beer.getDescription(),
            beer.getAbv(),
            beer.getBrewery(),
            beer.getCategory());
    }

    @PutMapping("beer/{id}")
    public void updateBeer(@PathVariable int id, @RequestBody BeerDTO beer) {
        jdbcTemplate.update(
            "UPDATE beer SET zyb_name = ?, zyb_description = ?, zyb_abv = ?, zyb_brewery = ?, zyb_category = ? WHERE zyb_id = ?",
            beer.getName(),
            beer.getDescription(),
            beer.getAbv(),
            beer.getBrewery(),
            beer.getCategory(),
            id
        );
    }
    
    @DeleteMapping("beer/{id}")
    public void deleteBeer(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM beer WHERE zyb_id = ?", id);
    }
}
