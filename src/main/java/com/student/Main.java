
package com.student;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.student.view.StudentDashboard;

public class Main {

    public static void main(String[] args) {

        // Set System Look and Feel
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );

        } catch (Exception e) {
            System.err.println("Look and Feel could not be set.");
            e.printStackTrace();
        }

        // Launch Student Dashboard
        SwingUtilities.invokeLater(() -> {

            StudentDashboard dashboard = new StudentDashboard();

            dashboard.setLocationRelativeTo(null);
            dashboard.setVisible(true);

        });
    }
}
