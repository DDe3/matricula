package com.ing_software;

import com.ing_software.entity.Administrativo;
import com.ing_software.entity.Cuenta;
import com.ing_software.entity.Estudiante;
import com.ing_software.entity.Profesor;
import com.ing_software.forms.Login;
import com.ing_software.repo.AdministrativoRepository;
import com.ing_software.repo.CuentaRepository;
import com.ing_software.repo.EstudianteRepository;
import com.ing_software.repo.ProfesorRepository;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Principal {

    public final static ExecutorService e = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public static SeContainer se = SeContainerInitializer.newInstance().initialize();


    public static void main(String[] args) throws ExecutionException, InterruptedException {



        JFrame login = new Login("Sistema de informacion");
        login.setVisible(true);


        //new Principal().test();

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

//
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
//        prueba.setNombre("correo1@uce.edu.ec");
//        prueba.setPassword("123456");
//
//        Profesor p = new Profesor();
//        p.setCedula("1725875579");
//        p.setNombre("Marco Salas");
//        p.setTelefono("2116615");
//        p.setMail(prueba2.getNombre());
//        p.setTitulo("Ingeniero en Computación");
//        p.setCuenta(prueba2);
//        prueba2.setOwner2(p);
//
//
//        Cuenta prueba3 = new Cuenta();
//        prueba.setNombre("correo2@uce.edu.ec");
//        prueba.setPassword("123456");
//
//        Administrativo a = new Administrativo();
//        a.setCedula("1725875571");
//        a.setNombre("Milena Mosquera");
//        a.setTelefono("2115615");
//        a.setMail(prueba3.getNombre());
//        a.setCargo("Secretaria");
//        a.setCuenta(prueba3);
//        prueba3.setOwner3(a);
//
//        repo1.save(e);
//        repo2.save(p);
//        repo3.save(a);

//        AdministrativoRepository repo3 = se.select(AdministrativoRepository.class).get();
//        CuentaRepository repo4 = se.select(CuentaRepository.class).get();
//        Cuenta c = repo4.findBy(6);
//        c.setNombre("mosquera@uce.edu.ec");
//        c.setPassword("123456");
//        repo4.save(c);


    }

}
