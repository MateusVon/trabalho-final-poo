package model;

import exceptions.ValidacaoException;

public class Cliente {
  private String nome;
  private String telefone;
  private String cpf;
  private Veiculo veiculo;
  private int id;
  private String login;
  private String senha;
  private Agendamento horarioMarcado;

  // Construtor padrão
  public Cliente() {
  }

  // Construtor para novo cadastro
  public Cliente(String nome, String cpf, String telefone, String login, String senha) throws ValidacaoException {
    this.nome = nome;
    setCpf(cpf);
    setTelefone(telefone);
    this.login = login;
    this.senha = senha;
  }

  // Construtor completo para o DAO
  public Cliente(int id, String nome, String cpf, String telefone, String login, String senha)
      throws ValidacaoException {
    this.id = id;
    this.nome = nome;
    setCpf(cpf);
    setTelefone(telefone);
    this.login = login;
    this.senha = senha;
  }

  // Getters e Setters

  public String getNome() {
    return this.nome;
  }

  public void setNome(String nome) {
    if (nome == null) {
      throw new IllegalArgumentException("O nome de usuário não pode ser vazio");
    }
    this.nome = nome;
  }

  public String getTelefone() {
    return this.telefone;
  }

  public void setTelefone(String telefone) throws ValidacaoException {
    try {
      validarTelefone(telefone);
      this.telefone = telefone;
    } catch (ValidacaoException e) {
      System.out.println("Erro! O espaço destinado para o número de telefone não foi preenchido.");
    }
  }

  public String getCpf() {
    return this.cpf;
  }

  public void setCpf(String cpf) throws ValidacaoException {
    try {
      validarCPF(cpf);
      this.cpf = cpf;
    } catch (ValidacaoException t) {
      System.out.println("Erro! O campo do CPF deve ser preenchido corretamente para efetuar o cadastro.");
    }
  }

  public Veiculo getVeiculo() {
    return this.veiculo;
  }

  public void setVeiculo(Veiculo veiculo) {
    this.veiculo = veiculo;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public Agendamento getHorarioMarcado() {
    return horarioMarcado;
  }

  public void setHorarioMarcado(Agendamento horarioMarcado) {
    this.horarioMarcado = horarioMarcado;
  }

  // METODOS para verificar CPF e TELEFONE
  private void validarCPF(String cpf) throws ValidacaoException {
    if (cpf == null) {
      throw new ValidacaoException("CPF não foi preenchido! Campo obrigatório.");
    }

    String cpfLimpo = cpf.replaceAll("[^0-9]", "");

    if (cpfLimpo.length() != 11) {
      throw new ValidacaoException("CPF inválido! Deve conter 11 dígitos.");
    }
  }

  private void validarTelefone(String telefone) throws ValidacaoException {
    if (telefone == null) {
      throw new ValidacaoException("Telefone não foi preenchido");
    }

    String telefoneLIMPO = telefone.replaceAll("[^0-9]", "");

    if (telefoneLIMPO.length() < 10 || telefoneLIMPO.length() > 11) {
      throw new ValidacaoException("Telefone inválido! Use DDD + Número (ex: 31999998888");
    }
  }

  @Override
  public String toString() {
    return "ID:" + id +
        "\nNome: " + this.nome +
        "\nCPF: " + this.cpf +
        "\nTelefone: " + this.telefone;
  }

}
