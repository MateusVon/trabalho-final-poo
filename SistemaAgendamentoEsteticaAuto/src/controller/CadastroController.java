package controller;

import dao.ClienteDAO;
import exceptions.ValidacaoException;

import java.util.*;
import model.Cliente;
public class CadastroController {
    private ClienteDAO clienteDAO;

    public CadastroController(){
        this.clienteDAO = new ClienteDAO();
    }

    public void cadastrarCliente(String nome, String cpf, String telefone, String login, String senha) throws ValidacaoException{
        // Validação dos campos:
        if(nome.isEmpty() || cpf.isEmpty() || telefone.isEmpty() || login.isEmpty() || senha.isEmpty()){
            throw new ValidacaoException("Por favor preencha todos os campos!");
        }

        // Validação de campo incompleto:
        if(cpf.contains("_") || telefone.contains("_")){
            throw new ValidacaoException("CPF ou Telefone incompletos.");
        }

        // Validação de usuário unico:
        if(verificarExistencia(login, cpf)){
            throw new ValidacaoException("Login já existente.");
        }

        try{
            Cliente cliente = new Cliente(nome, cpf, telefone, login, senha);
            clienteDAO.salvar(cliente);
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean verificarExistencia(String login, String cpf){
        List<Cliente> listaCliente = clienteDAO.listar();
        
        for(Cliente c : listaCliente){
            if(c.getLogin() != null && c.getLogin().equalsIgnoreCase(login) ){
                return true;
            }

            if(c.getCpf() != null && c.getCpf().equals(cpf)){
                return true;
            }
        }
        
        return false;
    }
}
