package model;

public class VeiculosTransporte extends Veiculo implements DefinicoesServicos{
    private boolean cabine;
    private double peso;

    public VeiculosTransporte(String marca, String modelo, String placa, double valor, boolean possuiCarreta) {
        super(marca, modelo, placa);
        this.setPeso(valor);
        this.cabine = possuiCarreta;
    }

    public void setPeso(double valor){
        if(valor<=0){
            System.out.println("Erro! Informe valores de massa maiores que 0.");
            return;
        }
        this.peso = valor;
    }

    @Override
    public double getTaxaAdicional() {
    
        if(this.peso>12000.00){
            return 0.0020 * peso;
        }
        return 0.0;
        
    }

    @Override
    public boolean getPronto() {
        return this.pronto;
    }

    public void setPronto(){
        this.pronto = true;
    }

    @Override
    public double lavagemCabine() {
        if(this.cabine == true){
            return 30.00;
        }
        return 0.0;
    }

    @Override
    public double polimentoTecnicoCabine() {
        return 50.00;
    }

    @Override
    public double limpezaDaQuintaRoda() {
        return 25.00;
    }



}
