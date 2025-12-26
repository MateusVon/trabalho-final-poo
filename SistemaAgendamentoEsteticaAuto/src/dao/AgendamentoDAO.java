package dao;

import model.Agendamento;
import model.Cliente;
import model.Pagamento;
import model.Veiculo;

import java.util.List;
import java.time.LocalTime;
import java.util.ArrayList;

public class AgendamentoDAO {
  private static final String ARQUIVO = "agendamento.txt";

  private ClienteDAO clienteDAO = new ClienteDAO();
  private VeiculoDAO veiculoDAO = new VeiculoDAO();
  private PagamentoDAO pagamentoDAO = new PagamentoDAO();

  public boolean salvar(Agendamento agendamento) {
    if (agendamento.getCliente() == null || agendamento.getVeiculo() == null) {
      System.out.println("Erro: Agendamento precisa de um Cliente e Veículo.");
      return false;
    }

    int id = gerarProximoId();
    agendamento.setId(id);
    int idPagamento = agendamento.getPagamento().getId();

    String linha = agendamento.getId() + ";" +
        agendamento.getCliente()+ ";" +
        agendamento.getVeiculo() + ";" +
        agendamento.getServicos() + ";" +
        agendamento.getDataEntrega() + ";" +
        agendamento.getHorario() + ";" +
        agendamento.getPrioridade() + ";" +
        
        
        idPagamento;
    GerenciadorDeArquivos.salvar(ARQUIVO, linha);

    return true;
  }

  public List<Agendamento> listar() {
    List<Agendamento> listaAgendamentos = new ArrayList<>();
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";");

        int idAgenda = Integer.parseInt(dados[0]);
        int idCliente = Integer.parseInt(dados[1]);
        int idVeiculo = Integer.parseInt(dados[2]);
        String servico = (dados[3]);
        String data = (dados[4]);
        LocalTime hora = LocalTime.parse(dados[5]);
        int prioridade = Integer.parseInt(dados[6]);
        
        
        int idPagamento = Integer.parseInt(dados[7]);

        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        Veiculo veiculo = veiculoDAO.buscarPorId(idVeiculo);

        if (cliente != null && veiculo != null) {
          Agendamento ag = new Agendamento(idAgenda, cliente, veiculo, servico, data, hora, prioridade);

          if (idPagamento != -1) {
            Pagamento pag = pagamentoDAO.buscarPorId(idPagamento);
            ag.setPagamento(pag);
          }
          listaAgendamentos.add(ag);
        }
      } catch (Exception e) {
        System.out.println("Erro ao ler o arquivo.");
      }
    }
    return listaAgendamentos;
  }

  public boolean atualizar(Agendamento agendamentoEditado) {
    List<String> linhasExistentes = GerenciadorDeArquivos.ler(ARQUIVO);
    List<String> novasLinhas = new ArrayList<>();
    boolean achou = false;

    for (String linha : linhasExistentes) {
      if (linha.trim().isEmpty())
        continue;
      try {
        String[] dados = linha.split(";");
        int idAtual = Integer.parseInt(dados[0]);

        if (idAtual == agendamentoEditado.getId()) {

          int idPagamento = (agendamentoEditado.getPagamento() != null) ? agendamentoEditado.getPagamento().getId()
              : -1;

          String linhaAtualizada = agendamentoEditado.getId() + ";" +
              agendamentoEditado.getDataEntrega() + ";" +
              agendamentoEditado.getHorario() + ";" +
              agendamentoEditado.getCliente().getId() + ";" +
              agendamentoEditado.getVeiculo().getId() + ";" +
              idPagamento;
          novasLinhas.add(linhaAtualizada);
          achou = true;
        } else {
          novasLinhas.add(linha);
        }
      } catch (Exception e) {
        novasLinhas.add(linha);
      }
    }

    if (achou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    } else {
      return false;
    }
  }

  public boolean deletar(int id) {
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);
    List<String> novasLinhas = new ArrayList<>();
    boolean achou = false;

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;
      String[] dados = linha.split(";");
      int idAtual = Integer.parseInt(dados[0]);

      if (idAtual == id) {
        achou = true;
      } else {
        novasLinhas.add(linha);
      }
    }

    if (achou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    }
    return false;
  }

  public Agendamento buscaPorId(int id) {
    List<Agendamento> todos = listar();
    for (Agendamento a : todos) {
      if (a.getId() == id)
        return a;
    }
    return null;
  }

  private int gerarProximoId() {
    List<Agendamento> lista = listar();
    int maior = 0;
    for (Agendamento a : lista) {
      if (a.getId() > maior)
        maior = a.getId();
    }
    return maior + 1;
  }
}
