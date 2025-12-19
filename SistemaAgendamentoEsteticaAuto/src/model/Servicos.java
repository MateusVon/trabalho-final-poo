package model;

import model.enums.TiposDeServicos;

public class Servicos {
  private TiposDeServicos tipos;
  private double preco;
  private String descricao;

  Servicos(TiposDeServicos servico, double percentualPorVeiculo) {
    this.setServico(servico, percentualPorVeiculo);
  }

  public void setServico(TiposDeServicos servico, double percentual) {
    switch (servico) {
      case LAVAGEM_SIMPLES:
        this.preco = 50.00 * percentual;
        this.descricao = "Limpeza superficial externa / Aspiração Interna / Aplicação de Pretinho nos pneus.";
        break;
      case LAVAGEM_DETALHADA:
        this.preco = 90.00 * percentual;
        this.descricao = "Limpeza profunda externa e interna / Aplicação de técnicas e produtos para restauração do visual / Revitalização de plásticos, estofados e remoção de odores.";
        break;
      case POLIMENTO_TECNICO:
        this.preco = 700.00 * percentual;
        this.descricao = "Processo avançado de restauração da pintura que vai além do brilho superficial, corrigindo defeitos como riscos, manchas de chuva ácida e oxidação através de múltiplas etapas (lavagem, descontaminação, corte, refino, lustro) ";
        break;
      case PPF:
        this.preco = 1900.00 * percentual;
        this.descricao = "Película transparente de poliuretano termoplástico de alta tecnologia, aplicada sobre a pintura do carro para protegê-la de danos na superfície do veículo";
        break;
      case INSULFILM:
        this.preco = 300.00 * percentual;
        this.descricao = "Película adesiva de poliéster aplicada nos vidros do carro, que oferece privacidade, conforto térmico (bloqueando calor e raios UV/infravermelhos), redução de ofuscamento e segurança ao dificultar o estilhaçamento do vidro";
        break;
      case DESCONTAMINACAO_PINTURA:
        this.preco = 650.00 * percentual;
        this.descricao = "Uso de produtos especiais para remover partículas de ferro, piche e fuligem que ficam incrustadas na superfície e não saem na lavagem comum.";
        break;
      case RENOVACAO_ESTOFADOS:
        this.preco = 235.00 * percentual;
        this.descricao = "Conjunto de processos técnicos voltados para a conservação, restauração e proteção dos assentos e revestimentos internos do veículo.";
        break;
      case POLIMENTO_TECNICO_CARRETA:
        this.preco = 950 * percentual;
        this.descricao = "Polimento técnico atribuído na carreta do veículo de transporte.";
        break;
      case LIMPEZA_DA_QUINTA_RODA:
        this.preco = 190 * percentual;
        this.descricao = "Procedimento de manutenção essencial que envolve a remoção de graxa velha e detritos da base superior e do mecanismo de travamento, seguida de uma nova lubrificação.";
        break;
      case LAVAGEM_CARRETA:
        this.preco = 100 * percentual;
        this.descricao = "Lavagem interna e superficial da carreta do veículo de transporte.";
        break;
    }
  }

  public TiposDeServicos getTipos() {
    return tipos;
  }

  public double getPreco() {
    return preco;
  }

  @Override
  public String toString() {
    return "Descrição: " + this.descricao + "\nPreço: R$" + String.format("%.2f", this.preco);
  }

}