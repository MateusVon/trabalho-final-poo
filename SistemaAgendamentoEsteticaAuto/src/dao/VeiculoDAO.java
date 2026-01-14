package dao;

import java.util.ArrayList;
import java.util.List;
import model.Carro;
import model.Moto;
import model.Veiculo;
import model.enums.CategoriaCarro;

public class VeiculoDAO implements IDAO<Veiculo> {

  private static final String ARQUIVO = "veiculos.txt";

  @Override
  public boolean salvar(Veiculo v) {
    if (v.getId() == 0) {
      int novoId = gerarProximoId();
      v.setId(novoId);
    }

    StringBuilder sb = new StringBuilder();
    sb.append(v.getId()).append(";");

    if (v instanceof Carro) {
      sb.append("CARRO").append(";");
      sb.append(v.getPlaca()).append(";");
      sb.append(v.getMarca()).append(";");
      sb.append(v.getModelo()).append(";");

      Carro c = (Carro) v;
      sb.append(c.getCategoria().name()).append(";");

    } else if (v instanceof Moto) {
      sb.append("MOTO").append(";");
      sb.append(v.getPlaca()).append(";");
      sb.append(v.getMarca()).append(";");
      sb.append(v.getModelo()).append(";");

      Moto m = (Moto) v;
      sb.append(m.getCilindradas()).append(";");
    }

    String cpfDono = (v.getCpfProprietario() != null) ? v.getCpfProprietario() : "SEM_DONO";
    sb.append(cpfDono);

    GerenciadorDeArquivos.salvar(ARQUIVO, sb.toString());
    return true;
  }

  @Override
  public List<Veiculo> listar() {
    List<Veiculo> lista = new ArrayList<>();
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;

      try {
        String[] dados = linha.split(";");

        int id = Integer.parseInt(dados[0]);
        String tipo = dados[1];
        String placa = dados[2];
        String marca = dados[3];
        String modelo = dados[4];
        String cpfDono = "";
        if (dados.length > 6) {
          cpfDono = dados[6];
        }

        Veiculo v = null;

        if (tipo.equals("CARRO")) {
          CategoriaCarro categoria = CategoriaCarro.valueOf(dados[5]);
          v = new Carro(marca, modelo, placa, categoria);

        } else if (tipo.equals("MOTO")) {
          int cilindradas = Integer.parseInt(dados[5]);
          v = new Moto(marca, modelo, placa, cilindradas);
        }

        if (v != null) {
          v.setId(id);
          v.setCpfProprietario(cpfDono);
          lista.add(v);
        }

      } catch (Exception e) {
        System.err.println("Erro ao ler veículo (Linha corrompida): " + linha);
      }
    }
    return lista;
  }

  public Veiculo buscarPorCpfCliente(String cpfCliente) {
    List<Veiculo> todos = listar();
    for (Veiculo v : todos) {
      if (v.getCpfProprietario() != null && v.getCpfProprietario().equals(cpfCliente)) {
        return v;
      }
    }
    return null;
  }

  @Override
  public boolean atualizar(Veiculo veiculoEditado) {
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);
    List<String> novasLinhas = new ArrayList<>();
    boolean achou = false;

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;

      String[] dados = linha.split(";");
      int idAtual = Integer.parseInt(dados[0]);

      if (idAtual == veiculoEditado.getId()) {
        StringBuilder sb = new StringBuilder();
        sb.append(veiculoEditado.getId()).append(";");

        if (veiculoEditado instanceof Carro) {
          sb.append("CARRO").append(";");
          Carro c = (Carro) veiculoEditado;
          sb.append(c.getPlaca()).append(";");
          sb.append(c.getMarca()).append(";");
          sb.append(c.getModelo()).append(";");
          sb.append(c.getCategoria().name()).append(";");
        } else if (veiculoEditado instanceof Moto) {
          sb.append("MOTO").append(";");
          Moto m = (Moto) veiculoEditado;
          sb.append(m.getPlaca()).append(";");
          sb.append(m.getMarca()).append(";");
          sb.append(m.getModelo()).append(";");
          sb.append(m.getCilindradas()).append(";");
        }

        String cpfDono = (veiculoEditado.getCpfProprietario() != null) ? veiculoEditado.getCpfProprietario()
            : "SEM_DONO";
        sb.append(cpfDono);

        novasLinhas.add(sb.toString());
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

  @Override
  public boolean deletar(int id) {
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);
    List<String> novasLinhas = new ArrayList<>();
    boolean apagou = false;

    for (String linha : linhas) {
      if (linha.trim().isEmpty())
        continue;
      String[] dados = linha.split(";");
      int idAtual = Integer.parseInt(dados[0]);

      if (idAtual == id) {
        apagou = true;
      } else {
        novasLinhas.add(linha);
      }
    }

    if (apagou) {
      GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
      return true;
    }
    return false;
  }

  @Override
  public Veiculo buscarPorId(int id) {
    List<Veiculo> todos = listar();
    for (Veiculo v : todos) {
      if (v.getId() == id) {
        return v;
      }
    }
    return null;
  }

  private int gerarProximoId() {
    List<Veiculo> lista = listar();
    int maior = 0;
    for (Veiculo v : lista) {
      if (v.getId() > maior)
        maior = v.getId();
    }
    return maior + 1;
  }

  public Veiculo buscarPorPlaca(String placa) {
    List<Veiculo> todos = listar();
    for (Veiculo v : todos) {
      if (v.getPlaca().equalsIgnoreCase(placa)) {
        return v;
      }
    }
    return null;
  }

  public List<Veiculo> listarPorCpf(String cpf) {
    List<Veiculo> todos = listar();
    List<Veiculo> meusVeiculos = new ArrayList<>();

    for (Veiculo v : todos) {
      // Verifica se o veículo tem dono e se o CPF bate
      if (v.getCpfProprietario() != null && v.getCpfProprietario().equals(cpf)) {
        meusVeiculos.add(v);
      }
    }
    return meusVeiculos;
  }
}

/*
 * Teste
 * public static void main(String[] args) {
 * Carro carro = new Carro("Volks", "Gol", "GKZ-1767", CategoriaCarro.HATCH);
 * Moto moto = new Moto("Honda", "Fan", "6HJ9-2030", 160);
 * VeiculoDAO veiculoDAO = new VeiculoDAO();
 * veiculoDAO.salvar(moto);
 * veiculoDAO.salvar(carro);
 * }
 */
