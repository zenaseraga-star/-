package ru.itmo.ArsikAndEva.db;

import ru.itmo.ArsikAndEva.model.Instrument;
import ru.itmo.ArsikAndEva.model.enums.InstrumentStatus;
import ru.itmo.ArsikAndEva.model.enums.InstrumentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class InstrumentRepository {

    public long insert(Instrument it) throws SQLException {
        String sql = "INSERT INTO instruments (name, type, inventory_number, location, status, owner_id) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, it.getName());
            ps.setString(2, it.getType().name());
            ps.setString(3, it.getInventoryNumber());
            ps.setString(4, it.getLocation());
            ps.setString(5, it.getStatus().name());
            if (it.getOwnerId() != null) {
                ps.setLong(6, it.getOwnerId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    public List<Instrument> findAll() throws SQLException {
        String sql = "SELECT * FROM instruments ORDER BY id";
        List<Instrument> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public void update(Instrument it) throws SQLException {
        String sql = "UPDATE instruments "
                + "SET name = ?, type = ?, inventory_number = ?, location = ?, status = ?, updated_at = now() "
                + "WHERE id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, it.getName());
            ps.setString(2, it.getType().name());
            ps.setString(3, it.getInventoryNumber());
            ps.setString(4, it.getLocation());
            ps.setString(5, it.getStatus().name());
            ps.setLong(6, it.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM instruments WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Instrument map(ResultSet rs) throws SQLException {
        Instrument it = new Instrument(
                rs.getString("name"),
                InstrumentType.valueOf(rs.getString("type")),
                rs.getString("inventory_number"),
                rs.getString("location"),
                InstrumentStatus.valueOf(rs.getString("status")),
                (Long) rs.getObject("owner_id")
        );
        it.setId(rs.getLong("id"));
        Timestamp upd = rs.getTimestamp("updated_at");
        if (upd != null) it.setUpdatedAt(upd.toInstant());
        return it;
    }
}
