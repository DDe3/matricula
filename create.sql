create sequence hibernate_sequence start 1 increment 1

    create table Administrativo (
       id int4 not null,
        edad int4,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        cargo varchar(255),
        observaciones varchar(255),
        primary key (id)
    )

    create table Cuenta (
       id int4 not null,
        nombre varchar(255),
        password varchar(255),
        estudiante_id int4,
        profesor_id int4,
        administrativo_id int4,
        primary key (id)
    )

    create table Curso (
       id int4 not null,
        aula varchar(255),
        ciclo varchar(255),
        descripcion varchar(255),
        estado boolean,
        paralelo char(1),
        profesor_id int4,
        primary key (id)
    )

    create table Estudiante (
       id int4 not null,
        edad int4,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        estado boolean,
        f_inscripcion date,
        f_nacimiento date,
        observaciones varchar(255),
        curso_id int4,
        representante_id int4,
        primary key (id)
    )

    create table Materia (
       id int4 not null,
        descripcion varchar(255),
        nombre varchar(255),
        curso_id int4,
        primary key (id)
    )

    create table Matricula (
       id int4 not null,
        ciclo varchar(255),
        estado boolean,
        f_matricula date,
        paralelo char(1),
        estudiante_id int4,
        primary key (id)
    )

    create table Nota (
       id int4 not null,
        aporteDeberes float8,
        aporteExamen float8,
        aporteLecciones float8,
        notaFinal float8,
        notaSupletorio float8,
        materia_id int4,
        estudiante_id int4,
        primary key (id)
    )

    create table Profesor (
       id int4 not null,
        edad int4,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        observaciones varchar(255),
        titulo varchar(255),
        primary key (id)
    )

    create table Representante (
       id int4 not null,
        edad int4,
        mail varchar(255),
        nombre varchar(255),
        telefono varchar(255),
        lugarTrabajo varchar(255),
        primary key (id)
    )

    alter table if exists Cuenta 
       add constraint FKn2ktsykioh4lnebnn94nm9ubi 
       foreign key (estudiante_id) 
       references Estudiante

    alter table if exists Cuenta 
       add constraint FKc7hmh8w5c2myyqus8hw6tscvx 
       foreign key (profesor_id) 
       references Profesor

    alter table if exists Cuenta 
       add constraint FKmnou9rijxmhel5i3tlpdwfi78 
       foreign key (administrativo_id) 
       references Administrativo

    alter table if exists Curso 
       add constraint FKmsrvpxe6d1gq9cdt590isqaon 
       foreign key (profesor_id) 
       references Profesor

    alter table if exists Estudiante 
       add constraint FKc709yluvloqwqdy7i1nje7l2q 
       foreign key (curso_id) 
       references Curso

    alter table if exists Estudiante 
       add constraint FKkj7sad6cdak5vqsp59l5kky7h 
       foreign key (representante_id) 
       references Representante

    alter table if exists Materia 
       add constraint FKtki0lpts2vtpm164duw85pugj 
       foreign key (curso_id) 
       references Curso

    alter table if exists Matricula 
       add constraint FKsly6if0ddpx3n2ober3eb05vn 
       foreign key (estudiante_id) 
       references Estudiante

    alter table if exists Nota 
       add constraint FK7uyomov1gu854x246952o6t3j 
       foreign key (materia_id) 
       references Materia

    alter table if exists Nota 
       add constraint FKqqpgn9wo6y1eblfil6s6s1r9t 
       foreign key (estudiante_id) 
       references Estudiante
