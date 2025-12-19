package model;

import model.enums.TiposDeServicos;

public class VeiculosTransporte extends Veiculo implements DefinicoesServicos{
    private boolean carreta;
    private double peso;
    private TiposDeServicos servicos;

    public VeiculosTransporte(String marca, String modelo, String placa, double valor, boolean possuiCarreta) {
        super(marca, modelo, placa);
        this.setPeso(valor);
        this.carreta = possuiCarreta;
    }

    public void setPeso(double valor){
        if(valor<=0){
            System.out.println("Erro! Informe valores de massa maiores que 0.");
            return;
        }
        this.peso = valor;
    }

    public boolean temCarreta(){
        return carreta;
    }

    @Override
    public boolean getPronto() {
        return this.pronto;
    }

    public void setPronto(){
        this.pronto = true;
    }

    @Override
    public double calcularPrecoEspecifico() {
        if(peso>12000){
            return 1.2;
        }
        return 1;
    
    }

    @Override
    public int calcularPrazoEstimado() {
        switch (servicos) {
            case LAVAGEM_SIMPLES:
                return 2;          
            case LAVAGEM_DETALHADA:
                return 3;
            case POLIMENTO_TECNICO:
                return 5;
            case PPF:
                return 5;
            case RENOVACAO_ESTOFADOS:
                return 3;
            case DESCONTAMINACAO_PINTURA:
                return 8;
            case LIMPEZA_DA_QUINTA_RODA:
                return 1;
            case LAVAGEM_CARRETA:
                return 2;
            case POLIMENTO_TECNICO_CARRETA:
                return 2;
            default:
                break;    
        }
        return 0;
    }



}
