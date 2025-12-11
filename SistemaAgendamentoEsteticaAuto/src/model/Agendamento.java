package model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Agendamento {
  private int id;
  private LocalDate prazo;
  private LocalTime horario;
  private int prioridade;
  private Cliente cliente;
  private List<Servicos> servicos;

  public Agendamento(int id, LocalDate prazo, LocalTime horario, Cliente cliente) {
    this.id = id;
    this.prazo = prazo;
    this.horario = horario;
    this.cliente = cliente;
    this.servicos = new ArrayList<>();
    definirPrioridade();
  }

  public void definirPrioridade() {
    LocalDate hoje = LocalDate.now();

    long diasHoje = hoje.toEpochDay();
    long diasPrazo = this.prazo.toEpochDay();

    long diferenca = diasPrazo - diasHoje;

    if (diferenca <= 0) {
      this.prioridade = 1;
    } else if (diferenca <= 2) {
      this.prioridade = 2;
    } else {
      this.prioridade = 3;
    }
  }

  public void adicionarServico(Servicos servico) {
    this.servicos.add(servico);
  }

  public double calcularTotal() {
    double total = 0.0;

    for (Servicos s : servicos) {
      total += s.getPreco();
    }

    if (cliente != null && cliente.getVeiculo() != null) {
      total += cliente.getVeiculo().getTaxaAdicional();
    }
    return total;
  }

  public int getId() {
    return id;
  }

  public LocalDate getPrazo() {
    return prazo;
  }

  public LocalTime getHorario() {
    return horario;
  }

  public int getPrioridade() {
    return prioridade;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public List<Servicos> getServicos() {
    return servicos;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return "Agendamento #" + id +
        " | Data: " + prazo + " às " + horario +
        " | Prioridade: " + prioridade +
        " | Cliente: " + (cliente != null ? cliente.getNome() : "N/A") +
        " | Total: R$" + String.format("%.2f", calcularTotal());

  }

}
