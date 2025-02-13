import java.util.Scanner;

import controllers.EstoqueController;
import controllers.UsuarioController;
import controllers.VendaController;
import exceptions.EstoqueException;
import exceptions.UsuarioException;
import exceptions.VendasException;

public class Main {
    public static void main(String[] args) {

        int response;        
    
        do {
            Scanner sc = new Scanner(System.in);

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
            
            response = sc.nextInt();
            System.out.println();

            // Todo o Sistema de Estoque deve ser acessado apenas por funcionários
            if (response == 1) {
                EstoqueController estoque = new EstoqueController();
                try {
                    estoque.EstoqueMenu();
                } catch (UsuarioException e) {
                    e.printStackTrace();
                }

            } else if (response == 2) {
                VendaController vendaController = new VendaController();
                try {
                    vendaController.MenuVendas();
                } catch (VendasException | EstoqueException | UsuarioException e) {
                    e.printStackTrace();
                }
            } else if (response == 3) {
                UsuarioController usuarioController = new UsuarioController();
                usuarioController.menuUsuario();
            }

        } while (response != 0);
    }
}
