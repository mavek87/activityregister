package com.matteoveroni.activityregister.domain;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;
import javax.persistence.Column;
import lombok.Data;

@Data
public class Categoria {

    @Column(name = "id_categoria")
    @SerializedName("id_categoria")
    private UUID idCategoria;
    @Column(name = "nome_categoria")
    @SerializedName("nome_categoria")
    private String nomeCategoria;

    public Categoria(UUID idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    public Categoria(String nomeCategoria) {
        this.idCategoria = UUID.randomUUID();
        this.nomeCategoria = nomeCategoria;
    }
}
