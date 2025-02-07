package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import exceptions.EstoqueException;
import interfaces.IEstoque;

public abstract class EstoqueService implements IEstoque{

    
    public void CadastramentoProduto(int idlote,String nome, String descricao, float preco, double quantidade) throws EstoqueException{

        //Funcionário aprova ou nao o id do lote
        if(idlote < 0) throw new EstoqueException("id de lote menor do que 0");

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
        String nomeArquivo = "produto_"+idlote+".txt";
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

    public Boolean ControleProduto(int idlote,int idVenda, int response) throws EstoqueException {

        // Verifica se a resposta é válida
        if (response != 1 && response != 2 && response != 3) {
            throw new EstoqueException("Resposta inválida!");
        }
    
        // Caminho correto da pasta onde os arquivos dos produtos são armazenados
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);
    
        // Verifica se a pasta existe
        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new EstoqueException("Pasta de produtos não encontrada.");
        }
    
        // Define o nome exato do arquivo que deve ser procurado
        String nomeArquivo = "produto_" + idlote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);
    
        // Verifica se o arquivo existe
        if (!arquivo.exists()) {
            throw new EstoqueException("Produto com ID de lote " + idlote + " não encontrado.");
        }
    
        // Se a resposta for 2 (rejeitado), deletamos o arquivo e retornamos imediatamente
        if (response == 2) { 
            if (arquivo.delete()) {
                System.out.println("Produto rejeitado e arquivo deletado com sucesso!");
                return true; // Retorna imediatamente para evitar execução desnecessária
            } else {
                throw new EstoqueException("Erro ao deletar o arquivo do produto rejeitado.");
            }
        }
    
        // Lê o conteúdo do arquivo
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
                encontradoQuantidade = true;
                break;
            }
        }
    
        if (!encontradoQuantidade) {
            throw new EstoqueException("Linha de quantidade não encontrada no arquivo.");
        }
    
        System.out.println("Produto aprovado com sucesso!");
        return true;
    }
   
    public void AlertaEstoque(int idlote) throws EstoqueException {
        // Caminho da pasta onde os arquivos dos produtos estão armazenados
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);
    
        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new EstoqueException("Pasta de produtos não encontrada.");
        }
    
        // Define o nome exato do arquivo do produto
        String nomeArquivo = "produto_" + idlote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);
    
        if (!arquivo.exists()) {
            throw new EstoqueException("Produto com ID de lote " + idlote + " não encontrado.");
        }
    
        // Lê o conteúdo do arquivo para verificar a quantidade disponível
        double quantidade = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.contains("quantidade disponivel do produto")) {
                    String quantidadeStr = linha.split(":")[1].trim();
                    quantidade = Double.parseDouble(quantidadeStr);
                    break;
                }
            }
        } catch (IOException e) {
            throw new EstoqueException("Erro ao ler o arquivo do produto: " + e.getMessage());
        }
    
        if (quantidade == -1) {
            throw new EstoqueException("Quantidade do produto não encontrada no arquivo.");
        }
    
        // Se a quantidade for menor que 5, cria um alerta na pasta "arquivosAlerta"
        if (quantidade < 5) {
            String alertaPastaPath = System.getProperty("user.dir") + File.separator + "arquivosAlerta";
            File alertaPasta = new File(alertaPastaPath);
    
            if (!alertaPasta.exists()) {
                alertaPasta.mkdirs(); // Cria a pasta se não existir
            }
    
            File alertaArquivo = new File(alertaPasta, "alerta_" + idlote + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(alertaArquivo))) {
                writer.write("ALERTA: Produto com ID de lote " + idlote + " está com baixo estoque.");
                writer.newLine();
                writer.write("Quantidade disponível: " + quantidade);
                writer.newLine();
                System.out.println("Arquivo de alerta criado com sucesso: " + alertaArquivo.getAbsolutePath());
            } catch (IOException e) {
                throw new EstoqueException("Erro ao criar o arquivo de alerta: " + e.getMessage());
            }
        }
    }

      // Método para realizar uma venda
    public static int Venda(int idLote, double quantidadeComprada, float valorPago) throws EstoqueException {
        // Caminho correto da pasta onde os arquivos dos produtos estão armazenados
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);

        // Verificar se a pasta existe
        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new EstoqueException("Pasta de produtos não encontrada.");
        }

        // Define o nome do arquivo que deve ser procurado
        String nomeArquivo = "produto_" + idLote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);

        // Verifica se o arquivo existe
        if (!arquivo.exists()) {
            throw new EstoqueException("Produto com ID de lote " + idLote + " não encontrado.");
        }

        // Lê o conteúdo do arquivo e extrai a quantidade e preço
        double quantidadeDisponivel = 0;
        float preco = 0;
        StringBuilder conteudo = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                conteudo.append(linha).append(System.lineSeparator());
                if (linha.startsWith("Preco do produto: ")) {
                    preco = Float.parseFloat(linha.split(": ")[1]);
                }
                if (linha.startsWith("quantidade disponivel do produto: ")) {
                    quantidadeDisponivel = Double.parseDouble(linha.split(": ")[1]);
                }
            }
        } catch (IOException e) {
            throw new EstoqueException("Erro ao ler o arquivo do produto.");
        }

        // Verifica se a quantidade disponível é suficiente
        if (quantidadeComprada > quantidadeDisponivel) {
            throw new EstoqueException("Quantidade insuficiente em estoque.");
        }

        // Calcula o valor total e verifica se o valor pago está correto
        float valorTotal = preco * (float) quantidadeComprada;
        if (valorPago != valorTotal) {
            throw new EstoqueException("Valor incorreto para a compra. Venda inválida.");
        }

        // Registra a venda
        int idVenda = UUID.randomUUID().hashCode();

        // Atualiza o estoque após a venda
        atualizarEstoque(idLote, quantidadeDisponivel - quantidadeComprada);

        // Cria o arquivo de venda
        String pastaVendasPath = System.getProperty("user.dir") + File.separator + "vendas";
        File pastaVendas = new File(pastaVendasPath);
        if (!pastaVendas.exists()) {
            pastaVendas.mkdirs();
        }

        File arquivoVenda = new File(pastaVendas, "venda_" + idVenda + ".txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoVenda))) {
            writer.write("ID da Venda: " + idVenda);
            writer.newLine();
            writer.write("ID do Lote: " + idLote);
            writer.newLine();
            writer.write("Quantidade Vendida: " + quantidadeComprada);
            writer.newLine();
            writer.write("Valor Pago: " + valorPago);
            writer.newLine();
            System.out.println("Venda registrada com sucesso!");
        } catch (IOException e) {
            throw new EstoqueException("Erro ao registrar a venda.");
        }

        return idVenda;
    }
            
            public static void atualizarEstoque(int idLote, double novaQuantidade) throws EstoqueException {
                // Caminho correto da pasta onde os arquivos dos produtos estão armazenados
                String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
                File pasta = new File(pastaPath);
        
                // Verificar se a pasta existe
                if (!pasta.exists() || !pasta.isDirectory()) {
                    throw new EstoqueException("Pasta de produtos não encontrada.");
                }
        
                // Define o nome do arquivo que deve ser procurado
                String nomeArquivo = "produto_" + idLote + ".txt";
                File arquivo = new File(pasta, nomeArquivo);
        
                // Verifica se o arquivo existe
                if (!arquivo.exists()) {
                    throw new EstoqueException("Produto com ID de lote " + idLote + " não encontrado.");
                }
        
                // Lê o conteúdo do arquivo e atualiza a quantidade
                StringBuilder conteudoAtualizado = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        // Atualiza a linha da quantidade disponível
                        if (linha.startsWith("quantidade disponivel do produto: ")) {
                            linha = "quantidade disponivel do produto: " + novaQuantidade;
                        }
                        conteudoAtualizado.append(linha).append(System.lineSeparator());
                    }
                } catch (IOException e) {
                    throw new EstoqueException("Erro ao ler o arquivo do produto: " + e.getMessage());
                }
        
                // Grava o conteúdo atualizado no arquivo
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                    writer.write(conteudoAtualizado.toString());
                } catch (IOException e) {
                    throw new EstoqueException("Erro ao atualizar o estoque no arquivo: " + e.getMessage());
                }
        
                System.out.println("Estoque atualizado com sucesso para o ID de lote " + idLote);
            }
    }

    
    


   

