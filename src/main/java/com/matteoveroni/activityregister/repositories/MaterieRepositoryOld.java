//package com.matteoveroni.activityregister.repositories;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.matteoveroni.activityregister.domain.Materia;
//import com.matteoveroni.activityregister.tables.pojos.Categorie;
//import com.matteoveroni.activityregister.tables.records.MaterieRecord;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.concurrent.atomic.AtomicReference;
//import org.jooq.Configuration;
//import org.jooq.JSONB;
//import org.jooq.Record5;
//import org.jooq.SelectOnConditionStep;
//import org.jooq.impl.DSL;
//import static com.matteoveroni.activityregister.tables.Categorie.CATEGORIE;
//import static com.matteoveroni.activityregister.tables.Materie.MATERIE;
//import static com.matteoveroni.activityregister.tables.MaterieCategorie.MATERIE_CATEGORIE;
//import static org.jooq.impl.DSL.*;
//
//public class MaterieRepositoryOld {
//
//    private final Gson gson = new Gson();
//    private final Configuration sqlConfig;
//
//    public MaterieRepositoryOld(Configuration sqlConfiguration) {
//        this.sqlConfig = sqlConfiguration;
//    }
//
//    public long count() {
//        return using(sqlConfig)
//                .select(DSL.count(MATERIE.ID_MATERIA))
//                .from(MATERIE)
//                .fetchOne(0, long.class);
//    }
//
//    public Optional<Materia> get(UUID id) {
//        Record5<UUID, String, String, Boolean, JSONB> materie = selectMaterie()
//                .where(MATERIE.ID_MATERIA.eq(id))
//                .orderBy(MATERIE.NOME)
////                        .fetchAnyInto(MateriaDto.class)
//                .fetchAny();
//
//        if (materie == null) {
//            return Optional.empty();
//        } else {
//            return Optional.of(
//                    materie
//                            .map(record -> {
//                                List<Categorie> categorie = new ArrayList<>();
//                                String jsonCategorie = record.get("json_categorie", String.class);
//                                for (JsonElement jsonCategoria : JsonParser.parseString(jsonCategorie).getAsJsonArray()) {
//                                    categorie.add(gson.fromJson(jsonCategoria, Categorie.class));
//                                }
//                                return new Materia(
//                                        record.get("id_materia", UUID.class),
//                                        record.get("nome", String.class),
//                                        record.get("descrizione", String.class),
//                                        record.get("studio_attivo", Boolean.class),
//                                        categorie
//                                );
//                            })
//            );
//        }
//    }
//
//    public List<Materia> getAll() {
//        return selectMaterie()
//                .orderBy(MATERIE.NOME)
//                .fetchInto(Materia.class);
//    }
//
//    public UUID save(Materia materia) {
//        AtomicReference<MaterieRecord> ar = new AtomicReference<>();
//
//        using(sqlConfig)
//                .transaction(config -> {
//                    MaterieRecord materiaRecord = using(sqlConfig).newRecord(MATERIE);
//                    materiaRecord.setNome(materia.getNome());
//                    materiaRecord.setDescrizione(materia.getDescrizione());
//                    materiaRecord.setStudioAttivo(materia.getIsStudioAttivo());
//                    materiaRecord.store();
//                    ar.set(materiaRecord);
//
//                    for (Categorie categoria : materia.getCategorie()) {
//                        System.out.printf("categoria " + categoria);
////                        MaterieCategorieRecord materieCategorie = using(sqlConfig).newRecord(MATERIE_CATEGORIE);
////                        materieCategorie.setIdMateria(materiaRecord.getIdMateria());
////                        materieCategorie.setIdCategoria(categoria.getIdCategoria());
////                        materieCategorie.store();
//                    }
//                });
//
//        return ar.get().getIdMateria();
//    }
//
//    public List<UUID> saveAll(Materia... materie) {
//        List<UUID> idMaterie = new ArrayList<>();
//        for (Materia materia : materie) {
//            save(materia);
//        }
//        return idMaterie;
//    }
//
//    public void deleteById(UUID id) {
//        using(sqlConfig)
//                .deleteFrom(MATERIE)
//                .where(MATERIE.ID_MATERIA.eq(id))
//                .execute();
//    }
//
//    public void delete(Materia entity) {
//        using(sqlConfig)
//                .newRecord(MATERIE, entity)
//                .delete();
//    }
//
//    public void clearTable() {
//        using(sqlConfig)
//                .deleteFrom(MATERIE)
//                .execute();
//    }
//
//    private SelectOnConditionStep<Record5<UUID, String, String, Boolean, JSONB>> selectMaterie() {
//        return using(sqlConfig)
//                .with("matcat").as
//                        (
//                                select(
//                                        MATERIE_CATEGORIE.ID_MATERIA.as("id_materia"),
//                                        jsonArrayAgg(jsonObject(
//                                                CATEGORIE.ID_CATEGORIA, CATEGORIE.NOME_CATEGORIA)
//                                        )
//                                                .filterWhere(
//                                                        field(name("matcat", "json_categorie")).ne(val(null, JSONB.class))
//                                                )
//                                                .as("json_categorie")
//                                )
//                                        .from(MATERIE_CATEGORIE)
//                                        .join(CATEGORIE)
//                                        .on(MATERIE_CATEGORIE.ID_CATEGORIA.eq(CATEGORIE.ID_CATEGORIA))
//                                        .groupBy(MATERIE_CATEGORIE.ID_MATERIA)
//                        )
//                .select(
//                        MATERIE.ID_MATERIA,
//                        MATERIE.NOME,
//                        MATERIE.DESCRIZIONE,
//                        MATERIE.STUDIO_ATTIVO,
//                        field(name("matcat", "json_categorie"), JSONB.class)
//                )
//                .from(MATERIE)
//                .leftJoin(table(name("matcat")))
//                .on(MATERIE.ID_MATERIA.eq(field(name("matcat", "id_materia"), MATERIE.ID_MATERIA.getDataType())));
//    }
//
////    private SelectHavingStep<Record5<UUID, String, String, Boolean, Object[]>> select() {
////        return using(sqlConfig)
////                .with("nomi_categorie").as
////                (
////                        select(
////                                MATERIE_CATEGORIE.ID_MATERIA.as("id_materia_matcat"),
////                                CATEGORIE.NOME_CATEGORIA.as("nome_categ_matcat")
////                        )
////                                .from(MATERIE_CATEGORIE)
////                                .join(CATEGORIE)
////                                .on(MATERIE_CATEGORIE.ID_CATEGORIA.eq(CATEGORIE.ID_CATEGORIA))
////                )
////                .select(
////                        MATERIE.ID_MATERIA,
////                        MATERIE.NOME,
////                        MATERIE.DESCRIZIONE,
////                        MATERIE.STUDIO_ATTIVO,
////                        arrayAgg(field(name("nomi_categorie", "nome_categ_matcat")))
////                                .filterWhere(
////                                        field(name("nomi_categorie", "nome_categ_matcat")).ne(val(null, CATEGORIE.NOME_CATEGORIA))
////                                )
////                                .as("nomi_categorie")
////                )
////                .from(MATERIE)
////                .leftJoin(table(name("nomi_categorie")))
////                .on(MATERIE.ID_MATERIA.eq(field(name("nomi_categorie", "id_materia_matcat"), MATERIE.ID_MATERIA.getDataType())))
////                .groupBy(MATERIE.ID_MATERIA);
////    }
//}
