package controllers;

import services.VendasService;
import java.util.Scanner;

import exceptions.VendasException;

public class VendasController extends VendasService{

    public void EstoqueVendas (){
    int response;
    Scanner scc = new Scanner(System.in);
    do {      
        System.out.println("1- cadastrar produt, 0- sair");
        response = scc.nextInt();
        scc.nextLine();
        if (response == 1 ) {
            ImplementacaoCadastroCliente();
        } else if (response != 0) {
            System.out.println("Opção inválida. Tente novamente.");
            System.out.println();
        }
    } while (response !=0);
}

    public void ImplementacaoCadastroCliente(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Nome do Cliente");
        String nome = sc.nextLine();

        System.out.println("Endereco do cliente");
        String endereco = sc.nextLine();

        System.out.println("Contato do cliente");
        String contato = sc.nextLine();

         try{
            CadastroClientes(nome, endereco, contato);
        } catch (VendasException e){
            System.err.println("Erro: " + e.getMessage());
        }

    }
}
