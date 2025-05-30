// package RLEnterprise.configs;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import RLEnterprise.entities.User;
// import RLEnterprise.services.UserService;

// @Configuration
// public class DataInitializer {

// @Autowired
// private UserService userService;

// @Autowired
// private PasswordEncoder passwordEncoder;

// @Bean
// public CommandLineRunner initAdminUser() {
// return args -> {
// String adminEmail = "gevaerrd@hotmail.com";
// if (userService.findByEmail(adminEmail) == null) {
// User admin = new User();
// admin.setName("Roger & Leo Admin");
// admin.setEmail(adminEmail);
// admin.setPassword(passwordEncoder.encode("Belavistalisboa04"));
// admin.setRole("ADMIN");
// userService.save(admin);
// }
// };
// }
// }