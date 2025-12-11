package model;

public abstract class Veiculo {
    protected String marca;
    protected String modelo;
    protected String placa;
    protected int id;

    public Veiculo(){}

    public Veiculo(String marca, String modelo, String placa){
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
    }

    public abstract double getTaxaAdicional();

    public String getMarca(){return this.marca;}
    public void setMarca(String marca){this.marca = marca;}

    public String getModelo() {return this.modelo;}
    public void setModelo(String modelo){this.modelo = modelo;}

    public String getPlaca(){return this.placa;}
    public void setPlaca(String placa){this.placa = placa.toUpperCase();}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}    

    @Override
    public String toString(){
        return "--- Veículo ---\nID: "+ id + "\nMarca: " + getMarca() + "\nModelo: " + getModelo() + "\nPlaca: " + getPlaca();
    }

    
}
