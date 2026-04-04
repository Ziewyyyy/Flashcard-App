package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CardDAO {
    public static void insertCard(int deckId, String front, String back)
    {
        String sql = "INSERT INTO cards(deck_id, front, back) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, deckId);
            stmt.setString(2, front);
            stmt.setString(3, back);

            stmt.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static java.util.List<Object[]> getCardsByDeck(int deckId) {
        java.util.List<Object[]> list = new java.util.ArrayList<>();

        String sql = "SELECT id, front, back FROM cards WHERE deck_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String front = rs.getString("front");
                String back = rs.getString("back");

                list.add(new Object[]{id, front, back});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
