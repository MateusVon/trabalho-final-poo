package dao;

import model.Agendamento;
import java.util.List;
import java.util.ArrayList;

public class AgendamentoDAO {
  private String arquivo = "agendamento.txt";

  public boolean salvar(Agendamento agendamento) {
    if (agendamento.getCliente() == null) {
      System.out.println("Erro: Agendamento sem cliente.");
      return false;
    }

    String linha = agendamento.getId() + ";" +
        agendamento.getPrazo() + ";" +
        agendamento.getHorario() + ";" +
        agendamento.getPrioridade() + ";" +
        agendamento.getCliente().getCpf();
    GerenciadorDeArquivos.salvar(arquivo, linha);

    return true;
  }

  public List<Agendamento> listar() {
    List<Agendamento> agendamentos = new ArrayList<>();
    List<String> linhas = GerenciadorDeArquivos.ler(arquivo);
    return agendamentos;
  }

  public boolean atualizar(Agendamento object) {
    return false;
  }

  public boolean deletar(int id) {
    return false;
  }

  public Agendamento buscaPorId(int id) {
    return null;
  }
}
