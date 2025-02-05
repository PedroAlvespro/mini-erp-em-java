package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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

    /*Precisa cadastrar um funcionário*/ 


    public Boolean ControleProduto(int idlote,int response) throws EstoqueException{
        //altera arquivo de texto selecionado, altera quantos tem disponível, de determinado lote.
            if(response != 1 || response != 2 ) throw new EstoqueException("Resposta inválida !");
        

            String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
            File pasta = new File(pastaPath);
            if (!pasta.exists()) {
                 throw new EstoqueException("Pasta de produtos não encontrada.");
            }

            File arquivo = null;
            for (File file : pasta.listFiles()) {
                if (file.getName().contains(String.valueOf(idlote))) {
                    arquivo = file;
                    break;
                }
            }

            if (arquivo == null) {
                throw new EstoqueException("Produto com id de lote " + idlote + " não encontrado.");
            }

            StringBuilder conteudo = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
            conteudo.append(linha).append(System.lineSeparator());
            }
            } catch (IOException e) {
             throw new EstoqueException("Erro ao ler o arquivo: " + e.getMessage());
            }

            String[] linhas = conteudo.toString().split(System.lineSeparator());

            boolean encontradoQuantidade = false;

            for (int i = 0; i < linhas.length; i++) {
                if (linhas[i].contains("quantidade disponivel do produto")) {
                    // Localiza a linha com a quantidade
                    String quantidadeStr = linhas[i].split(":")[1].trim();
                    double quantidade = Double.parseDouble(quantidadeStr);
        
                    // Se a resposta for 1 (aprovar), diminui a quantidade em 1
                    if (response == 1) {
                        quantidade -= 1;
                        if (quantidade < 0) {
                            throw new EstoqueException("Quantidade insuficiente de produto para aprovação.");
                        }
                    }
                    // Se a resposta for 2 (rejeitar), diminui a quantidade em 1
                    if (response == 2) {
                        quantidade -= 1;
                        if (quantidade < 0) {
                            throw new EstoqueException("Quantidade insuficiente de produto para rejeição.");
                        }
                    }
        
                    // Atualiza a linha de quantidade
                    linhas[i] = "quantidade disponivel do produto: " + quantidade;
                    encontradoQuantidade = true;
                    break;
                }
            }
        
            if (!encontradoQuantidade) {
                throw new EstoqueException("Linha de quantidade não encontrada no arquivo.");
            }
        
            // Regrava o conteúdo atualizado no arquivo
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                for (String linha : linhas) {
                    writer.write(linha);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new EstoqueException("Erro ao escrever no arquivo: " + e.getMessage());
            }
        
            return true;

    }


   
}
