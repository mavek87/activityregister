package com.matteoveroni.activityregister.repositories;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.matteoveroni.activityregister.domain.Categoria;
import com.matteoveroni.activityregister.domain.Materia;
import com.matteoveroni.activityregister.tables.daos.CategorieDao;
import com.matteoveroni.activityregister.tables.daos.MaterieDao;
import com.matteoveroni.activityregister.tables.records.MaterieRecord;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jooq.*;
import org.jooq.impl.DSL;
import static com.matteoveroni.activityregister.tables.Categorie.CATEGORIE;
import static com.matteoveroni.activityregister.tables.Materie.MATERIE;
import static com.matteoveroni.activityregister.tables.MaterieCategorie.MATERIE_CATEGORIE;
import static org.jooq.JSON.json;
import static org.jooq.impl.DSL.*;

public class MaterieRepository {

    private final Gson gson = new Gson();
    private final Configuration sqlConfig;
    private final DSLContext dslContext;
    private MaterieDao materieDao;
    private CategorieDao categorieDao;

    public MaterieRepository(Configuration sqlConfiguration, MaterieDao materieDao, CategorieDao categorieDao) {
        this.sqlConfig = sqlConfiguration;
        this.materieDao = materieDao;
        this.categorieDao = categorieDao;
        this.dslContext = using(sqlConfig);
    }

    public long count() {
        return dslContext
                .select(DSL.count(MATERIE.ID_MATERIA))
                .from(MATERIE)
                .fetchOne(0, long.class);
    }

    public Optional<Materia> get(UUID id) {
        Record5<UUID, String, String, Boolean, JSONB> materia = selectMaterieWithCategorie()
                .where(MATERIE.ID_MATERIA.eq(id))
                .orderBy(MATERIE.NOME)
                .fetchAny();

        if (materia == null) {
            return Optional.empty();
        } else {
            return Optional.of(mapRecordToMateriaWithCategoria(materia));
        }
    }

    public List<Materia> getAll() {
        return selectMaterieWithCategorie()
                .orderBy(MATERIE.NOME)
                .fetch()
                .map(this::mapRecordToMateriaWithCategoria);
    }

    public void saveMateria(Materia materia) {
        dslContext
                .transaction(config -> {
                    MaterieRecord materiaRecord = using(sqlConfig).newRecord(MATERIE);
                    materiaRecord.setIdMateria(materia.getIdMateria());
                    materiaRecord.setNome(materia.getNome());
                    materiaRecord.setDescrizione(materia.getDescrizione());
                    materiaRecord.setStudioAttivo(materia.getIsStudioAttivo());
                    materiaRecord.store();

                    List<Categoria> categorieMateria = materia.getCategorie();
                    // salvo tutte le categorie
                    if (!categorieMateria.isEmpty()) {
                        saveCategorie(categorieMateria);

                        // salvo tutte le materie_categorie inedite
                    }
                });
    }

    public List<UUID> saveAll(Materia... materie) {
        List<UUID> idMaterie = new ArrayList<>();
        for (Materia materia : materie) {
            saveMateria(materia);
        }
        return idMaterie;
    }

    public void deleteById(UUID id) {
        dslContext
                .deleteFrom(MATERIE)
                .where(MATERIE.ID_MATERIA.eq(id))
                .execute();
    }

    public void delete(Materia entity) {
        dslContext
                .newRecord(MATERIE, entity)
                .delete();
    }

    public void clear() {
        dslContext
                .deleteFrom(MATERIE)
                .execute();
    }

    private SelectOnConditionStep<Record5<UUID, String, String, Boolean, JSONB>> selectMaterieWithCategorie() {
        return dslContext
                .with("matcat").as
                        (
                                select(
                                        MATERIE_CATEGORIE.ID_MATERIA.as("id_materia"),
                                        jsonArrayAgg(jsonObject(
                                                CATEGORIE.ID_CATEGORIA, CATEGORIE.NOME_CATEGORIA)
                                        ).as("json_categorie")
                                )
                                        .from(MATERIE_CATEGORIE)
                                        .join(CATEGORIE)
                                        .on(MATERIE_CATEGORIE.ID_CATEGORIA.eq(CATEGORIE.ID_CATEGORIA))
                                        .groupBy(MATERIE_CATEGORIE.ID_MATERIA)
                        )
                .select(
                        MATERIE.ID_MATERIA,
                        MATERIE.NOME,
                        MATERIE.DESCRIZIONE,
                        MATERIE.STUDIO_ATTIVO,
                        coalesce(field(name("matcat", "json_categorie"), JSONB.class), inline(json("[]"))).as("json_categorie")
                )
                .from(MATERIE)
                .leftJoin(table(name("matcat")))
                .on(MATERIE.ID_MATERIA.eq(field(name("matcat", "id_materia"), MATERIE.ID_MATERIA.getDataType())));
    }

    private Materia mapRecordToMateriaWithCategoria(Record5<UUID, String, String, Boolean, JSONB> record) {
        List<Categoria> categorie = new ArrayList<>();
        String jsonCategorie = record.get("json_categorie", String.class);
        for (JsonElement jsonCategoria : JsonParser.parseString(jsonCategorie).getAsJsonArray()) {
            Categoria categoria = gson.fromJson(jsonCategoria, Categoria.class);
            categorie.add(categoria);
        }
        return new Materia(
                record.get("id_materia", UUID.class),
                record.get("nome", String.class),
                record.get("descrizione", String.class),
                record.get("studio_attivo", Boolean.class),
                categorie
        );
    }

    void saveCategorie(List<Categoria> categorie) {
        saveCategorie(categorie.stream());
    }

    void saveCategorie(Categoria... categorie) {
        saveCategorie(Arrays.stream(categorie));
    }

    void saveCategorie(Stream<Categoria> streamCategorie) {
        foldLeft(streamCategorie,
                dslContext
                        .insertInto(CATEGORIE)
                        .columns(CATEGORIE.ID_CATEGORIA, CATEGORIE.NOME_CATEGORIA),
                (insertStep, recordToInsert) ->
                        insertStep.values(recordToInsert.getIdCategoria(), recordToInsert.getNomeCategoria())
        )
                .onConflictDoNothing()
                .execute();
    }

    List<Categoria> getCategorie() {
        return dslContext
                .selectFrom(CATEGORIE)
                .fetchInto(Categoria.class);
    }

    private static <T, U> U foldLeft(Stream<T> stream, U i, BiFunction<U, T, U> function) {
        return stream.<Function<U, U>>map(e -> r -> function.apply(r, e))
                .reduce(Function.identity(), Function::andThen).apply(i);
    }


}
