package controllers;

import java.util.Scanner;
import exceptions.UsuarioException;
import services.UsuarioService;

public class UsuarioController extends UsuarioService {

    public void menuUsuario() {
        Scanner sc = new Scanner(System.in);
        int response;

        do {
            System.out.println("1- Para Cadastrar usuário");
            System.out.println("0- Para Sair");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    cadastroUsuario(sc);
                    break;
                case 0:
                    System.out.println("Sistema encerrado.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    System.out.println();
                    break;
            }

        } while (response != 0);
        
    }

    private void cadastroUsuario(Scanner scanner) {
        int tipoUsuario;

        do{
            
        System.out.println();
        System.out.println("Selecione o tipo de usuário:");
        System.out.println("1 - Gerente");
        System.out.println("2 - Funcionário");
        System.out.println("3 - Cliente");
        System.out.println();
        tipoUsuario = scanner.nextInt();
        scanner.nextLine();  

        if (tipoUsuario != 1 && tipoUsuario != 2 && tipoUsuario != 3) {
            System.out.println("Opção inválida. Tente novamente.");
            System.out.println();
            return;
        }
        }while(tipoUsuario != 1 && tipoUsuario != 2 && tipoUsuario != 3);

        System.out.println("Digite o nickname:");
        String nickname = scanner.nextLine();
        System.out.println();

        System.out.println("Digite o seu endereco:");
        String endereco = scanner.nextLine();
        System.out.println();

        System.out.println("Digite o contato:");
        String contato = scanner.nextLine();
        System.out.println();

        try {
            if (verificarExistenciaUsuario(nickname)) {
                System.out.println("O nickname já existe no sistema. Tente outro.");
                System.out.println();
                return;
            }

            System.out.println("Digite a senha");
            String senha = scanner.nextLine();
            System.out.println();

            
            if (senha.length() < 8) {
                throw new UsuarioException("A senha deve ter no mínimo 8 caracteres.");
            }

            cadastrarUsuario(nickname, endereco, contato, senha, tipoUsuario);
        } catch (UsuarioException e) {
            System.err.println("Erro: " + e.getMessage());
            System.out.println();
        }
    }

   

   


}
