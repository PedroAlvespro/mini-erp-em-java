package services;

import java.io.*;
import exceptions.UsuarioException;
import interfaces.IUsuario;

public abstract class UsuarioService implements IUsuario {

    private final String pastaUsuarios = System.getProperty("user.dir") + File.separator + "usuarios";
    
    public void cadastrarUsuario(String nickname, String senha, int tipoUsuario) throws UsuarioException {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new UsuarioException("Nickname inválido.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new UsuarioException("Senha inválida.");
        }

        File pasta = new File(pastaUsuarios);
        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        File arquivoUsuario = new File(pasta, nickname + ".txt");
        if (arquivoUsuario.exists()) {
            throw new UsuarioException("Usuário já cadastrado.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoUsuario))) {
            writer.write("Nickname: " + nickname);
            writer.newLine();
            writer.write("Senha: " + senha);
            writer.newLine();
            writer.write("Tipo de usuário: " + (tipoUsuario == 1 ? "Gerente" : "Funcionário"));
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (IOException e) {
            throw new UsuarioException("Erro ao salvar usuário.");
        }
    }
    
    public boolean validarUsuario(String nickname, String senha) throws UsuarioException {
        File arquivoUsuario = new File(pastaUsuarios, nickname + ".txt");

        if (!arquivoUsuario.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuario))) {
            String linha;
            String senhaArmazenada = null;

            while ((linha = reader.readLine()) != null) {
                if (linha.startsWith("Senha: ")) {
                    senhaArmazenada = linha.substring(7);
                }
            }

            return senha != null && senha.equals(senhaArmazenada);
        } catch (IOException e) {
            throw new UsuarioException("Erro ao validar usuário.");
        }
    }

    
    public boolean verificarExistenciaUsuario(String nickname) {
        File arquivoUsuario = new File(pastaUsuarios, nickname + ".txt");
        return arquivoUsuario.exists();
    }
}
