package model;

public class Cliente {
  private String nome;
  private String telefone;
  private String cpf;
  private Veiculo veiculo;

  // private Agendamento horarioMarcado;

  public Cliente() {
  }

  public Cliente(String n, String t, String c, Veiculo v) {
    this.nome = n;

    if (verificarTelefone(t)) {
      this.telefone = t;
    }

    if (verificaCPF(c) == true) {
      this.cpf = c;
    }

    this.veiculo = v;
  }

  // Setters
  public void setNome(String n) {
    this.nome = n;
  }

  public void setTelefone(String t) {
    this.telefone = t;
  }

  public void setCpf(String c) {
    if (verificaCPF(c)) {
      this.cpf = c;
    } else {
      System.out.println("CPF INVALIDO");
    }

  }

  public void setVeiculo(Veiculo v) {
    this.veiculo = v;
  }

  // Getters

  public String getNome() {
    return this.nome;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public String getCpf() {
    return this.cpf;
  }

  public Veiculo getVeiculo() {
    return this.veiculo;
  }

  public boolean verificaCPF(String c1) {
    if (c1 == null) {
      return false;
    }

    String cpfLIMPO = c1.replaceAll("[^0-9]", "");

    if (cpfLIMPO.length() != 11) {

      return false;
    }

    return true;

  }

  public boolean verificarTelefone(String t) {
    if (t == null) {
      return false;
    }

    String telefoneLIMPO = t.replaceAll("[^0-9]", "");

    if (telefoneLIMPO.length() != 9) {
      return false;
    }

    return true;
  }

  public String toString() {
    return "Nome: " + this.nome +
        "\nCPF: " + this.cpf +
        "\nTelefone: " + this.telefone +
        "\nVeiculo: " + this.veiculo;
  }

  public static void main(String[] args) {

    Veiculo chev = new Carro("CHevrolet", "Chevette", "GWO-5557", 2);
    Cliente c1 = new Cliente("Jorge", "99877-6704", "149.899.356-75", chev);
    System.out.println(c1.toString());

  }

}
