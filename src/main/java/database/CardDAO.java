package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    public static void insertCard(int deckId, String front, String back) {
        String sql = "INSERT INTO cards(deck_id, front, back) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            stmt.setString(2, front);
            stmt.setString(3, back);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Object[]> getCardsByDeck(int deckId) {
        List<Object[]> list = new ArrayList<>();

        String sql = "SELECT id, front, back FROM cards WHERE deck_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("front"),
                        rs.getString("back")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void updateCard(int id, String front, String back){
        String sql = "UPDATE cards SET front = ?, back = ? WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, front);
            stmt.setString(2, back);
            stmt.setInt(3, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteCard(int cardId){
        String sql = "DELETE FROM cards WHERE id = ?";

        try(Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cardId);
            stmt.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}