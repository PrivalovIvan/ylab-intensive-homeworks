package com.ylab.homework_1;

import com.ylab.homework_1.ui.ConsoleApp;

public class Homework1Application {
    public static void main(String[] args) {
        try {
            ConsoleApp app = new ConsoleApp();
            app.run();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}