package controller;

import java.util.List;

import dao.ClienteDAO;
import dao.VeiculoDAO;
import exceptions.AutenticacaoException;
import model.Cliente;
import model.Sessao;
import model.Veiculo;

public class LoginController {
  private ClienteDAO clienteDAO;

  public LoginController() {
    this.clienteDAO = new ClienteDAO();
  }

  public void logar(String login, String senha) throws AutenticacaoException {
    List<Cliente> listaClientes = clienteDAO.listar();

    for (Cliente c : listaClientes) {
      if (c.getLogin().equals(login) && c.getSenha().equals(senha)) {
        try {
          VeiculoDAO veiculoDAO = new VeiculoDAO();
          // Só busca se o cliente tiver CPF válido
          if (c.getCpf() != null && !c.getCpf().isEmpty()) {
            Veiculo carroDoCliente = veiculoDAO.buscarPorCpfCliente(c.getCpf());

            if (carroDoCliente != null) {
              c.setVeiculo(carroDoCliente);
            }
          }
        } catch (Exception e) {
          System.err.println("Aviso: Não foi possível carregar o veículo. O login continuará normal.");
        }
        Sessao.setUsuarioLogado(c); // Inicia a sessão do usuario
        return;
      }
    }

    throw new AutenticacaoException("Usuário ou senha incorretos.");
  }

  /*
   * Teste
   * public static void main(String[] args) {
   * ClienteDAO dao = new ClienteDAO();
   * Cliente c = new Cliente("Rayssa", "31998563332", "14342756887", "rayssamgf",
   * "123456");
   * dao.salvar(c);
   * List<Cliente> lista = dao.listar();
   * for(Cliente cliente : lista){
   * System.out.println("Login: " + cliente.getLogin() + " Senha:" +
   * cliente.getSenha());
   * }
   * 
   * LoginController controller = new LoginController();
   * boolean sucesso = controller.autenticar("duduzinhoshow", "123456");
   * boolean falha = controller.autenticar("duduzinhoshow", "192929");
   * System.out.println(sucesso);
   * System.out.println(falha);
   * }
   */
}
