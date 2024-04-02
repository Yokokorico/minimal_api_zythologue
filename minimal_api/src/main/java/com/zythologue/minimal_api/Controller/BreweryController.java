package com.zythologue.minimal_api.Controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zythologue.minimal_api.Model.BreweryDTO;

@RestController
public class BreweryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("brewery")
    public List<BreweryDTO> getAllBrewery() {
        String sql = "SELECT * FROM Brewery";

        List<BreweryDTO> brewerys = jdbcTemplate.query(sql, (rs, rowNum) -> {
            BreweryDTO brewery = new BreweryDTO();
            brewery.setName(rs.getString("zybr_name"));
            brewery.setCountry(rs.getString("zybr_country"));
            brewery.setWebsite(rs.getString("zybr_website"));

            return brewery;
        });
        return brewerys;
    }

    @GetMapping("brewery/{id}")
    public List<BreweryDTO> getOneBrewery(@PathVariable int id) {
        String sql = "SELECT zybr_name, zybr_country, zybr_website FROM Brewery WHERE zybr_id = ?";

        List<BreweryDTO> brewery = jdbcTemplate.query(sql, (rs, rowNum) -> {
            BreweryDTO breweryQuery = new BreweryDTO();
            breweryQuery.setName(rs.getString("zybr_name"));
            breweryQuery.setCountry(rs.getString("zybr_country"));
            breweryQuery.setWebsite(rs.getString("zybr_website"));
            return breweryQuery;
        }, id);
        return brewery;
    }

    @PostMapping("/brewery")
    public void insertBrewery(@RequestBody BreweryDTO breweryDTO) {
        jdbcTemplate.update("INSERT INTO brewery (zybr_name, zybr_country, zybr_website) VALUES (?, ?, ?)",
                breweryDTO.getName(), breweryDTO.getCountry(), breweryDTO.getWebsite());
    }

    @PutMapping("brewery/{id}")
    public void updateBrewery(@PathVariable int id, @RequestBody BreweryDTO breweryDTO) {
        jdbcTemplate.update("UPDATE brewery SET zybr_name = ?, zybr_country = ?, zybr_website = ? WHERE zybr_id = ?",
                breweryDTO.getName(), breweryDTO.getCountry(), breweryDTO.getWebsite(), id);
    }

    @DeleteMapping("brewery/{id}")
    public void deleteBeer(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM brewery WHERE zybr_id = ?", id);
    }
}
