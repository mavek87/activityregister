/*
 * This file is generated by jOOQ.
 */
package com.matteoveroni.activityregister.tables;


import com.matteoveroni.activityregister.Keys;
import com.matteoveroni.activityregister.Study;
import com.matteoveroni.activityregister.tables.records.MaterieCategorieRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MaterieCategorie extends TableImpl<MaterieCategorieRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>study.materie_categorie</code>
     */
    public static final MaterieCategorie MATERIE_CATEGORIE = new MaterieCategorie();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MaterieCategorieRecord> getRecordType() {
        return MaterieCategorieRecord.class;
    }

    /**
     * The column <code>study.materie_categorie.id_materia</code>.
     */
    public final TableField<MaterieCategorieRecord, UUID> ID_MATERIA = createField(DSL.name("id_materia"), SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>study.materie_categorie.id_categoria</code>.
     */
    public final TableField<MaterieCategorieRecord, UUID> ID_CATEGORIA = createField(DSL.name("id_categoria"), SQLDataType.UUID.nullable(false), this, "");

    private MaterieCategorie(Name alias, Table<MaterieCategorieRecord> aliased) {
        this(alias, aliased, null);
    }

    private MaterieCategorie(Name alias, Table<MaterieCategorieRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>study.materie_categorie</code> table reference
     */
    public MaterieCategorie(String alias) {
        this(DSL.name(alias), MATERIE_CATEGORIE);
    }

    /**
     * Create an aliased <code>study.materie_categorie</code> table reference
     */
    public MaterieCategorie(Name alias) {
        this(alias, MATERIE_CATEGORIE);
    }

    /**
     * Create a <code>study.materie_categorie</code> table reference
     */
    public MaterieCategorie() {
        this(DSL.name("materie_categorie"), null);
    }

    public <O extends Record> MaterieCategorie(Table<O> child, ForeignKey<O, MaterieCategorieRecord> key) {
        super(child, key, MATERIE_CATEGORIE);
    }

    @Override
    public Schema getSchema() {
        return Study.STUDY;
    }

    @Override
    public UniqueKey<MaterieCategorieRecord> getPrimaryKey() {
        return Keys.MATERIE_CATEGORIE_PKEY;
    }

    @Override
    public List<UniqueKey<MaterieCategorieRecord>> getKeys() {
        return Arrays.<UniqueKey<MaterieCategorieRecord>>asList(Keys.MATERIE_CATEGORIE_PKEY);
    }

    @Override
    public List<ForeignKey<MaterieCategorieRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<MaterieCategorieRecord, ?>>asList(Keys.MATERIE_CATEGORIE__FK_ID_MATERIA_MATERIE_CATEGORIE, Keys.MATERIE_CATEGORIE__FK_ID_CATEGORIA_MATERIE_CATEGORIE);
    }

    public Materie materie() {
        return new Materie(this, Keys.MATERIE_CATEGORIE__FK_ID_MATERIA_MATERIE_CATEGORIE);
    }

    public Categorie categorie() {
        return new Categorie(this, Keys.MATERIE_CATEGORIE__FK_ID_CATEGORIA_MATERIE_CATEGORIE);
    }

    @Override
    public MaterieCategorie as(String alias) {
        return new MaterieCategorie(DSL.name(alias), this);
    }

    @Override
    public MaterieCategorie as(Name alias) {
        return new MaterieCategorie(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public MaterieCategorie rename(String name) {
        return new MaterieCategorie(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MaterieCategorie rename(Name name) {
        return new MaterieCategorie(name, null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<UUID, UUID> fieldsRow() {
        return (Row2) super.fieldsRow();
    }
}