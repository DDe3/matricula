package com.ing_software.forms;

import com.ing_software.Principal;
import com.ing_software.abstraccion.Case;
import com.ing_software.abstraccion.Result;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Representante;
import com.ing_software.servicios.CuentaUtilities;
import com.ing_software.servicios.EstudianteUtilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ing_software.abstraccion.Case.defaultCase;
import static com.ing_software.abstraccion.Case.mcase;
import static com.ing_software.abstraccion.Result.failure;
import static com.ing_software.abstraccion.Result.success;

import static com.ing_software.utils.Patterns.*;

public class GestionarAlumno extends JFrame {


    EstudianteUtilities servicioEstudiante = Principal.se.select(EstudianteUtilities.class).get();
    CuentaUtilities servicioCuenta = Principal.se.select(CuentaUtilities.class).get();


    private JButton salirButton;
    private JTabbedPane tabbedPane1;
    private JButton nuevoButton;
    private JButton modificarButton;
    private JButton cancelarButton;
    private JButton buscarButton;
    private JButton registrarButton;
    private JButton eliminarButton;
    private JTextField rep_cdi_jtext;
    private JTextField rep_nombre_jtext;
    private JTextField rep_telf_jtext;
    private JTextField rep_correo_jtext;
    private JTabbedPane tabbedPane2;
    private JTextField est_cdi_jtext;
    private JTextField est_mail_jtext;
    private JTextField est_nombre_jtext;
    private JTextField est_telef_jtext;
    private JList<String> est_matri_list;
    private JTabbedPane tabbedPane3;
    private JTextField cuenta_mail_jtext;
    private JTextField cuenta_password_jtext;
    private JTextField rep_trab_jtext;
    private JPanel gestionarPanel;
    private JScrollPane matriculaScrollPane;

    CompletableFuture<List<Cuenta>> aux;
    List<String> disponibles;


    public GestionarAlumno(String title) throws HeadlessException {

        super(title);
        init();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(gestionarPanel);
        setup();
        this.pack();


        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrar();
            }
        });


        nuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupNuevo();
                clearText();
            }
        });


        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setup();
                clearText();
            }
        });


        registrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String alumno_cdi = est_cdi_jtext.getText();
                    String alumno_nombre = est_nombre_jtext.getText();
                    String alumno_telf = est_telef_jtext.getText();
                    String alumno_mail = est_mail_jtext.getText();

                    String rep_cdi = rep_cdi_jtext.getText();
                    String rep_nombre = rep_nombre_jtext.getText();
                    String rep_telf = rep_telf_jtext.getText();
                    String rep_mail = rep_correo_jtext.getText();
                    String rep_trab = rep_trab_jtext.getText();

                    String cuenta_mail = est_mail_jtext.getText();
                    String cuenta_pass = cuenta_password_jtext.getText();
                    validacion();

                    Result<String> result = Case.match(
                            defaultCase(() -> success("Estudiante Registrado con Exito!")),
                            mcase(() -> alumno_cdi.equals("") || !cdiPattern.matcher(alumno_cdi).matches(), () -> failure("CDI de Estudiante no valido")),
                            mcase(() -> alumno_nombre.equals(""), () -> failure("Nombre de estudiante vacio")),
                            mcase(() -> alumno_telf.equals("") || !telefonoPattern.matcher(alumno_telf).matches(), () -> failure("Telefono de estudiante no valido")),
                            mcase(() -> alumno_mail.equals("") || !emailPattern.matcher(alumno_mail).matches(), () -> failure("Mail de estudiante no valido")),
                            // Representante
                            mcase(() -> rep_cdi.equals("") || !cdiPattern.matcher(rep_cdi).matches(), () -> failure("CDI de representante no valido")),
                            mcase(() -> rep_nombre.equals(""), () -> failure("Nombre de representante vacio")),
                            mcase(() -> rep_telf.equals("") || !telefonoPattern.matcher(rep_telf).matches(), () -> failure("Telefono de representante no valido")),
                            mcase(() -> rep_mail.equals("") || !emailPattern.matcher(rep_mail).matches(), () -> failure("Mail de representante no valido")),
                            // Cuenta
                            mcase(() -> !passwordPattern.matcher(cuenta_pass).matches(), () -> failure("La password debe contener: " +
                                    "\n-Un numero" +
                                    "\n-Una mayuscula y una minuscula" +
                                    "\n-Al menos 8 caracteres")),
                            mcase(() -> disponibles.contains(alumno_mail), () -> failure("El correo de estudiante ya esta registrado"))
                    );

                    result.bind(x ->
                    {
                        Representante rep = new Representante();
                        rep.setCedula(rep_cdi);
                        rep.setNombre(rep_nombre);
                        rep.setTelefono(rep_telf);
                        rep.setMail(rep_mail);
                        rep.setLugarTrabajo(rep_trab);

                        Cuenta cuenta = new Cuenta();
                        cuenta.setNombre(cuenta_mail);
                        cuenta.setPassword(cuenta_pass);
                        servicioEstudiante.crearEstudiante(alumno_cdi, alumno_nombre, alumno_telf, rep, cuenta);
                        clearText();
                        JOptionPane.showMessageDialog(null, x);
                    }, x -> JOptionPane.showMessageDialog(null, x));


                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //try {
                    clearText();
                    camposHabilitados(true);
                    String cdi = JOptionPane.showInputDialog("Ingrese el cdi del estudiante");
                    if (!cdi.equals(null)) {
                        Estudiante estudiante = servicioEstudiante.findByCdi(cdi).get();
                        buscarAux(est_cdi_jtext, estudiante.getCedula());
                        buscarAux(est_nombre_jtext, estudiante.getNombre());
                        buscarAux(est_telef_jtext, estudiante.getTelefono());
                        buscarAux(est_mail_jtext, estudiante.getMail());

                        DefaultListModel<String> model = new DefaultListModel<>();
                        if (estudiante.getMatriculasRegistradas() != null) {
                            estudiante.getMatriculasRegistradas().forEach(x -> model.addElement(x.toString()));
                        }
                        est_matri_list.setModel(model);


                        if (!(estudiante.getRepresentante() == null)) {
                            buscarAux(rep_cdi_jtext, estudiante.getRepresentante().getCedula());
                            buscarAux(rep_nombre_jtext, estudiante.getRepresentante().getNombre());
                            buscarAux(rep_telf_jtext, estudiante.getRepresentante().getTelefono());
                            buscarAux(rep_correo_jtext, estudiante.getRepresentante().getMail());
                            buscarAux(rep_trab_jtext, estudiante.getRepresentante().getLugarTrabajo());
                        }

                        // Cuenta
                        buscarAux(cuenta_mail_jtext, estudiante.getMail());
                        buscarAux(cuenta_password_jtext, estudiante.getCuenta().getPassword());

                        camposEditables(true);
                        est_cdi_jtext.setEditable(false);
                        est_mail_jtext.setEditable(false);
                        cuenta_mail_jtext.setEditable(false);

                        cancelarButton.setEnabled(true);
                        eliminarButton.setEnabled(true);
                        modificarButton.setEnabled(true);

                        nuevoButton.setEnabled(false);
                        buscarButton.setEnabled(false);
                        registrarButton.setEnabled(false);
                    }
//                } catch (Exception n) {
//                    JOptionPane.showMessageDialog(null,
//                            "Error: " + n,
//                            "Error de Busqueda",
//                            JOptionPane.ERROR_MESSAGE);
//
//                }

            }
        });
        eliminarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Estudiante est = servicioEstudiante.findByCdi(est_cdi_jtext.getText()).get();

                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar al estudiante " + est.getNombre() + " del sistema?", "Warning", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        servicioEstudiante.borrarEstudiante(est);
                    }
                    setup();
                    JOptionPane.showMessageDialog(null, "Se elimino al estudiante del sistema");
                } catch (Exception ignored) {
                    JOptionPane.showMessageDialog(null, "No se pudo borrar al estudiante");
                }
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Esta seguro que desea modificar al estudiante", "Warning", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        String alumno_nombre = est_nombre_jtext.getText();
                        String alumno_telf = est_telef_jtext.getText();
                        String alumno_mail = est_mail_jtext.getText();
                        String rep_cdi = rep_cdi_jtext.getText();
                        String rep_nombre = rep_nombre_jtext.getText();
                        String rep_telf = rep_telf_jtext.getText();
                        String rep_mail = rep_correo_jtext.getText();
                        String rep_trab = rep_trab_jtext.getText();
                        String cuenta_pass = cuenta_password_jtext.getText();


                        Result<String> result = Case.match(
                                defaultCase(() -> success("Estudiante modificado con exito!")),
                                mcase(() -> alumno_nombre.equals(""), () -> failure("Nombre de estudiante vacio")),
                                mcase(() -> alumno_telf.equals("") || !telefonoPattern.matcher(alumno_telf).matches(), () -> failure("Telefono de estudiante no valido")),
                                mcase(() -> alumno_mail.equals("") || !emailPattern.matcher(alumno_mail).matches(), () -> failure("Mail de estudiante no valido")),
                                // Representante
                                mcase(() -> rep_cdi.equals("") || !cdiPattern.matcher(rep_cdi).matches(), () -> failure("CDI de representante no valido")),
                                mcase(() -> rep_nombre.equals(""), () -> failure("Nombre de representante vacio")),
                                mcase(() -> rep_telf.equals("") || !telefonoPattern.matcher(rep_telf).matches(), () -> failure("Telefono de representante no valido")),
                                mcase(() -> rep_mail.equals("") || !emailPattern.matcher(rep_mail).matches(), () -> failure("Mail de representante no valido")),
                                // Cuenta
                                mcase(() -> !passwordPattern.matcher(cuenta_pass).matches(), () -> failure("La password debe contener: " +
                                        "\n-Un numero" +
                                        "\n-Una mayuscula y una minuscula" +
                                        "\n-Al menos 8 caracteres")));


                        result.bind(x ->
                        {
                            Estudiante est = servicioEstudiante.findByCdi(est_cdi_jtext.getText()).get();
                            Representante rep = new Representante();

                            est.setNombre(est_nombre_jtext.getText());
                            est.setTelefono(est_telef_jtext.getText());

                            rep.setCedula(rep_cdi);
                            rep.setNombre(rep_nombre);
                            rep.setTelefono(rep_telf);
                            rep.setMail(rep_mail);
                            rep.setLugarTrabajo(rep_trab);

                            est.setRepresentante(rep);
                            est.getCuenta().setPassword(cuenta_password_jtext.getText());

                            servicioEstudiante.merge(est);
                            JOptionPane.showMessageDialog(null, x);
                            setup();
                            clearText();

                        }, x -> JOptionPane.showMessageDialog(null, x));


                    }

                } catch (Exception notIgnored) {
                    JOptionPane.showMessageDialog(null, "No se pudo modificar al estudiante\nError: " + notIgnored);
                }
            }
        });
    }

    private void cerrar() {
        this.dispose();
    }

    private void init() {
        aux = servicioCuenta.nombresDisponibles();
    }

    private void validacion() throws ExecutionException, InterruptedException {
        disponibles = (List<String>) aux.get().stream()
                .map(Cuenta::getNombre)
                .collect(Collectors.toList());
    }

    private void setup() {

        clearText();
        camposHabilitados(false);
        camposEditables(false);


        registrarButton.setEnabled(false);
        eliminarButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        nuevoButton.setEnabled(true);
        buscarButton.setEnabled(true);
    }

    private void setupNuevo() {

        camposHabilitados(true);
        camposEditables(true);

        registrarButton.setEnabled(true);
        cancelarButton.setEnabled(true);

        eliminarButton.setEnabled(false);
        buscarButton.setEnabled(false);
        modificarButton.setEnabled(false);
        nuevoButton.setEnabled(false);

    }

    private void clearText() {
        est_cdi_jtext.setText("");
        est_nombre_jtext.setText("");
        est_telef_jtext.setText("");
        est_mail_jtext.setText("");
        rep_cdi_jtext.setText("");
        rep_nombre_jtext.setText("");
        rep_telf_jtext.setText("");
        rep_correo_jtext.setText("");
        rep_trab_jtext.setText("");
        cuenta_mail_jtext.setText("");
        cuenta_password_jtext.setText("");
        est_matri_list.setModel(new DefaultListModel<>());
    }

    private void camposEditables(Boolean op) {
        est_cdi_jtext.setEditable(op);
        est_nombre_jtext.setEditable(op);
        est_telef_jtext.setEditable(op);
        est_mail_jtext.setEditable(op);
        rep_cdi_jtext.setEditable(op);
        rep_nombre_jtext.setEditable(op);
        rep_telf_jtext.setEditable(op);
        rep_correo_jtext.setEditable(op);
        rep_trab_jtext.setEditable(op);
        cuenta_mail_jtext.setEditable(op);
        cuenta_password_jtext.setEditable(op);
    }

    private void camposHabilitados(Boolean op) {
        est_cdi_jtext.setEnabled(op);
        est_nombre_jtext.setEnabled(op);
        est_telef_jtext.setEnabled(op);
        est_mail_jtext.setEnabled(op);
        rep_cdi_jtext.setEnabled(op);
        rep_nombre_jtext.setEnabled(op);
        rep_telf_jtext.setEnabled(op);
        rep_correo_jtext.setEnabled(op);
        rep_trab_jtext.setEnabled(op);
        cuenta_mail_jtext.setEnabled(op);
        cuenta_password_jtext.setEnabled(op);
    }

    private void buscarAux(JTextField j, String s) {
        if (s == null) {
            j.setText("");
        } else {
            j.setText(s);
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        gestionarPanel = new JPanel();
        gestionarPanel.setLayout(new GridLayoutManager(10, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("GESTIONAR ALUMNO");
        gestionarPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(134, 16), null, 0, false));
        final Spacer spacer1 = new Spacer();
        gestionarPanel.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        nuevoButton = new JButton();
        nuevoButton.setText("Nuevo");
        gestionarPanel.add(nuevoButton, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificarButton = new JButton();
        modificarButton.setText("Modificar");
        gestionarPanel.add(modificarButton, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        gestionarPanel.add(cancelarButton, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buscarButton = new JButton();
        buscarButton.setText("Buscar");
        gestionarPanel.add(buscarButton, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarButton = new JButton();
        registrarButton.setText("Registrar");
        gestionarPanel.add(registrarButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        eliminarButton = new JButton();
        eliminarButton.setText("Eliminar");
        gestionarPanel.add(eliminarButton, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane1 = new JTabbedPane();
        tabbedPane1.setEnabled(true);
        gestionarPanel.add(tabbedPane1, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Representante", panel1);
        rep_cdi_jtext = new JTextField();
        rep_cdi_jtext.setEnabled(true);
        rep_cdi_jtext.setText("");
        panel1.add(rep_cdi_jtext, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_nombre_jtext = new JTextField();
        rep_nombre_jtext.setEnabled(true);
        rep_nombre_jtext.setText("");
        panel1.add(rep_nombre_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_trab_jtext = new JTextField();
        rep_trab_jtext.setEnabled(true);
        rep_trab_jtext.setText("");
        panel1.add(rep_trab_jtext, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_telf_jtext = new JTextField();
        rep_telf_jtext.setEditable(true);
        rep_telf_jtext.setEnabled(true);
        panel1.add(rep_telf_jtext, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_correo_jtext = new JTextField();
        rep_correo_jtext.setEnabled(true);
        rep_correo_jtext.setText("");
        panel1.add(rep_correo_jtext, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("CDI");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Nombre");
        panel1.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Telefono");
        panel1.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Direccion Trabajo");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Correo");
        panel1.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane2 = new JTabbedPane();
        gestionarPanel.add(tabbedPane2, new GridConstraints(1, 0, 6, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Estudiante", panel2);
        est_cdi_jtext = new JTextField();
        est_cdi_jtext.setText("");
        panel2.add(est_cdi_jtext, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("CDI");
        panel2.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        est_nombre_jtext = new JTextField();
        est_nombre_jtext.setText("");
        panel2.add(est_nombre_jtext, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        est_telef_jtext = new JTextField();
        est_telef_jtext.setText("");
        panel2.add(est_telef_jtext, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Nombre");
        panel2.add(label8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Telefono");
        panel2.add(label9, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Correo");
        panel2.add(label10, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Matriculas");
        panel2.add(label11, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        matriculaScrollPane = new JScrollPane();
        panel2.add(matriculaScrollPane, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        est_matri_list = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        est_matri_list.setModel(defaultListModel1);
        matriculaScrollPane.setViewportView(est_matri_list);
        est_mail_jtext = new JTextField();
        est_mail_jtext.setText("");
        panel2.add(est_mail_jtext, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        tabbedPane3 = new JTabbedPane();
        gestionarPanel.add(tabbedPane3, new GridConstraints(2, 2, 5, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane3.addTab("Cuenta", panel3);
        cuenta_mail_jtext = new JTextField();
        cuenta_mail_jtext.setText("");
        panel3.add(cuenta_mail_jtext, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cuenta_password_jtext = new JTextField();
        panel3.add(cuenta_password_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Mail");
        panel3.add(label12, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Password");
        panel3.add(label13, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        salirButton = new JButton();
        salirButton.setText("Salir");
        gestionarPanel.add(salirButton, new GridConstraints(8, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return gestionarPanel;
    }

}
