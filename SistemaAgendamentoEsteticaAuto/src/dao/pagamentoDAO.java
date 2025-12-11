package dao;

import java.util.List;

import model.FormaPagamento;
import model.Pagamento;
import model.StatusPagamento;

public class PagamentoDAO implements IDAO<Pagamento>{

    private static final String ARQUIVO = "pagamentos.txt";

    public boolean salvar(Pagamento pagamento){
        String linha = pagamento.getValor() + ";"+
        pagamento.getFormaPagamento() + ";" +
        pagamento.getStatusPagamento();
        GerenciadorDeArquivos.salvar(ARQUIVO, linha);
        return true;
    }

    public List<Pagamento> listar() {
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    public boolean atualizar(Pagamento objeto){
        
        return true;
    }


    public boolean deletar(int id){
        return true;
    }

    public Pagamento buscarPorId(int id){
        return null;
    }

public static void main(String[] args) {
    Pagamento pagamento = new Pagamento(1000, FormaPagamento.CREDITO, StatusPagamento.PAGO);
    Pagamento pagamento2 = new Pagamento(2345, FormaPagamento.DINHEIRO, StatusPagamento.PENDENTE);
    PagamentoDAO dao = new PagamentoDAO();

    dao.salvar(pagamento);
    dao.salvar(pagamento2);
}

}
