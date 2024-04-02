package com.zythologue.minimal_api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.zythologue.minimal_api.Model.Favorite;
import com.zythologue.minimal_api.Model.FavoriteDTO;

@RestController
public class FavoriteController {
	@Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/favorites")
    public List<Favorite> getAllFavorites() {
        String sql = "SELECT * FROM favorite";

        List<Favorite> favorites = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Favorite favorite = new Favorite();
			favorite.setUserId(rs.getInt("zyf_user"));
			favorite.setBeerId(rs.getInt("zyf_beer"));
			favorite.setCreated(rs.getTimestamp("zyf_created_at"));
			favorite.setUpdated(rs.getTimestamp("zyf_updated_at"));
            return favorite;
        });

        return favorites;
    }

    @GetMapping("/favorite/{id}")
    public ResponseEntity<List<FavoriteDTO>> getFavoriteByUserId(@PathVariable
    int id) {
        String sql = "SELECT zyf_user,zyf_beer FROM favorite WHERE zyf_user = ?";

        List<FavoriteDTO> favorites = jdbcTemplate.query(sql, (rs, rowNum) -> {
            FavoriteDTO favoriteDTO = new FavoriteDTO();
			favoriteDTO.setUserId(rs.getInt("zyf_user"));
			favoriteDTO.setBeerId(rs.getInt("zyf_beer"));
            return favoriteDTO;
        }, id);

        if(favorites.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/favorite")
    public void updateFavorite(@RequestBody
    FavoriteDTO favorite) {
        jdbcTemplate.update(
                "INSERT INTO favorite (zyf_user,zyf_beer) VALUES (?, ?)", favorite.getUserId(), favorite.getBeerId()
                );
    }

    @PutMapping("favorite/{id}")
    public void updateFavorite(@PathVariable
    int id, @RequestBody
    FavoriteDTO favorite) {
        jdbcTemplate.update(
                "UPDATE favorite SET zyf_user = ?, zyf_beer = ? WHERE zyf_user = ?", favorite.getUserId(), favorite.getBeerId(), id);
    }

    @DeleteMapping("favorite/{id}")
    public void DeleteFavorite(@PathVariable
    int id) {
        jdbcTemplate.update("DELETE FROM favorite WHERE zyf_user = ?", id);
    }
}
