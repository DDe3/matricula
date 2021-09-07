package com.ing_software.forms;

import com.ing_software.Principal;
import com.ing_software.abstraccion.Case;
import com.ing_software.abstraccion.Result;
import com.ing_software.entity.*;
import com.ing_software.servicios.CuentaUtilities;
import com.ing_software.servicios.CursoUtilities;
import com.ing_software.servicios.EstudianteUtilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import lombok.SneakyThrows;

import javax.persistence.criteria.CriteriaBuilder;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.ing_software.abstraccion.Case.defaultCase;
import static com.ing_software.abstraccion.Case.mcase;
import static com.ing_software.abstraccion.Result.failure;
import static com.ing_software.abstraccion.Result.success;
import static com.ing_software.utils.Patterns.*;

public class VentanaAlumno extends JFrame {

    EstudianteUtilities servicioEstudiante = Principal.se.select(EstudianteUtilities.class).get();
    CuentaUtilities servicioCuenta = Principal.se.select(CuentaUtilities.class).get();
    CursoUtilities servicioCurso = Principal.se.select(CursoUtilities.class).get();

    private JTabbedPane tabbedPane1;
    private JPanel estudiantePanel;
    private JTabbedPane tabbedPane2;
    private JTextField cdi_jtext;
    private JTextField name_jtext;
    private JTextField telf_jtext;
    private JTextField mail_jtext;
    private JTextField pass_jtext;
    private JButton guardarCambiosButton;
    private JButton modificarButton;
    private JButton cancelarButton;
    private JButton logoutButton;
    private JLabel welcomeLabel;
    private JList listaMatriculas;
    private JButton listarMatriculasButton;
    private JScrollPane scrollPane;
    private JLabel debersLabel;
    private JLabel leccionLabel;
    private JLabel examenLabel;
    private JLabel supLabel;
    private JLabel finalLabel;
    private JScrollPane scrollPane2;
    private JList listaMaterias;
    private JLabel labelEstado;
    private JList listaCurso;
    private JScrollPane scrollMat;
    private JButton listarCursosDisponiblesButton;
    private JButton matricularseEnEsteCursoButton;
    private Cuenta cuenta;
    private Estudiante estudiante;


    CompletableFuture<List<Cuenta>> aux;
    List<String> disponibles = new ArrayList<>();

    CompletableFuture<List<Curso>> auxCurso;
    List<Curso> cursosAMatricular = new ArrayList<>();


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
        estudiantePanel = new JPanel();
        estudiantePanel.setLayout(new GridLayoutManager(2, 3, new Insets(10, 10, 10, 10), -1, -1));
        tabbedPane1 = new JTabbedPane();
        estudiantePanel.add(tabbedPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab("Informacion", panel1);
        cdi_jtext = new JTextField();
        cdi_jtext.setText("");
        panel1.add(cdi_jtext, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        name_jtext = new JTextField();
        name_jtext.setText("");
        panel1.add(name_jtext, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        telf_jtext = new JTextField();
        panel1.add(telf_jtext, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        mail_jtext = new JTextField();
        mail_jtext.setText("");
        panel1.add(mail_jtext, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pass_jtext = new JTextField();
        panel1.add(pass_jtext, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("CDI:");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Nombre:");
        panel1.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Telefono");
        panel1.add(label3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Correo");
        panel1.add(label4, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Password");
        panel1.add(label5, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Informacion alumno");
        panel1.add(label6, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Informacion Cuenta");
        panel1.add(label7, new GridConstraints(0, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificarButton = new JButton();
        modificarButton.setText("Modificar");
        panel1.add(modificarButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        guardarCambiosButton = new JButton();
        guardarCambiosButton.setText("Guardar Cambios");
        panel1.add(guardarCambiosButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        panel1.add(cancelarButton, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(8, 6, new Insets(10, 10, 10, 50), -1, -1));
        tabbedPane1.addTab("Notas", panel2);
        scrollPane = new JScrollPane();
        panel2.add(scrollPane, new GridConstraints(1, 0, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listaMatriculas = new JList();
        scrollPane.setViewportView(listaMatriculas);
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Matriculas Registradas:");
        panel2.add(label8, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Deberes");
        panel2.add(label9, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Supletorio");
        panel2.add(label10, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        debersLabel = new JLabel();
        debersLabel.setText("");
        panel2.add(debersLabel, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supLabel = new JLabel();
        supLabel.setText("");
        panel2.add(supLabel, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Final");
        panel2.add(label11, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        finalLabel = new JLabel();
        finalLabel.setText("");
        panel2.add(finalLabel, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Lecciones");
        panel2.add(label12, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leccionLabel = new JLabel();
        leccionLabel.setText("");
        panel2.add(leccionLabel, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Examen");
        panel2.add(label13, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        examenLabel = new JLabel();
        examenLabel.setText("");
        panel2.add(examenLabel, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listarMatriculasButton = new JButton();
        listarMatriculasButton.setText("Listar Matriculas");
        panel2.add(listarMatriculasButton, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Notas");
        panel2.add(label14, new GridConstraints(0, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(1, 2, 5, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listaMaterias = new JList();
        scrollPane2.setViewportView(listaMaterias);
        final JLabel label15 = new JLabel();
        label15.setText("Materias");
        panel2.add(label15, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelEstado = new JLabel();
        labelEstado.setText("");
        panel2.add(labelEstado, new GridConstraints(6, 3, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane2 = new JTabbedPane();
        tabbedPane1.addTab("Procesos", tabbedPane2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Matricular", panel3);
        scrollMat = new JScrollPane();
        panel3.add(scrollMat, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        listaCurso = new JList();
        scrollMat.setViewportView(listaCurso);
        listarCursosDisponiblesButton = new JButton();
        listarCursosDisponiblesButton.setText("Listar Cursos Disponibles");
        panel3.add(listarCursosDisponiblesButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label16 = new JLabel();
        label16.setText("Cursos disponibles");
        panel3.add(label16, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        matricularseEnEsteCursoButton = new JButton();
        matricularseEnEsteCursoButton.setText("Matricularse en este Curso");
        panel3.add(matricularseEnEsteCursoButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setHideActionText(false);
        logoutButton.setIcon(new ImageIcon(getClass().getResource("/icon/logout_icon_151219.png")));
        logoutButton.setText("");
        estudiantePanel.add(logoutButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        welcomeLabel = new JLabel();
        Font welcomeLabelFont = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 16, welcomeLabel.getFont());
        if (welcomeLabelFont != null) welcomeLabel.setFont(welcomeLabelFont);
        welcomeLabel.setText("Label");
        estudiantePanel.add(welcomeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return estudiantePanel;
    }


    public VentanaAlumno(String title, Cuenta c) throws HeadlessException {
        super(title);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(estudiantePanel);
        this.pack();
        cuenta = c;
        estudiante = cuenta.getOwner1();
        welcomeLabel.setText("Bienvenid@ " + estudiante.getNombre());
        llenarInformacion();
        camposEditables(false);
        guardarCambiosButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        init();
        listaMatriculas.setModel(new DefaultListModel());
        listaMaterias.setModel(new DefaultListModel());


        modificarButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                if (disponibles.isEmpty()) {
                    validacion();
                }
                telf_jtext.setEditable(true);
                mail_jtext.setEditable(true);
                pass_jtext.setEditable(true);
                cancelarButton.setEnabled(true);
                guardarCambiosButton.setEnabled(true);
                modificarButton.setEnabled(false);
            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                llenarInformacion();
                modificarButton.setEnabled(true);
                guardarCambiosButton.setEnabled(false);
                cancelarButton.setEnabled(false);
                camposEditables(false);
            }
        });


        guardarCambiosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                disponibles.remove(c.getNombre());
                String telf = telf_jtext.getText();
                String mail = mail_jtext.getText();
                String pass = pass_jtext.getText();


                Result<String> result = Case.match(
                        defaultCase(() -> success("Datos modificados")),
                        mcase(() -> !telf.equals("") && !telefonoPattern.matcher(telf).matches(), () -> failure("Telefono no valido")),
                        mcase(() -> !emailPattern.matcher(mail).matches(), () -> failure("Mail no valido")),
                        mcase(() -> disponibles.contains(mail), () -> failure("Este correo ya se encuentra registrado"))
                );

                result.bind(x -> {
                    estudiante.setTelefono(telf);
                    estudiante.setMail(mail);
                    c.setNombre(mail);
                    c.setPassword(pass);
                    servicioEstudiante.merge(estudiante);
                    JOptionPane.showMessageDialog(null, x);
                }, x -> {
                    JOptionPane.showMessageDialog(null, x);
                });
                disponibles.add(mail);
                llenarInformacion();
                camposEditables(false);

                modificarButton.setEnabled(true);
                guardarCambiosButton.setEnabled(false);
                cancelarButton.setEnabled(false);


            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrar();
            }
        });


        listarMatriculasButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Matricula> matriculas = estudiante.getMatriculasRegistradas();
                DefaultListModel<Matricula> model = (DefaultListModel<Matricula>) listaMatriculas.getModel();
                if (!matriculas.isEmpty()) {
                    matriculas.forEach(model::addElement);
                } else {
                    JOptionPane.showMessageDialog(null, "No existen matriculas registradas");
                }
                listarMatriculasButton.setEnabled(false);
            }
        });

        listaMatriculas.addListSelectionListener(e -> {
            Matricula m = (Matricula) listaMatriculas.getSelectedValue();
            Optional<Curso> op = servicioCurso.findByAula(m.getAula());
            if (op.isPresent()) {
                Curso curso = op.get();
                listaMaterias.setModel(new DefaultListModel());
                DefaultListModel<Materia> model = (DefaultListModel<Materia>) listaMaterias.getModel();
                List<Materia> materias = curso.getMaterias();
                if (!materias.isEmpty()) {
                    materias.forEach(model::addElement);
                }
            }

        });

        listaMaterias.addListSelectionListener(e -> {
            Materia materia = (Materia) listaMaterias.getSelectedValue();
            String cod = materia.getCodigo();
            List<Nota> notas = estudiante.getNotas();
            if (!estudiante.getNotas().isEmpty()) {
                for (Nota ax : notas) {
                    if (ax.getCodmat().equalsIgnoreCase(cod)) {
                        debersLabel.setText(ax.getAporteDeberes() == null ? "" : String.format("%.2f", ax.getAporteDeberes()));
                        leccionLabel.setText(ax.getAporteLecciones() == null ? "" : String.format("$%.2f", ax.getAporteLecciones()));
                        examenLabel.setText(ax.getAporteExamen() == null ? "" : String.format("%.2f", ax.getAporteExamen()));
                        supLabel.setText(ax.getNotaSupletorio() == null ? "" : String.format("%.2f", ax.getNotaSupletorio()));
                        finalLabel.setText(ax.getNotaFinal() == null ? "" : String.format("%.2f", ax.getNotaFinal()));
                        labelEstado.setText(ax.dameEstado());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay notas registradas para esta materia y matricula");
            }
        });


        listarCursosDisponiblesButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                getMisCursos();
                listaCurso.setModel(new DefaultListModel());
                DefaultListModel<Curso> model = (DefaultListModel<Curso>) listaCurso.getModel();
                if (!estudiante.getMatriculasRegistradas().isEmpty()) {
                    Matricula m = estudiante.getMatriculasRegistradas().get(0);
                    if (!m.getEstado()) {
                        int actual = dameNumeros(m.getCiclo());
                        if (m.getAprobado().equalsIgnoreCase("reprobado")) {
                            cursosAMatricular = auxCurso.get().stream().filter(x -> dameNumeros(x.getCiclo()) == actual).collect(Collectors.toList());
                        } else {
                            cursosAMatricular = auxCurso.get().stream().filter(x -> dameNumeros(x.getCiclo()) == actual + 1).collect(Collectors.toList());
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Registra una matricula actual");
                    }
                } else {
                    cursosAMatricular = auxCurso.get().stream().filter(x -> dameNumeros(x.getCiclo()) == 1).collect(Collectors.toList());
                }
                System.out.println("Cursos filtrados para este alumno: "+cursosAMatricular);
                cursosAMatricular.forEach(model::addElement);
                listarCursosDisponiblesButton.setEnabled(false);
            }
        });


        matricularseEnEsteCursoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!listaCurso.isSelectionEmpty()) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea matricularse en este curso?", "Confirmar Accion", dialogButton);
                    if (dialogResult == JOptionPane.YES_OPTION) {
                        Curso curso = (Curso) listaCurso.getSelectedValue();
                        estudiante.setCurso(curso);
                        servicioEstudiante.matriculaAdd(estudiante);
                        servicioCurso.save(curso);
                        matricularseEnEsteCursoButton.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "Se ha matriculado con exito en el curso: " + curso);
                    }

                }
            }
        });
    }

    private Integer dameNumeros(String s) {

        switch (s.toUpperCase(Locale.ROOT)) {
            case "PRIMERO":
                return 1;
            case "SEGUNDO":
                return 2;
            case "TERCERO":
                return 3;
            case "CUARTO":
                return 4;
            case "QUINTO":
                return 5;
            case "SEXTO":
                return 6;
            case "SEPTIMO":
                return 7;
            default:
                return 0;
        }

    }

    private void cerrar() {
        JFrame login = new Login("Sistema de informacion");
        login.setVisible(true);
        this.dispose();
    }

    private void camposEditables(Boolean op) {
        cdi_jtext.setEditable(op);
        name_jtext.setEditable(op);
        telf_jtext.setEditable(op);
        mail_jtext.setEditable(op);
        pass_jtext.setEditable(op);
    }

    private void llenarInformacion() {
        cdi_jtext.setText(estudiante.getCedula());
        name_jtext.setText(estudiante.getNombre());
        telf_jtext.setText(estudiante.getTelefono());
        mail_jtext.setText(cuenta.getNombre());
        pass_jtext.setText(cuenta.getPassword());
    }

    private void init() {
        aux = servicioCuenta.nombresDisponibles();
    }

    private void validacion() throws ExecutionException, InterruptedException {
        disponibles = aux.get().stream()
                .map(Cuenta::getNombre)
                .collect(Collectors.toList());
    }

    private void getMisCursos() {
        auxCurso = CompletableFuture.supplyAsync(() -> servicioCurso.findAll(), Principal.e);
    }


}