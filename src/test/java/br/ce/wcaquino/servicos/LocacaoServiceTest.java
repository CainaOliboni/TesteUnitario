package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private LocacaoService service;

    @Before
    public void setup(){
        service = new LocacaoService();
    }

    @Test
    public void testeLocacao() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 2, 6.0);
        Filme filme2 = new Filme("Filme 2", 3, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        //acao
        Locacao locacao = service.alugarFilme(usuario, listaFilmes);

            //verificacao
//        assertEquals(5.0,locacao.getValor(), 0.01);
//        assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
//        assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
//        assertThat(locacao.getValor(), is(not(5.0)));
            errorCollector.checkThat(locacao.getValor(), is(equalTo(16.0)));
            errorCollector.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
            errorCollector.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void testeLocacaoFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 0, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        //acao
        Locacao locacao = service.alugarFilme(usuario, listaFilmes);
    }

    @Test
    public void testeLocacaoFilmeSemEstoque2() {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 0, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        //acao
        try {
            Locacao locacao = service.alugarFilme(usuario, listaFilmes);
            Assert.fail("Deveria ter lançado execeção");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    @Test
    public void testeLocacaoFilmeSemEstoque3() throws Exception {

        //cenario
        Usuario usuario = new Usuario("Usuario 1");
        Filme filme = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 0, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        expectedException.expect(FilmeSemEstoqueException.class);
        expectedException.expectMessage("Filme sem estoque");
        
        //acao
        Locacao locacao = service.alugarFilme(usuario, listaFilmes);
    }

    @Test
    public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueException{

        //cenario
        Filme filme = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 0, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        //acao
        try {
            Locacao locacao = service.alugarFilme(null, listaFilmes);
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    @Test
    public void testeLocacaoFilmeVazio() throws FilmeSemEstoqueException, LocadoraException{

        //cenario
        Usuario usuario = new Usuario("Usuario 1");

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        Locacao locacao = service.alugarFilme(usuario, null);

    }


}
