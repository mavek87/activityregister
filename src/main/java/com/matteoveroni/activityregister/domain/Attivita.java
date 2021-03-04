package com.matteoveroni.activityregister.domain;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Data;

@Data
public class Attivita {

    @SerializedName("id_attivita")
    private UUID idAttivita = UUID.randomUUID();
    @SerializedName("id_materia")
    private UUID idMateria;
    @SerializedName("descrizione")
    private String descrizione;
    @SerializedName("data")
    private LocalDateTime data;
    @SerializedName("inizio_attivita")
    private LocalTime inizioAttivita;
    @SerializedName("fine_attivita")
    private LocalTime fineAttivita;
    @SerializedName("durata_attivita")
    private Long durataAttivita;

    public Attivita(UUID idMateria, String descrizione, LocalDateTime data, LocalTime inizioAttivita, LocalTime fineAttivita, Long durataAttivita) {
        this.idMateria = idMateria;
        this.descrizione = descrizione;
        this.data = data;
        this.inizioAttivita = inizioAttivita;
        this.fineAttivita = fineAttivita;
        this.durataAttivita = durataAttivita;
    }
}
