package com.example.jokempo.pessoa;

import java.io.Serializable;

public class Pessoa implements Serializable {

    private int id;
    private String nome;
    private int qtdPartidas;
    private int qtdVitorias;
    private int horasJogadas;
    //taxa de vitorias será calculada pela qtdVitorias/qtdPartidas
    //TO-DO
    //[] Saber a quantidade de horas
    public Pessoa() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdPartidas() {
        return qtdPartidas;
    }

    public void setQtdPartidas(int qtdPartidas) {
        this.qtdPartidas = qtdPartidas;
    }

    public int getQtdVitorias() {
        return qtdVitorias;
    }

    public void setQtdVitorias(int qtdVitorias) {
        this.qtdVitorias = qtdVitorias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHorasJogadas() {
        return horasJogadas;
    }

    public void setHorasJogadas(int horasJogadas) {
        this.horasJogadas = horasJogadas;
    }
}
