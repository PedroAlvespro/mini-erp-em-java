package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import exceptions.EstoqueException;
import interfaces.IEstoque;

public abstract class EstoqueService implements IEstoque{

    
    public void CadastramentoProduto(int idlote, String nome, String descricao, float preco, double quantidade) throws EstoqueException {
        if (idlote < 0) throw new EstoqueException("id de lote menor do que 0");
        if (nome == null || nome.trim().isEmpty()) throw new EstoqueException("Nome do produto inválido.");
        if (descricao == null || descricao.trim().isEmpty()) throw new EstoqueException("Descrição do produto inválida.");
        if (preco < 0) throw new EstoqueException("Preço do produto inválido.");
        if (quantidade < 0) throw new EstoqueException("Quantidade do produto inválida.");

        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);
        String nomeArquivo = "produto_" + idlote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);

        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write("Id do lote do produto: " + idlote);
            writer.newLine();
            writer.write("Nome do produto: " + nome);
            writer.newLine();
            writer.write("Descricao do Produto : " + descricao);
            writer.newLine();
            writer.write("Preco do produto: " + preco);
            writer.newLine();
            writer.write("quantidade inicial do produto: " + quantidade);  // Guarda a quantidade inicial
            writer.newLine();
            writer.write("quantidade disponivel do produto: " + quantidade);  // Quantidade disponível
            writer.newLine();
            System.out.println("Arquivo criado com sucesso: " + arquivo.getAbsolutePath());
            System.out.printf("Produto cadastrado com sucesso!\n", nome);
        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo: " + e.getMessage());
        }
    }

    public void relatoriomovimentacao() {
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.err.println("Pasta de produtos não encontrada.");
            return;
        }

        File[] arquivos = pasta.listFiles();
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        System.out.println("Relatório de movimentação de estoque:");
        for (File arquivo : arquivos) {
            if (arquivo.isFile() && arquivo.getName().startsWith("produto_") && arquivo.getName().endsWith(".txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                    System.out.println("------------------------------------------------");
                    System.out.println("Arquivo: " + arquivo.getName());

                    String linha;
                    Double quantidadeInicial = null;
                    Double quantidadeAtual = null;
                    while ((linha = reader.readLine()) != null) {
                        if (linha.startsWith("quantidade inicial do produto: ")) {
                            quantidadeInicial = Double.parseDouble(linha.split(": ")[1]);
                        }
                        if (linha.startsWith("quantidade disponivel do produto: ")) {
                            quantidadeAtual = Double.parseDouble(linha.split(": ")[1]);
                        }
                    }

                    if (quantidadeInicial != null && quantidadeAtual != null) {
                        System.out.println("Quantidade inicial: " + quantidadeInicial);
                        System.out.println("Quantidade atual: " + quantidadeAtual);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + arquivo.getName() + ": " + e.getMessage());
                }
            }
        }
    }

    public void addEstoque(int idlote, int idVenda, int response) throws EstoqueException {
        String pastaVendas = System.getProperty("user.dir") + File.separator + "vendas";
        File pastaVenda = new File(pastaVendas);

        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pastaProduto = new File(pastaPath);

        if (response == 2) {  // Venda rejeitada -> devolver estoque
            File arquivoVenda = new File(pastaVenda, "venda_" + idVenda + ".txt");

            if (!arquivoVenda.exists()) {
                throw new EstoqueException("Arquivo de venda com ID " + idVenda + " não encontrado.");
            }

            double quantidadeVendida = 0;
            boolean encontrouQuantidade = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoVenda))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    if (linha.contains("Quantidade Vendida: ")) {
                        String quantidadeStr = linha.split(": ")[1].trim();
                        try {
                            quantidadeVendida = Double.parseDouble(quantidadeStr);
                            encontrouQuantidade = true;
                        } catch (NumberFormatException e) {
                            throw new EstoqueException("Erro ao converter quantidade vendida: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                throw new EstoqueException("Erro ao ler o arquivo de venda: " + e.getMessage());
            }

            if (!encontrouQuantidade) {
                throw new EstoqueException("Quantidade vendida não encontrada no arquivo de venda.");
            }

            File arquivoProduto = new File(pastaProduto, "produto_" + idlote + ".txt");

            if (!arquivoProduto.exists()) {
                throw new EstoqueException("Produto com ID de lote " + idlote + " não encontrado.");
            }

            double quantidadeAtual = 0;
            boolean encontrouQuantidadeProduto = false;
            StringBuilder conteudoAtualizado = new StringBuilder();

            try (BufferedReader readerProduto = new BufferedReader(new FileReader(arquivoProduto))) {
                String linha;
                while ((linha = readerProduto.readLine()) != null) {
                    if (linha.startsWith("quantidade disponivel do produto: ")) {
                        String quantidadeStr = linha.split(": ")[1].trim();
                        try {
                            quantidadeAtual = Double.parseDouble(quantidadeStr);
                            encontrouQuantidadeProduto = true;
                        } catch (NumberFormatException e) {
                            throw new EstoqueException("Erro ao converter quantidade do produto: " + e.getMessage());
                        }
                        linha = "quantidade disponivel do produto: " + (quantidadeAtual + quantidadeVendida);
                    }
                    conteudoAtualizado.append(linha).append(System.lineSeparator());
                }
            } catch (IOException e) {
                throw new EstoqueException("Erro ao ler o arquivo do produto: " + e.getMessage());
            }

            if (!encontrouQuantidadeProduto) {
                throw new EstoqueException("Quantidade do produto não encontrada no arquivo.");
            }

            try (BufferedWriter writerProduto = new BufferedWriter(new FileWriter(arquivoProduto))) {
                writerProduto.write(conteudoAtualizado.toString());
                System.out.println("Quantidade devolvida ao estoque com sucesso. Nova quantidade: " + (quantidadeAtual + quantidadeVendida));
            } catch (IOException e) {
                throw new EstoqueException("Erro ao atualizar o arquivo do produto: " + e.getMessage());
            }
        }
    }



    public void verificarEstoqueBaixo() {
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);
    
        if (!pasta.exists() || !pasta.isDirectory()) {
            System.err.println("Pasta de produtos não encontrada.");
            return;
        }
    
        File[] arquivos = pasta.listFiles();
        if (arquivos == null || arquivos.length == 0) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }
    
        System.out.println("Produtos com estoque baixo:");
        for (File arquivo : arquivos) {
            if (arquivo.isFile() && arquivo.getName().startsWith("produto_") && arquivo.getName().endsWith(".txt")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                    String linha;
                    double quantidade = -1;
                    String nomeProduto = "Desconhecido";
    
                    while ((linha = reader.readLine()) != null) {
                        if (linha.contains("Nome do produto")) {
                            nomeProduto = linha.split(":")[1].trim();
                        }
                        if (linha.contains("quantidade disponivel do produto")) {
                            String quantidadeStr = linha.split(":")[1].trim();
                            quantidade = Double.parseDouble(quantidadeStr);
                            break;
                        }
                    }
                    if (quantidade != -1 && quantidade < 5) {
                        System.out.println("Produto: " + nomeProduto + " (" + arquivo.getName() + ") - Quantidade: " + quantidade);
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo " + arquivo.getName() + ": " + e.getMessage());
                }
            }
        }
    }
   
     
        // Método para realizar uma venda, retorna um idvenda quando a venda é efetuada corretamente
      public static int Venda(int idLote, double quantidadeComprada, float valorPago) throws EstoqueException {
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);

        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new EstoqueException("Pasta de produtos não encontrada.");
        }

        String nomeArquivo = "produto_" + idLote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);

        if (!arquivo.exists()) {
            throw new EstoqueException("Produto com ID de lote " + idLote + " não encontrado.");
        }

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

        if (quantidadeComprada > quantidadeDisponivel) {
            throw new EstoqueException("Quantidade insuficiente em estoque.");
        }

        float valorTotal = preco * (float) quantidadeComprada;
        if (valorPago != valorTotal) {
            throw new EstoqueException("Valor incorreto para a compra. Venda inválida.");
        }

        int idVenda = UUID.randomUUID().hashCode();
        atualizarEstoque(idLote, quantidadeDisponivel - quantidadeComprada);
        registrarMovimentacaoEstoque(idLote, "Venda", quantidadeComprada);
        registrarVenda(idVenda, idLote, quantidadeComprada, valorPago);

        return idVenda;
    }


    // Cria uma registro de Movimentaçãp
    public static void registrarMovimentacaoEstoque(int idLote, String tipoMovimentacao, double quantidade) throws EstoqueException {
        String pastaPath = System.getProperty("user.dir") + File.separator + "movimentacoes";
        File pasta = new File(pastaPath);

        if (!pasta.exists()) {
            pasta.mkdirs();
        }

        File arquivo = new File(pasta, "movimentacoes_produto_" + idLote + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, true))) {
            writer.write("Movimentação: " + tipoMovimentacao);
            writer.newLine();
            writer.write("Quantidade: " + quantidade);
            writer.newLine();
            writer.write("Data: " + LocalDateTime.now());
            writer.newLine();
            writer.write("-----------------------------------");
            writer.newLine();
        } catch (IOException e) {
            throw new EstoqueException("Erro ao registrar movimentação de estoque.");
        }
    }

    //atualiza o estoque.
    public static void atualizarEstoque(int idLote, double novaQuantidade) throws EstoqueException {
    String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
    File arquivo = new File(pastaPath, "produto_" + idLote + ".txt");

    if (!arquivo.exists()) {
        throw new EstoqueException("Produto com ID de lote " + idLote + " não encontrado.");
    }

    double quantidadeInicial = 0;
    boolean encontrouQuantidade = false;
    StringBuilder conteudoAtualizado = new StringBuilder();

    try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
        String linha;
        while ((linha = reader.readLine()) != null) {
            if (linha.startsWith("quantidade disponivel do produto: ")) {
                quantidadeInicial = Double.parseDouble(linha.split(": ")[1]);
                encontrouQuantidade = true;
                linha = "quantidade disponivel do produto: " + novaQuantidade; // Update quantity line
            }
            conteudoAtualizado.append(linha).append(System.lineSeparator());
        }
    } catch (IOException | NumberFormatException e) {
        throw new EstoqueException("Erro ao ler o arquivo do produto: " + e.getMessage());
    }

    if (!encontrouQuantidade) {
        throw new EstoqueException("Formato do arquivo do produto inválido. Quantidade não encontrada.");
    }

    // Writing the updated content back to the file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
        writer.write(conteudoAtualizado.toString());
    } catch (IOException e) {
        throw new EstoqueException("Erro ao atualizar o estoque no arquivo: " + e.getMessage());
    }

    // Print the initial and current quantities
    System.out.println("Quantidade inicial: " + quantidadeInicial);
    System.out.println("Quantidade atual: " + novaQuantidade);

    // Determine the type of movement (Entrada or Saída) based on the quantity change
    String tipoMovimentacao = (novaQuantidade > quantidadeInicial) ? "Entrada" : "Saída";
    double quantidadeMovimentada = Math.abs(novaQuantidade - quantidadeInicial);

    // Register the movement in the stock movement log
    registrarMovimentacaoEstoque(idLote, tipoMovimentacao, quantidadeMovimentada);
}



    public static void registrarVenda(int idVenda, int idLote, double quantidade, float valorPago) throws EstoqueException {
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
            writer.write("Quantidade Vendida: " + quantidade);
            writer.newLine();
            writer.write("Valor Pago: " + valorPago);
            writer.newLine();
            System.out.println("Venda registrada com sucesso!");
        } catch (IOException e) {
            throw new EstoqueException("Erro ao registrar a venda.");
        }
    }
}

    

    
    


   

