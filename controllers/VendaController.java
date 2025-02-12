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
        System.out.println("1- Comprar(Apenas para Clientes), 2- Analisar Relatórios (Apenas para Gerente),0- sair");
        rep = me.nextInt();
        if(rep == 1){
        ImplementacaoCompra();
        }
        if(rep == 2){
        ImplementacaoRelatorio();
        }
    } while(rep !=0);
}

    public void ImplementacaoCompra() throws VendasException, EstoqueException{
        Scanner mee = new Scanner(System.in);


        System.out.println("digite seu nickname");
        String nome = mee.nextLine();
        Boolean responseLogin = ConfirmaCliente(nome);

        if(responseLogin != true){
            System.out.println("Usuário nao encontrado");
            return;
        } 

            /*Compra */
            try {
        System.out.println("Digite o Id do produto");
        int id = mee.nextInt();
        mee.nextLine();

        System.out.println("Digite quantos você quer comprar");
        double quantidade = mee.nextDouble();
        mee.nextLine();

        System.out.println("Digite o valor do produto, lembrando que o valor do produto é multiplicado pela quantidade de produtos que você comprar!!");
        float preco = mee.nextFloat();
        mee.nextLine();

        int idVenda = EstoqueService.Venda(id, quantidade, preco); //pegando id da venda
        System.out.println("Venda realizada com sucesso! ID da venda: " + idVenda);
    } catch (InputMismatchException e) {
        System.err.println("Erro na entrada de dados. Por favor, digite os valores corretamente.");
        mee.nextLine();  // Limpar o buffer para nova entrada
    } catch (EstoqueException e) {
        System.err.println("Erro ao processar a venda: " + e.getMessage());
    }
    }

    public void ImplementacaoRelatorio() throws UsuarioException{
        Scanner meea = new Scanner(System.in);


        System.out.println("digite seu nickname");
        String nome = meea.nextLine();


        System.out.println("Digite a senha:");
        String senha = meea.nextLine().trim();

        Boolean responseLogin = validarGerente(nome,senha);

        if(responseLogin != true){
            System.out.println("Usuário nao encontrado");
            return;
        } 

        System.out.println("Olá, gerente  " +nome);
        /*  implementar o relatórior*/
        relatorioDeVendas();


    }

}