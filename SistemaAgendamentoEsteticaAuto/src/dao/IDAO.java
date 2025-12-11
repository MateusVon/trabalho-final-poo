package dao;

import java.util.List;

// interface generica dao, IDAO, <T> = aceita qualquer classe (Carro, Cliente, Moto, etc)
public interface IDAO<T> {

  // Método para salvar um objeto novo no arquivo
  public boolean salvar(T objeto);

  // Método para etorna todos os dados do arquivo
  public List<T> listar();

  // Método para atualizar um objeto existente
  public boolean atualizar(T objeto);

  // Método para remover um objeto pelo ID
  public boolean deletar(int id);

  // Método para buscar objeto específico pelo ID
  public T buscarPorId(int id);

}
