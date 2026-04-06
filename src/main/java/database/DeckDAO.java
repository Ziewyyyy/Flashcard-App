package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeckDAO {

    public static int insertDeck(String name) {
        String sql = "INSERT INTO decks(name) VALUES (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteDeck(int deckId){
        String deleteCards = "DELETE FROM cards WHERE deck_id = ?";
        String deleteDeck = "DELETE FROM decks WHERE id = ?";

        try(Connection conn = Database.getConnection()) {

            try(PreparedStatement stmt1 = conn.prepareStatement(deleteCards)) {
                stmt1.setInt(1, deckId);
                stmt1.executeUpdate();
            }

            try(PreparedStatement stmt2 = conn.prepareStatement(deleteDeck)) {
                stmt2.setInt(1, deckId);
                stmt2.executeUpdate();
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Object[]> getAllDecks() {
        List<Object[]> list = new ArrayList<>();

        String sql = """
            SELECT d.id, d.name, COUNT(c.id) AS amount
            FROM decks d
            LEFT JOIN cards c ON d.id = c.deck_id
            GROUP BY d.id
        """;

        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                list.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        0,
                        "⚙"
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void updateLearned(int deckId, int learned) {
        String sql = "UPDATE decks SET learned = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, learned);
            stmt.setInt(2, deckId);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object[] getDeckById(int deckId) {
        String sql = """
        SELECT d.id, d.name, COUNT(c.id) AS amount, d.learned
        FROM decks d
        LEFT JOIN cards c ON d.id = c.deck_id
        WHERE d.id = ?
        GROUP BY d.id
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("amount"),
                        rs.getInt("learned")
                };
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}