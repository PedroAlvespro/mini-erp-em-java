import java.util.InputMismatchException;
import java.util.Scanner;

import controllers.EstoqueController;
import controllers.UsuarioController;
import controllers.VendaController;
import exceptions.EstoqueException;
import exceptions.UsuarioException;
import exceptions.VendasException;

public class Main {
    public static void main(String[] args) {
        int response = -1;
        Scanner sc = new Scanner(System.in);
        
        do {
            try {
                System.out.println("1 - Menu do Estoque");
                System.out.println("   (Especialmente para Funcionários, Cadastrar Produtos, Validar ou Rejeitar, Relatórios de Movimentação de Estoque)");
                System.out.println();
                System.out.println("2 - Menu Sistema de Vendas");
                System.out.println("   (Especialmente para Clientes e Gerente, dividido para que: Clientes possam fazer compras com seus nomes e endereços já cadastrados. ");
                System.out.println("    Gerentes possam verificar Relatórios Gerais, como cada venda, quem comprou, quantos produtos compraram e a hora da compra)");
                System.out.println();
                System.out.println("3 - Menu de Usuário");
                System.out.println("   (Sistema Inicial, onde cada Gerente, Funcionário ou Cliente pode se cadastrar. Porta de entrada do MiniERP)");
                System.out.println();
                System.out.println("0 - Encerrar");
                
                System.out.print("Escolha uma opção: ");
                response = sc.nextInt();
                System.out.println();

                switch (response) {
                    case 1:
                        EstoqueController estoque = new EstoqueController();
                        try {
                            estoque.EstoqueMenu();
                        } catch (UsuarioException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        VendaController vendaController = new VendaController();
                        try {
                            vendaController.MenuVendas();
                        } catch (VendasException | EstoqueException | UsuarioException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        UsuarioController usuarioController = new UsuarioController();
                        usuarioController.menuUsuario();
                        break;
                    case 0:
                        System.out.println("Encerrando o sistema...");
                        break;
                    default:
                        System.out.println("Opção inválida, tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida! Por favor, digite um número.");
                sc.next(); // Limpa o buffer do scanner
            }
        } while (response != 0);
        
        
    }
}

