package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import exceptions.AgendamentoException;

public class Agendamento {
  private int id;
  private LocalTime horario;
  private int prioridade;
  private Cliente cliente;
  private List<Servicos> servicos;
  private LocalDate dataEntregaCliente;
  private String dataEntrega;
  private Veiculo veiculo; // Adicionei atributo veiculo
  private Pagamento pagamento; // Adicionei atributo pagamento

  public Agendamento() {
    this.servicos = new ArrayList<>();
  }

  // Construtor para novos agendamentos 
  public Agendamento(Cliente cliente, Veiculo veiculo, Servicos servico, LocalTime horario, LocalDate dataEntregaCliente) throws AgendamentoException {
    
    this.cliente = cliente;
    this.veiculo = veiculo;
    this.setHorario(horario);
    this.dataEntregaCliente = dataEntregaCliente;
    this.definirPrazos(veiculo, servico,dataEntregaCliente);
    this.servicos = new ArrayList<>();
  }

  // Construtor para o DAO 
  public Agendamento(int id, Cliente cliente, Veiculo veiculo, String servico, String dataEntrega, LocalTime horario, int prioridade) {
    this.id = id;
    this.cliente = cliente;
    this.veiculo = veiculo;
    this.horario = horario;
    this.prioridade = prioridade;
    this.dataEntrega = dataEntrega;

    this.servicos = new ArrayList<>();

    if (servico != null && !servico.isEmpty()) {
      try {
        model.enums.TiposDeServicos tipo = model.enums.TiposDeServicos.valueOf(servico);
        this.servicos.add(new Servicos(tipo, prioridade));
      } catch (Exception e) {
        System.out.println("Erro ao converter serviço: " + servico);
      }
    }
  }
 
  public void definirPrazos(Veiculo veiculo, Servicos servico, LocalDate dataEntregaCliente) {
    LocalDate dataEntrega;
    // if(this.prazo == null) return; // Verificação para que exista prazo

    LocalDate dataAtual = dataEntregaCliente;
    dataEntrega = dataAtual.plusDays(veiculo.calcularPrazoEstimado(servico));
    int diaEntrega = dataEntrega.getDayOfMonth();

    this.prioridade = diaEntrega - dataAtual.getDayOfMonth();
    this.dataEntrega = diaEntrega + "-" + dataEntrega.getMonthValue() + "-" + dataEntrega.getYear();
  }

  public void adicionarServico(Servicos servico) {
    this.servicos.add(servico);
  }

  // Adicionei metodo remover
  public void removerServico(Servicos servico) {
    this.servicos.remove(servico);
  }

  public double calcularTotal() {
    double total = 0.0;

    for (Servicos s : servicos) {
      total += s.getPreco();
    }

    if (veiculo != null) {
      total = total * veiculo.calcularPrecoEspecifico();
    }
    return total;
  }

  // Setters e Getters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LocalTime getHorario() {
    return horario;
  }

  public void setHorario(LocalTime horario)throws AgendamentoException{
      LocalTime abertura = LocalTime.of(7, 30);
      LocalTime fechamento = LocalTime.of(18, 0);

      if(horario.isBefore(abertura) || horario.isAfter(fechamento)){
        throw new AgendamentoException("Horário inválido. O funcionamento padrão é de 7:30 às 18:30");
      }
      this.horario = horario;
  }

  public int getPrioridade() {
    return prioridade;
  }
  public void setPrioridade(int prioridade){
    this.prioridade = prioridade;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Veiculo getVeiculo() {
    return veiculo;
  }

  public void setVeiculo(Veiculo veiculo) {
    this.veiculo = veiculo;
  }

  public List<Servicos> getServicos() {
    return servicos;
  }

  public Pagamento getPagamento() {
    return pagamento;
  }

  public void setPagamento(Pagamento pagamento) {
    this.pagamento = pagamento;
  }

  public String getDataEntrega() {
    return dataEntrega;
  }

  public void setDataEntrega(String dataEntrega) {
    this.dataEntrega = dataEntrega;
  }

  @Override
  public String toString() {
    String nomeCliente = (cliente != null) ? cliente.getNome() : "Sem Cliente";
    String descVeiculo = (veiculo != null) ? veiculo.toString() : "Sem Veículo";
    String statusPag = (pagamento != null) ? pagamento.getStatus().toString() : "Não Gerado";

    return String.format("ID: %d | Data: %s às %s | %s | %s | Total: R$ %.2f | Pag: %s",
        id, dataEntrega, horario, nomeCliente, descVeiculo, calcularTotal(), statusPag);
  }

}
