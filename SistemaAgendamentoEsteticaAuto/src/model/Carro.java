package model;

public class Carro extends Veiculo {
  private int categoria; // 1 - Hatch, 2 - Sedan, 3 - SUV, 4 - Camionete

  public Carro(String marca, String modelo, String placa, int categoria) {
    super(marca, modelo, placa);
    this.categoria = categoria;
  }

  public Carro(String marca, String modelo, String placa) {
    super(marca, modelo, placa);
    this.categoria = 1; // Categoria default é Hatch
  }

  public int getCategoria() {
    return this.categoria;
  }

  public void setCategoria(int categoria) {
    this.categoria = categoria;
  }

  @Override
  public String toString() {
    return super.toString() + " (Carro)";
  }

  public boolean pronto() {
    return true;
  }

  // Metodo que calcula a taxa adicional de acordo com o tamanho do carro
  public double getTaxaAdicional() {
    switch (this.categoria) {
      case 1:
        return 10.00;
      case 2:
        return 15.00;
      case 3:
        return 20.00;
      case 4:
        return 25.00;
      default:
        return 0.0;
    }
  }
}
