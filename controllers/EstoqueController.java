package controllers;

import java.util.Scanner;

import exceptions.EstoqueException;
import exceptions.UsuarioException;
import services.EstoqueService;

public class EstoqueController extends EstoqueService {

    public void EstoqueMenu () throws UsuarioException{

        Scanner sce = new Scanner(System.in);
        /*COnfirme sua identidade*/    
        int responsee;

        do {      
            System.out.println("Você realmente é um Funcionário ? 1 - Sim, 2 - Para nao");
            responsee = sce.nextInt();
            sce.nextLine();

            if (responsee == 1 ) {

                UsuarioController login = new UsuarioController();
                System.err.println("Digite seu nome");
                String nickname = sce.nextLine();

                System.err.println("Digite sua senha");
                String senha = sce.nextLine();

                /*Autenticacao Usuario*/

                if(login.validarUsuario(nickname,senha) == true) {
                    System.out.println("Olá " +nickname);
                    break;
                    //para e continua para o restante do método.
                } else {
                    System.out.println("Não encontramos nenhum funcionário com as credenciais" + nickname + "voltando para o menu...S");
                    return;
                }


            } else if(responsee == 2){
                System.out.println("Ok, retornando para o Menu anterior...");
                return;
            }
            
            else if (responsee != 0) {

                System.out.println("Opção inválida. Tente novamente.");
                System.out.println();

            }

    } while (responsee !=0);

        Scanner sc = new Scanner(System.in);
        int response;

        do {      
            System.out.println("1- cadastrar produt, 2- validar produto, 0- sair");
            response = sc.nextInt();
            sc.nextLine();

            if (response == 1 ) {

                ImplementacaoEstoque();

            } else if(response == 2){
                ImplementacaoControle();
            }
            
            else if (response != 0) {

                System.out.println("Opção inválida. Tente novamente.");
                System.out.println();

            }

    } while (response !=0);
    }

    public void ImplementacaoEstoque() {

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("digite o numero do lote do produto");
        int idlote = scanner.nextInt();
        scanner.nextLine();

        System.out.println("digite o nome do produto");
        String nome = scanner.nextLine();

        System.out.println("digite a descricao do produto");
        String descricao = scanner.nextLine();

        System.out.println("digite o preco do produto");
        float preco = scanner.nextFloat();

        System.out.println("digite a quantidade do produto");
        double quantidade = scanner.nextDouble();

        try{
            CadastramentoProduto(idlote,nome, descricao, preco, quantidade);
        } catch (EstoqueException e){
            System.err.println("Erro: " + e.getMessage());
        }
        
    }

    //acessivel apenas para funcionário

    public void ImplementacaoControle() throws UsuarioException {
        Scanner scaner = new Scanner(System.in);
  
        System.out.println("Digite o número do lote do produto:");
        int idlote = scaner.nextInt();
        scaner.nextLine();

        System.out.println("Digite o número do ID da venda do produto:");
        int idvenda = scaner.nextInt();
        scaner.nextLine();

        System.out.println("Digite a resposta (1 para aprovar, 2 para rejeitar):");
        int response = scaner.nextInt();

        try {
            /*Id de venda para que o funcionário aprove*/
            boolean resultado = ControleProduto(idlote, idvenda, response);

            if (response == 1 && resultado) {
                System.out.println("Produto aprovado com sucesso!");
            } else if (response == 2 && resultado) {
                System.out.println("Produto rejeitado com sucesso!");
            }
        } catch (EstoqueException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
    
    
   

    }

