package service;

import dto.UserDto;
import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    public boolean save(UserDto dto) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getRole());
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public UserDto findById(int id) {
        String sql = "SELECT id, username, password, role FROM users WHERE id = ?";

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserDto findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

        String sql = "SELECT id, username, password, role FROM users WHERE username = ?";

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username.trim());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<UserDto> findAll() {
        List<UserDto> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role FROM users ORDER BY id";

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                users.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, UserDto dto) {
        String sql = """
                UPDATE users
                SET username = ?,
                    password = ?,
                    role = ?
                WHERE id = ?
                """;

        try (Connection connection = JdbcUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, dto.getUsername());
            statement.setString(2, dto.getPassword());
            statement.setString(3, dto.getRole());
            statement.setInt(4, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserDto mapRow(ResultSet rs) throws SQLException {
        return new UserDto(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"));
    }
}
