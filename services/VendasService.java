package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import exceptions.VendasException;
import interfaces.IVendasInterface;

public abstract class VendasService implements IVendasInterface {
    
    
    public void CadastroClientes (String nome, String endereco, String contato) throws VendasException {

         if (nome == null || nome.trim().isEmpty()) {
            throw new VendasException ("Nome do produto inválido.");
        }
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new VendasException ("Descrição do produto inválida.");
        }
        if (contato == null || contato.trim().isEmpty()) {
            throw new VendasException ("Preço do produto inválido.");
        }

        //caminho relativo c:// Loja//serc
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosclientes";

        File pasta = new File(pastaPath);
        String nomeArquivo = nome + "_produto.txt";
        File arquivo = new File(pasta, nomeArquivo);
            
        if (!pasta.exists()) {
                pasta.mkdirs();
            }
            
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write("Nome do cliente: " + nome);
                writer.newLine();
                writer.write("Descricao do cliente: " +endereco);
                writer.newLine();
                writer.write("contato do cliente: " + contato);
                writer.newLine();
                System.out.println("Arquivo criado com sucesso: " + arquivo.getAbsolutePath());
                System.out.printf("Produto cadastrado com sucesso !", nome);
                System.out.println();
                } catch (IOException e) {
                    System.err.println("Erro ao criar o arquivo: " + e.getMessage());
                }
        
    }

}
