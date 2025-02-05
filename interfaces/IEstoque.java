package interfaces;

import exceptions.EstoqueException;

public interface IEstoque
    {
    
        public void CadastramentoProduto(String nome, String descricao, String preco, double quantidade) throws EstoqueException;
        public void ControleProduto(String nome, String descricao, String preco, double quantidade) throws EstoqueException;
    }
