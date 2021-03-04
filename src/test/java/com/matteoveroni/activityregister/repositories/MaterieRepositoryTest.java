package com.matteoveroni.activityregister.repositories;

import com.matteoveroni.activityregister.domain.Categoria;
import com.matteoveroni.activityregister.domain.Materia;
import com.matteoveroni.activityregister.tables.daos.CategorieDao;
import com.matteoveroni.activityregister.tables.daos.MaterieDao;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.sql.DataSource;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static com.matteoveroni.activityregister.Tables.CATEGORIE;
import static com.matteoveroni.activityregister.Tables.MATERIE_CATEGORIE;
import static com.matteoveroni.activityregister.tables.Materie.MATERIE;
import static org.jooq.impl.DSL.count;
import static org.jooq.impl.DSL.using;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
public class MaterieRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(MaterieRepositoryTest.class);

    private static final String DOCKER_IMAGE = "postgres:13.1-alpine";
    private static final String DB_MASTER_CHANGELOG = "db/liquibase/master.changelog.xml";
    private static final String DB_NAME = "study";
    private static final String DB_SCHEMA = "study";
    private static final String USER = "test";
    private static final String PWD = "test";

    @Container
    public static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(DOCKER_IMAGE)
            .withDatabaseName(DB_NAME)
            .withUsername(USER)
            .withPassword(PWD);

    private static HikariDataSource dataSource;
    private static Connection connection;
    private static Database database;
    private static Configuration sqlConfig;
    private static MaterieRepository materieRepository;

    @BeforeAll
    public static final void beforeAll() throws Exception {
        log.debug("Postgresql container running! JDBC URL => " + postgresContainer.getJdbcUrl());
        dataSource = buildDataSource();
        database = buildDatabase(dataSource);
        sqlConfig = new DefaultConfiguration()
                .set(dataSource)
                .set(SQLDialect.POSTGRES);
        materieRepository = new MaterieRepository(sqlConfig, new MaterieDao(sqlConfig), new CategorieDao(sqlConfig));
        applyChangesetsFromChangelog();
    }


    @AfterAll
    public static final void afterAll() throws DatabaseException, SQLException {
        if (dataSource != null && dataSource.isRunning()) {
            dataSource.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @BeforeEach
    public void beforeEach() {
        DSLContext dslContext = DSL.using(sqlConfig);
        dslContext.deleteFrom(CATEGORIE).execute();
        dslContext.deleteFrom(MATERIE).execute();
        dslContext.deleteFrom(MATERIE_CATEGORIE).execute();
    }

    @Test
    public void testSimple() {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        log.debug("JDBC URL => " + jdbcUrl);

        UUID idMateria = UUID.randomUUID();
        Materia materia = new Materia(idMateria, "Java", "Corso di java", true, new ArrayList<>());

        materieRepository.saveMateria(materia);

        int countResult = using(sqlConfig)
                .select(count(MATERIE.ID_MATERIA))
                .from(MATERIE)
                .fetchOne()
                .value1();

        assertEquals(1, countResult);
    }

    @Test
    public void testInsertCategorie() {
        Categoria categoria1 = new Categoria("informatica");
        Categoria categoria2 = new Categoria("informatica");
        Categoria categoria3 = new Categoria("religione");
        Categoria categoria4 = new Categoria(null);
        Categoria categoria5 = new Categoria(null);

        materieRepository.saveCategorie(categoria1, categoria2, categoria3, categoria4, categoria5);

        assertFalse(materieRepository.getCategorie().isEmpty(), "Errore, tabella categoria vuota!");
        assertEquals(4, materieRepository.getCategorie().size(), "Errore, non sono state inserite 4 categorie come da attese!");
    }

    @Test
    public void testInsertMaterie() {
        Categoria categoria1 = new Categoria("Programmazione");
        Categoria categoria2 = new Categoria("Informatica");
        Categoria categoria3 = new Categoria("Software development");
        List<Categoria> categorie = Collections.unmodifiableList(Arrays.asList(categoria1, categoria2, categoria3));

        Materia materia1 = new Materia("Java", "Corso di java", true, categorie);
        Materia materia2 = new Materia("Python", "Corso di python", true, categorie);

        materieRepository.saveMateria(materia1);

        assertFalse(materieRepository.getCategorie().isEmpty(), "Errore, tabella materie vuota!");
        assertEquals(1, materieRepository.getAll().size(), "Errore, non è stata inserita 1 materia come da attese!");
        assertFalse(materieRepository.getCategorie().isEmpty(), "Errore, tabella categoria vuota!");
        assertEquals(3, materieRepository.getCategorie().size(), "Errore, non sono state inserite 3 categorie come da attese!");
    }

    private static HikariDataSource buildDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgresContainer.getJdbcUrl());
        hikariConfig.setUsername(postgresContainer.getUsername());
        hikariConfig.setPassword(postgresContainer.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    private static Database buildDatabase(DataSource dataSource) throws SQLException, DatabaseException {
        connection = dataSource.getConnection();

        String createSchemaSQL = String.format("CREATE SCHEMA %s", DB_SCHEMA);
        log.debug("createSchemaSQL => " + createSchemaSQL);
        connection.prepareStatement(createSchemaSQL)
                .execute();

        String setSearchPathSQL = String.format("SET search_path TO %s", DB_SCHEMA);
        log.debug("searchPath => " + setSearchPathSQL);
        connection.prepareStatement(setSearchPathSQL)
                .execute();

        Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(new JdbcConnection(connection));
        database.setDefaultSchemaName(DB_SCHEMA);

        return database;
    }

    private static void applyChangesetsFromChangelog() throws LiquibaseException {
        Liquibase liquibase = new Liquibase(DB_MASTER_CHANGELOG, new ClassLoaderResourceAccessor(), database);
        List<ChangeSet> changeSets = liquibase.getDatabaseChangeLog().getChangeSets();
        changeSets.forEach(System.out::println);
        liquibase.update(new Contexts());
    }
}
