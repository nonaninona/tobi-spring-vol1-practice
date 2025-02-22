package com.example.chapter3.v9;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
    @Test
    void calcSumTest() {
        Calculator calculator = new Calculator();
        int sum = 0;
        try {
            sum = calculator.calcSum("numbers.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            Assertions.fail();
        }
        System.out.println("sum = " + sum);
        assertThat(sum).isEqualTo(55);
    }
}
