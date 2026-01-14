package controller;

import dao.PagamentoDAO;
import java.util.List;
import model.Pagamento;
import model.enums.FormaPagamento;

public class PagamentoController {

  private final PagamentoDAO pagamentoDAO;

  public PagamentoController() {
    this.pagamentoDAO = new PagamentoDAO();
  }

  public boolean cadastrarPagamento(double valor, FormaPagamento formaPagamento) {

    if (valor <= 0) {
      return false;
    }
    if (formaPagamento == null) {
      return false;
    }

    Pagamento pagamento = new Pagamento(valor, formaPagamento);

    return pagamentoDAO.salvar(pagamento);
  }

  public List<Pagamento> listarPagamentos() {

    return pagamentoDAO.listar();
  }

  public boolean confirmarPagamento(int id) {

    if (id <= 0) {
      return false;
    }

    Pagamento pagamento = pagamentoDAO.buscarPorId(id);
    if (pagamento == null) {
      return false;
    }

    pagamento.confirmarPagamento();

    return pagamentoDAO.atualizar(pagamento);
  }

}
