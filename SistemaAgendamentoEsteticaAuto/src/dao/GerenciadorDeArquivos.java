package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }

        
    }

    // Método para ler todas as linhas do arquivo e devolver uma lista de String
    public static List<String> ler(String nomeArquivo){
        List<String> linhas = new ArrayList<>();
        File arquivo = new File(nomeArquivo);

        // Se o arquivo não existe, devolve lista vazia
        if(!arquivo.exists()){return linhas;}

        try{
            FileReader fr = new FileReader(arquivo);
            BufferedReader br = new BufferedReader(fr);
            
            String linha;

            // Enquanto tiver linha para ler (não for nulo)
            while((linha = br.readLine()) != null){
                linhas.add(linha);
            }
            br.close();
        }catch(IOException e){
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }

        return linhas;
    }

    // Método para sobrescrever/deletar 
    public static void sobrescrever(String nomeArquivo, List<String> novasLinhas){
        File arquivo = new File(nomeArquivo);

        try{
            FileWriter fw = new FileWriter(arquivo, false);
            BufferedWriter bw = new BufferedWriter(fw);

            for(String linha : novasLinhas){
                bw.write(linha);
                bw.newLine();
            }
            bw.close();
        }catch(IOException e){
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
