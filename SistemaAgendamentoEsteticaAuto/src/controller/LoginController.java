package controller;

import java.util.List;

import dao.ClienteDAO;
import model.Cliente;

public class LoginController {
    private ClienteDAO clienteDAO;

    public LoginController(){
        this.clienteDAO = new ClienteDAO();
    }

    public boolean autenticar(String login, String senha){
        List<Cliente> listaClientes = clienteDAO.listar();

        for(Cliente c : listaClientes){
            if(c.getLogin() != null && c.getLogin().equals(login)){
                if(c.getSenha() != null && c.getSenha().equals(senha)){
                    return true;    
                }
            }
        }
        return false;
    }


    /*
    Teste
    public static void main(String[] args) {
        ClienteDAO dao = new ClienteDAO();
        Cliente c = new Cliente("Rayssa", "31998563332", "14342756887", "rayssamgf", "123456");
        dao.salvar(c);
        List<Cliente> lista = dao.listar();
        for(Cliente cliente : lista){
            System.out.println("Login: " + cliente.getLogin() + " Senha:" + cliente.getSenha());
        }

        LoginController controller = new LoginController();
        boolean sucesso = controller.autenticar("duduzinhoshow", "123456");
        boolean falha = controller.autenticar("duduzinhoshow", "192929");
        System.out.println(sucesso);
        System.out.println(falha);
    }
    */
}
