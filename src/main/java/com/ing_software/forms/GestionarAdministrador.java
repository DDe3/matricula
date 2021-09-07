package com.ing_software.forms;

import com.ing_software.Principal;
import com.ing_software.abstraccion.Case;
import com.ing_software.abstraccion.Result;
import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Profesor;
import com.ing_software.entity.Representante;
import com.ing_software.servicios.AdministrativoUtilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.ing_software.abstraccion.Case.defaultCase;
import static com.ing_software.abstraccion.Case.mcase;
import static com.ing_software.abstraccion.Result.failure;
import static com.ing_software.abstraccion.Result.success;
import static com.ing_software.utils.Patterns.*;


public class GestionarAdministrador extends JFrame {


    AdministrativoUtilities servicio = Principal.se.select(AdministrativoUtilities.class).get();

    private JButton listarAdministradoresButton;
    private JList listaAdministradores;
    private JButton editarSeleccionadoButton;
    private JButton cancelarButton;
    private JButton modificarButton;
    private JButton salirButton;
    private JButton nuevoButton;
    private JTextField cdi_jtext;
    private JTextField nombre_jtext;
    private JTextField telf_jtext;
    private JTextField mail_jtext;
    private JTextField pass_jtext;
    private JPanel panelAd;
    private JScrollPane scrollPane;
    private JButton registrarNuevoButton;

    CompletableFuture<List<Administrativo>> aux;
    List<Administrativo> disponibles = new ArrayList<>();
    List<Administrativo> adds = new ArrayList<>();

    public GestionarAdministrador(String title) throws HeadlessException {
        super(title);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(panelAd);
        this.pack();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        listaAdministradores.setVisibleRowCount(5);
        setup();
        listaAdministradores.setModel(new DefaultListModel<Administrativo>());
        findFuture();


        listaAdministradores.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                editarSeleccionadoButton.setEnabled(true);
            }
        });


        listarAdministradoresButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel<Administrativo> model = (DefaultListModel) (listaAdministradores.getModel());
                if (disponibles.isEmpty()) {
                    disponibles = aux.get();
                }
                listaAdministradores.setModel(model);
                disponibles.forEach(model::addElement);

            }
        });


        editarSeleccionadoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!listaAdministradores.isSelectionEmpty()) {
                    Administrativo admin = (Administrativo) listaAdministradores.getSelectedValue();
                    cdi_jtext.setText(admin.getCedula());
                    nombre_jtext.setText(admin.getNombre());
                    telf_jtext.setText(admin.getTelefono());
                    Cuenta tmp = admin.getCuenta();
                    mail_jtext.setText(tmp.getNombre());
                    pass_jtext.setText(tmp.getPassword());

                    editarSeleccionadoButton.setEnabled(false);
                    nuevoButton.setEnabled(false);
                    registrarNuevoButton.setEnabled(false);

                    modificarButton.setEnabled(true);
                    cancelarButton.setEnabled(true);

                    cdi_jtext.setEditable(false);


                }
            }
        });
        nuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                cdi_jtext.setEditable(true);
                modificarButton.setEnabled(false);
                cancelarButton.setEnabled(true);
                nuevoButton.setEnabled(false);
                registrarNuevoButton.setEnabled(true);

            }
        });
        registrarNuevoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cdi = cdi_jtext.getText();
                String nombre = nombre_jtext.getText();
                String telefono = telf_jtext.getText();
                String mail = mail_jtext.getText();
                String pass = pass_jtext.getText();

                Result<String> result = Case.match(
                        defaultCase(() -> success("Administrador Registrado con Exito!")),
                        mcase(() -> cdi.equals(""), () -> failure("CDI vacio")),
                        mcase(() -> nombre.equals(""), () -> failure("Nombre de administrador vacio")),
                        mcase(() -> telefono.equals(""), () -> failure("Telefono de administrador vacio")),
                        mcase(() -> mail.equals(""), () -> failure("Mail de administrador vacio")),
                        mcase(() -> pass.equals(""), () -> failure("Password de administrador vacio")),
                        mcase(() -> !cdiPattern.matcher(cdi).matches(), () -> failure("CDI no valido")),
                        mcase(() -> !telefonoPattern.matcher(telefono).matches(), () -> failure("Telefono no valido")),
                        mcase(() -> !emailPattern.matcher(mail).matches(), () -> failure("Mail no valido")),
                        mcase(() -> !passwordPattern.matcher(pass).matches(), () -> failure("La password debe contener: " +
                                "\n-Un numero" +
                                "\n-Una mayuscula y una minuscula" +
                                "\n-Al menos 8 caracteres")));


                result.bind(x ->
                {
                    clearText();
                    setup();
                    Administrativo tmp = new Administrativo();
                    tmp.setCedula(cdi);
                    tmp.setNombre(nombre);
                    tmp.setTelefono(telefono);
                    tmp.setMail(mail);

                    Cuenta ctmp = new Cuenta();
                    ctmp.setNombre(mail);
                    ctmp.setPassword(pass);
                    servicio.persistirAdmin(ctmp, tmp);
                    disponibles.add(tmp);
                    refrescarLista();

                    JOptionPane.showMessageDialog(null, x);
                }, x -> JOptionPane.showMessageDialog(null, x));
            }
        });


        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                servicio.clear();
                cerrar();
            }
        });
        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cdi = cdi_jtext.getText();
                String nombre = nombre_jtext.getText();
                String telefono = telf_jtext.getText();
                String mail = mail_jtext.getText();
                String pass = pass_jtext.getText();

                Result<String> result = Case.match(
                        defaultCase(() -> success("Administrador modificado con Exito!")),
                        mcase(() -> cdi.equals(""), () -> failure("CDI vacio")),
                        mcase(() -> nombre.equals(""), () -> failure("Nombre de administrador vacio")),
                        mcase(() -> telefono.equals(""), () -> failure("Telefono de administrador vacio")),
                        mcase(() -> mail.equals(""), () -> failure("Mail de administrador vacio")),
                        mcase(() -> pass.equals(""), () -> failure("Password de administrador vacio")),
                        mcase(() -> !cdiPattern.matcher(cdi).matches(), () -> failure("CDI no valido")),
                        mcase(() -> !telefonoPattern.matcher(telefono).matches(), () -> failure("Telefono no valido")),
                        mcase(() -> !emailPattern.matcher(mail).matches(), () -> failure("Mail no valido")),
                        mcase(() -> !passwordPattern.matcher(pass).matches(), () -> failure("La password debe contener: " +
                                "\n-Un numero" +
                                "\n-Una mayuscula y una minuscula" +
                                "\n-Al menos 8 caracteres")));


                result.bind(x ->
                {

                    Administrativo aux = null;
                    for (Administrativo tmp : disponibles) {
                        if (tmp.getCedula().equals(cdi)) {
                            aux = tmp;
                            disponibles.remove(tmp);
                            break;
                        }
                    }
                    if (aux != null) {
                        aux.setNombre(nombre);
                        aux.setTelefono(telefono);
                        aux.setMail(mail);
                        Cuenta ctmp = aux.getCuenta();
                        ctmp.setNombre(mail);
                        ctmp.setPassword(pass);
                        servicio.saveAdmin(aux);
                        disponibles.add(aux);
                        refrescarLista();
                        JOptionPane.showMessageDialog(null, x);
                        clearText();
                        setup();
                    }

                }, x -> JOptionPane.showMessageDialog(null, x));

            }
        });


        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
                cdi_jtext.setEditable(false);
                setup();
            }
        });
    }

    private void refrescarLista() {
        listaAdministradores.setModel(new DefaultListModel());
        DefaultListModel<Administrativo> model = (DefaultListModel) (listaAdministradores.getModel());
        disponibles.forEach(model::addElement);
    }

    private void cerrar() {
        this.dispose();
    }

    private void setup() {
        editarSeleccionadoButton.setEnabled(false);
        modificarButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        registrarNuevoButton.setEnabled(false);
        nuevoButton.setEnabled(true);

    }

    private void findFuture() {
        aux = CompletableFuture.supplyAsync(
                () -> servicio.findAll()
                , Principal.e);
    }

    private void clearText() {
        cdi_jtext.setText("");
        nombre_jtext.setText("");
        telf_jtext.setText("");
        mail_jtext.setText("");
        pass_jtext.setText("");
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
        panelAd = new JPanel();
        panelAd.setLayout(new GridLayoutManager(9, 7, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Gestion Administrador");
        panelAd.add(label1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelAd.add(spacer1, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        listarAdministradoresButton = new JButton();
        listarAdministradoresButton.setText("Listar Administradores");
        panelAd.add(listarAdministradoresButton, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane = new JScrollPane();
        panelAd.add(scrollPane, new GridConstraints(2, 0, 5, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listaAdministradores = new JList();
        scrollPane.setViewportView(listaAdministradores);
        editarSeleccionadoButton = new JButton();
        editarSeleccionadoButton.setText("Editar Seleccionado");
        panelAd.add(editarSeleccionadoButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificarButton = new JButton();
        modificarButton.setText("Modificar");
        panelAd.add(modificarButton, new GridConstraints(7, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        salirButton = new JButton();
        salirButton.setText("Salir");
        panelAd.add(salirButton, new GridConstraints(8, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Datos Administrador");
        panelAd.add(label2, new GridConstraints(1, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Lista Administradores:");
        panelAd.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nuevoButton = new JButton();
        nuevoButton.setText("Nuevo");
        panelAd.add(nuevoButton, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Datos Cuenta");
        panelAd.add(label4, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cdi_jtext = new JTextField();
        panelAd.add(cdi_jtext, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        nombre_jtext = new JTextField();
        panelAd.add(nombre_jtext, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        telf_jtext = new JTextField();
        telf_jtext.setText("");
        panelAd.add(telf_jtext, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("CDI:");
        panelAd.add(label5, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mail_jtext = new JTextField();
        panelAd.add(mail_jtext, new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pass_jtext = new JTextField();
        panelAd.add(pass_jtext, new GridConstraints(3, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Mail");
        panelAd.add(label6, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Password");
        panelAd.add(label7, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        panelAd.add(cancelarButton, new GridConstraints(7, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Nombre:");
        panelAd.add(label8, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Telefono:");
        panelAd.add(label9, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarNuevoButton = new JButton();
        registrarNuevoButton.setText("Registrar Nuevo");
        panelAd.add(registrarNuevoButton, new GridConstraints(4, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelAd;
    }

}
