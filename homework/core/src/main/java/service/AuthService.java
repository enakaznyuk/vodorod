package service;

import dto.UserDto;

public class AuthService {

    private static final String ROLE_CLIENT = "CLIENT";

    private final UserService userService = new UserService();

    public UserDto login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        UserDto user = userService.findByUsername(username.trim());
        if (user != null && password.equals(user.getPassword())) {
            return user;
        }
        return null;
    }

    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }

        String normalizedUsername = username.trim();
        if (userService.findByUsername(normalizedUsername) != null) {
            return false;
        }

        UserDto dto = new UserDto();
        dto.setUsername(normalizedUsername);
        dto.setPassword(password);
        dto.setRole(ROLE_CLIENT);
        return userService.save(dto);
    }
}
