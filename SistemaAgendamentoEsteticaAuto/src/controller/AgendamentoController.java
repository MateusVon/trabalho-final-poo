package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import dao.AgendamentoDAO;
import dao.PagamentoDAO;
import exceptions.AgendamentoException;
import model.Agendamento;
import model.Cliente;
import model.Pagamento;
import model.Servicos;
import model.Veiculo;
import model.enums.FormaPagamento;
import model.enums.StatusPagamento;
import model.enums.TiposDeServicos;

public class AgendamentoController {
  private AgendamentoDAO agendamentoDAO;
  private PagamentoDAO pagamentoDAO;

  public AgendamentoController() {
    this.agendamentoDAO = new AgendamentoDAO();
    this.pagamentoDAO = new PagamentoDAO();
  }

  public void verificaAgendamento(Cliente cliente, Veiculo veiculo, TiposDeServicos tipoServico, String dataStr,
      String horaStr, FormaPagamento forma) throws AgendamentoException {

    // Converte a data e hora recebidas do usuário pela interface do formato String
    // para LocalDate/LocalTime
    DateTimeFormatter fmtData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate dataEntregaCliente = LocalDate.parse(dataStr, fmtData);
    LocalTime horarioEntregaCliente = LocalTime.parse(horaStr, DateTimeFormatter.ofPattern("HH:mm"));

    double percentual = veiculo.calcularPrecoEspecifico(); // Serve para resgatar o percentual que será aplicado na
                                                           // criação do preço do serviço.
    Servicos servico = new Servicos(tipoServico, percentual); // Criação do serviço desejado.

    // Cria e salva o pagamento.
    Pagamento pagamento = new Pagamento(servico.getPreco(), forma, StatusPagamento.PENDENTE);
    pagamentoDAO.salvar(pagamento);

    // Por fim, o objeto agendamento é criado.

    Agendamento agendamento = new Agendamento(cliente, veiculo, servico, horarioEntregaCliente, dataEntregaCliente);
    agendamento.adicionarServico(servico); // Adiciona a list de serviços.
    agendamento.setPagamento(pagamento); // Seta a forma que o pagamento se encontra naquele momento.

    if (!agendamentoDAO.salvar(agendamento)) {
      throw new AgendamentoException("Erro! Não foi possível gerar este agendamento.");
    }
  }
}
