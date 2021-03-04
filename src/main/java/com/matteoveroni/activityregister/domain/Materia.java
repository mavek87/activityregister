package com.matteoveroni.activityregister.domain;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import lombok.Data;

@Data
public class Materia {

    @Id
    @Column(name = "id_materia")
    @SerializedName("id_materia")
    private UUID idMateria;
    @Column(name = "nome")
    @SerializedName("nome")
    private String nome;
    @Column(name = "descrizione")
    @SerializedName("descrizione")
    private String descrizione;
    @Column(name = "studio_attivo")
    @SerializedName("studio_attivo")
    private Boolean isStudioAttivo;
    @Column(name = "categorie")
    @SerializedName("categorie")
    private List<Categoria> categorie;

    public Materia(UUID idMateria, String nome, String descrizione, Boolean isStudioAttivo, List<Categoria> categorie) {
        this.idMateria = idMateria;
        this.nome = nome;
        this.descrizione = descrizione;
        this.isStudioAttivo = isStudioAttivo;
        this.categorie = categorie;
    }

    public Materia(String nome, String descrizione, Boolean isStudioAttivo, List<Categoria> categorie) {
        this.idMateria = UUID.randomUUID();
        this.nome = nome;
        this.descrizione = descrizione;
        this.isStudioAttivo = isStudioAttivo;
        this.categorie = categorie;
    }
}