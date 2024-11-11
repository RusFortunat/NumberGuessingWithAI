package com.guessNumbersWithAI.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DrawnImages {

    private ArrayList<Double> rawPixelInput;
    private double[] inputVector;

    public DrawnImages(){
        this.rawPixelInput = new ArrayList<>();
        inputVector = new double[28*28]; // MNIST training dataset consists of 28x28 pixel images.
    }


    // compress 280x280 pixel image to 28x28 one
    public double[] processRawInput(){

        // our new 28x28 image
        for(int Y = 0; Y < 28; Y++) {
            for(int X = 0; X < 28; X++){

                double avergePixelValue = 0;

                // 10x10 pixel square that we compress into a single pixel
                for(int x = 0; x < 10; x++){
                    for(int y = 0; y < 10; y++) {

                        // it looks a bit complicated, but this is how 280x280 list maps to 28x28 image, where each
                        // new pixel is an average of 100 pixels in 10x10 square
                        int index = 280 * (10 * Y + y) + 10 * X + x;

                        avergePixelValue += rawPixelInput.get(index) / 100.0;
                    }
                }

                int inputIndex = 28 * Y + X;
                inputVector[inputIndex] = avergePixelValue;
            }
        }

        return inputVector;
    }

    // ==========================================================
    // CRUD methods;  I don't think I need Update in this API
    // ==========================================================

    public void saveImageToDB() throws SQLException {

        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO DrawnImages (pixelData) VALUES (?)");

            String inputVectorStr = Arrays.toString(inputVector);

            stmt.setString(1, inputVectorStr);
            stmt.executeUpdate();
        }
    }


    public ArrayList<String> readAllImages() throws SQLException{

        ArrayList<String> savedImages = new ArrayList<>();

        try (Connection connection = createConnectionAndEnsureDatabase();
             ResultSet images = connection.prepareStatement("SELECT * FROM DrawnImages").executeQuery()) {

            while (images.next()) {
                savedImages.add("Image id: " + images.getInt("id")
                        + "; pixel data: " + images.getString("pixelData"));
            }
        }

        return savedImages;
    }


    public String readSingleImage(int id) throws SQLException{

        String selectedImage = "";

        try (Connection connection = createConnectionAndEnsureDatabase()){

            ResultSet image = connection.prepareStatement(
                    "SELECT * FROM DrawnImages WHERE id = " + id).executeQuery();

            selectedImage = "Image id: " + image.getInt("id")
                    + "; pixel data: " + image.getString("pixelData");
        }

        return selectedImage;
    }


    public void delete(int id) throws SQLException {
        try (Connection connection = createConnectionAndEnsureDatabase()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM DrawnImages WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // establishes connection
    private Connection createConnectionAndEnsureDatabase() throws SQLException {

        String dbPath = "jdbc:h2:./src/main/resources/image-database";

        Connection conn = DriverManager.getConnection(dbPath, "sa", "");

        try {

            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " +
                    "DrawnImages (id int auto_increment primary key, pixelData varchar(10000))").execute();

        } catch (SQLException t) {
            System.out.println(t.getMessage());
        }

        return conn;
    }

    // getters and setters

    public ArrayList<Double> getRawPixelInput() {
        return rawPixelInput;
    }

    public void setRawPixelInput(ArrayList<Double> rawPixelInput) {
        this.rawPixelInput = rawPixelInput;
    }
}
