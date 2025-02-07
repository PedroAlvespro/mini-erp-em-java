package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import exceptions.VendasException;
import interfaces.IVendaInterface;

public class VendaService implements IVendaInterface {
    
    
    private final String pastaUsuarios = System.getProperty("user.dir") + File.separator + "usuarios";



    public Boolean ConfirmaCliente(String nickname) throws VendasException {
            File arquivoUsuario = new File(pastaUsuarios, nickname + ".txt");
            return arquivoUsuario.exists();
        
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

    
}
