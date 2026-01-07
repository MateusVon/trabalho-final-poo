package model;

import model.enums.CategoriaCarro;

public class Carro extends Veiculo {
  private CategoriaCarro categoria;

  public Carro(String marca, String modelo, String placa, CategoriaCarro categoria) {
    super(marca, modelo, placa);
    this.categoria = categoria;
  }

  // public Carro(String marca, String modelo, String placa) {
  // super(marca, modelo, placa);
  // this.categoria = 1; // Categoria default é Hatch
  // }

  public CategoriaCarro getCategoria() {
    return this.categoria;
  }

  @Override
  public String toString() {
    return super.toString() + " (Carro)";
  }

  @Override
  public boolean getPronto() {
    return this.pronto;
  }

  public void setPronto() {
    this.pronto = true;
  }

  @Override
  public double calcularPrecoEspecifico() { // Responsável por modificar o preço padrão do serviço em relação à
                                            // categoria do carro do cliente.
    switch (this.categoria) {
      case HATCH:
        return 0.75;
      case SEDAN:
        return 0.85;
      case SUV:
        return 0.90;
      case CAMINHONETE:
        return 0.95;
      default:
        return 1.0;
    }

  }

  @Override
  public int calcularPrazoEstimado(Servicos servico) {
    if (servico == null || servico.getTipos() == null)
      return 0;

    switch (servico.getTipos()) {
      case INSULFILM:
        return 3;
      case LAVAGEM_SIMPLES:
        return 1;
      case LAVAGEM_DETALHADA:
        return 2;
      case POLIMENTO_TECNICO:
        return 3;
      case PPF:
        return 3;
      case RENOVACAO_ESTOFADOS:
        return 4;
      case DESCONTAMINACAO_PINTURA:
        return 6;
      default:
        break;
    }
    return 0;
  }

}
