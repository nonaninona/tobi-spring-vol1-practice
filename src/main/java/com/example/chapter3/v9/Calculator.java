package com.example.chapter3.v9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    private int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            return callback.doSomethingWithCallBack(br);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public int calcSum(String filePath) throws IOException {
        BufferedReaderCallback sumCallBack = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithCallBack(BufferedReader br) throws IOException {
                int total = 0;
                String line = null;
                while ((line = br.readLine()) != null) {
                    int num = Integer.parseInt(line);
                    total += num;
                }
                return total;
            }
        };
        return fileReadTemplate(filePath, sumCallBack);
    }

    public int calcMul(String filePath) throws IOException {
        BufferedReaderCallback mulCallBack = new BufferedReaderCallback() {
            @Override
            public Integer doSomethingWithCallBack(BufferedReader br) throws IOException {
                int total = 1;
                String line = null;
                while ((line = br.readLine()) != null) {
                    int num = Integer.parseInt(line);
                    total *= num;
                }
                return total;
            }
        };
        return fileReadTemplate(filePath, mulCallBack);
    }
}
