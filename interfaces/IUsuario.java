package interfaces;

import exceptions.UsuarioException;

public interface IUsuario {
    void cadastrarUsuario(String nickname,String endereco, String contato, String senha, int tipoUsuario) throws UsuarioException;
    boolean validarUsuario(String nickname, String senha) throws UsuarioException;
    boolean verificarExistenciaUsuario(String nickname);
}
