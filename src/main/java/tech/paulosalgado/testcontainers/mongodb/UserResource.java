package tech.paulosalgado.testcontainers.mongodb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserResource {

    private final UserRepository repository;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userSaved = repository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

}
