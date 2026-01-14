package model;

public abstract class Veiculo implements DefinicoesServicos {
  protected String marca;
  protected String modelo;
  protected String placa;
  protected boolean pronto;
  private int id;
  private String cpfProprietario;

  public Veiculo() {
  }

  public Veiculo(int id, String marca, String modelo, String placa) {
    this.id = id;
    this.marca = marca;
    this.modelo = modelo;
    this.placa = placa;
  }

  public Veiculo(String marca, String modelo, String placa) {
    this.marca = marca;
    this.modelo = modelo;
    this.placa = placa;
    this.pronto = false;
  }

  public abstract boolean getPronto();

  public abstract int calcularPrazoEstimado(Servicos servico);

  public abstract double calcularPrecoEspecifico();

  public abstract void setPronto();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getMarca() {
    return this.marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return this.modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getPlaca() {
    return this.placa;
  }

  public void setPlaca(String placa) {
    this.placa = placa;
  }

  public String getCpfProprietario() {
    return cpfProprietario;
  }

  public void setCpfProprietario(String cpfProprietario) {
    this.cpfProprietario = cpfProprietario;
  }

  @Override
  public String toString() {
    return getMarca() + " " + getModelo() + " (" + getPlaca() + ")";
  }
}
