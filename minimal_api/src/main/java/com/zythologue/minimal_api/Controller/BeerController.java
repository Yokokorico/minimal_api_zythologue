package com.zythologue.minimal_api.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Beer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class BeerController {

    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @GetMapping("/beers")
 
    public List<Beer> getAllBeers() {
        return jdbcTemplate.query(
                "SELECT * FROM beer",
                (rs, rowNum) -> new Beer(
                        rs.getInt("zyb_id"),
                        rs.getString("zyb_name"),
                        rs.getString("zyb_description"),
                        rs.getInt("zyb_category"),
                        rs.getFloat("zyb_abv"),
                        rs.getInt("zyb_brewery"),
                        rs.getTimestamp("zyb_created_at"),
                        rs.getTimestamp("zyb_updated_at")
                )
        );
    }
    

}
