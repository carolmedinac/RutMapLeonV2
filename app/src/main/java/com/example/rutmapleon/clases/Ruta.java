package com.example.rutmapleon.clases;

public class Ruta {
    private int Id;
    private String Numero;
    private String Origen;
    private String Destino;
    private String conductor;
    private String cooperativa;
    private String nombreconductor;
    private String nombreorigen;
    private String nombredestino;

    public Ruta(){}

    public Ruta(int id, String numero, String origen, String destino, String conductor, String cooperativa, String nombreconductor, String nombreorigen, String nombredestino) {
        Id = id;
        Numero = numero;
        Origen = origen;
        Destino = destino;
        this.conductor = conductor;
        this.cooperativa = cooperativa;
        this.nombreconductor = nombreconductor;
        this.nombreorigen = nombreorigen;
        this.nombredestino = nombredestino;
    }

    public String getNombreconductor() { return nombreconductor; }

    public void setNombreconductor(String nombreconductor) { this.nombreconductor = nombreconductor; }

    public String getNombreorigen() { return nombreorigen; }

    public void setNombreorigen(String nombreorigen) { this.nombreorigen = nombreorigen; }

    public String getNombredestino() { return nombredestino; }

    public void setNombredestino(String nombredestino) { this.nombredestino = nombredestino; }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getNumero() {
        return Numero;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getDestino() {
        return Destino;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getConductor() {
        return conductor;
    }

    public void setCooperativa(String Cooperativa) {
        cooperativa = Cooperativa;
    }

    public String getCooperativa() {
        return cooperativa;
    }
}

