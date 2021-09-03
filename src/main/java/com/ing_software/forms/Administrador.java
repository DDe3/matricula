package com.ing_software.forms;

import javax.swing.*;
import java.awt.*;

public class Administrador extends JFrame {
    private JPanel adminPanel;
    private JToolBar toolbar;

    private JButton alumno;


    public Administrador(String title) throws HeadlessException {
        super(title);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(adminPanel);
        this.setup();
        this.pack();

    }


    private void setup() {
        this.add(toolbar);
        alumno = new JButton("Alumno");
        toolbar.add(alumno);

    }
}
