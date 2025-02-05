package services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import exceptions.EstoqueException;
import interfaces.IEstoque;

public abstract class EstoqueService implements IEstoque{

    
    public void CadastramentoProduto(int idlote,String nome, String descricao, float preco, double quantidade) throws EstoqueException{

        //Funcionário aprova ou nao o id do lote
        if(idlote < 0) throw new EstoqueException("in de lote menor do que 0");

        if (nome == null || nome.trim().isEmpty()) {
            throw new EstoqueException ("Nome do produto inválido.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new EstoqueException("Descrição do produto inválida.");
        }
        if (preco < 0) {
            throw new EstoqueException("Preço do produto inválido.");
        }
        if (quantidade < 0) {
            throw new EstoqueException ("Quantidade do produto inválida.");
        }
    
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";

        File pasta = new File(pastaPath);
        String nomeArquivo = nome + "_produto.txt";
        File arquivo = new File(pasta, nomeArquivo);
            
        if (!pasta.exists()) {
                pasta.mkdirs();
            }
            
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write("Id do lote do produto: " + idlote);
                writer.newLine();
                writer.write("Nome do produto: " + nome);
                writer.newLine();
                writer.write("Nome do produto: " + nome);
                writer.newLine();
                writer.write("Descricao do Produto : " +descricao);
                writer.newLine();
                writer.write("Preco do produto: " + preco);
                writer.newLine();
                writer.write("quantidade disponivel do produto: " + quantidade);
                writer.newLine();
                System.out.println("Arquivo criado com sucesso: " + arquivo.getAbsolutePath());
                System.out.printf("Produto cadastrado com sucesso !", nome);
                System.out.println();
                } catch (IOException e) {
                    System.err.println("Erro ao criar o arquivo: " + e.getMessage());
                }
    }


    public Boolean ControleProduto(int idlote,int response) throws EstoqueException{
        //altera arquivo de texto selecionado, altera quantos tem disponível, de determinado lote.
       if(response == 1) return true;
       if(response == 2) return false;

    }


   
}
