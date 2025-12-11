package dao;

import java.util.ArrayList;
import java.util.List;

import model.FormaPagamento;
import model.Pagamento;
import model.StatusPagamento;

public class PagamentoDAO implements IDAO<Pagamento> {

  private static final String ARQUIVO = "pagamentos.txt";

  public boolean salvar(Pagamento pagamento) {
    int novoId = gerarProximoId();
    pagamento.setId(novoId);
    String linha = pagamento.getId() + ";" +
        pagamento.getValor() + ";" +
        pagamento.getFormaPagamento() + ";" +
        pagamento.getStatus();
    GerenciadorDeArquivos.salvar(ARQUIVO, linha);
    return true;
  }

  public List<Pagamento> listar() {
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO); // Pede ao Gerenciador as linhas do arquivo
    List<Pagamento> listaDePagamentos = new ArrayList<>(); // Criando lista que vai ser retornada

    for (String linha : linhas) { // Loop para percorrer linha por linha para converter
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";"); // Quebra a linha nos pontos e vírgula

        int id = Integer.parseInt(dados[0]);
        double valor = Double.parseDouble(dados[1]); // Converte os textos para os tipos originais
        FormaPagamento forma = FormaPagamento.valueOf(dados[2]);
        StatusPagamento statusLido = StatusPagamento.valueOf(dados[3]);

        Pagamento p = new Pagamento(valor, forma, statusLido); // Recria o objeto
        p.setId(id);
        listaDePagamentos.add(p); // Adiciona na lista final

      } catch (Exception e) {
        System.err.println("Erro ao ler linha do pagamento: " + linha); // Avisar se a linha está corrompida sem travar
                                                                        // o sistema
      }
    }
    return listaDePagamentos;
  }

  public boolean atualizar(Pagamento pagamentoEditado) {
    List<String> linhasExistentes = GerenciadorDeArquivos.ler(ARQUIVO); // Ler tudo o que tem no arquivo

    List<String> novasLinhas = new ArrayList<>(); // Cria uma lista nova para guardar as alterações
    boolean achou = false;

    for (String linha : linhasExistentes) {
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";");
        int idAtual = Integer.parseInt(dados[0]);

        if (idAtual == pagamentoEditado.getId()) { // Verifica se é o pagamento que queremos alterar
          // Cria novas linhas com os dados editados
          String linhaAtualizada = pagamentoEditado.getId() + ";" +
              pagamentoEditado.getValor() + ";" +
              pagamentoEditado.getFormaPagamento() + ";" +
              pagamentoEditado.getStatus();
          novasLinhas.add(linhaAtualizada);
          achou = true; // Encontrou
        } else {
          novasLinhas.add(linha); // Se não encontrou mantenha as linhas como estava
        }
      } catch (Exception e) {
        novasLinhas.add(linha); // Se der erro na leitura, mantemos para não perder os dados
      }
    }

    if (achou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    } else {
      return false; // Nenhum pagamento com o id
    }
  }

  public boolean deletar(int id) {
    List<String> linhasExistentes = GerenciadorDeArquivos.ler(ARQUIVO);

    List<String> novasLinhas = new ArrayList<>();
    boolean apagou = false;

    for (String linha : linhasExistentes) {
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";");
        int idAtual = Integer.parseInt(dados[0]);

        if (idAtual == id) {
          apagou = true;
        } else {
          novasLinhas.add(linha);
        }
      } catch (Exception e) {
        novasLinhas.add(linha);
      }
    }
    if (apagou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    } else {
      return false;
    }
  }

  public Pagamento buscarPorId(int id) {
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";");
        int idAtual = Integer.parseInt(dados[0]);

        if (idAtual == id) {
          double valor = Double.parseDouble(dados[1]);
          FormaPagamento forma = FormaPagamento.valueOf(dados[2]);
          StatusPagamento status = StatusPagamento.valueOf(dados[3]);
          Pagamento p = new Pagamento(valor, forma, status);
          p.setId(idAtual);

          return p;
        }
      } catch (Exception e) {
        System.out.println("Erro nessa linha, ignorar e tentar a proxima");
      }
    }
    return null;
  }

  // Método para gerar ID automatico
  private int gerarProximoId() {
    List<Pagamento> lista = this.listar();
    int maiorId = 0;

    for (Pagamento p : lista) {
      if (p.getId() > maiorId) {
        maiorId = p.getId();
      }
    }

    return maiorId + 1;
  }

  public static void main(String[] args) {
    /*
     * PagamentoDAO dao = new PagamentoDAO();
     * Pagamento pagamento1 = new Pagamento(3234.00, FormaPagamento.DINHEIRO);
     * Pagamento pagamento2 = new Pagamento(2345.00, FormaPagamento.PIX);
     * 
     * System.out.println("--- 1. SALVANDO NO ARQUIVO ---");
     * dao.salvar(pagamento1);
     * dao.salvar(pagamento2);
     * 
     * System.out.println("Pagamento 1 salvo com ID: " + pagamento1.getId());
     * System.out.println("Pagamento 2 salvo com ID: " + pagamento2.getId());
     * 
     * System.out.println("\n--- 2. LISTANDO TUDO (ANTES DA MUDANÇA) ---");
     * List<Pagamento> lista = dao.listar();
     * for (Pagamento p : lista) {
     * System.out.println(p);
     * }
     * 
     * System.out.println("\n--- 3. TESTANDO ATUALIZAÇÃO ---");
     * System.out.println("Alterando pagamento " + pagamento1.getId() +
     * " para CANCELADO...");
     * 
     * pagamento1.setStatus(StatusPagamento.CANCELADO);
     * boolean atualizou = dao.atualizar(pagamento1);
     * 
     * if (atualizou) {
     * System.out.println("Sucesso! O arquivo foi atualizado.");
     * } else {
     * System.err.println("Erro: Não encontrei o ID para atualizar.");
     * }
     * 
     * System.out.println("\n--- 4. LISTANDO TUDO (DEPOIS DA MUDANÇA) ---");
     * List<Pagamento> listaNova = dao.listar();
     * for (Pagamento p : listaNova) {
     * System.out.println(p);
     * }
     * 
     * System.out.println("\n--- 5. TESTANDO DELETAR ---");
     * 
     * int idParaDeletar = pagamento2.getId();
     * 
     * System.out.println("Tentando deletar o ID: " + idParaDeletar);
     * boolean deletou = dao.deletar(idParaDeletar);
     * 
     * if (deletou) {
     * System.out.println("Sucesso! Pagamento removido.");
     * } else {
     * System.err.println("Erro: ID não encontrado.");
     * }
     * 
     * System.out.println("\n--- 6. LISTAGEM FINAL (O ID " + idParaDeletar +
     * " deve ter sumido) ---");
     * List<Pagamento> listaFinal = dao.listar();
     * for (Pagamento p : listaFinal) {
     * System.out.println(p);
     * }
     * 
     * Pagamento pEncontrado = dao.buscarPorId(2);
     * 
     * if (pEncontrado != null) {
     * System.out.println("Encontrado: " + pEncontrado);
     * } else {
     * System.out.println("Pagamento com ID " + pEncontrado.getId() +
     * " não existe.");
     * }
     */
  }

}
