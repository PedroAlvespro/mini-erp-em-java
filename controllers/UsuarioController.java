package controllers;

import java.util.Scanner;
import exceptions.UsuarioException;
import services.UsuarioService;

public class UsuarioController extends UsuarioService {

    public void menuUsuario() {
        Scanner sc = new Scanner(System.in);
        int response;

        do {
            System.out.println("1 - Cadastrar usuário");
            System.out.println("2 - Fazer login");
            System.out.println("0 - Sair");
            response = sc.nextInt();
            sc.nextLine();

            switch (response) {
                case 1:
                    cadastroUsuario(sc);
                    break;
                case 2:
                    loginUsuario(sc);
                    break;
                case 0:
                    System.out.println("Sistema encerrado.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }

        } while (response != 0);
        
    }

    private void cadastroUsuario(Scanner scanner) {
        int tipoUsuario;

        do{
        System.out.println("Selecione o tipo de usuário:");
        System.out.println("1 - Gerente");
        System.out.println("2 - Funcionário");
        tipoUsuario = scanner.nextInt();
        scanner.nextLine();  

        if (tipoUsuario != 1 && tipoUsuario != 2) {
            System.out.println("Opção inválida. Tente novamente.");
            return;
        }
        }while(tipoUsuario != 1 && tipoUsuario != 2);

        System.out.println("Digite o nickname:");
        String nickname = scanner.nextLine();

        try {
            if (verificarExistenciaUsuario(nickname)) {
                System.out.println("O nickname já existe no sistema. Tente outro.");
                return;
            }

            System.out.println("Digite a senha");
            String senha = scanner.nextLine();

            
            if (senha.length() < 8) {
                throw new UsuarioException("A senha deve ter no mínimo 8 caracteres.");
            }

            cadastrarUsuario(nickname, senha, tipoUsuario);
        } catch (UsuarioException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void loginUsuario(Scanner scanner) {
        System.out.println("Digite seu nickname:");
        String nickname = scanner.nextLine();

        System.out.println("Digite sua senha:");
        String senha = scanner.nextLine();

        try {
            if (validarUsuario(nickname, senha)) {
                System.out.println("Login realizado com sucesso!");
            } else {
                System.out.println("Nickname ou senha incorretos.");
            }
        } catch (UsuarioException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
