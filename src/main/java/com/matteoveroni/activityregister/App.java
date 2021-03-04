package com.matteoveroni.activityregister;

import com.matteoveroni.activityregister.config.DataSourceConfig;
import com.matteoveroni.activityregister.repositories.MaterieRepository;
import com.matteoveroni.activityregister.tables.daos.CategorieDao;
import com.matteoveroni.activityregister.tables.daos.MaterieDao;
import javax.sql.DataSource;
import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static final void main(String[] args) {
        new App().start();
    }

    private void start() {
        log.info("Hello from log");

        DataSource dataSource = DataSourceConfig.getDataSource();
        Configuration sqlConfiguration = new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.POSTGRES);

        MaterieDao materieDao = new MaterieDao(sqlConfiguration);
        CategorieDao categorieDao = new CategorieDao(sqlConfiguration);

        MaterieRepository materieRepository = new MaterieRepository(sqlConfiguration, materieDao, categorieDao);
        materieRepository.getAll().forEach(a -> System.out.println("categorie => " + a.getCategorie()));
//
//        Optional<Materia> materia = materieRepository.get(UUID.fromString("ae11f753-65c0-48fb-b234-563c8226dc24"));
//        Materia materia1 = materia.get();
//        List<Categoria> categorie = materia1.getCategorie();
//        System.out.println("categoria nome: " + categorie);

//        Optional<Materia> materia = materieRepository.get(UUID.fromString("e0c68302-0c5f-4b5a-a686-a0d27f1fea18"));
//        Optional<Materia> materia = materieRepository.get(UUID.fromString("ec938fa6-c51e-4062-9b0e-04e9dc699e7f"));
//        materia.ifPresent(m -> {
//            System.out.println("Materia: " + m.toString());
//
//            List<Categoria> categorie = m.getCategorie();
//            for(Categoria categoria: categorie) {
//                System.out.println("contiene la categoria: " + categoria);
//            }
//        });


//        MaterieRepositoryOld materieRepositoryOld = new MaterieRepositoryOld(sqlConfiguration);
//        CategorieRepository categorieRepository = new CategorieRepository(sqlConfiguration);
//        Optional<MateriaDto> materiaDto = materieRepository.get(UUID.fromString("e0c68302-0c5f-4b5a-a686-a0d27f1fea18"));
//        Optional<MateriaDto> materiaDto = materieRepository.get(UUID.fromString("ec938fa6-c51e-4062-9b0e-04e9dc699e7f"));
//        Optional<MateriaDto> materiaDto = materieRepository.get(UUID.randomUUID());
//        materiaDto.ifPresent(System.out::println);


//        MaterieDao materieDao = new MaterieDao(sqlConfiguration);
//        Materie materie = materieDao.fetchOneByIdMateria(UUID.fromString("e0c68302-0c5f-4b5a-a686-a0d27f1fea18"));

//        System.out.println("materies " + materie);


//        List<CategoriaDto> categorie = materiaDto.get().getCategorie();
//        System.out.println("aaaaaaaaaa " + categorie.get(0) + " " + categorie.get(0).getClass());

//        materieRepository.save(materiaDto.get());

//        System.out.println((categorie.isEmpty()) ? "si" : "no");
//        List<MateriaDto> all = materieRepository.getAll();
//        all.forEach(System.out::println);

//        materieRepository.clearTable();
//        categorieRepository.clearTable();
//
//        Materia materia = new Materia("Java", "Corso di java", false);
//        materieRepository.save(materia);
//
//        Categoria categoria = new Categoria("Informatica");
//        categorieRepository.save(categoria);
//
//        materia.setNome("Python");
//        materieRepository.save(materia);
//
//        List<Categoria> categorie = categorieRepository.getAll();
//        log.debug("categorie: " + categorie);
//
//        List<Materia> materie = materieRepository.getAll();
//        log.debug("materie: " + materie);

//        materieRepository.getCategorieForMateria(UUID.fromString("e0c68302-0c5f-4b5a-a686-a0d27f1fea18"))
//                .forEach(System.out::println);

//        UUID uuidAutogeneratoDalDb = materieRepository.save(new MateriaDto("Kotlin", "Corso di kotlin", true));
//        System.out.println("uuid => " + uuidAutogeneratoDalDb);
    }

}
