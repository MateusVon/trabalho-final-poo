package dao;

import java.util.List;

import model.Pagamento;

public class pagamentoDAO implements IDAO<Pagamento>{

    private static final String ARQUIVO = "pagamentos.txt";

    public boolean salvar(Pagamento pagamento){

        return true;
    }

    public List<Pagamento> listar() {
        throw new UnsupportedOperationException("Unimplemented method 'listar'");
    }

    public boolean atualizar(Pagamento pagamento){
        return true;
    }


    public boolean deletar(int id){
        return true;
    }

    public Pagamento buscarPorId(int id){
        return null;
    }


    
}
