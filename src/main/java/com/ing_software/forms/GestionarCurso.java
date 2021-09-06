package com.ing_software.forms;

import com.ing_software.Principal;
import com.ing_software.abstraccion.Case;
import com.ing_software.abstraccion.Result;
import com.ing_software.entity.*;
import com.ing_software.servicios.*;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.SneakyThrows;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ing_software.abstraccion.Case.defaultCase;
import static com.ing_software.abstraccion.Case.mcase;
import static com.ing_software.abstraccion.Result.failure;
import static com.ing_software.abstraccion.Result.success;
import static com.ing_software.utils.Patterns.*;

public class GestionarCurso extends JFrame {


    EstudianteUtilities servicioEstudiante = Principal.se.select(EstudianteUtilities.class).get();
    ProfesorUtilities servicioProfesor = Principal.se.select(ProfesorUtilities.class).get();
    MateriaUtilities servicioMateria = Principal.se.select(MateriaUtilities.class).get();
    CursoUtilities servicioCurso = Principal.se.select(CursoUtilities.class).get();
    MatriculaUtilities servicioMatricula = Principal.se.select(MatriculaUtilities.class).get();


    private JTabbedPane cursoPanel;
    private JPanel gestionCursoPanel;
    private JTabbedPane tabbedPane2;
    private JTextField aula_jtext;
    private JList estudiantesRegistradosList;
    private JList materiasRegistradasList;
    private JTextField est_jtext;
    private JButton addEstudianteButton;
    private JButton removeEstudianteButton;
    private JButton addMateriaButton;
    private JButton removeMateriaButton;
    private JButton crearButton;
    private JButton button6;
    private JButton button7;
    private JButton buscarButton;
    private JButton salirButton;
    private JButton modificarButton;
    private JButton cancelarButton;
    private JList materiasAddList;
    private JButton listarMateriasButton;
    private JList estudiantesAddList;
    private JButton listarEstudianteButton;
    private JButton addPorCDIButton;
    private JList profesoresRegistradosList;
    private JButton addProfesorButton;
    private JButton removeProfesorButton;
    private JList profesoresAddList;
    private JButton listarProfesoresButton;
    private JButton registrarCursoButton;
    private JPanel panelGestion;
    private JScrollPane materiasScrollPane;
    private JScrollPane estudianteScrollPane;
    private JScrollPane profesorScrollPane;
    private JScrollPane materiasAddScrollPane;
    private JScrollPane estudiantesAddScrollPane;
    private JScrollPane profesorAddScrollPane;
    private JTextField paralelo_jtext;
    private JComboBox cicloBox;
    private JSpinner cupoSpinner;

    CompletableFuture<List<Estudiante>> listEstudiantesAux;

    CompletableFuture<List<Profesor>> listProfesorAux;

    CompletableFuture<List<Materia>> listMateriaAux;



    public GestionarCurso(String title) throws HeadlessException {
        super(title);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(gestionCursoPanel);
        setup();
        this.pack();
        habilitarCampos(false);
        editarCampos(false);

        setupScrolls();


        crearButton.addActionListener(e -> {
            clearText();
            habilitarCampos(true);
            editarCampos(true);
            habilitarBotones(true);
            buscarButton.setEnabled(false);
            crearButton.setEnabled(false);
            modificarButton.setEnabled(true);
            cancelarButton.setEnabled(true);
        });


        addPorCDIButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cdi = est_jtext.getText();
                Optional<Estudiante> op = servicioEstudiante.findByCdi(est_jtext.getText());


                Result<String> result = Case.match(
                        defaultCase(() -> success("")),
                        mcase(() -> cdi.equals(""), () -> failure("Ingrese un cdi primero")),
                        mcase(() -> !cdiPattern.matcher(cdi).matches(), () -> failure("Ingrese un cdi valido")),
                        mcase(() -> !(op.isPresent()), () -> failure("No se encuentra a ningun estudiante con cdi: " + cdi)),
                        mcase(() -> {
                            Estudiante estudiante = op.get();
                            return !(estudiante.getCurso() == null);
                        }, () -> failure("Este estudiante ya tiene un curso registrado"))
                );


                result.bind(
                        x ->
                        {
                            Estudiante estudiante = op.get();
                            DefaultListModel<String> model = (DefaultListModel<String>) estudiantesAddList.getModel();
                            model.addElement(estudiante.getNombre() + " | " + estudiante.getCedula());
                        }

                        ,
                        x -> JOptionPane.showMessageDialog(null,
                                "Error: " + x,
                                "Error de Busqueda",
                                JOptionPane.ERROR_MESSAGE));

            }
        });
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
                setup();
            }
        });
        listarMateriasButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                findFutureMateria();
                DefaultListModel<Materia> model = (DefaultListModel<Materia>) materiasRegistradasList.getModel();
                listarAux(false);
                List<Materia> materias = listMateriaAux.get();
                materias.parallelStream().forEach(model::addElement);
                listarAux(true);


            }
        });
        listarEstudianteButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                findFutureEstudiante();
                DefaultListModel<Estudiante> model = (DefaultListModel<Estudiante>) estudiantesRegistradosList.getModel();
                listarAux(false);
                List<Estudiante> estudiantes = listEstudiantesAux.get();
                estudiantes.parallelStream().filter(x -> x.getCurso() == null).forEach(model::addElement);
                listarAux(true);
            }
        });
        listarProfesoresButton.addActionListener(new ActionListener() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                findFutureProfesor();
                DefaultListModel<Profesor> model = (DefaultListModel<Profesor>) profesoresRegistradosList.getModel();
                listarAux(false);
                List<Profesor> profesors = listProfesorAux.get();
                profesors.parallelStream().filter(x -> x.getCurso() == null).forEach(model::addElement);
                listarAux(true);
            }
        });

        addEstudianteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!estudiantesRegistradosList.isSelectionEmpty()) {
                    listarAux(false);
                    Estudiante estudiante = (Estudiante) estudiantesRegistradosList.getSelectedValue();
                    int index = estudiantesRegistradosList.getSelectedIndex();
                    DefaultListModel<Estudiante> modelo = (DefaultListModel<Estudiante>) estudiantesRegistradosList.getModel();
                    modelo.remove(index);
                    DefaultListModel<Estudiante> model = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                    model.addElement(estudiante);
                    listarAux(true);
                }
            }
        });
        removeEstudianteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //try {
                    if (!estudiantesAddList.isSelectionEmpty()) {
                        listarAux(false);
                        Estudiante estudiante = (Estudiante) estudiantesAddList.getSelectedValue();
                        servicioEstudiante.removeMatricula(estudiante);
                        estudiante.setCurso(null);
                        int index = estudiantesAddList.getSelectedIndex();
                        DefaultListModel<Estudiante> modelo = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                        modelo.remove(index);
                        DefaultListModel<Estudiante> model = (DefaultListModel<Estudiante>) estudiantesRegistradosList.getModel();
                        model.addElement(estudiante);
                        listarAux(true);

                    }
//                } catch (Exception ex){
//                    JOptionPane.showMessageDialog(null, "No se pudo modificar al estudiante\nError: " + ex);
//                }

            }
        });
        addMateriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!materiasRegistradasList.isSelectionEmpty()) {
                    listarAux(false);
                    Materia materia = (Materia) materiasRegistradasList.getSelectedValue();
                    int index = materiasRegistradasList.getSelectedIndex();
                    DefaultListModel<Materia> modelo = (DefaultListModel<Materia>) materiasRegistradasList.getModel();
                    modelo.remove(index);
                    DefaultListModel<Materia> model = (DefaultListModel<Materia>) materiasAddList.getModel();
                    model.addElement(materia);
                    listarAux(true);
                }
            }
        });
        removeMateriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!materiasAddList.isSelectionEmpty()) {
                    listarAux(false);
                    String aula = aula_jtext.getText();
                    Materia materia = (Materia) materiasAddList.getSelectedValue();
                    Curso curso = servicioCurso.findByAula(aula).get();
                    servicioCurso.removeMaterias(curso,materia);
                    int index = materiasAddList.getSelectedIndex();
                    DefaultListModel<Materia> modelo = (DefaultListModel<Materia>) materiasAddList.getModel();
                    modelo.remove(index);
                    DefaultListModel<Materia> model = (DefaultListModel<Materia>) materiasRegistradasList.getModel();
                    model.addElement(materia);
                    listarAux(true);
                }
            }
        });
        addProfesorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!profesoresRegistradosList.isSelectionEmpty()) {
                    listarAux(false);
                    Profesor profesor = (Profesor) profesoresRegistradosList.getSelectedValue();
                    int index = profesoresRegistradosList.getSelectedIndex();
                    DefaultListModel<Profesor> modelo = (DefaultListModel<Profesor>) profesoresRegistradosList.getModel();
                    modelo.remove(index);
                    DefaultListModel<Profesor> model = (DefaultListModel<Profesor>) profesoresAddList.getModel();
                    model.addElement(profesor);
                    listarAux(true);
                }
            }
        });
        removeProfesorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!profesoresAddList.isSelectionEmpty()) {
                    listarAux(false);
                    String aula = aula_jtext.getText();
                    Profesor profesor = (Profesor) profesoresAddList.getSelectedValue();
                    Curso curso = servicioCurso.findByAula(aula).get();
                    int index = profesoresAddList.getSelectedIndex();
                    servicioCurso.removeProfesor(curso,profesor);
                    DefaultListModel<Profesor> modelo = (DefaultListModel<Profesor>) profesoresAddList.getModel();
                    modelo.remove(index);
                    DefaultListModel<Profesor> model = (DefaultListModel<Profesor>) profesoresRegistradosList.getModel();
                    model.addElement(profesor);
                    listarAux(true);
                }
            }
        });

        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
                habilitarBotones(true);
                habilitarCampos(true);
                editarCampos(true);
                modificarButton.setEnabled(true);
                crearButton.setEnabled(false);
                cancelarButton.setEnabled(true);
                buscarButton.setEnabled(false);
                registrarCursoButton.setEnabled(false);

                String aula = JOptionPane.showInputDialog("Ingrese el aula del curso: ");
                if (!aula.equals(null)) {
                    Optional<Curso> op = servicioCurso.findByAula(aula);
                    if (op.isPresent()) {

                        Curso curso = op.get();
                        System.out.println(curso.getMaterias());
                        aula_jtext.setText(curso.getAula());
                        cicloBox.setSelectedItem(curso.getCiclo());
                        paralelo_jtext.setText(String.valueOf(curso.getParalelo()));
                        cupoSpinner.setValue(curso.getCupo());


                        DefaultListModel<Estudiante> modelEstudiantes = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                        List<Estudiante> estudiantes = curso.getEstudiantes();
                        estudiantes.parallelStream().forEach(modelEstudiantes::addElement);

                        DefaultListModel<Materia> modelMaterias = (DefaultListModel<Materia>) materiasAddList.getModel();
                        List<Materia> materias = curso.getMaterias();
                        materias.parallelStream().forEach(modelMaterias::addElement);

                        DefaultListModel<Profesor> modelProfesor = (DefaultListModel<Profesor>) profesoresAddList.getModel();
                        Profesor profesor = curso.getEncargado();
                        modelProfesor.addElement(profesor);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "No hay un curso asignado a esa aula",
                                "Error de Busqueda",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }

            }
        });

        modificarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                try {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Esta seguro que desea modificar este curso?", "Warning", dialogButton);
                if (dialogResult == JOptionPane.YES_OPTION) {

                    String aula = aula_jtext.getText();
                    String ciclo = (String) cicloBox.getSelectedItem();
                    String paralelo = paralelo_jtext.getText();
                    try {
                        cupoSpinner.commitEdit();
                    } catch (ParseException ignored) {
                    }
                    Integer cupos = (Integer) cupoSpinner.getValue();

                    DefaultListModel<Estudiante> modelEstudiantes = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                    List<Estudiante> estudiantes = IntStream.range(0, modelEstudiantes.size()).mapToObj(modelEstudiantes::get).collect(Collectors.toList());

                    DefaultListModel<Materia> modelMaterias = (DefaultListModel<Materia>) materiasAddList.getModel();
                    List<Materia> materias = IntStream.range(0, modelMaterias.size()).mapToObj(modelMaterias::get).collect(Collectors.toList());

                    DefaultListModel<Profesor> modelProfesor = (DefaultListModel<Profesor>) profesoresAddList.getModel();
                    List<Profesor> profesors = IntStream.range(0, modelProfesor.size()).mapToObj(modelProfesor::get).collect(Collectors.toList());
                    if (profesors.size() > 1) {
                        if (profesors.get(0) == null) {
                            profesors.remove(0);
                        }
                    }

                    System.out.println(profesors.size());
                    System.out.println(profesors);

                    Result<String> result = Case.match(
                            defaultCase(() -> success("Curso modificado con exito!")),
                            mcase(() -> aula.equals(""), () -> failure("Debe asignarse un aula a este curso")),
                            mcase(() -> !aulaPattern.matcher(aula).matches(), () -> failure("Codigo de aula invalido\nFormato: [A-Z]{1}+ - + [0-9]{1,2}")),
                            mcase(() -> !paraleloPattern.matcher(paralelo).matches(), () -> failure("Paralelo debe tener una sola letra")),
                            mcase(() -> cupos < 15 || cupos > 60, () -> failure("Rango de cupos validos: [15-60]")),
                            mcase(() -> estudiantes.size() > cupos, () -> failure("Existen más alumnos que cupos")),
                            mcase(() -> profesors.size() > 1, () -> failure("Un curso solo puede tener un profesor")),
                            mcase(() -> materias.size() > 7 || materias.size() < 1, () -> failure("Rango de materias valido: [1-7]")));


                    result.bind(x ->
                    {

                        System.out.println("Lista sacada de addEstudiantes: " + estudiantes);
                        Curso curso = servicioCurso.findByAula(aula).get();
                        curso.setAula(aula.toUpperCase(Locale.ROOT));
                        curso.setCiclo(ciclo);
                        curso.setParalelo(paralelo.charAt(0));
                        curso.setCupo(cupos);
                        curso.setEstudiantes(estudiantes);
                        curso.addMateria(materias);
                        curso.setEstado(true);
                        if (profesors.size() == 0) {
                            curso.setEncargado(null);
                        } else {
                            curso.setEncargado(profesors.get(0));
                        }

                        servicioCurso.registrarCurso(curso);


                        //servicioCurso.crearCursoAndSave(aula,paralelo.charAt(0),ciclo,profesors.get(0),materias,estudiantes,cupos);

                        JOptionPane.showMessageDialog(null,
                                x,
                                "Exito modificacion",
                                JOptionPane.INFORMATION_MESSAGE);



                    }, x ->
                            JOptionPane.showMessageDialog(null,
                                    "Error: " + x,
                                    "Error en Modificacion",
                                    JOptionPane.ERROR_MESSAGE));
                }
                servicioEstudiante.clear();
                clearText();
                setup();


//                } catch (Exception notIgnored) {
//                    JOptionPane.showMessageDialog(null, "No se pudo modificar al estudiante\nError: " + notIgnored);
//                }
            }
        });
        crearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
                crearButton.setEnabled(false);
                modificarButton.setEnabled(false);
                buscarButton.setEnabled(false);
                habilitarCampos(true);
                habilitarBotones(true);

                String aula = aula_jtext.getText();
                String ciclo = (String) cicloBox.getSelectedItem();
                String paralelo = paralelo_jtext.getText();
                try {
                    cupoSpinner.commitEdit();
                } catch (ParseException ignored) {
                }
                Integer cupos = (Integer) cupoSpinner.getValue();

                DefaultListModel<Estudiante> modelEstudiantes = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                List<Estudiante> estudiantes = IntStream.range(0, modelEstudiantes.size()).mapToObj(modelEstudiantes::get).collect(Collectors.toList());

                DefaultListModel<Materia> modelMaterias = (DefaultListModel<Materia>) materiasAddList.getModel();
                List<Materia> materias = IntStream.range(0, modelMaterias.size()).mapToObj(modelMaterias::get).collect(Collectors.toList());

                DefaultListModel<Profesor> modelProfesor = (DefaultListModel<Profesor>) profesoresAddList.getModel();
                List<Profesor> profesors = IntStream.range(0, modelProfesor.size()).mapToObj(modelProfesor::get).collect(Collectors.toList());
                if (profesors.size() > 1) {
                    if (profesors.get(0) == null) {
                        profesors.remove(0);
                    }
                }
                Result<String> result = Case.match(
                        defaultCase(() -> success("Curso modificado con exito!")),
                        mcase(() -> aula.equals(""), () -> failure("Debe asignarse un aula a este curso")),
                        mcase(() -> !aulaPattern.matcher(aula).matches(), () -> failure("Codigo de aula invalido\nFormato: [A-Z]{1}+ - + [0-9]{1,2}")),
                        mcase(() -> !paraleloPattern.matcher(paralelo).matches(), () -> failure("Paralelo debe tener una sola letra")),
                        mcase(() -> cupos < 15 || cupos > 60, () -> failure("Rango de cupos validos: [15-60]")),
                        mcase(() -> estudiantes.size() > cupos, () -> failure("Existen más alumnos que cupos")),
                        mcase(() -> profesors.size() > 1, () -> failure("Un curso solo puede tener un profesor")),
                        mcase(() -> materias.size() > 7 || materias.size() < 1, () -> failure("Rango de materias valido: [1-7]")));



                result.bind( x-> {



                        }, x -> {

                        }
                );




            }
        });
    }

    private void findFutureEstudiante() {
        listEstudiantesAux = CompletableFuture.supplyAsync(
                () -> servicioEstudiante.findAll()
                , Principal.e);

    }

    private void findFutureMateria() {
        listMateriaAux = CompletableFuture.supplyAsync(
                () -> servicioMateria.findAll()
                , Principal.e);
    }

    private void findFutureProfesor() {
        listProfesorAux = CompletableFuture.supplyAsync(
                () -> servicioProfesor.findAll()
                , Principal.e);
    }

    private void clearText() {
        est_jtext.setText("");
        cicloBox.setSelectedItem(0);
        aula_jtext.setText("");
        clearListas();
        cupoSpinner.setValue(0);
    }

    private void clearListas() {
        estudiantesAddList.setModel(new DefaultListModel<>());
        estudiantesRegistradosList.setModel(new DefaultListModel<>());
        materiasRegistradasList.setModel(new DefaultListModel<>());
        materiasAddList.setModel(new DefaultListModel<>());
        profesoresRegistradosList.setModel(new DefaultListModel<>());
        profesoresAddList.setModel(new DefaultListModel<>());
    }

    private void setup() {
        modificarButton.setEnabled(false);
        crearButton.setEnabled(true);
        buscarButton.setEnabled(true);
        cancelarButton.setEnabled(false);

        habilitarBotones(false);
        habilitarCampos(false);
    }

    private void habilitarCampos(Boolean op) {
        aula_jtext.setEnabled(op);
        cicloBox.setEnabled(op);
        est_jtext.setEnabled(op);
        paralelo_jtext.setEnabled(op);
        cupoSpinner.setEnabled(op);

        estudiantesRegistradosList.setEnabled(op);
        materiasRegistradasList.setEnabled(op);
        materiasAddList.setEnabled(op);
        estudiantesAddList.setEnabled(op);
        profesoresRegistradosList.setEnabled(op);
        profesoresAddList.setEnabled(op);
    }

    private void editarCampos(Boolean op) {
        aula_jtext.setEditable(op);
        est_jtext.setEditable(op);
        paralelo_jtext.setEditable(op);
        cicloBox.setEditable(op);
    }

    private void habilitarBotones(Boolean op) {

        addPorCDIButton.setEnabled(op);
        registrarCursoButton.setEnabled(op);

        addEstudianteButton.setEnabled(op);
        removeEstudianteButton.setEnabled(op);

        addMateriaButton.setEnabled(op);
        removeMateriaButton.setEnabled(op);

        addProfesorButton.setEnabled(op);
        removeProfesorButton.setEnabled(op);

        listarEstudianteButton.setEnabled(op);
        listarMateriasButton.setEnabled(op);
        listarProfesoresButton.setEnabled(op);
    }

    private void listarAux(Boolean op) {
        listarEstudianteButton.setEnabled(op);
        listarMateriasButton.setEnabled(op);
        listarProfesoresButton.setEnabled(op);

        addEstudianteButton.setEnabled(op);
        removeEstudianteButton.setEnabled(op);

        addMateriaButton.setEnabled(op);
        removeMateriaButton.setEnabled(op);

        addProfesorButton.setEnabled(op);
        removeProfesorButton.setEnabled(op);

    }

    private void setupScrolls() {
        materiasScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        materiasScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        estudianteScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        estudianteScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        profesorScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        profesorScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        materiasAddScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        materiasAddScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        estudiantesAddScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        estudiantesAddScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        profesorAddScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        profesorAddScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        materiasRegistradasList.setVisibleRowCount(5);
        estudiantesRegistradosList.setVisibleRowCount(5);
        profesoresRegistradosList.setVisibleRowCount(5);

        estudiantesAddList.setVisibleRowCount(5);
        materiasAddList.setVisibleRowCount(5);
        profesoresAddList.setVisibleRowCount(5);
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
        gestionCursoPanel = new JPanel();
        gestionCursoPanel.setLayout(new GridLayoutManager(4, 4, new Insets(0, 0, 0, 0), -1, -1));
        cursoPanel = new JTabbedPane();
        gestionCursoPanel.add(cursoPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        panelGestion = new JPanel();
        panelGestion.setLayout(new GridLayoutManager(13, 11, new Insets(0, 0, 0, 0), -1, -1));
        cursoPanel.addTab("Cursos", panelGestion);
        aula_jtext = new JTextField();
        panelGestion.add(aula_jtext, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Aula");
        panelGestion.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Materias");
        panelGestion.add(label2, new GridConstraints(6, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Ciclo");
        panelGestion.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Estudiantes");
        panelGestion.add(label4, new GridConstraints(2, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listarMateriasButton = new JButton();
        listarMateriasButton.setText("Listar Materias");
        panelGestion.add(listarMateriasButton, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 30), null, 0, false));
        listarEstudianteButton = new JButton();
        listarEstudianteButton.setText("Listar Estudiantes");
        panelGestion.add(listarEstudianteButton, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        est_jtext = new JTextField();
        panelGestion.add(est_jtext, new GridConstraints(4, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("CDI:");
        panelGestion.add(label5, new GridConstraints(4, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 16), null, 0, false));
        addPorCDIButton = new JButton();
        addPorCDIButton.setText("Add");
        panelGestion.add(addPorCDIButton, new GridConstraints(4, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addEstudianteButton = new JButton();
        addEstudianteButton.setText(">>");
        panelGestion.add(addEstudianteButton, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeEstudianteButton = new JButton();
        removeEstudianteButton.setText("<<");
        panelGestion.add(removeEstudianteButton, new GridConstraints(3, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMateriaButton = new JButton();
        addMateriaButton.setText(">>");
        panelGestion.add(addMateriaButton, new GridConstraints(6, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Estudiantes de este Curso");
        panelGestion.add(label6, new GridConstraints(1, 8, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Materias de este Curso");
        panelGestion.add(label7, new GridConstraints(5, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Profesores");
        panelGestion.add(label8, new GridConstraints(10, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addProfesorButton = new JButton();
        addProfesorButton.setText(">>");
        panelGestion.add(addProfesorButton, new GridConstraints(10, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeProfesorButton = new JButton();
        removeProfesorButton.setText("<<");
        panelGestion.add(removeProfesorButton, new GridConstraints(11, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Profesor de este Curso");
        panelGestion.add(label9, new GridConstraints(9, 8, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listarProfesoresButton = new JButton();
        listarProfesoresButton.setText("Listar Profesores");
        panelGestion.add(listarProfesoresButton, new GridConstraints(12, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarCursoButton = new JButton();
        registrarCursoButton.setText("Registrar Curso");
        panelGestion.add(registrarCursoButton, new GridConstraints(12, 8, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        materiasScrollPane = new JScrollPane();
        panelGestion.add(materiasScrollPane, new GridConstraints(6, 1, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        materiasRegistradasList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        materiasRegistradasList.setModel(defaultListModel1);
        materiasScrollPane.setViewportView(materiasRegistradasList);
        estudianteScrollPane = new JScrollPane();
        panelGestion.add(estudianteScrollPane, new GridConstraints(2, 1, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        estudiantesRegistradosList = new JList();
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        estudiantesRegistradosList.setModel(defaultListModel2);
        estudianteScrollPane.setViewportView(estudiantesRegistradosList);
        profesorScrollPane = new JScrollPane();
        panelGestion.add(profesorScrollPane, new GridConstraints(10, 1, 2, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        profesoresRegistradosList = new JList();
        final DefaultListModel defaultListModel3 = new DefaultListModel();
        profesoresRegistradosList.setModel(defaultListModel3);
        profesorScrollPane.setViewportView(profesoresRegistradosList);
        materiasAddScrollPane = new JScrollPane();
        panelGestion.add(materiasAddScrollPane, new GridConstraints(6, 8, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        materiasAddList = new JList();
        materiasAddScrollPane.setViewportView(materiasAddList);
        estudiantesAddScrollPane = new JScrollPane();
        panelGestion.add(estudiantesAddScrollPane, new GridConstraints(2, 8, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        estudiantesAddList = new JList();
        estudiantesAddScrollPane.setViewportView(estudiantesAddList);
        profesorAddScrollPane = new JScrollPane();
        panelGestion.add(profesorAddScrollPane, new GridConstraints(10, 8, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        profesoresAddList = new JList();
        profesorAddScrollPane.setViewportView(profesoresAddList);
        removeMateriaButton = new JButton();
        removeMateriaButton.setText("<<");
        panelGestion.add(removeMateriaButton, new GridConstraints(7, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Paralelo");
        panelGestion.add(label10, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paralelo_jtext = new JTextField();
        panelGestion.add(paralelo_jtext, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cicloBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Primero");
        defaultComboBoxModel1.addElement("Segundo");
        defaultComboBoxModel1.addElement("Tercero");
        defaultComboBoxModel1.addElement("Cuarto");
        defaultComboBoxModel1.addElement("Quinto");
        defaultComboBoxModel1.addElement("Sexto");
        defaultComboBoxModel1.addElement("Septimo");
        cicloBox.setModel(defaultComboBoxModel1);
        panelGestion.add(cicloBox, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Cupos");
        panelGestion.add(label11, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cupoSpinner = new JSpinner();
        panelGestion.add(cupoSpinner, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tabbedPane2 = new JTabbedPane();
        gestionCursoPanel.add(tabbedPane2, new GridConstraints(1, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane2.addTab("Materia", panel1);
        crearButton = new JButton();
        crearButton.setText("Crear");
        gestionCursoPanel.add(crearButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button6 = new JButton();
        button6.setText("Button");
        gestionCursoPanel.add(button6, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        button7 = new JButton();
        button7.setText("Button");
        gestionCursoPanel.add(button7, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buscarButton = new JButton();
        buscarButton.setText("Buscar");
        gestionCursoPanel.add(buscarButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 16, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Gestionar Curso y Materia");
        gestionCursoPanel.add(label12, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        salirButton = new JButton();
        salirButton.setText("Salir");
        gestionCursoPanel.add(salirButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificarButton = new JButton();
        modificarButton.setText("Modificar");
        gestionCursoPanel.add(modificarButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelarButton = new JButton();
        cancelarButton.setText("Cancelar");
        gestionCursoPanel.add(cancelarButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return gestionCursoPanel;
    }

}
