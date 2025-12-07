package model;

public class Moto extends Veiculo{
    private int cilindradas;
    
    public Moto(String marca, String modelo, String placa, int cilindradas){
        super(marca, modelo, placa);
        this.cilindradas = cilindradas;
    }

    public Moto(String marca, String modelo, String placa){
        super(marca, modelo, placa);
        this.cilindradas = 160;
    }

    public int getCilindradas(){return this.cilindradas;}
    public void setCilindradas(int cilindradas){this.cilindradas = cilindradas;}

    @Override
    public String toString(){
        return super.toString() + " (Moto " + getCilindradas() + "cc)";
    }

    public boolean pronto(){
        return true;
    }

    // Metodo para calcular a taxa adicional de acordo com o tamanho da moto
    public double getTaxaAdicional(){
        if(this.getCilindradas() > 1000){
            return 15.00;
        }
        return 0.0;
    }
}
