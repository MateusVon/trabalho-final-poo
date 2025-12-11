package model;

public class Pagamento {
  private FormaPagamento formaPagamento;
  private StatusPagamento status;
  private double valor;
  private int id;

  public Pagamento(double valor, FormaPagamento formaPagamento, StatusPagamento status) {
    this.formaPagamento = formaPagamento;
    this.status = status;
    this.valor = valor;
  }

  public Pagamento(double valor, FormaPagamento formaPagamento) {
    this.valor = valor;
    this.formaPagamento = formaPagamento;
    this.status = status.PENDENTE;
  }

  public FormaPagamento getFormaPagamento() {
    return this.formaPagamento;
  }

  public void setFormaPagamento(FormaPagamento formaPagamento) {
    this.formaPagamento = formaPagamento;
  }

  public StatusPagamento getStatus() {
    return status;
  }

  public void setStatus(StatusPagamento status) {
    this.status = status;
  }

  public double getValor() {
    return this.valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void confirmarPagamento() {
    this.status = StatusPagamento.PAGO;
  }

  @Override
  public String toString() {
    return "--- Pagamento ---\n" + "ID do Pagamento: " + id + "\nValor: R$" + String.format("%.2f", valor)
        + "\nForma de Pagamento: " + formaPagamento + "\nStatus: " + status + "\n";
  }

  // Testes
  public static void main(String[] args) {
    Pagamento pag = new Pagamento(500, FormaPagamento.DINHEIRO, StatusPagamento.PENDENTE);
    System.out.println(pag.toString());
    pag.setStatus(StatusPagamento.PAGO);
    System.out.println(pag.toString());
    pag.setFormaPagamento(FormaPagamento.CREDITO);
    System.out.println(pag.toString());
  }
}
