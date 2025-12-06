package model;

public class Carro extends Veiculo{
    public Carro(String marca, String modelo, String placa, Cliente dono){
        super(marca, modelo, placa,dono);
    }

    public boolean pronto(){
        return true;
    }

    //varios codigos
}
