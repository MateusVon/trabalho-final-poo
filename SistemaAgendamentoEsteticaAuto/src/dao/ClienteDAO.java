package dao;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;

public class ClienteDAO implements IDAO<Cliente>{
    private static final String ARQUIVO = "clientes.txt";

    public boolean salvar(Cliente cliente){
        int id = gerarProximoId();
        cliente.setId(id);
        String linha = cliente.getId() + ";" + 
                cliente.getNome() + ";" +
                cliente.getCpf() + ";" + 
                cliente.getTelefone() + ";";
        GerenciadorDeArquivos.salvar(ARQUIVO, linha);
        return true;
    }

  // Método para etorna todos os dados do arquivo
  public List<Cliente> listar(){
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);
    List<Cliente> listaCliente = new ArrayList<>();

    for(String linha : linhas){
        if(linha.trim().isEmpty()) continue;

        try{
            String [] dados = linha.split(";");

            int id = Integer.parseInt(dados[0]);
            String nome = new String(dados[1]);
            String cpf = new String(dados[2]);
            String telefone = new String(dados[3]);

            Cliente c = new Cliente(id, nome, cpf, telefone);
            listaCliente.add(c);
        }catch(Exception e){
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    return listaCliente;
  }

  // Método para atualizar um objeto existente
  public boolean atualizar(Cliente clienteEditado){
    List<String> linhasExistentes = GerenciadorDeArquivos.ler(ARQUIVO);
    List<String> novasLinhas = new ArrayList<>();
    boolean achou = false;

    for(String linha : linhasExistentes){
        if(linha.trim().isEmpty()) continue;

        try{
            String [] dados = linha.split(";");
            int idAtual = Integer.parseInt(dados[0]);

            if(idAtual == clienteEditado.getId()){
                String linhaAtualizada = clienteEditado.getId() + ";" + 
                clienteEditado.getNome() + ";" +
                clienteEditado.getCpf() + ";" + 
                clienteEditado.getTelefone() + ";";
                novasLinhas.add(linhaAtualizada);
                achou = true;
            }else{
                novasLinhas.add(linha);
            }
        }catch(Exception e){
            System.out.println("Erro ao atulizar.");
        }
    }

    if(achou == true){
        GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
        return true;
    }else{
        return false;
    }
  }

  // Método para remover um objeto pelo ID
  public boolean deletar(int id){
    List<String> linhasExistentes = GerenciadorDeArquivos.ler(ARQUIVO);

        List<String> novasLinhas = new ArrayList<>();
        boolean apagou = false;

        for (String linha : linhasExistentes) {
            if (linha.trim().isEmpty())
                continue;

            try {
                String[] dados = linha.split(";");
                int idAtual = Integer.parseInt(dados[0]);

                if (idAtual == id) {
                    apagou = true;
                } else {
                    novasLinhas.add(linha);
                }
            } catch (Exception e) {
                novasLinhas.add(linha);
            }
        }
        if (apagou) {
            GerenciadorDeArquivos.sobrescrever(ARQUIVO, novasLinhas);
            return true;
        } else {
            return false;
        }
  }

  // Método para buscar objeto específico pelo ID
  public Cliente buscarPorId(int idBusca){
    List<String> linhas = GerenciadorDeArquivos.ler(ARQUIVO);

        for (String linha : linhas) {
            if (linha.trim().isEmpty())
                continue;

            try {
                String[] dados = linha.split(";");
                int idAtual = Integer.parseInt(dados[0]);

                if (idAtual == idBusca) {
                int id = Integer.parseInt(dados[0]);
                String nome = new String(dados[1]);
                String cpf = new String(dados[2]);
                String telefone = new String(dados[3]);
                Cliente c = new Cliente(id, nome, cpf, telefone);
                    
                return c;
                }
            } catch (Exception e) {
                System.out.println("Erro nessa linha, ignorar e tentar a proxima");
            }
        }
        return null;
  }

  private int gerarProximoId() {
        List<Cliente> lista = this.listar();
        int maiorId = 0;

        for (Cliente c : lista) {
            if (c.getId() > maiorId) {
                maiorId = c.getId();
            }
        }

        return maiorId + 1;
    }
}
