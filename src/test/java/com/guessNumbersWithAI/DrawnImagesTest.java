package com.guessNumbersWithAI;

import com.guessNumbersWithAI.model.DrawnImages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.DoubleStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DrawnImagesTest {

    DrawnImages drawnImages;

    @BeforeEach
    public void setupDrawnImages(){
        StringBuilder rawImageData = new StringBuilder();
        rawImageData.append(Math.random());
        for(int pixel = 0; pixel < 280*280-1; pixel++){
            rawImageData.append(",").append(Math.random());
        }

        drawnImages = new DrawnImages(rawImageData.toString());
    }

    @DisplayName("Test non-standard constructor")
    @Test
    public void testDrawnImagesConstructor(){

        // check rawPixelInput size, should be 280x280
        double[] rawPixelInput = drawnImages.getRawPixelInput();
        assertEquals(280*280, rawPixelInput.length,
                "rawPixelInput size must be 280x280 = 78400 pixels");

        // test that rawPixelInput is not empty
        boolean containsNonZeroElements = DoubleStream.of(rawPixelInput).anyMatch(x -> x != 0);
        assertTrue(containsNonZeroElements, "rawPixelInput cannot be empty");
    }

    @DisplayName("Test processRawInput() method")
    @Test
    public void testProcessRawInput(){

        drawnImages.processRawInput();

        // check that produced inputVector is not empty
        double[] inputVector = drawnImages.getInputVector();
        assertEquals(28*28, inputVector.length,
                "inputVector size must be 28x28 = 784 pixels");

        // test that rawPixelInput is not empty
        boolean containsNonZeroElements = DoubleStream.of(inputVector).anyMatch(x -> x != 0);
        assertTrue(containsNonZeroElements, "inputVector cannot be empty");

        // check that elements of inputVector are in fact obtained from compressing rawPixelInput
        double avergePixelValue = 0;
        double[] rawPixelInput = drawnImages.getRawPixelInput();
        // pick random pixel of compressed image
        int Y = 10;
        int X = 5;
        for(int x = 0; x < 10; x++){
            for(int y = 0; y < 10; y++) {
                int index = 280 * (10 * Y + y) + 10 * X + x;
                avergePixelValue += rawPixelInput[index] / 100.0;
            }
        }
        int inputIndex = 28 * Y + X;

        assertEquals(avergePixelValue, inputVector[inputIndex],
                "inputVector values must come from compressing rawPixelInput");

    }

    // 



}
