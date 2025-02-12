package interfaces;

import java.io.File;

import exceptions.UsuarioException;
import exceptions.VendasException;

public interface IVendaInterface {
    public int Venda(int idLote, double quantidadeComprada, float valorPago) throws VendasException;
    public void atualizarEstoque(File arquivo, double novaQuantidade) throws VendasException; 
    public boolean validarGerente(String nickname, String senha) throws UsuarioException;
    public Boolean ConfirmaCliente(String nickname) throws VendasException;
    public void relatorioDeVendas();
}
