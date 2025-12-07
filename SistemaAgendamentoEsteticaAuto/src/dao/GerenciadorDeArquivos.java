package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GerenciadorDeArquivos {
    
    // Método que recebe o nome do arquivo (ex: "clientes.txt") e o texto a ser salvo
    public static void salvar(String nomeArquivo, String conteudo){
        // Define o arquivo
        File arquivo = new File(nomeArquivo);

        try{
            FileWriter fw = new FileWriter(arquivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(conteudo);
            bw.newLine();
            bw.flush();
            bw.close();

            System.out.println("Conteúdo inserido!");
        }catch(IOException e){
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        
    }

    public static void main(String[] args) {
        GerenciadorDeArquivos.salvar("teste.txt", "Eduardo Henrique");
    }
}
