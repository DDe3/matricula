package com.ing_software.forms;

import com.ing_software.Principal;
import com.ing_software.abstraccion.Case;
import com.ing_software.abstraccion.Result;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Representante;
import com.ing_software.servicios.EstudianteUtilities;
import com.ing_software.servicios.ServicioCuenta;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import static com.ing_software.abstraccion.Case.*;
import static com.ing_software.abstraccion.Result.*;
import static com.ing_software.utils.Patterns.*;


public class Signin extends JFrame {


    ServicioCuenta servicioCuenta = Principal.se.select(ServicioCuenta.class).get();
    EstudianteUtilities servicioEstudiante = Principal.se.select(EstudianteUtilities.class).get();


    private JTextField mail_jtext;
    private JButton registrarseButton;
    private JButton regresarButton;
    private JPanel panelSign;
    private JPasswordField passc_jtext;
    private JPasswordField pass_jtext;
    private JTabbedPane tabbedPane1;
    private JTextField alumno_cdi_jtext;
    private JTextField alumno_nombre_jtext;
    private JTextField alumno_telf_jtext;
    private JTextField rep_cdi_jtext;
    private JTextField rep_nombre_jtext;
    private JTextField rep_telf_jtext;
    private JTextField rep_mail_jtext;
    private JTextField rep_trab_jtext;

    CompletableFuture<List<Cuenta>> aux;
    List<String> disponibles = new ArrayList<>();


    public Signin(String title) throws HeadlessException {
        super(title);
        setup();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panelSign);
        this.pack();


        registrarseButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = mail_jtext.getText();  // <--
                String con = passc_jtext.getText(); // <--
                String conConf = pass_jtext.getText();  // <--

                String alumno_cdi = alumno_cdi_jtext.getText();  // <--
                String alumno_name = alumno_nombre_jtext.getText().toUpperCase(Locale.ROOT);  // <--
                String telf = alumno_telf_jtext.getText();

                String rep_cdi = rep_cdi_jtext.getText();        // <--
                String rep_name = rep_nombre_jtext.getText().toUpperCase(Locale.ROOT);    // <--
                String rep_telf = rep_telf_jtext.getText();
                String rep_mail = rep_mail_jtext.getText();      // <--
                String rep_trab = rep_trab_jtext.getText();

                if (disponibles.isEmpty()) {
                    validacion();
                }

                Result<String> result = Case.match(
                        defaultCase(() -> success("Se ha registrado con exito!")),
                        // VACIOS
                        mcase(() -> nom.equals("") || con.equals("") || conConf.equals("") || alumno_cdi.equals("") || alumno_name.equals("") || rep_cdi.equals("") || rep_name.equals("") || rep_mail.equals(""), () -> failure("Los campos con * son obligatorios")),
                        // CUENTA
                        mcase(() -> !emailPattern.matcher(nom).matches(), () -> failure("Correo de Cuenta no valido")),
                        mcase(() -> !passwordPattern.matcher(con).matches(), () -> failure("La password debe contener: " +
                                "\n-Un numero" +
                                "\n-Una mayuscula y una minuscula" +
                                "\n-Al menos 8 caracteres")),
                        mcase(() -> !con.equals(conConf), () -> failure("Confirmacion de password erronea")),
                        mcase(() -> disponibles.contains(nom), () -> failure("El correo de cuenta ingresado ya se encuentra registrado")),
                        // DATOS PERSONALES
                        mcase(() -> !cdiPattern.matcher(alumno_cdi).matches(), () -> failure("CDI de Datos Personales no valido")),
                        // REPRESENTANTE
                        mcase(() -> !cdiPattern.matcher(rep_cdi).matches(), () -> failure("CDI de Representante no valido")),
                        mcase(() -> !emailPattern.matcher(rep_mail).matches(), () -> failure("Correo de Representante no valido"))
                );

                result.bind(x ->
                {

                    Cuenta ctmp = new Cuenta();
                    ctmp.setNombre(nom);
                    ctmp.setPassword(con);

                    Representante rep = new Representante();
                    rep.setCedula(rep_cdi);
                    rep.setNombre(rep_name);
                    rep.setTelefono(rep_telf);
                    rep.setMail(rep_mail);
                    rep.setLugarTrabajo(rep_trab);

                    servicioEstudiante.crearEstudiante(alumno_cdi, alumno_name, telf, rep, ctmp);

                    JOptionPane.showMessageDialog(null, x);
                    goBack();
                }, x -> JOptionPane.showMessageDialog(null, x));

            }
        });
        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
    }


    private void setup() {
        aux = servicioCuenta.nombresDisponibles();
    }

    private void validacion() throws ExecutionException, InterruptedException {
        disponibles = aux.get().stream()
                .map(Cuenta::getNombre)
                .collect(Collectors.toList());
    }


    private void goBack() {
        JFrame login = new Login("Sistema de informacion");
        login.setVisible(true);
        this.dispose();
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
        panelSign = new JPanel();
        panelSign.setLayout(new GridLayoutManager(5, 1, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1 = new JTabbedPane();
        panelSign.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab("Cuenta", panel1);
        pass_jtext = new JPasswordField();
        panel1.add(pass_jtext, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passc_jtext = new JPasswordField();
        panel1.add(passc_jtext, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        mail_jtext = new JTextField();
        mail_jtext.setText("");
        panel1.add(mail_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("*Correo");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("*Confirmar Password");
        panel1.add(label2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("*Password");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 28, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Registrar Cuenta");
        panel1.add(label4, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab("Datos Personales", panel2);
        alumno_cdi_jtext = new JTextField();
        panel2.add(alumno_cdi_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        alumno_nombre_jtext = new JTextField();
        alumno_nombre_jtext.setText("");
        panel2.add(alumno_nombre_jtext, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 28, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("Ingrese sus datos personales");
        panel2.add(label5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        alumno_telf_jtext = new JTextField();
        panel2.add(alumno_telf_jtext, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("*CDI:");
        panel2.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("*Nombre Completo");
        panel2.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Telefono");
        panel2.add(label8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 3, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab("Representante", panel3);
        final JLabel label9 = new JLabel();
        Font label9Font = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 20, label9.getFont());
        if (label9Font != null) label9.setFont(label9Font);
        label9.setText("Ingrese los datos de su Representante");
        panel3.add(label9, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rep_cdi_jtext = new JTextField();
        panel3.add(rep_cdi_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_nombre_jtext = new JTextField();
        panel3.add(rep_nombre_jtext, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_telf_jtext = new JTextField();
        panel3.add(rep_telf_jtext, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_mail_jtext = new JTextField();
        panel3.add(rep_mail_jtext, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        rep_trab_jtext = new JTextField();
        panel3.add(rep_trab_jtext, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("*CDI:");
        panel3.add(label10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("*Nombre:");
        panel3.add(label11, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Telefono");
        panel3.add(label12, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("*Correo");
        panel3.add(label13, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Direccion de Trabajo");
        panel3.add(label14, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarseButton = new JButton();
        registrarseButton.setText("Registrarse");
        panelSign.add(registrarseButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        regresarButton = new JButton();
        regresarButton.setText("Regresar");
        panelSign.add(regresarButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Los campos con * son obligatorios");
        panelSign.add(label15, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Formato de nombre: APELLIDO01 APELLIDO02 NOMBRE01 NOMBRE02");
        panelSign.add(label16, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelSign;
    }

}
