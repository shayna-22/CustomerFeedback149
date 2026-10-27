import java.sql.*;
import java.util.Scanner;

public class CustomerFeedback {
    static Scanner sc = new Scanner(System.in);
    static Connection con;

    public static void main(String[] args) {
        try {
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/customer_feedback",
                "root",
                "MySQL#pass"
            );

            System.out.println("Database connected successfully!");

            int ch;
            do {
                System.out.println("\n--- Customer Feedback System ---");
                System.out.println("1. Add Feedback");
                System.out.println("2. Average Rating");
                System.out.println("3. Show Low Ratings");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");

                ch = sc.nextInt();
                sc.nextLine();

                switch (ch) {
                    case 1:
                        add();
                        break;
                    case 2:
                        avg();
                        break;
                    case 3:
                        low();
                        break;
                    case 4:
                        System.out.println("Thank you!");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } while (ch != 4);

            con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void add() throws SQLException {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        System.out.print("Enter service name: ");
        String srv = sc.nextLine();

        System.out.print("Enter rating (1 to 5): ");
        int r = sc.nextInt();
        sc.nextLine();

        if (r < 1 || r > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        System.out.print("Enter comments: ");
        String com = sc.nextLine();

        String sql = "INSERT INTO feedback (name, service, rating, comments) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, srv);
        ps.setInt(3, r);
        ps.setString(4, com);
        ps.executeUpdate();

        System.out.println("Feedback saved successfully!");
    }

    static void avg() throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT service, AVG(rating) AS avg_rating FROM feedback GROUP BY service"
        );

        while (rs.next()) {
            System.out.println(rs.getString("service") + ": "
                + rs.getDouble("avg_rating"));
        }
    }

    static void low() throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(
            "SELECT name, service, rating, comments FROM feedback WHERE rating < 3"
        );

        while (rs.next()) {
            System.out.println(
                rs.getString("name") + " | " +
                rs.getString("service") + " | Rating: " +
                rs.getInt("rating") + " | " +
                rs.getString("comments")
            );
        }
    }
}