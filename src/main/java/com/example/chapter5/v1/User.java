package com.example.chapter5.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;

    private Level level;
    private int loginCount;
    private int recommendedCount;
}
