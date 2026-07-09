package com.faculty.main;

import com.faculty.view.LoginView;

public class Main {
    public static void main(String[] args) {
        // Enforce thread-safe UI creation environments
        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}