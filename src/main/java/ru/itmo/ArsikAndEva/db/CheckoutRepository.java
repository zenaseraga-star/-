package ru.itmo.ArsikAndEva.db;

import ru.itmo.ArsikAndEva.model.Checkout;
import ru.itmo.ArsikAndEva.model.enums.ReturnCondition;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CheckoutRepository {

    public long insert(Checkout c) throws SQLException {
        String sql = "INSERT INTO checkouts "
                + "(instrument_id, username, comment, taken_at, returned_at, return_condition, owner_id, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, c.getInstrumentId());
            ps.setString(2, c.getUsername());
            ps.setString(3, c.getComment());
            ps.setTimestamp(4, Timestamp.from(c.getTakenAt()));
            // returned_at и return_condition при создании обычно null
            if (c.getReturnedAt() != null) ps.setTimestamp(5, Timestamp.from(c.getReturnedAt()));
            else ps.setNull(5, Types.TIMESTAMP);
            if (c.getReturnCondition() != null) ps.setString(6, c.getReturnCondition().name());
            else ps.setNull(6, Types.VARCHAR);
            if (c.getOwnerId() != null) ps.setLong(7, c.getOwnerId());
            else ps.setNull(7, Types.BIGINT);
            ps.setTimestamp(8, Timestamp.from(c.getCreatedAt()));
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    public List<Checkout> findAll() throws SQLException {
        String sql = "SELECT * FROM checkouts ORDER BY id";
        List<Checkout> result = new ArrayList<>();
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public void update(Checkout c) throws SQLException {
        String sql = "UPDATE checkouts "
                + "SET instrument_id = ?, username = ?, comment = ?, taken_at = ?, "
                + "    returned_at = ?, return_condition = ?, owner_id = ? "
                + "WHERE id = ?";
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, c.getInstrumentId());
            ps.setString(2, c.getUsername());
            ps.setString(3, c.getComment());
            ps.setTimestamp(4, Timestamp.from(c.getTakenAt()));
            if (c.getReturnedAt() != null) ps.setTimestamp(5, Timestamp.from(c.getReturnedAt()));
            else ps.setNull(5, Types.TIMESTAMP);
            if (c.getReturnCondition() != null) ps.setString(6, c.getReturnCondition().name());
            else ps.setNull(6, Types.VARCHAR);
            if (c.getOwnerId() != null) ps.setLong(7, c.getOwnerId());
            else ps.setNull(7, Types.BIGINT);
            ps.setLong(8, c.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection cn = Database.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM checkouts WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Checkout map(ResultSet rs) throws SQLException {
        Timestamp returned = rs.getTimestamp("returned_at");
        String rc = rs.getString("return_condition");
        return new Checkout(
                rs.getLong("id"),
                rs.getLong("instrument_id"),
                rs.getString("username"),
                rs.getString("comment"),
                rs.getTimestamp("taken_at").toInstant(),
                returned != null ? returned.toInstant() : null,
                rc != null ? ReturnCondition.valueOf(rc) : null,
                (Long) rs.getObject("owner_id"),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}