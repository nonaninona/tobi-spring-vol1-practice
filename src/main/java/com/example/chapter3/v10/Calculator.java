package com.example.chapter3.v10;

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

    private <T> T lineReadTemplate(String filePath, LineCallBack<T> callBack, T initVal) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            T total = initVal;
            String line = null;
            while ((line = br.readLine()) != null) {
                total = callBack.doSomethingWithLine(line, total);
            }
            return total;
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
        LineCallBack<Integer> sumCallBack = new LineCallBack<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.parseInt(line) + value;
            }
        };
        return lineReadTemplate(filePath, sumCallBack, 0);
    }

    public int calcMul(String filePath) throws IOException {
        LineCallBack<Integer> mulCallBack = new LineCallBack<Integer>() {
            @Override
            public Integer doSomethingWithLine(String line, Integer value) {
                return Integer.parseInt(line) * value;
            }
        };
        return lineReadTemplate(filePath, mulCallBack, 1);
    }

    public String concentrate(String filePath) throws IOException {
        LineCallBack<String> sumCallBack = new LineCallBack<String>() {
            @Override
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };
        return lineReadTemplate(filePath, sumCallBack, "");
    }
}
