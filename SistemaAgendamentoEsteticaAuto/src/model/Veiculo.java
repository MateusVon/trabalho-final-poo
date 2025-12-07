package model;

public abstract class Veiculo {
    protected String marca;
    protected String modelo;
    protected String placa;

    public Veiculo(String marca, String modelo, String placa){
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
    }

    public abstract boolean pronto();

    public abstract double getTaxaAdicional();

    public String getMarca(){return this.marca;}
    public void setMarca(String marca){this.marca = marca;}

    public String getModelo() {return this.modelo;}
    public void setModelo(String modelo){this.modelo = modelo;}

    public String getPlaca(){return this.placa;}
    public void setPlaca(String placa){this.placa = placa;}

    @Override
    public String toString(){
        return "--- Veículo ---\nMarca: " + getMarca() + "\nModelo: " + getModelo() + "\nPlaca: " + getPlaca();
    }    
}
