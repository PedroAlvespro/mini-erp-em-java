package controllers;

import java.util.InputMismatchException;
import java.util.Scanner;

import exceptions.EstoqueException;
import exceptions.UsuarioException;
import exceptions.VendasException;
import services.EstoqueService;
import services.VendaService;

public class VendaController extends VendaService{

    

    public void MenuVendas() throws VendasException, EstoqueException, UsuarioException{
    //opc de autenticação
    //opc de compras

    Scanner me = new Scanner(System.in);
    int rep;
    do{
        System.out.println("1- Para Efetuar uma Compra (Apenas para Clientes)");
        System.out.println();
        System.out.println("2- Para Analisar Relatórios (Apenas para Gerente),");
        System.out.println();
        System.out.println("0- Para sair do Menu de Vendas");
       
        rep = me.nextInt();
        if(rep == 1){
        ImplementacaoCompra();
        }
        if(rep == 2){
        ImplementacaoRelatorio();
        }
    } while(rep !=0);
}

    public void ImplementacaoCompra() throws VendasException, EstoqueException {
        Scanner mee = new Scanner(System.in);


        System.out.println("digite seu nickname");
        String nome = mee.nextLine();
        System.out.println();

        System.out.println("digite seu endereço");
        String endereco = mee.nextLine();
        System.out.println();

        Boolean responseLogin = ConfirmaCliente(nome);

        if(responseLogin != true){
            System.out.println();
            System.out.println("Usuário nao encontrado");
            System.out.println();
            return;
        } 

        try {

        System.out.println();
        exibeProdutos();
        System.out.println();

        System.out.println("Digite o Id do produto");
        int id = mee.nextInt();
        mee.nextLine();
        System.out.println();

        System.out.println("Digite quantos você quer comprar");
        double quantidade = mee.nextDouble();
        mee.nextLine();
        System.out.println();

        System.out.println("Digite o valor do produto, lembrando que o valor do produto é multiplicado pela quantidade de produtos que você comprar!!");
        float preco = mee.nextFloat();
        mee.nextLine();
        System.out.println();

        int idVenda = EstoqueService.Venda(id, quantidade, preco,nome,endereco); //pegando id da venda
        System.out.println("Venda realizada com sucesso! ID da venda: " + idVenda);
        System.out.println();

        } catch (InputMismatchException e) {

        System.err.println("Erro na entrada de dados. Por favor, digite os valores corretamente.");
        mee.nextLine();  
        System.out.println();

        } catch (EstoqueException e) {

        System.err.println("Erro ao processar a venda: " + e.getMessage());
        System.out.println();

        }

    }

    public void ImplementacaoRelatorio() throws UsuarioException{

        Scanner meea = new Scanner(System.in);


        System.out.println("digite seu nickname");
        String nome = meea.nextLine().trim();
        System.out.println();


        System.out.println("Digite a senha:");
        String senha = meea.nextLine().trim();
        System.out.println();

        Boolean responseLogin = validarGerente(nome,senha);

        if(responseLogin != true){
            System.out.println("Usuário nao encontrado");
            System.out.println();
            return;
        } 

        System.out.println("Olá, gerente " +nome);
        System.out.println();

        relatorioDeVendas();


    }

}