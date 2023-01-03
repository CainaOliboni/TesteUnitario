package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    public static FilmeBuilder umFilme(){
        FilmeBuilder filmeBuilder = new FilmeBuilder();
        filmeBuilder.filme = new Filme();
        filmeBuilder.filme.setNome("Filme 1");
        filmeBuilder.filme.setEstoque(3);
        filmeBuilder.filme.setPrecoLocacao(4.0);

        return filmeBuilder;
    }

    public Filme agora(){
        return filme;
    }

}
