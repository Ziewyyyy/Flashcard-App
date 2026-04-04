package database;

import java.sql.*;

public class DeckDAO {

    public static int insertDeck(String name) {
        String sql = "INSERT INTO decks(name) VALUES (?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void deleteDeck(int deckId){
        String deleteCards = "DELETE FROM cards WHERE deck_id = ?";
        String deleteDeck = "DELETE FROM decks WHERE id = ?";

        try(Connection conn = Database.getConnection())
        {
            try(PreparedStatement stmt1 = conn.prepareStatement(deleteCards)) {
                stmt1.setInt(1, deckId);
                stmt1.executeUpdate();
            }

            try(PreparedStatement stmt2 = conn.prepareStatement(deleteDeck))
            {
                stmt2.setInt(1, deckId);
                stmt2.executeUpdate();
            }
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static java.util.List<Object[]> getAllDecks() {
        java.util.List<Object[]> list = new java.util.ArrayList<>();

        String sql = """
            SELECT d.id, d.name, COUNT(c.id) AS amount
            FROM decks d
            LEFT JOIN cards c ON d.id = c.deck_id
            GROUP BY d.id
        """;

        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            while(rs.next())
            {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int amount = rs.getInt("amount");
                list.add(new Object[]{id, name, amount, 0, "⚙"});
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}