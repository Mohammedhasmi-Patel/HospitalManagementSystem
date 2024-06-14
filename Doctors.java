package Hospital;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctors {
    private Connection connection;


    public Doctors(Connection connection) {
        this.connection = connection;

    }

    // method for doing addidng patient


    public void viewDoctor() {
        String query = "SELECT * FROM doctors";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Doctors...");

            System.out.println("+--------    +--------------------+--------------+----------------+");
            System.out.println("| Doctor Id | Name                | Specialization  |");
            System.out.println("+--------    +--------------------+--------------+----------------+");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String speciallization = resultSet.getString("speciallization");
                System.out.printf("| %-12s |%-20s| %-13s | %-18s\n",id,name,speciallization);
            }
            System.out.println("+--------    +--------------------+--------------+----------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT * FROM doctors WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}