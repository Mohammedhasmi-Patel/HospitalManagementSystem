package Hospital;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";

    private static final String username = "root";

    private static final String password ="root";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection,scanner);
            Doctors doctor = new Doctors(connection);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM.....");
                System.out.println("1..Add Patient....");
                System.out.println("2...View Patient....");
                System.out.println("3...View Doctor....");
                System.out.println("4.. Book Appointment....");
                System.out.println("5.... Exit");

                System.out.println("Please Enter Your Choice...");

                int choice = scanner.nextInt();

                switch (choice){
                    case 1 :
                        // add patient
                        patient.addPatient();
                        System.out.println();
                    case 2 :
                        // view patient
                        patient.viewpatients();
                        System.out.println();

                    case 3 :
                        // view Doctor
                        doctor.viewDoctor();
                        System.out.println();

                    case 4 :
                        // Book Appointment

                        bookAppoinment(patient,doctor,connection,scanner);
                        System.out.println();


                    case 5 :
                        // Exit
                        return;

                    default:
                        System.out.println("Enter Valid Choice....");
                        System.out.println();
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppoinment(Patient patient,Doctors doctors,Connection connection,Scanner scanner){
        System.out.println("Enter Patient id....");
        int patientId = scanner.nextInt();

        System.out.println("Enter Doctor id....");
        int doctorId = scanner.nextInt();

        System.out.println("Enter Appointment Date (yyyy--mm--dd)");
        String appoinmentDate = scanner.next();

        if(patient.getPatientById(patientId) && doctors.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appoinmentDate,connection)){
                String appointmentQuery = "INSERT INTO appoinments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1,patientId);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appoinmentDate);

                    int affectedRows = preparedStatement.executeUpdate();
                    if(affectedRows>0){
                        System.out.println("Appoinment booked.....");
                    }else{
                        System.out.println("failed to Booked Appointment......");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor Not Available this date.....");
            }
        }else{
            System.out.println("Either Patient or Doctor Doesn't Exit....");
        }




    }

    public static  boolean checkDoctorAvailability(int doctorId,String appoinmentDate,Connection connection){
        String query = "SELECT COUNT(*) FROM appoinments WHERE doctor_id=? AND appointment_date=?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appoinmentDate);

            ResultSet resultset =  preparedStatement.executeQuery();
            if(resultset.next()){
                int count = resultset.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }



        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
