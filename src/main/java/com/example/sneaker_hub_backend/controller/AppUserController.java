// package com.example.sneaker_hub_backend.controller;

// import com.example.sneaker_hub_backend.model.AppUser;
// import com.example.sneaker_hub_backend.service.AppUserService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;
// // import org.springframework.web.multipart.MultipartFile;

// import java.util.HashMap;
// import java.util.Map;
// // import java.io.File;
// // import java.io.IOException;
// // import java.time.LocalDate;
// import java.util.Optional;
// @RestController
// @CrossOrigin
// @RequestMapping("/users")
// public class AppUserController {
//     @Autowired
//     private AppUserService appUserService;

//     @PostMapping("/signup")
//     public ResponseEntity<Object> signup(@RequestBody AppUser appUser) {
//     System.out.println("Received AppUser: " + appUser);

//     if (appUserService.isUsernameTaken(appUser.getUsername())) {
//         Map<String, String> errorResponse = new HashMap<>();
//         errorResponse.put("error", "Username already taken");
//         return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
//     }

//     AppUser savedUser = appUserService.saveUser(appUser);

//     return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
// }


//     @PostMapping("/login")
// public ResponseEntity<AppUser> login(@RequestBody AppUser user) {
//     Optional<AppUser> existingUser = appUserService.findByUsername(user.getUsername());
//     if (existingUser.isPresent()) {
//         boolean passwordMatch = appUserService.validatePassword(user.getPassword(), existingUser.get().getPassword());
//         boolean userTypeMatch = existingUser.get().getUserType().equals(user.getUserType());

//         if (passwordMatch && userTypeMatch) {
//             return ResponseEntity.ok(existingUser.get()); // Return user details
//         }
//     }
//     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Return null or an error object
// }



//     @GetMapping("/check-username")
//     public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
//         boolean isTaken = appUserService.isUsernameTaken(username);
//         return ResponseEntity.ok(!isTaken);
//     }
// }
