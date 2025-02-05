
import java.util.Scanner;

import controllers.EstoqueController;
import controllers.UsuarioController;
import exceptions.UsuarioException;

public class Main {
    public static void main(String[] args) {

    int response;        
    
    do{
        Scanner sc = new Scanner(System.in);

        System.out.println("1-Menu do Estoque, 2-Menu Sistema de Vendas, 3- Menu de Usuario  0- Encerrar");
        response = sc.nextInt();
        System.out.println();

        //Todo o Sistema de Estoque deve ser acessado apenas por funcionários
        if (response == 1) {
            EstoqueController estoque = new EstoqueController();
            try {
                estoque.EstoqueMenu();
            } catch (UsuarioException e) {
                e.printStackTrace();
            }

        } else if (response == 3){
              UsuarioController usuarioController = new UsuarioController();
              usuarioController.menuUsuario();
        }
    } while (response != 0);

    }
}