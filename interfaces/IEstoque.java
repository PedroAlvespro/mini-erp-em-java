package interfaces;


import exceptions.EstoqueException;

public interface IEstoque
    {
        public void CadastramentoProduto(int idlote,String nome, String descricao, float preco, double quantidade) throws EstoqueException;
        public Boolean ControleProduto(int idlote,int idvenda,int response) throws EstoqueException;
        public void AlertaEstoque(int idlot) throws EstoqueException; //quantidade por parâmetro ?
        
    }
