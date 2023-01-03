package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculadoraMockTest {


    @Mock
    private Calculadora calculadoraMock;

    //SPY FUNCIONA APENAS COM CLASSES CONCRETAS
    @Spy
    private Calculadora calculadoraSpy;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy(){

        System.out.println(calculadoraMock.somar(1, 2)); //RETORNA ZERO POIS O RETORNO DEFAULT DO MOCK PRA INT É 0

        Mockito.when(calculadoraMock.somar(1,2)).thenReturn(8);
        Mockito.when(calculadoraSpy.somar(1,2)).thenReturn(8);

        System.out.println("Mock: " + calculadoraMock.somar(1, 5));
        System.out.println("Spy: " + calculadoraSpy.somar(1, 5));



    }

    @Test
    public void teste(){
        Calculadora calc = Mockito.mock(Calculadora.class);
//        Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
//        Mockito.when(calc.somar(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.somar(argumentCaptor.capture(), Mockito.anyInt())).thenReturn(5);
        Assert.assertEquals(5, calc.somar(1, 8));
    }


}
