package model;

public class Cliente {
  private String nome;
  private String telefone;
  private String cpf;
  private Veiculo veiculo;
  private int id;
  private String login;
  private String senha;
  private Agendamento horarioMarcado;

  public Cliente(){}

  public Cliente(String login, String senha){
    this.login = login;
    this.senha = senha;
  }

  public Cliente(String nome, String telefone, String cpf, Veiculo veiculo, Agendamento horarioMarcado, String login, String senha) {
    this.nome = nome;
    setCpf(cpf);
    setTelefone(telefone);
    this.veiculo = veiculo;
    this.horarioMarcado = horarioMarcado;
    this.login = login;
    this.senha = senha;
  }

  public Cliente(int id, String nome, String telefone, String cpf, String login, String senha) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone; // 
        this.cpf = cpf;
        this.login = login;
        this.senha = senha;
    }

  // Getters e Setters

  public String getNome() {return this.nome;}
  public void setNome(String nome) {
    if(nome == null){
      throw new IllegalArgumentException("O nome de usuário não pode ser vazio");
    }
    this.nome = nome;
  }

  public String getTelefone() {return this.telefone;}
  public void setTelefone(String telefone) {
    validarTelefone(telefone);
    this.telefone = telefone;
  }

  public String getCpf() {return this.cpf;}
  public void setCpf(String cpf) {
      validarCPF(cpf);
      this.cpf = cpf;
  }
  public Veiculo getVeiculo() {return this.veiculo;}
  public void setVeiculo(Veiculo veiculo) {this.veiculo = veiculo;}

  public int getId() {return id;}
  public void setId(int id) {this.id = id;}

  public String getLogin() {return login;}
  public void setLogin(String login) {this.login = login;}

  public String getSenha() {return senha;}
  public void setSenha(String senha) {this.senha = senha;}

  public Agendamento getHorarioMarcado() {return horarioMarcado;}
  public void setHorarioMarcado(Agendamento horarioMarcado) {this.horarioMarcado = horarioMarcado;}

/* 
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
*/

// METODOS para verificar CPF e TELEFONE 

private void validarCPF(String cpf){
  if(cpf==null){throw  new IllegalArgumentException("CPF não foi preenchido! Campo obrigatório.");}

  String cpfLimpo = cpf.replaceAll("[^0-9]", "");

  if (cpfLimpo.length() != 11) {
    throw  new IllegalArgumentException("CPF inválido! Deve conter 11 dígitos.");
   }
}

  private void validarTelefone(String telefone){
    if (telefone == null) {throw  new IllegalArgumentException("Telefone não foi preenchido");}

    String telefoneLIMPO = telefone.replaceAll("[^0-9]", "");

    if (telefoneLIMPO.length() < 10 || telefoneLIMPO.length() > 11) {
      throw  new IllegalArgumentException("Telefone inválido! Use DDD + Número (ex: 31999998888");
    }
  }

  @Override
  public String toString() {
    return "ID:"+id+
           "\nNome: " + this.nome +
           "\nCPF: " + this.cpf +
           "\nTelefone: " + this.telefone ;
  }

  public static void main(String[] args) {
    /*
    Veiculo chev = new Carro("CHevrolet", "Chevette", "GWO-5557", 2);
    Cliente c1 = new Cliente("Jorge", "31 9 9877-6704", "149.899.356-75", chev);
    System.out.println(c1.toString());
    
    */
  }
}
