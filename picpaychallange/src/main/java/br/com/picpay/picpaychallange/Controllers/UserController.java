package br.com.picpay.picpaychallange.Controllers;

import br.com.picpay.picpaychallange.Dto.UserResponseDTO;
import br.com.picpay.picpaychallange.domain.user.User;
import br.com.picpay.picpaychallange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.picpay.picpaychallange.Dto.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserDTO user) {
        User newUser = userService.createUser(user);
        UserResponseDTO response = UserResponseDTO.fromUser(newUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        // Converte List<User> para List<UserResponseDTO> com campos sensíveis como null
        List<UserResponseDTO> userResponses = users.stream()
                .map(UserResponseDTO::fromUser)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findUserById(id);
            UserResponseDTO response = UserResponseDTO.fromUser(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}