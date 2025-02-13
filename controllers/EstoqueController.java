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
            System.out.println("Olá, vamos confirmar sua identidade, você realmente é um Funcionário ?");
            System.out.println("1- Para Prosseguir e Confirmar sua Identidade, 2- Para retornar ao menu inicial.");
            responsee = sce.nextInt();
            sce.nextLine();

            if (responsee == 1 ) {

                UsuarioController login = new UsuarioController();
                System.err.println("Digite seu nome de Identificação de Funcionário");
                String nickname = sce.nextLine();

                System.err.println("Digite sua senha de Identificação");
                String senha = sce.nextLine();
                
                System.out.println();
                System.out.println();

                if(login.validarUsuario(nickname,senha) == true) {
                    System.out.println("Olá " +nickname);
                    System.out.println();
                    break;
    
                } else {
                    System.out.println("Não encontramos nenhum funcionário com as credenciais: " + nickname + ", vamos voltar para o Menu Inicial.");
                    return;
                }


            } else if(responsee == 2){
                System.out.println("Ok, retornando para o Menu Inicial");
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
            System.out.println("1- Para Adicionar Produto ao Estoque ");
            System.out.println();
            System.out.println("2- Para Processar Compra (Negar Venda para o Cliente.)");
            System.out.println();
            System.out.println("3- Para visualizar os Alertas de Produtos com Estoque Baixo(Caso não tenha Produtos com baixo Estoque, não notificaremos)");
            System.out.println();
            System.out.println("4- Para visualizar o Relatório de movimentacao estoque (Todos os Produtos que entraram e saíram do Estoque).");
            System.out.println();
            System.out.println("0- Para sair do Menu de Funcionário");
            System.out.println();
            response = sc.nextInt();
            sc.nextLine();

            if (response == 1 ) {

                ImplementacaoEstoque();
                System.out.println();

            } else if(response == 2){
                ImplementacaoControle();
                System.out.println();
            }
            else if(response == 3){
                ImplementacaoAlerta();
                System.out.println();
            }
            else if(response == 4){
               ImplementacaoRelatorioMovimentacao();
               System.out.println();
            }
            
            else if (response != 0) {

                System.out.println("Opção inválida. Tente novamente.");
                System.out.println();

            }

    } while (response !=0);
    }

    public void ImplementacaoEstoque() throws UsuarioException {

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

    public void ImplementacaoControle() throws UsuarioException {
        Scanner scaner = new Scanner(System.in);
  
        System.out.println("Digite o número do lote do produto:");
        int idlote = scaner.nextInt();
        scaner.nextLine();

        System.out.println("Digite o número do ID da venda do produto:");
        int idvenda = scaner.nextInt();
        scaner.nextLine();

        System.out.println();
        System.out.println("Tudo Pronto, Digite 2-  para Rejeitar a venda, (Caso você digitar 2 para confirmar a  rejeição, voltaremos para o Menu Anterior :)");
        int response = scaner.nextInt();
        System.out.println();

        try {
            
             if (response == 2) {
                
                /*AddEstoque, remove quantidade do estoque, processa venda */
                addEstoque( idlote, idvenda, response);
            }
        } catch (EstoqueException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        System.out.println();
    }

    public void ImplementacaoAlerta(){

    verificarEstoqueBaixo();
    System.out.println();

    }
    
    public void ImplementacaoRelatorioMovimentacao(){
        relatoriomovimentacao();
        System.out.println();
    }

}

