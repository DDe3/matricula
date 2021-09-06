package com.ing_software;

import com.ing_software.entity.*;
import com.ing_software.forms.Administrador;
import com.ing_software.forms.Login;
import com.ing_software.repo.*;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Principal {

    public final static ExecutorService e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public static SeContainer se = SeContainerInitializer.newInstance().initialize();
    //public static SeContainer se;


    public static void main(String[] args) throws ExecutionException, InterruptedException {



//        JFrame login = new Login("Sistema de informacion");
//        login.setVisible(true);

//        EntityManagerFactory factory = Persistence
//                .createEntityManagerFactory("matricula");
//        EntityManager em = factory.createEntityManager();


        new Principal().test();

//        CuentaRepository repo = se.select(CuentaRepository.class).get();
//        Optional<Cuenta> aux = repo.findByName("correo2@uce.edu.ec","123456");
//        System.out.println(aux.isPresent());
//
//        CompletableFuture<Optional<Cuenta>> ret = CompletableFuture.supplyAsync(
//                () -> repo.findByName("correo2@uce.edu.ec", "123456"), e);
//        System.out.println(ret.get().isPresent());

//        System.out.println("Nombre: " + cuenta.getNombre() + " Password: " + cuenta.getPassword());
    //    List<Cuenta> cuentas = repo.findAll();
    //    cuentas.stream().forEach(x->System.out.println("Nombre: " + x.getNombre() + " Password: " + x.getPassword()));



    }

    @TransactionScoped
    public void test() {
//
//        EstudianteRepository repo1 = se.select(EstudianteRepository.class).get();
//        ProfesorRepository repo2 = se.select(ProfesorRepository.class).get();
//        AdministrativoRepository repo3 = se.select(AdministrativoRepository.class).get();


//        Cuenta prueba = new Cuenta();
//        prueba.setNombre("lasalinasb@uce.edu.ec");
//        prueba.setPassword("123456");
//
//        Estudiante e = new Estudiante();
//        e.setNombre("Leonardo Salinas");
//        e.setCedula("1725875569");
//        e.setTelefono("094358183");
//        e.setMail(prueba.getNombre());
//        e.setEstado(true);
//        e.setCuenta(prueba);
//        prueba.setOwner1(e);
//
//
//        Cuenta prueba2 = new Cuenta();
//        prueba2.setNombre("guamanjose@uce.edu.ec");
//        prueba2.setPassword("123456");
//
//        Profesor p = new Profesor();
//        p.setCedula("1725875503");
//        p.setNombre("Jose Guaman");
//        p.setTelefono("099845721");
//        p.setMail(prueba2.getNombre());
//        p.setTitulo("Licenciado en Pedagogia");
//        p.setCuenta(prueba2);
//        prueba2.setOwner2(p);


//        Cuenta prueba3 = new Cuenta();
//        prueba3.setNombre("administrador@hotmail.com");
//        prueba3.setPassword("123456");
//
//        Administrativo a = new Administrativo();
//        a.setCedula("1725875571");
//        a.setNombre("Milena Mosquera");
//        a.setTelefono("099845671");
//        a.setMail(prueba3.getNombre());
//        a.setCargo("Secretaria");
//        a.setCuenta(prueba3);
//        prueba3.setOwner3(a);

//        repo1.save(e);
//        repo2.save(p);
//        repo3.save(a);



//        EstudianteRepository repo1 = se.select(EstudianteRepository.class).get();
//        Estudiante e = repo1.findPorCedula("1725875569").get();
//        List<Matricula> mat = e.getMatriculasRegistradas();
//        System.out.println(mat.isEmpty());



//        CursoRepository repo5 = se.select(CursoRepository.class).get();
//        //MateriaRepository repo6 = se.select(MateriaRepository.class).get();
//        Materia m1 = new Materia();
//        m1.setNombre("Matematica");
//        m1.setCodigo("MM1");
//        m1.setDescripcion("Matematicas primer nivel");
//        Materia m2 = new Materia();
//        m2.setNombre("Lengua");
//        m2.setCodigo("L1");
//        m2.setDescripcion("Lengua primer nivel");
//        Materia m3 = new Materia();
//        m3.setNombre("Ciencias Sociales");
//        m3.setCodigo("CS1");
//        m3.setDescripcion("Ciencias Sociales primer nivel");
//        Materia m4 = new Materia();
//        m4.setNombre("Ciencias Naturales");
//        m4.setCodigo("CN1");
//        m4.setDescripcion("Ciencias Naturales primer nivel");
//        Materia m5 = new Materia();
//        m5.setNombre("Educacion fisica");
//        m5.setCodigo("EF1");
//        m5.setDescripcion("Educacion fisica primer nivel");
//        Materia m6 = new Materia();
//        m6.setNombre("Musica");
//        m6.setCodigo("MU1");
//        m6.setDescripcion("Musica primer nivel");
//        Materia m7 = new Materia();
//        m7.setNombre("Ingles");
//        m7.setCodigo("IN1");
//        m7.setDescripcion("Ingles primer nivel");
//
//        Curso c = new Curso();
//        c.setAula("A-12");
//        c.setParalelo('B');
//        c.setCiclo("Primero");
//        c.setDescripcion("Primero B");
//        c.setEstado(true);
//        List<Materia> materias = Arrays.asList(m1,m2,m3,m4,m5,m6,m7);
//        c.setMaterias(materias);
//        materias.forEach(x->x.addCurso(c));
//        repo5.save(c);
//
//
//        Cuenta prueba2 = new Cuenta();
//        prueba2.setNombre("msalas@uce.edu.ec");
//        prueba2.setPassword("PasswordP01");
//
//        Profesor p = new Profesor();
//        p.setCedula("1725875503");
//        p.setNombre("Jose Guaman");
//        p.setTelefono("099845721");
//        p.setMail(prueba2.getNombre());
//        p.setTitulo("Licenciado en Pedagogia");
//        p.setCuenta(prueba2);
//        prueba2.setOwner2(p);
//
//        c.setEncargado(p);
//        p.setCurso(c);
//
//        repo5.save(c);

//        List<Estudiante> op = repo1.findAll();
//        op.forEach(x-> System.out.println("Nombre: "+x.getNombre()));

//        EstudianteRepository repo1 = se.select(EstudianteRepository.class).get();
//        Optional<Estudiante> op = repo1.findPorCedula("1725875560");
//        System.out.println(op.isPresent());
//        Estudiante e = op.get();
//        System.out.println("Nombre cuenta: " +e.getCuenta().getNombre());
//        System.out.println("Pass cuenta: " +e.getCuenta().getPassword());
//        System.out.println(e.getMatriculasRegistradas());




        {
            CuentaRepository repo4 = se.select(CuentaRepository.class).get();
            Cuenta c = repo4.findBy(6);
            JFrame login = new Administrador("Sistema de informacion - Administrador",c);
            login.setVisible(true);
        }



    }

}
