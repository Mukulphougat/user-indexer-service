package org.mukulphougat.userindexerservice.controller;

import org.mukulphougat.userindexerservice.model.UserRegisteredEvent;
import org.mukulphougat.userindexerservice.service.UserIndexService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class UserSearchController {

    private final UserIndexService userIndexService;

    public UserSearchController(UserIndexService userIndexService) {
        this.userIndexService = userIndexService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRegisteredEvent> getUserById(@PathVariable String id) throws IOException {
        UserRegisteredEvent user = userIndexService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Search users by username
    @GetMapping("/byUsername")
    public ResponseEntity<List<UserRegisteredEvent>> searchUsersByUsername(@RequestParam String username) throws IOException {
        List<UserRegisteredEvent> users = userIndexService.searchByUsername(username);
        return ResponseEntity.ok(users);
    }

    // Search users by email
    @GetMapping("/byEmail")
    public ResponseEntity<List<UserRegisteredEvent>> searchUsersByEmail(@RequestParam String email) throws IOException {
        List<UserRegisteredEvent> users = userIndexService.searchByEmail(email);
        return ResponseEntity.ok(users);
    }
    @GetMapping("/bulk")
    public ResponseEntity<List<UserRegisteredEvent>> bulkFetch(@RequestParam int limit, @RequestParam int offset) throws IOException {
        List<UserRegisteredEvent> users = userIndexService.fetchUsers(limit, offset);
        return ResponseEntity.ok(users);
    }
}
