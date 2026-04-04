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
}
