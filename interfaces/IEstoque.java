package interfaces;


import exceptions.EstoqueException;

public interface IEstoque
    {
        public void CadastramentoProduto(int idlote,String nome, String descricao, float preco, double quantidade) throws EstoqueException;
        public void verificarEstoqueBaixo();
        public void relatoriomovimentacao();
        public void addEstoque(int response,int idlote, int idVenda) throws EstoqueException;
        public void addEstoquePosCadastro(int idlote, double novaQuantidade) throws EstoqueException;
    }
