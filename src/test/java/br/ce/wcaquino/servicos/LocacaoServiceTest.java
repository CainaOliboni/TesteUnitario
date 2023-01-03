package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.DiaSemanaMatcher;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.*;

import java.util.*;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.LocacaoBuilder.umaLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.*;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @InjectMocks
    private LocacaoService service;

    @Mock
    private SPCService spcService;
    @Mock
    private EmailService emailService;
    @Mock
    private LocacaoDAO dao;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        // SEM ANOTAÇÕES
//        service = new LocacaoService();
//        dao = Mockito.mock(LocacaoDAO.class);
//        service.setLocacaoDAO(dao);
//        spcService = Mockito.mock(SPCService.class);
//        service.setSpcService(spcService);
//        emailService = Mockito.mock(EmailService.class);
//        service.setEmailService(emailService);
    }

    @Test
    public void deveAlugarFilmeComSucesso() throws Exception {

        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
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
            errorCollector.checkThat(locacao.getDataLocacao(), ehHoje());
            errorCollector.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

    }

    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        Filme filme = new Filme("Filme 1", 0, 5.0);
        Filme filme2 = new Filme("Filme 2", 0, 10.0);

        List<Filme> listaFilmes = new ArrayList<>();
        listaFilmes.add(filme);
        listaFilmes.add(filme2);

        //acao
        Locacao locacao = service.alugarFilme(usuario, listaFilmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemEstoque2() {

        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = umUsuario().agora();
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
    public void naoDeveAlugarFilmeSemEstoque3() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
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
    public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{

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
    public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException{

        //cenario
        Usuario usuario = umUsuario().agora();

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Filme vazio");

        Locacao locacao = service.alugarFilme(usuario, null);

    }


    @Test
    public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> listaFilmes = Arrays.asList(
                new Filme("Filme 1", 3, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 3, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, listaFilmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(11.0));
    }

    @Test
    public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> listaFilmes = Arrays.asList(
                new Filme("Filme 1", 3, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 3, 4.0),
                new Filme("Filme 4", 3, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, listaFilmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(13.0));

    }

    @Test
    public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> listaFilmes = Arrays.asList(
                new Filme("Filme 1", 3, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 3, 4.0),
                new Filme("Filme 4", 3, 4.0),
                new Filme("Filme 5", 3, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, listaFilmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(14.0));

    }

    @Test
    public void devePagar0PctNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
        //cenario

        Usuario usuario = umUsuario().agora();
        List<Filme> listaFilmes = Arrays.asList(
                new Filme("Filme 1", 3, 4.0),
                new Filme("Filme 2", 3, 4.0),
                new Filme("Filme 3", 3, 4.0),
                new Filme("Filme 4", 3, 4.0),
                new Filme("Filme 5", 3, 4.0),
                new Filme("Filme 6", 3, 4.0)
        );

        //acao
        Locacao resultado = service.alugarFilme(usuario, listaFilmes);

        //verificacao
        Assert.assertThat(resultado.getValor(), is(14.0));

    }

    @Test
    public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> listaFilmes = Arrays.asList(
                new Filme("Filme 1", 3, 4.0));

        //acao
        Locacao locacao = service.alugarFilme(usuario, listaFilmes);

        //verificacao
        boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(ehSegunda);
        Assert.assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
        Assert.assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
        Assert.assertThat(locacao.getDataRetorno(), caiNumaSegunda());


    }

    @Test
    public void naoaDeveAlugarFilmeNegativadoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();

        List<Filme> listaFilmes = Collections.singletonList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenReturn(true);

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Usuário Negativado");

        //acao
        service.alugarFilme(usuario, listaFilmes);

    }

    @Test
    public void naoaDeveAlugarFilmeNegativadoSPC2() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();

        List<Filme> listaFilmes = Collections.singletonList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenReturn(true);

        //acao
        try {
            service.alugarFilme(usuario, listaFilmes);
        //verificacao
            Assert.fail();
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuário Negativado"));
        }
        Mockito.verify(spcService).possuiNegativacao(usuario);

    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas(){
        //cenario

        Usuario usuario = umUsuario().agora();
        Usuario usuario2 = umUsuario().comNome("Usuario em dia").agora();
        Usuario usuario3 = umUsuario().comNome("Usuario atrasado").agora();

        List<Locacao> locacoes =
                Arrays.asList(
                        umaLocacao().atrasado().comUsuario(usuario).agora(),
                        umaLocacao().atrasado().comUsuario(usuario3).agora(),
                        umaLocacao().atrasado().comUsuario(usuario3).agora(),
                        umaLocacao().comUsuario(usuario2).agora()
                );

        when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

        //acao
        service.notificarAtrasos();

        //verificacao
        Mockito.verify(emailService).notificarAtraso(usuario);
//        Mockito.verify(emailService).notificarAtraso(usuario3);
//        Mockito.verify(emailService, Mockito.times(2)).notificarAtraso(usuario3);
//        Mockito.verify(emailService, Mockito.atLeast(2)).notificarAtraso(usuario3);
//        Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
        Mockito.verify(emailService, Mockito.atMost(2)).notificarAtraso(usuario3);
        Mockito.verify(emailService, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
        Mockito.verify(emailService, Mockito.never()).notificarAtraso(usuario2);
        Mockito.verifyNoMoreInteractions(emailService);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(umFilme().agora());

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrófica"));

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Problemas com SPC, tente novamente");

        //acao
        service.alugarFilme(usuario, filmes);


    }


    @Test
    public void deveProrrogarUmaLocacao(){

        //cenario
        Locacao locacao = umaLocacao().agora();

        //acao
        service.prorrogarLocacao(locacao, 3);

        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetorno = argCapt.getValue();

        Assert.assertThat(locacaoRetorno.getValor(), is(12.0));
        Assert.assertThat(locacaoRetorno.getDataLocacao(), ehHoje());
        Assert.assertThat(locacaoRetorno.getDataRetorno(), ehHojeComDiferencaDias(3));

    }

}
