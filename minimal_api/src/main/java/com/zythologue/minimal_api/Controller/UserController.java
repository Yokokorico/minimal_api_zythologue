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

import com.zythologue.minimal_api.Model.UserDTO;

@RestController
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            String sql = "SELECT * FROM \"User\"";

            List<UserDTO> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserDTO user = new UserDTO();
                user.setName(rs.getString("zyu_name"));
                user.setEmail(rs.getString("zyu_email"));
                user.setPassword(rs.getString("zyu_password"));
                return user;
            });

            return ResponseEntity.ok(users);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération des utilisateurs.");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable
    int id) {
        try {
            String sql = "SELECT zyu_name, zyu_email, zyu_password FROM \"User\" WHERE zyu_id = ?";

            List<UserDTO> users = jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserDTO userDTO = new UserDTO();
                userDTO.setName(rs.getString("zyu_name"));
                userDTO.setEmail(rs.getString("zyu_email"));
                userDTO.setPassword(rs.getString("zyu_password"));
                return userDTO;
            }, id);

            if (users.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(users);
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la récupération de l'utilisateur.");
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody
    UserDTO user) {
        try {
            jdbcTemplate.update("INSERT INTO \"User\" (zyu_name, zyu_email, zyu_password) VALUES (?, ?, ?)",
                    user.getName(), user.getEmail(), user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur ajouté avec succès.");
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de l'ajout de l'utilisateur.");
        }
    }

    @PutMapping("user/{id}")
    public ResponseEntity<String> updateUser(@PathVariable
    int id, @RequestBody
    UserDTO user) {
        try {
            int rowsUpdated = jdbcTemplate.update(
                    "UPDATE \"User\" SET zyu_name = ?, zyu_email = ?, zyu_password = ? WHERE zyu_id = ?",
                    user.getName(), user.getEmail(), user.getPassword(), id);

            if (rowsUpdated > 0) {
                return ResponseEntity.ok("Utilisateur mis à jour avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun utilisateur trouvé avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la mise à jour de l'utilisateur.");
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable
    int id) {
        try {
            int rowsDeleted = jdbcTemplate.update("DELETE FROM \"User\" WHERE zyu_id = ?", id);

            if (rowsDeleted > 0) {
                return ResponseEntity.ok("Utilisateur supprimé avec succès.");
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun utilisateur trouvé avec l'ID spécifié.");
            }
        }
        catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur s'est produite lors de la suppression de l'utilisateur.");
        }
    }

}
