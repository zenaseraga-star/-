package ru.itmo.ArsikAndEva.db;

import ru.itmo.ArsikAndEva.users.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    public long insert(String login, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (login, password) VALUES (?, ?) RETURNING id";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    public Optional<User> findByLogin(String login) throws SQLException {
        String sql = "SELECT id, login, password FROM users WHERE login = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, login, password FROM users ORDER BY id";
        List<User> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                result.add(map(rs));
        }
        return result;
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("login"),
                rs.getString("password"),
                rs.getLong("id")
        );
    }
}