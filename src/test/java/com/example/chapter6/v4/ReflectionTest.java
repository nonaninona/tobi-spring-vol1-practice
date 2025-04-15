package com.example.chapter6.v4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


public class ReflectionTest {
    @Test
    @DisplayName("리플렉션 학습 테스트")
    void testReflectionAPI() throws Exception {
        // given
        String name = "이현희";

        // when
        Method lengthMethod = String.class.getMethod("length");
        Method charAtMethod = String.class.getMethod("charAt", int.class);

        int length = (int) lengthMethod.invoke(name);
        char character = (char) charAtMethod.invoke(name, 1);

        // then
        assertThat(name.length()).isEqualTo(3);
        assertThat(length).isEqualTo(3);

        assertThat(name.charAt(1));
        assertThat(character).isEqualTo('현');
    }
}
