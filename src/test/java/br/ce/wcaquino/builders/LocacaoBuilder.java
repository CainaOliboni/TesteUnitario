package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Collections;
import java.util.Date;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;

public class LocacaoBuilder {

    private Locacao locacao;

    public static LocacaoBuilder umaLocacao(){

        LocacaoBuilder locacaoBuilder = new LocacaoBuilder();
        locacaoBuilder.locacao = new Locacao();
        locacaoBuilder.locacao.setUsuario(umUsuario().agora());
        locacaoBuilder.locacao.setFilme(Collections.singletonList(umFilme().agora()));
        locacaoBuilder.locacao.setDataLocacao(new Date());
        locacaoBuilder.locacao.setValor(4.0);
        locacaoBuilder.locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));

        return locacaoBuilder;

    }

    public Locacao agora(){
        return locacao;
    }

    public LocacaoBuilder comDataRetorno(Date dataRetorno){
        locacao.setDataRetorno(dataRetorno);
        return this;
    }

    public LocacaoBuilder comUsuario(Usuario usuario){
        locacao.setUsuario(usuario);
        return this;
    }

    public LocacaoBuilder atrasado(){
        locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
        return this;
    }

}
