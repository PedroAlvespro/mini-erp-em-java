package controllers;

import java.util.Scanner;

import exceptions.EstoqueException;
import services.EstoqueService;

public class EstoqueController extends EstoqueService {

    public void EstoqueMenu (){
        Scanner sc = new Scanner(System.in);
        int response;

        do {      
            System.out.println("1- cadastrar produt, 0- sair");
            response = sc.nextInt();
            sc.nextLine();

            if (response == 1 ) {

                ImplementacaoEstoque();

            } else if (response != 0) {

                System.out.println("Opção inválida. Tente novamente.");
                System.out.println();

            }

    } while (response !=0);
    }

    public void ImplementacaoEstoque() {

        Scanner scanner = new Scanner(System.in);

        System.out.println("digite o nome do produto");
        String nome = scanner.nextLine();

        System.out.println("digite a descricao do produto");
        String descricao = scanner.nextLine();

        System.out.println("digite o preco do produto");
        String preco = scanner.nextLine();

        System.out.println("digite a quantidade do produto");
        double quantidade = scanner.nextDouble();

        try{
            CadastramentoProduto(nome, descricao, preco, quantidade);
        } catch (EstoqueException e){
            System.err.println("Erro: " + e.getMessage());
        }
        
    }

   

    }

