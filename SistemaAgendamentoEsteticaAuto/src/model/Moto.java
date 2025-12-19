package model;

import model.enums.TiposDeServicos;

public class Moto extends Veiculo implements DefinicoesServicos {
  private int cilindradas;
  private TiposDeServicos servicos;

  public Moto(String marca, String modelo, String placa, int cilindradas) {
    super(marca, modelo, placa);
    this.cilindradas = cilindradas;
  }

  public Moto(String marca, String modelo, String placa) {
    super(marca, modelo, placa);
    this.cilindradas = 160;
  }

  @Override
  public boolean getPronto() {
      return this.pronto;
  }

  public void setPronto(){
      this.pronto = true;
  }

  public int getCilindradas() {
    return this.cilindradas;
  }

  public void setCilindradas(int cilindradas) {
    this.cilindradas = cilindradas;
  }

  @Override
  public String toString() {
    return super.toString() + " (Moto " + getCilindradas() + "cc)";
  }


  @Override
  public double calcularPrecoEspecifico() {
      if(this.cilindradas<1000){
        return 0.75;
      }
      return 1;
  }

  @Override
  public int calcularPrazoEstimado() {
    switch (servicos) {
      case LAVAGEM_SIMPLES:
        return 1;
      case LAVAGEM_DETALHADA:
        return 1;
      case DESCONTAMINACAO_PINTURA:
        return 3;
      case PPF:
        return 2;
      case POLIMENTO_TECNICO:
        return 2;
      case RENOVACAO_ESTOFADOS:
        return 3;
      default:
        break;
    }
    return 0;
  }
}
