package com.example.teambackend.web.minecraft;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class MinecraftController {

    private final List<String> onlineUsers = new CopyOnWriteArrayList<>();

    @PostMapping("/api/minecraft/online-users")
    public ResponseEntity<List<String>> receiveOnlineUsers(@RequestBody List<String> users) {
        onlineUsers.clear();
        onlineUsers.addAll(users);
        return ResponseEntity.ok(onlineUsers);
    }


    @GetMapping("/minecraft/online-users")
    public ResponseEntity<List<String>> showOnlineUsers() {
        return ResponseEntity.ok(onlineUsers);
    };
}