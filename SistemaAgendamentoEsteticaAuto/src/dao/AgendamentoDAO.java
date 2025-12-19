package dao;

import model.Agendamento;
import java.util.List;
import java.util.ArrayList;

public class AgendamentoDAO {
  private static final String ARQUIVO = "agendamento.txt";

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
    GerenciadorDeArquivos.salvar(ARQUIVO, linha);

    return true;
  }

  public List<Agendamento> listar() {
    List<Agendamento> agendamentos = new ArrayList<>();
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);

    for(String linha : linhas){
      if(linha.trim().isEmpty()) continue;

      try{
        String[] dados = linha.split(";");

        int id = Integer.parseInt(dados[0]);
      }catch(Exception e){
        System.out.println("Erro");
      }


    }
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
