package interfaces;

import exceptions.VendasException;


public interface IVendasInterface {
    public void CadastroClientes (String nome, String endereco, String contato) throws VendasException;
}
