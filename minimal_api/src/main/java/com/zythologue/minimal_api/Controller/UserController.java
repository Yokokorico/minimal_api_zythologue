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

import com.zythologue.minimal_api.Model.UserDTO;
@RestController
public class UserController {
@Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        String sql = "SELECT * FROM \"User\" ";

        List<UserDTO> users = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    UserDTO user = new UserDTO();
                    user.setName(rs.getString("zyu_name"));
                    user.setEmail(rs.getString("zyu_email"));
                    user.setPassword(rs.getString("zyu_password"));
                    return user;
                });

        return users;
    }

    @GetMapping("/user/{id}")
    public List<UserDTO> getUserById(@PathVariable int id) {
        String sql = "SELECT zyu_name, zyu_email, zyu_password FROM \"User\" WHERE zyu_id = ?";

        List<UserDTO> users = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    UserDTO UserDTO = new UserDTO();
                    UserDTO.setName(rs.getString("zyu_name"));
                    UserDTO.setEmail(rs.getString("zyu_email"));
                    UserDTO.setPassword(rs.getString("zyu_password"));
                    return UserDTO;
                },
                id
                );

        return users;
    }

    @PostMapping("/user")
    public void addUser(@RequestBody UserDTO user) {
        jdbcTemplate.update(
            "INSERT INTO \"User\" (zyu_name, zyu_email, zyu_password) VALUES (?, ?, ?)",
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );
    }

    @PutMapping("user/{id}")
    public void updateUser(@PathVariable int id, @RequestBody UserDTO user) {
        jdbcTemplate.update(
            "UPDATE \"User\" SET zyu_name = ?, zyu_email = ?, zyu_password = ? WHERE zyu_id = ?",
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            id
        );
    }
    
    @DeleteMapping("user/{id}")
    public void deleteUser(@PathVariable int id) {
        jdbcTemplate.update("DELETE FROM \"User\" WHERE zyu_id = ?", id);
    }
}
