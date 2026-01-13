package dao;

import model.Agendamento;
import model.Cliente;
import model.Pagamento;
import model.Servicos;
import model.Veiculo;
import model.enums.TiposDeServicos;


import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class AgendamentoDAO {
  private static final String ARQUIVO = "agendamento.txt";

  private ClienteDAO clienteDAO = new ClienteDAO();
  private VeiculoDAO veiculoDAO = new VeiculoDAO();
  private PagamentoDAO pagamentoDAO = new PagamentoDAO();
  private DateTimeFormatter formatador = DateTimeFormatter.ofPattern("d-M-yyyy");

  public boolean salvar(Agendamento agendamento) {
    if (agendamento.getCliente() == null || agendamento.getVeiculo() == null) {
      System.out.println("Erro: Agendamento precisa de um Cliente e Veículo.");
      return false;
    }

    if(agendamento.getId() == 0){
      agendamento.setId(gerarProximoId());
    }

    // 2. Tratamento para evitar NullPointerException no Pagamento
    int idPagamento = (agendamento.getPagamento() != null) ? agendamento.getPagamento().getId() : -1;

    StringBuilder sbServicos = new StringBuilder();
    if (agendamento.getServicos() != null) {
      for (Servicos s : agendamento.getServicos()) {
        // Salva o NOME do Enum
        sbServicos.append(s.getTipos().name()).append(",");
      }
    }

    String linha = agendamento.getId() + ";" +
        agendamento.getCliente().getId() + ";" +
        agendamento.getVeiculo().getId() + ";" +
        sbServicos.toString() + ";" +
        agendamento.getDataEntregaCliente().format(formatador) + ";" + // AQUI MUDOU
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

        // Parse dos dados básicos
        int idAgenda = Integer.parseInt(dados[0]);
        int idCliente = Integer.parseInt(dados[1]);
        int idVeiculo = Integer.parseInt(dados[2]);
        String strServicos = dados[3]; // "LAVAGEM,POLIMENTO"
        LocalDate dataEntrega = LocalDate.parse(dados[4], formatador);
        LocalTime hora = LocalTime.parse(dados[5]);
        int prioridade = Integer.parseInt(dados[6]);
        int idPagamento = Integer.parseInt(dados[7]);

        // Busca os objetos completos (Hydration)
        Cliente cliente = clienteDAO.buscarPorId(idCliente);
        Veiculo veiculo = veiculoDAO.buscarPorId(idVeiculo);

        // Reconstrói a lista de serviços
        List<Servicos> listaServicos = new ArrayList<>();
        if (!strServicos.isEmpty()) {
          String[] nomesEnums = strServicos.split(",");
          for (String nome : nomesEnums) {
            try {
              // Converte String do arquivo de volta para Enum
              TiposDeServicos tipo = TiposDeServicos.valueOf(nome);
              // Cria o objeto serviço (Assumindo peso 1.0 ou pegando do veiculo)
              listaServicos.add(new Servicos(tipo, 1.0));
            } catch (Exception e) {
              // Ignora serviço se o nome estiver errado no arquivo
            }
          }
        }

        if (cliente != null && veiculo != null) {
          // Usa o construtor completo que criamos anteriormente
          Agendamento ag = new Agendamento(idAgenda, cliente, veiculo, listaServicos, dataEntrega, hora, prioridade);

          if (idPagamento != -1) {
            Pagamento pag = pagamentoDAO.buscarPorId(idPagamento);
            ag.setPagamento(pag);
          }
          listaAgendamentos.add(ag);
        }
      } catch (Exception e) {
        System.out.println("Erro ao processar linha do agendamento: " + linha);
        e.printStackTrace(); // Ajuda a achar erros
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
          // --- CORREÇÃO CRÍTICA ---
          // A ordem aqui PRECISA ser igualzinha ao método salvar()

          int idPagamento = (agendamentoEditado.getPagamento() != null) ? agendamentoEditado.getPagamento().getId()
              : -1;

          StringBuilder sbServicos = new StringBuilder();
          if (agendamentoEditado.getServicos() != null) {
            for (Servicos s : agendamentoEditado.getServicos()) {
              sbServicos.append(s.getTipos().name()).append(",");
            }
          }

          String linhaAtualizada = agendamentoEditado.getId() + ";" +
              agendamentoEditado.getCliente().getId() + ";" +
              agendamentoEditado.getVeiculo().getId() + ";" +
              sbServicos.toString() + ";" +
              agendamentoEditado.getDataEntregaCliente().format(formatador) + ";" + // AQUI MUDOU
              agendamentoEditado.getHorario() + ";" +
              agendamentoEditado.getPrioridade() + ";" +
              idPagamento;

          novasLinhas.add(linhaAtualizada);
          achou = true;
        } else {
          novasLinhas.add(linha);
        }
      } catch (Exception e) {
        novasLinhas.add(linha); // Mantém a linha original se der erro
      }
    }

    if (achou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    }
    return false;
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
        achou = true; // Apenas não adiciona na nova lista (apaga)
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
