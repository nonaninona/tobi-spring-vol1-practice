package com.example.chapter3.v10;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
    private Calculator calculator;
    private String filePath;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        filePath = "src/main/resources/chapter3/numbers.txt";
    }


    @Test
    void calcSumTest() throws IOException {
        assertThat(calculator.calcSum(filePath)).isEqualTo(9);
    }

    @Test
    void calcMulTest() throws IOException {
        assertThat(calculator.calcMul(filePath)).isEqualTo(24);
    }
}
