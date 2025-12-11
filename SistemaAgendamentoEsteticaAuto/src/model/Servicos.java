package model;

public class Servicos {
  private TiposDeServicos tipos;
  private double preco;
  private String descricao;

  Servicos(TiposDeServicos servico) {
    this.setServico(servico);
  }

  public void setServico(TiposDeServicos servico) {
    switch (servico) {
      case LAVAGEM_SIMPLES:
        this.preco = 50.00;
        this.descricao = "Limpeza superficial externa / Aspiração Interna / Aplicação de Pretinho nos pneus.";
        break;
      case LAVAGEM_DETALHADA:
        this.preco = 90.00;
        this.descricao = "Limpeza profunda externa e interna / Aplicação de técnicas e produtos para restauração do visual / Revitalização de plásticos, estofados e remoção de odores.";
        break;
      case POLIMENTO_TECNICO:
        this.preco = 700.00;
        this.descricao = "Processo avançado de restauração da pintura que vai além do brilho superficial, corrigindo defeitos como riscos, manchas de chuva ácida e oxidação através de múltiplas etapas (lavagem, descontaminação, corte, refino, lustro) ";
        break;
      case PPF:
        this.preco = 1900.00;
        this.descricao = "Película transparente de poliuretano termoplástico de alta tecnologia, aplicada sobre a pintura do carro para protegê-la de danos na superfície do veículo";
        break;
      case INSULFILM:
        this.preco = 300.00;
        this.descricao = "Película adesiva de poliéster aplicada nos vidros do carro, que oferece privacidade, conforto térmico (bloqueando calor e raios UV/infravermelhos), redução de ofuscamento e segurança ao dificultar o estilhaçamento do vidro";
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

  public static void main(String[] args) {
    Servicos s1 = new Servicos(TiposDeServicos.LAVAGEM_DETALHADA);

    System.out.println(s1);
  }

}