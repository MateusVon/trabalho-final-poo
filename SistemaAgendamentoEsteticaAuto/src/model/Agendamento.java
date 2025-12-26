package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Agendamento {
  private int id;
  private LocalTime horario;
  private int prioridade;
  private Cliente cliente;
  private List<Servicos> servicos;
  private String dataEntrega;
  private Veiculo veiculo; // Adicionei atributo veiculo
  private Pagamento pagamento; // Adicionei atributo pagamento

  public Agendamento(){
    this.servicos = new ArrayList<>();
  }

  public Agendamento(Cliente cliente, Veiculo veiculo, Servicos servico, LocalTime horario){
    this.cliente = cliente;
    this.veiculo = veiculo;
    this.horario = horario;
    this.definirPrioridade(veiculo, servico);
    this.servicos = new ArrayList<>(); 
  }

  public Agendamento(int id, Cliente cliente, Veiculo veiculo, String servico, String dataEntrega, LocalTime horario, int prioridade){
    this.id = getId();
    this.cliente = getCliente();
    this.veiculo = getVeiculo();
    this.horario = getHorario();
    this.prioridade = getPrioridade();
    this.servicos = getServicos(); 
    this.dataEntrega = getDataEntrega();
  }

  public void definirPrioridade(Veiculo veiculo, Servicos servico) {
    LocalDate dataEntrega;
    //if(this.prazo == null) return; // Verificação para que exista prazo
    
    LocalDate dataAtual = LocalDate.now();
    dataEntrega = dataAtual.plusDays(veiculo.calcularPrazoEstimado(servico)); 
    int diaEntrega = dataEntrega.getDayOfMonth();

    this.prioridade = diaEntrega - dataAtual.getDayOfMonth();
    this.dataEntrega = diaEntrega + "-" + dataEntrega.getMonthValue() + "-" + dataEntrega.getYear();
  }

  public void adicionarServico(Servicos servico) {
    this.servicos.add(servico);
  }
  // Adicionei metodo remover
  public void removerServico(Servicos servico){
    this.servicos.remove(servico);
  }

  public double calcularTotal() {
    double total = 0.0;

    for (Servicos s : servicos) {
      total += s.getPreco();
    }

    if (cliente != null && veiculo != null) {
      //total += veiculo.getTaxaAdicional(); ARRUMAR ESSE CALCULO
    }
    return total;
  }

  // Adicionei Setters
  public int getId() {return id;}
  public void setId(int id){this.id = id;}

  public LocalTime getHorario() {return horario;}
  public void setHorario(LocalTime horario){this.horario = horario;}

  public int getPrioridade() {return prioridade;}

  public Cliente getCliente() {return cliente;}
  public void setCliente(Cliente cliente){this.cliente = cliente;}

  public Veiculo getVeiculo(){return veiculo;}
  public void setVeiculo(Veiculo veiculo){this.veiculo = veiculo;}

  public List<Servicos> getServicos() {return servicos;}

  public Pagamento getPagamento(){return pagamento;}
  public void setPagamento(Pagamento pagamento){this.pagamento = pagamento;}

  public String getDataEntrega() {return dataEntrega;}
  public void setDataEntrega(String dataEntrega) {this.dataEntrega = dataEntrega;}

  @Override
    public String toString() {
        String nomeCliente = (cliente != null) ? cliente.getNome() : "Sem Cliente";
        String descVeiculo = (veiculo != null) ? veiculo.toString() : "Sem Veículo";
        String statusPag = (pagamento != null) ? pagamento.getStatus().toString() : "Não Gerado";

        return String.format("ID: %d | Data: %s às %s | %s | %s | Total: R$ %.2f | Pag: %s",
                id, dataEntrega, horario, nomeCliente, descVeiculo, calcularTotal(), statusPag);
    }

}
