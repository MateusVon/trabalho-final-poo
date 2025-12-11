package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Classe responsável por toda a manipulação de arquivos de texto que serão usados para fazer a persistência de dados do trabalho
public class GerenciadorDeArquivos {

  // Método salvar (CREATE) recebe o nome do arquivo (ex: cliente.txt) e adiciona
  // uma nova linha ao final do arquivo
  public static void salvar(String nomeArquivo, String conteudo) {
    File arquivo = new File(nomeArquivo); // Utilizando File para criar a representção do arquivo

    try {
      FileWriter fw = new FileWriter(arquivo, true); // O parâmetro true ativa o modo APPEND do FileWriter, faz com que
                                                     // não apague os dados
      BufferedWriter bw = new BufferedWriter(fw); // O Buffer acumula dados na memória para escrever tudo de uma vez no
                                                  // disco (melhor performance)
      bw.write(conteudo); // Escreve o conteudo
      bw.newLine(); // Quebra de linha para listar os dados separados em cada linha
      bw.flush(); // Garante que não sobrou nada no buffer
      bw.close(); // Fecha o arquivo e libera o recurso

      System.out.println("Conteúdo inserido com sucesso!");
    } catch (IOException e) {
      // Tratamento de erro caso o disco esteja cheio ou sem permissão
      System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
    }

  }

  // Método ler (READ) lê todas as linhas do arquivo e devolve uma lista de String
  public static List<String> ler(String nomeArquivo) {
    List<String> linhas = new ArrayList<>();
    File arquivo = new File(nomeArquivo);

    if (!arquivo.exists()) {
      return linhas;
    } // Verificação para caso seja a primeira execução e o arquivo não exista, não
      // quebra o programa

    try {
      FileReader fr = new FileReader(arquivo); // FileReader abre o arquivo para leitura
      BufferedReader br = new BufferedReader(fr); // Criando Buffer para leitura

      String linha; // String que vai guardar temporariamente a entrada

      // Enquanto tiver linha para ler (não for nulo)
      while ((linha = br.readLine()) != null) { // br.readLine() pega a proxima linha, se for diferente de nulo ainda
                                                // tem texto
        linhas.add(linha); // Adiciona a linha lida a lista
      }
      br.close(); // Fecha o leitor
    } catch (IOException e) {
      System.err.println("Erro ao ler o arquivo: " + e.getMessage());
    }

    return linhas;
  }

  // Método sobrescrever (UPDATE/DELETE)
  /*
   * Lógica: Como não é possível deletar uma linha específica no arquivo .txt,
   * passamos a lista atualizada (sem o item removido) e gravamos tudo por cima do
   * arquivo antigo
   */
  public static void sobrescrever(String nomeArquivo, List<String> novasLinhas) {
    File arquivo = new File(nomeArquivo);

    try {
      // O parâmetro false ativa o OVERWRITE, isso deleta o conteúdo antigo e prepara
      // para escrever do zero
      FileWriter fw = new FileWriter(arquivo, false);
      BufferedWriter bw = new BufferedWriter(fw);

      for (String linha : novasLinhas) { // Percorre a lista que está na memória e grava linha por linha
        bw.write(linha);
        bw.newLine();
      }
      bw.close();
    } catch (IOException e) {
      System.err.println("Erro ao sobrescrever o arquivo: " + e.getMessage());
    }

  }

  public static void main(String[] args) {
    String arquivo = "teste.txt";

    // 1. Testando Salvar (Adiciona 3 pessoas)
    System.out.println("--- Teste 1: Salvando ---");
    GerenciadorDeArquivos.salvar(arquivo, "ID:1;Nome:Eduardo");
    GerenciadorDeArquivos.salvar(arquivo, "ID:2;Nome:Mateus");
    GerenciadorDeArquivos.salvar(arquivo, "ID:3;Nome:Samuel");

    List<String> lista = GerenciadorDeArquivos.ler(arquivo);
    System.out.println("Lista Atual: " + lista);

    System.out.println("\n--- Teste 2: Deletando Mateus ---");

    List<String> novaLista = new ArrayList<>();
    for (String linha : lista) {
      if (!linha.contains("Mateus")) { // Só mantém quem NÃO é o Mateus
        novaLista.add(linha);
      }
    }

    GerenciadorDeArquivos.sobrescrever(arquivo, novaLista);

    // Confere se sumiu
    System.out.println("Lista Após Deletar: " + GerenciadorDeArquivos.ler(arquivo));
  }
}
