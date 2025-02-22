package com.example.chapter3.v9;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;

public class CalculatorTest {
    private Calculator calculator;
    private String filePath;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        filePath = "numbers.txt";
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
