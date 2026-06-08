package ru.itmo.ArsikAndEva.db;

import ru.itmo.ArsikAndEva.model.Booking;
import ru.itmo.ArsikAndEva.model.enums.BookingStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public long insert(Booking b) throws SQLException {
        String sql = "INSERT INTO bookings (instrument_id, start_at, end_at, status, owner_id, owner_name) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, b.getInstrumentId());
            ps.setTimestamp(2, Timestamp.from(b.getStartAt()));
            ps.setTimestamp(3, Timestamp.from(b.getEndAt()));
            ps.setString(4, b.getStatus().name());
            if (b.getOwnerId() != null) ps.setLong(5, b.getOwnerId());
            else ps.setNull(5, Types.BIGINT);
            ps.setString(6, b.getOwnerName());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    public List<Booking> findAll() throws SQLException {
        String sql = "SELECT * FROM bookings ORDER BY id";
        List<Booking> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.add(map(rs));
        }
        return result;
    }

    public void update(Booking b) throws SQLException {
        String sql = "UPDATE bookings "
                + "SET instrument_id = ?, start_at = ?, end_at = ?, status = ?, owner_id = ?, owner_name = ?, updated_at = now() "
                + "WHERE id = ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, b.getInstrumentId());
            ps.setTimestamp(2, Timestamp.from(b.getStartAt()));
            ps.setTimestamp(3, Timestamp.from(b.getEndAt()));
            ps.setString(4, b.getStatus().name());
            if (b.getOwnerId() != null) ps.setLong(5, b.getOwnerId());
            else ps.setNull(5, Types.BIGINT);
            ps.setString(6, b.getOwnerName());
            ps.setLong(7, b.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM bookings WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    private Booking map(ResultSet rs) throws SQLException {
        Timestamp upd = rs.getTimestamp("updated_at");
        return new Booking(
                rs.getLong("id"),
                rs.getLong("instrument_id"),
                rs.getTimestamp("start_at").toInstant(),
                rs.getTimestamp("end_at").toInstant(),
                BookingStatus.valueOf(rs.getString("status")),
                (Long) rs.getObject("owner_id"),
                rs.getTimestamp("created_at").toInstant(),
                upd != null ? upd.toInstant() : null,
                rs.getString("owner_name")
        );
    }
}