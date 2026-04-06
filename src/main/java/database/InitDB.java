    package database;
    import java.sql.Connection;
    import java.sql.Statement;

    public class InitDB {
        public static void init() {
            String decks = """
                    CREATE TABLE IF NOT EXISTS decks (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        learned INTEGER DEFAULT 0
                    );
            """;

            String cards = """
                    CREATE TABLE IF NOT EXISTS cards (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        deck_id INTEGER,
                        front TEXT,
                        back TEXT,
                        FOREIGN KEY(deck_id) REFERENCES decks(id)
                    );
            """;

            try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement()) {
                stmt.execute(decks);
                stmt.execute(cards);
            } catch(Exception e)
            {
                e.printStackTrace();
            }

            String alterDeck = "ALTER TABLE decks ADD COLUMN learned INTEGER DEFAULT 0";
            try (Connection conn = Database.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute(alterDeck);
            } catch (Exception e) {

            }
        }
    }
