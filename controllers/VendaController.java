package controllers;

import java.util.Scanner;

import exceptions.EstoqueException;
import exceptions.VendasException;
import services.EstoqueService;
import services.VendaService;

public class VendaController extends VendaService{

    

public void MenuVendas() throws VendasException, EstoqueException{
    //opc de autenticação
    //opc de compras

    Scanner me = new Scanner(System.in);
    int rep;
    do{
        System.out.println("1- Comprar, 0- sair");
        rep = me.nextInt();
        if(rep == 1){
        ImplementacaoCompra();
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
        System.out.println("Digite o Id do produto");
        int id = mee.nextInt();

        System.out.println("Digite quantos você quer comprar");
        double quantidade = mee.nextDouble();

        System.out.println("Digite o valor do produto");
        float preco = mee.nextFloat();

        
       int idVenda = EstoqueService.Venda(id, quantidade, preco); //pegando id da venda

       


        
        
        

        /*Comprar, selecionar o id do lote*/ 
    }

}