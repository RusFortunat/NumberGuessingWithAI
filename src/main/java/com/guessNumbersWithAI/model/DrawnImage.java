package com.guessNumbersWithAI.model;

import java.util.ArrayList;

public class DrawnImage {

    private ArrayList<Double> rawPixelInput;

    public DrawnImage(){
        this.rawPixelInput = new ArrayList<>();
    }


    // MNIST training dataset consists of 28x28 pixel images. This is why I've chosen 280x280
    // pixel canvas, so that I could prepare the input layer for my neural network in this method,
    // by compressing 10x10 pixel squares into 1 pixel
    public double[] processRawInput(){

        double[] inputVector = new double[28*28];

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
                //if(avergePixelValue == 0) System.out.print("0");
                //else System.out.print("1");
            }
            //System.out.println("");
        }

        return inputVector;
    }


    // getters and setters

    public ArrayList<Double> getRawPixelInput() {
        return rawPixelInput;
    }

    public void setRawPixelInput(ArrayList<Double> rawPixelInput) {
        this.rawPixelInput = rawPixelInput;
    }
}
