package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import exceptions.UsuarioException;
import exceptions.VendasException;
import interfaces.IVendaInterface;

public class VendaService implements IVendaInterface {
    
    private final String pastaUsuarios = System.getProperty("user.dir") + File.separator + "usuarios";

    public Boolean ConfirmaCliente(String nickname) throws VendasException {
            File arquivoUsuario = new File(pastaUsuarios, nickname + ".txt");
            return arquivoUsuario.exists();
        
    }

    public boolean validarGerente(String nickname, String senha) throws UsuarioException {
    File arquivoUsuario = new File(pastaUsuarios, nickname + ".txt");

    if (!arquivoUsuario.exists()) {
        return false;
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(arquivoUsuario))) {
        String linha;
        String senhaArmazenada = null;
        boolean isGerente = false;

        while ((linha = reader.readLine()) != null) {
            if (linha.startsWith("Senha: ")) {
                senhaArmazenada = linha.substring(7).trim();
            }
            if (linha.startsWith("Tipo de usuário: Gerente")) {
                isGerente = true;
            }
        }

        return isGerente && senha != null && senha.equals(senhaArmazenada);

    } catch (IOException e) {
        throw new UsuarioException("Erro ao validar gerente.");
    }
}

    public int Venda(int idLote, double quantidadeComprada, float valorPago) throws VendasException {
        
        String pastaProdutos = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        String pastaVendas = System.getProperty("user.dir") + File.separator + "vendas";

        File pasta = new File(pastaProdutos);
        if (!pasta.exists() || !pasta.isDirectory()) {
            throw new VendasException ("Pasta de produtos não encontrada.");
        }

        String nomeArquivo = "produto_" + idLote + ".txt";
        File arquivo = new File(pasta, nomeArquivo);

        if (!arquivo.exists()) {
            throw new VendasException ("Produto com ID de lote " + idLote + " não encontrado.");
        }

        float preco = 0;
        double quantidadeDisponivel = 0;
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
            throw new VendasException ("Erro ao ler o arquivo do produto.");
        }

        if (quantidadeComprada > quantidadeDisponivel) {
            throw new VendasException ("Quantidade insuficiente em estoque.");
        }

        float valorTotal = preco * (float) quantidadeComprada;
        if (valorPago != valorTotal) {
            throw new VendasException ("Valor incorreto para a compra. Venda inválida.");
        }

        int idVenda = UUID.randomUUID().hashCode();
        File pastaV = new File(pastaVendas);
        if (!pastaV.exists()) {
            pastaV.mkdirs();
        }

        File arquivoVenda = new File(pastaV, "venda_" + idVenda + ".txt");
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
            throw new VendasException ("Erro ao registrar a venda.");
        }

        atualizarEstoque(arquivo, quantidadeDisponivel - quantidadeComprada);
        return idVenda;
    }

    public void atualizarEstoque(File arquivo, double novaQuantidade) throws VendasException  {
        try {
            StringBuilder conteudoAtualizado = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    if (linha.startsWith("quantidade disponivel do produto: ")) {
                        linha = "quantidade disponivel do produto: " + novaQuantidade;
                    }
                    conteudoAtualizado.append(linha).append(System.lineSeparator());
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
                writer.write(conteudoAtualizado.toString());
            }
        } catch (IOException e) {
            throw new VendasException ("Erro ao atualizar o estoque.");
        }
    }

    public void relatorioDeVendas() {
    String pastaVendasPath = System.getProperty("user.dir") + File.separator + "vendas";
    File pastaVendas = new File(pastaVendasPath);

    String pastaRelatoriosPath = System.getProperty("user.dir") + File.separator + "relatorios_vendas";
    File pastaRelatorios = new File(pastaRelatoriosPath);

    if (!pastaRelatorios.exists()) {
        pastaRelatorios.mkdirs();
    }

    File[] arquivosVendas = pastaVendas.listFiles();
    if (arquivosVendas == null || arquivosVendas.length == 0) {
        System.out.println("Nenhuma venda registrada.");
        return;
    }

    File arquivoRelatorio = new File(pastaRelatorios, "relatorio_vendas.txt");
    StringBuilder conteudoRelatorio = new StringBuilder();
    conteudoRelatorio.append("Relatório de Vendas\n");
    conteudoRelatorio.append("====================================\n");

    for (File arquivoVenda : arquivosVendas) {
        if (arquivoVenda.isFile() && arquivoVenda.getName().startsWith("venda_")) {
            try (BufferedReader reader = new BufferedReader(new FileReader(arquivoVenda))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    conteudoRelatorio.append(linha).append("\n");
                }
                conteudoRelatorio.append("------------------------------------\n");
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo de venda: " + arquivoVenda.getName());
                System.out.println();
            }
        }
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivoRelatorio))) {
        writer.write(conteudoRelatorio.toString());
        System.out.println("Relatório de vendas gerado com sucesso: ");
    } catch (IOException e) {
        System.err.println("Erro ao criar o relatório de vendas: " + e.getMessage());
    }

    System.out.println(conteudoRelatorio.toString());
}

    public void exibeProdutos() {
        String pastaPath = System.getProperty("user.dir") + File.separator + "arquivosprodutos";
        File pasta = new File(pastaPath);

        if (!pasta.exists() || !pasta.isDirectory()) {
            System.out.println("Nenhum produto encontrado. Pasta não existe.");
            return;
         }

         File[] arquivosProdutos = pasta.listFiles();
        if (arquivosProdutos == null || arquivosProdutos.length == 0) {
         System.out.println("Nenhum produto disponível.");
         return;
    }

        System.out.println("Lista de Produtos Disponíveis:");
        System.out.println("====================================");
        
        for (File arquivo : arquivosProdutos) {
            if (arquivo.isFile() && arquivo.getName().startsWith("produto_")) {
                String idLote = arquivo.getName().replace("produto_", "").replace(".txt", "");
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
                    String nomeProduto = reader.readLine();
                    if (nomeProduto != null && !nomeProduto.trim().isEmpty()) {
                        System.out.println("ID: " + idLote + " - Produto: " + nomeProduto);
                    } else {
                        System.out.println("ID: " + idLote + " - Produto sem nome disponível.");
                    }
                } catch (IOException e) {
                    System.err.println("Erro ao ler o arquivo: " + arquivo.getName());
                }
            }
        }
    }

    
}
