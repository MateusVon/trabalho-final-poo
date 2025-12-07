package model;

public class Pagamento {
    private FormaPagamento formaPagamento; 
    private StatusPagamento status;
    private double valor;
    
    public Pagamento(double valor, FormaPagamento formaPagamento, StatusPagamento status){
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.valor = valor;
    }

    public Pagamento(double valor, FormaPagamento formaPagamento){
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.status = status.PENDENTE;
    }

    public FormaPagamento getFormaPagamento(){return this.formaPagamento;}
    public void setFormaPagamento(FormaPagamento formaPagamento){this.formaPagamento = formaPagamento;}

    public StatusPagamento getStatusPagamento(){return this.status;}
    public void setStatusPagamento(StatusPagamento status){this.status = status;}

    public double getValor(){return this.valor;}
    public void setValor(double valor){this.valor = valor;}

    public void confirmarPagamento(){
        this.status = StatusPagamento.PAGO;
    }

    @Override
    public String toString(){
        return "--- Pagamento ---\n" + "Valor: R$" + String.format("%.2f", valor) + "\nForma de Pagamento: " + formaPagamento + "\nStatus: " + status + "\n";
    }

    // Testes
    public static void main(String[] args) {
        Pagamento pag = new Pagamento(500, FormaPagamento.DINHEIRO, StatusPagamento.PENDENTE);
        System.out.println(pag.toString());
        pag.setStatusPagamento(StatusPagamento.PAGO);
        System.out.println(pag.toString());
        pag.setFormaPagamento(FormaPagamento.CREDITO);
        System.out.println(pag.toString());
    }
}
