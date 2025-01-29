
import java.util.Scanner;

import controllers.EstoqueController;

public class Main {
    public static void main(String[] args) {

    int response;        
    
    do{
        Scanner sc = new Scanner(System.in);

        System.out.println("1-Menu do Estoque, 0- Encerrar");
        response = sc.nextInt();
        System.out.println();


        if (response == 1) {
            EstoqueController estoque = new EstoqueController();
            estoque.EstoqueMenu();
        }
    } while (response != 0);

    }
}