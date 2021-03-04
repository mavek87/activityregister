//package com.matteoveroni.activityregister.repositories;
//
//import com.google.gson.Gson;
//import com.matteoveroni.activityregister.domain.Categoria;
//import com.matteoveroni.activityregister.domain.Materia;
//import com.matteoveroni.activityregister.tables.records.CategorieRecord;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import org.jooq.Configuration;
//import org.jooq.impl.DSL;
//import static com.matteoveroni.activityregister.tables.Categorie.CATEGORIE;
//import static org.jooq.impl.DSL.using;
//
//public class CategorieRepository {
//
//    private final Gson gson = new Gson();
//    private final Configuration sqlConfig;
//
//    public CategorieRepository(Configuration sqlConfiguration) {
//        this.sqlConfig = sqlConfiguration;
//    }
//
//    public long count() {
//        return using(sqlConfig)
//                .select(DSL.count(CATEGORIE.ID_CATEGORIA))
//                .from(CATEGORIE)
//                .fetchOne(0, long.class);
//    }
//
//    public Optional<Categoria> get(UUID id) {
//        return Optional.ofNullable(
//                using(sqlConfig)
//                        .select()
//                        .from(CATEGORIE)
//                        .where(CATEGORIE.ID_CATEGORIA.eq(id))
//                        .fetchAnyInto(Categoria.class)
//        );
//    }
//
//    public List<Categoria> getAll() {
//        return using(sqlConfig)
//                .selectFrom(CATEGORIE)
//                .fetchInto(Categoria.class);
//    }
//
//    public UUID save(Categoria categoria) {
//        CategorieRecord categorieRecord = using(sqlConfig).newRecord(CATEGORIE);
//        categorieRecord.setIdCategoria(categoria.getIdCategoria());
//        categorieRecord.setNomeCategoria(categoria.getNomeCategoria());
//        categorieRecord.store();
//        return categorieRecord.getIdCategoria();
//    }
//
//    public List<UUID> saveAll(Categoria... categorie) {
//        List<UUID> idMaterie = new ArrayList<>();
//        for (Categoria categoria : categorie) {
//            save(categoria);
//        }
//        return idMaterie;
//    }
//
//    public void deleteById(UUID id) {
//        using(sqlConfig)
//                .deleteFrom(CATEGORIE)
//                .where(CATEGORIE.ID_CATEGORIA.eq(id))
//                .execute();
//    }
//
//    public void delete(Materia entity) {
//        using(sqlConfig)
//                .newRecord(CATEGORIE, entity)
//                .delete();
//    }
//
//    public void clear() {
//        using(sqlConfig)
//                .deleteFrom(CATEGORIE)
//                .execute();
//    }
//}