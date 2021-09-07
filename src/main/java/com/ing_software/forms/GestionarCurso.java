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
import javax.swing.border.TitledBorder;
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
    private JTextField aula_jtext;
    private JList estudiantesRegistradosList;
    private JList materiasRegistradasList;
    private JTextField est_jtext;
    private JButton addEstudianteButton;
    private JButton removeEstudianteButton;
    private JButton addMateriaButton;
    private JButton removeMateriaButton;
    private JButton crearButton;
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
                            DefaultListModel<Estudiante> model = (DefaultListModel<Estudiante>) estudiantesAddList.getModel();
                            model.addElement(estudiante);
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
                    servicioCurso.removeMaterias(curso, materia);
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
                    servicioCurso.removeProfesor(curso, profesor);
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


            }
        });
        registrarCursoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                        mcase(() -> materias.size() > 7, () -> failure("Rango de materias valido: [1-7]")));


                result.bind(x -> {
                            System.out.println("Profesor: " + profesors);
                            System.out.println("Materias: " + materias);
                            System.out.println("Estudiantes : " + estudiantes);
                            servicioCurso.crearCursoAndSave(aula, paralelo.charAt(0), ciclo, profesors.isEmpty() ? null : profesors.get(0), materias, estudiantes, cupos);
                            JOptionPane.showMessageDialog(null, "Curso creado con exito");
                            servicioEstudiante.clear();
                            clearText();
                            setup();

                        }, x -> JOptionPane.showMessageDialog(null, "No se pudo crear el curso\nError: " + x)
                );
                servicioEstudiante.clear();

            }
        });
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cerrar();
            }
        });
    }

    private void cerrar() {
        this.dispose();
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
        gestionCursoPanel.setLayout(new GridLayoutManager(4, 7, new Insets(5, 5, 5, 5), -1, -1));
        gestionCursoPanel.setBackground(new Color(-1973802));
        gestionCursoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-1249292)));
        cursoPanel = new JTabbedPane();
        cursoPanel.setBackground(new Color(-7100224));
        cursoPanel.setForeground(new Color(-16777216));
        gestionCursoPanel.add(cursoPanel, new GridConstraints(1, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 1, false));
        cursoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        panelGestion = new JPanel();
        panelGestion.setLayout(new GridLayoutManager(17, 10, new Insets(5, 5, 5, 5), -1, -1));
        panelGestion.setBackground(new Color(-7100224));
        cursoPanel.addTab("Cursos", panelGestion);
        panelGestion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        aula_jtext = new JTextField();
        aula_jtext.setBackground(new Color(-6255970));
        panelGestion.add(aula_jtext, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Aula");
        panelGestion.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Materias");
        panelGestion.add(label2, new GridConstraints(7, 0, 4, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        final JLabel label3 = new JLabel();
        Font label3Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label3.getFont());
        if (label3Font != null) label3.setFont(label3Font);
        label3.setText("Ciclo");
        panelGestion.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        final JLabel label4 = new JLabel();
        Font label4Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label4.getFont());
        if (label4Font != null) label4.setFont(label4Font);
        label4.setText("Estudiantes");
        panelGestion.add(label4, new GridConstraints(2, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        listarMateriasButton = new JButton();
        listarMateriasButton.setBackground(new Color(-2639983));
        Font listarMateriasButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, listarMateriasButton.getFont());
        if (listarMateriasButtonFont != null) listarMateriasButton.setFont(listarMateriasButtonFont);
        listarMateriasButton.setText("Listar Materias");
        panelGestion.add(listarMateriasButton, new GridConstraints(11, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 30), null, 0, false));
        listarEstudianteButton = new JButton();
        listarEstudianteButton.setBackground(new Color(-2639983));
        Font listarEstudianteButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, listarEstudianteButton.getFont());
        if (listarEstudianteButtonFont != null) listarEstudianteButton.setFont(listarEstudianteButtonFont);
        listarEstudianteButton.setText("Listar Estudiantes");
        panelGestion.add(listarEstudianteButton, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        est_jtext = new JTextField();
        est_jtext.setBackground(new Color(-6255970));
        panelGestion.add(est_jtext, new GridConstraints(5, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        Font label5Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label5.getFont());
        if (label5Font != null) label5.setFont(label5Font);
        label5.setText("CDI:");
        panelGestion.add(label5, new GridConstraints(5, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(71, 16), null, 0, false));
        addPorCDIButton = new JButton();
        addPorCDIButton.setBackground(new Color(-2639983));
        Font addPorCDIButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, addPorCDIButton.getFont());
        if (addPorCDIButtonFont != null) addPorCDIButton.setFont(addPorCDIButtonFont);
        addPorCDIButton.setText("Add");
        panelGestion.add(addPorCDIButton, new GridConstraints(5, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addEstudianteButton = new JButton();
        addEstudianteButton.setBackground(new Color(-2639983));
        Font addEstudianteButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, addEstudianteButton.getFont());
        if (addEstudianteButtonFont != null) addEstudianteButton.setFont(addEstudianteButtonFont);
        addEstudianteButton.setText(">>");
        panelGestion.add(addEstudianteButton, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeEstudianteButton = new JButton();
        removeEstudianteButton.setBackground(new Color(-2639983));
        Font removeEstudianteButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, removeEstudianteButton.getFont());
        if (removeEstudianteButtonFont != null) removeEstudianteButton.setFont(removeEstudianteButtonFont);
        removeEstudianteButton.setText("<<");
        panelGestion.add(removeEstudianteButton, new GridConstraints(3, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addMateriaButton = new JButton();
        addMateriaButton.setBackground(new Color(-2639983));
        Font addMateriaButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, addMateriaButton.getFont());
        if (addMateriaButtonFont != null) addMateriaButton.setFont(addMateriaButtonFont);
        addMateriaButton.setText(">>");
        panelGestion.add(addMateriaButton, new GridConstraints(7, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Estudiantes de este Curso");
        panelGestion.add(label6, new GridConstraints(1, 8, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Materias de este Curso");
        panelGestion.add(label7, new GridConstraints(6, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Profesores");
        panelGestion.add(label8, new GridConstraints(13, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(74, 16), null, 0, false));
        addProfesorButton = new JButton();
        addProfesorButton.setBackground(new Color(-2639983));
        Font addProfesorButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, addProfesorButton.getFont());
        if (addProfesorButtonFont != null) addProfesorButton.setFont(addProfesorButtonFont);
        addProfesorButton.setText(">>");
        panelGestion.add(addProfesorButton, new GridConstraints(13, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeProfesorButton = new JButton();
        removeProfesorButton.setBackground(new Color(-2639983));
        Font removeProfesorButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, removeProfesorButton.getFont());
        if (removeProfesorButtonFont != null) removeProfesorButton.setFont(removeProfesorButtonFont);
        removeProfesorButton.setText("<<");
        panelGestion.add(removeProfesorButton, new GridConstraints(14, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Profesor de este Curso");
        panelGestion.add(label9, new GridConstraints(12, 8, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listarProfesoresButton = new JButton();
        listarProfesoresButton.setBackground(new Color(-2639983));
        Font listarProfesoresButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, listarProfesoresButton.getFont());
        if (listarProfesoresButtonFont != null) listarProfesoresButton.setFont(listarProfesoresButtonFont);
        listarProfesoresButton.setText("Listar Profesores");
        panelGestion.add(listarProfesoresButton, new GridConstraints(16, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        materiasScrollPane = new JScrollPane();
        panelGestion.add(materiasScrollPane, new GridConstraints(7, 1, 3, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        materiasRegistradasList = new JList();
        materiasRegistradasList.setBackground(new Color(-6255970));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        materiasRegistradasList.setModel(defaultListModel1);
        materiasScrollPane.setViewportView(materiasRegistradasList);
        estudianteScrollPane = new JScrollPane();
        estudianteScrollPane.setBackground(new Color(-6255970));
        panelGestion.add(estudianteScrollPane, new GridConstraints(2, 1, 3, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        estudiantesRegistradosList = new JList();
        estudiantesRegistradosList.setBackground(new Color(-6255970));
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        estudiantesRegistradosList.setModel(defaultListModel2);
        estudianteScrollPane.setViewportView(estudiantesRegistradosList);
        profesorScrollPane = new JScrollPane();
        panelGestion.add(profesorScrollPane, new GridConstraints(13, 1, 3, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        profesoresRegistradosList = new JList();
        profesoresRegistradosList.setBackground(new Color(-6255970));
        final DefaultListModel defaultListModel3 = new DefaultListModel();
        profesoresRegistradosList.setModel(defaultListModel3);
        profesorScrollPane.setViewportView(profesoresRegistradosList);
        materiasAddScrollPane = new JScrollPane();
        panelGestion.add(materiasAddScrollPane, new GridConstraints(7, 8, 4, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        materiasAddList = new JList();
        materiasAddList.setBackground(new Color(-6255970));
        materiasAddScrollPane.setViewportView(materiasAddList);
        estudiantesAddScrollPane = new JScrollPane();
        panelGestion.add(estudiantesAddScrollPane, new GridConstraints(2, 8, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        estudiantesAddList = new JList();
        estudiantesAddList.setBackground(new Color(-6255970));
        estudiantesAddScrollPane.setViewportView(estudiantesAddList);
        profesorAddScrollPane = new JScrollPane();
        panelGestion.add(profesorAddScrollPane, new GridConstraints(13, 8, 2, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        profesoresAddList = new JList();
        profesoresAddList.setBackground(new Color(-6255970));
        profesorAddScrollPane.setViewportView(profesoresAddList);
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setText("Paralelo");
        panelGestion.add(label10, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        paralelo_jtext = new JTextField();
        paralelo_jtext.setBackground(new Color(-6255970));
        panelGestion.add(paralelo_jtext, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cicloBox = new JComboBox();
        cicloBox.setBackground(new Color(-6255970));
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
        Font label11Font = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 12, label11.getFont());
        if (label11Font != null) label11.setFont(label11Font);
        label11.setText("Cupos");
        panelGestion.add(label11, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cupoSpinner = new JSpinner();
        cupoSpinner.setBackground(new Color(-6255970));
        Font cupoSpinnerFont = this.$$$getFont$$$(null, -1, -1, cupoSpinner.getFont());
        if (cupoSpinnerFont != null) cupoSpinner.setFont(cupoSpinnerFont);
        panelGestion.add(cupoSpinner, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMateriaButton = new JButton();
        removeMateriaButton.setBackground(new Color(-2639983));
        Font removeMateriaButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 16, removeMateriaButton.getFont());
        if (removeMateriaButtonFont != null) removeMateriaButton.setFont(removeMateriaButtonFont);
        removeMateriaButton.setText("<<");
        panelGestion.add(removeMateriaButton, new GridConstraints(8, 7, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(78, 48), null, 0, false));
        crearButton = new JButton();
        crearButton.setBackground(new Color(-2639983));
        Font crearButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, crearButton.getFont());
        if (crearButtonFont != null) crearButton.setFont(crearButtonFont);
        crearButton.setForeground(new Color(-6255970));
        crearButton.setText("Crear");
        gestionCursoPanel.add(crearButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        Font label12Font = this.$$$getFont$$$("JetBrains Mono", Font.PLAIN, 22, label12.getFont());
        if (label12Font != null) label12.setFont(label12Font);
        label12.setText("Gestionar Curso y Materia");
        gestionCursoPanel.add(label12, new GridConstraints(0, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modificarButton = new JButton();
        modificarButton.setBackground(new Color(-2639983));
        Font modificarButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, modificarButton.getFont());
        if (modificarButtonFont != null) modificarButton.setFont(modificarButtonFont);
        modificarButton.setForeground(new Color(-6255970));
        modificarButton.setText("Modificar");
        gestionCursoPanel.add(modificarButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registrarCursoButton = new JButton();
        registrarCursoButton.setBackground(new Color(-2639983));
        Font registrarCursoButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, registrarCursoButton.getFont());
        if (registrarCursoButtonFont != null) registrarCursoButton.setFont(registrarCursoButtonFont);
        registrarCursoButton.setForeground(new Color(-6255970));
        registrarCursoButton.setText("Registrar Curso");
        gestionCursoPanel.add(registrarCursoButton, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buscarButton = new JButton();
        buscarButton.setBackground(new Color(-2639983));
        Font buscarButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, buscarButton.getFont());
        if (buscarButtonFont != null) buscarButton.setFont(buscarButtonFont);
        buscarButton.setForeground(new Color(-6255970));
        buscarButton.setText("Buscar");
        gestionCursoPanel.add(buscarButton, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        salirButton = new JButton();
        salirButton.setBackground(new Color(-2639983));
        Font salirButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, salirButton.getFont());
        if (salirButtonFont != null) salirButton.setFont(salirButtonFont);
        salirButton.setForeground(new Color(-6255970));
        salirButton.setText("Salir");
        gestionCursoPanel.add(salirButton, new GridConstraints(3, 3, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cancelarButton = new JButton();
        cancelarButton.setBackground(new Color(-2639983));
        Font cancelarButtonFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 14, cancelarButton.getFont());
        if (cancelarButtonFont != null) cancelarButton.setFont(cancelarButtonFont);
        cancelarButton.setForeground(new Color(-6255970));
        cancelarButton.setText("Cancelar");
        gestionCursoPanel.add(cancelarButton, new GridConstraints(2, 3, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
