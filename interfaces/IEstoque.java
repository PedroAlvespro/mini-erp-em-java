package interfaces;

import exceptions.EstoqueException;

public interface IEstoque
    {
        public void CadastramentoProduto(String nome, String descricao, String preco, double quantidade) throws EstoqueException;
    }
