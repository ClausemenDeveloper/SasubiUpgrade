package sasubiupgrade.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Encomenda {
    private String nomeEstudante;
    private String id;
    private String dataRegistro;
    private String fonte; // "Local" ou "Externa"

    public Encomenda(String nomeEstudante, String id, String dataRegistro, String fonte) {
        this.nomeEstudante = nomeEstudante;
        this.id = id;
        this.dataRegistro = dataRegistro;
        this.fonte = fonte;
    }

    public String getNomeEstudante() {
        return nomeEstudante;
    }

    public String getId() {
        return id;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public String getFonte() {
        return fonte;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Estudante: " + nomeEstudante + " | Registrado em: " + dataRegistro + " | Fonte: " + fonte;
    }
}