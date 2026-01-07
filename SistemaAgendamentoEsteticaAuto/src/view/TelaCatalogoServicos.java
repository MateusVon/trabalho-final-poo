package view;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import model.*;
import model.enums.*;

public class TelaCatalogoServicos extends JFrame {

  private JTable tabelaServicos;
  private DefaultTableModel modeloTabela;
  private JComboBox<String> boxVeiculos; // Caixa para que o usuário selecione o tipo de veículo.

  public TelaCatalogoServicos() {
    super("Catálogo de Serviços - EDS");
    setSize(700, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(null); // Centraliza a janela.
    setLayout(null); // Layout nulo para usar setBounds.
    getContentPane().setBackground(Color.decode("#34495e"));

    // Definindo o layout da tabela.
    JLabel lblTitulo = new JLabel("<<< TABELA DE PREÇOS E PRAZOS >>>");
    lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
    lblTitulo.setForeground(Color.YELLOW);
    lblTitulo.setBounds(160, 20, 400, 30);
    add(lblTitulo);

    // Box de Veículos para o usuário selecionar a categoria do seu veículo.
    String[] categoria = { "Hatch", "Sedan", "SUV", "Caminhonete", "Moto < 1000cc", "Moto > 1000cc",
        "Transporte > 12.000 KG", "Transporte < 12.000 KG" };
    boxVeiculos = new JComboBox<>(categoria);
    boxVeiculos.setBounds(240, 70, 200, 25);
    boxVeiculos.addActionListener(e -> atualizarDados()); // atualizarDados() é responsável por atualizar as
                                                          // informações da tabela em virtude da categoria do
                                                          // veículo
    add(boxVeiculos);

    // Definindo colunas e linhas da tabela.
    String[] colunas = { "Serviço", "Preço (R$)", "Prazo Estimado" };
    modeloTabela = new DefaultTableModel(colunas, 0) {
      @Override
      public boolean isCellEditable(int linha, int coluna) {
        return false; // Bloqueia a edição do conteúdo da tabela.
      }
    };
    tabelaServicos = new JTable(modeloTabela);
    tabelaServicos.setFillsViewportHeight(false); // Impede que a tabela estique as linhas vazias quando serviçso são
                                                  // ocultados em relação a categoria do veículo.
    JScrollPane scrollTabela = new JScrollPane(tabelaServicos); // Coloca a tabela de serviços em um espaço de rolagem
                                                                // do scroll do mouse do usuário.
    scrollTabela.setBorder(javax.swing.BorderFactory.createEmptyBorder()); // retira a borda da tabela onde ficariam as
                                                                           // linhas vazias.
    scrollTabela.setBounds(50, 110, 600, 180); // Define as medidas da tabela de serviços na janela.
    scrollTabela.getViewport().setBackground(Color.decode("#34495e")); // Coloca a cor azul escuro onde ficariam as
                                                                       // linhas vazias.
    add(scrollTabela); // Adiciona o painel de rolagem na interface.

    // Lógica de interação do click do mouse na tabela.
    tabelaServicos.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent m) {
        if (m.getClickCount() == 2) { // Verifica se o usuário clicou duas vezes no serviço para mostrar a sua
                                      // descrição.
          int linha = tabelaServicos.getSelectedRow(); // Linha contém o valor numérico da linha clicada pelo
                                                       // usuário.
          if (linha != -1) {
            String nomeTabelado = (String) tabelaServicos.getValueAt(linha, 0); // nomeTabelado contém o
                                                                                // nome do serviço que o
                                                                                // usuário clicou duas vezes
                                                                                // com o mouse.
            exibirDescricao(nomeTabelado); // Envia o nome do serviço que o usuário clicou
          }
        }
      }
    });

    // Mensagem de instrução
    JLabel lblDica = new JLabel("* Clique duas vezes em um serviço para ver detalhes.");
    lblDica.setForeground(Color.LIGHT_GRAY);
    lblDica.setFont(new Font("Arial", Font.ITALIC, 11));
    lblDica.setBounds(50, 415, 400, 20);
    add(lblDica);

    atualizarDados(); // Carga inicial
    setVisible(true);

  }

  private void exibirDescricao(String nomeServico) {
    try {
      String nomeEnum = nomeServico.replace(" ", "_");
      TiposDeServicos tipo = TiposDeServicos.valueOf(nomeEnum);

      Servicos s = new Servicos(tipo, 1.0);

      JOptionPane.showMessageDialog(this, "<html><body style='width: 300px; padding: 5px;'>" +
          s.getDescricao() + "</body></html>",
          "Descrição: " + nomeServico,
          JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
      System.out.println("Erro ao carregar a descrição do serviço. Tente novamente!");
    }

  }

  private void atualizarDados() {
    modeloTabela.setRowCount(0); // Limpa a tabela antes do preenchimento atualizado.
    // Criação de objetos temporários para atualizar dados da tabela (Preço e
    // Prazo);
    Veiculo v = obterInstanciaVeiculo();
    double percentual = v.calcularPrecoEspecifico(); // percentual recebe a porcentagem específica calculada no
                                                     // método chamado.

    for (TiposDeServicos tipo : TiposDeServicos.values()) { // " TiposDeServicos.values()" é um método implementado
                                                            // pelo Java automaticamente quando se cria um Enum.
                                                            // Esse método entrega um array contendo todos os
                                                            // elementos escritos dentro do Enum.
      if (tipo.isExclusivoTransporte() && !(v instanceof VeiculosTransporte)) {
        continue;
      }

      if (tipo == TiposDeServicos.INSULFILM && (v instanceof Moto)) {
        continue;
      }

      Servicos servico = new Servicos(tipo, percentual);
      int prazo = v.calcularPrazoEstimado(servico);

      modeloTabela.addRow(new Object[] {
          tipo.name().replace("_", " "), // replace troca o underline para um espaço vazio. Questão de
                                         // visibilidade e estética para o usuário.
          String.format("R$ %.2f", servico.getPreco()), // Armazena o preço do serviço na linha da tabela.
          prazo + " dia(s)" // Armazena o prazo do serviço na linha da tabela.
      });
    }

  }

  private boolean IsServicosDeVeiculosTransporte(TiposDeServicos tipo) {
    if (tipo == TiposDeServicos.LAVAGEM_CARRETA || tipo == TiposDeServicos.LIMPEZA_DA_QUINTA_RODA
        || tipo == TiposDeServicos.POLIMENTO_TECNICO_CARRETA) {
      return true;
    }
    return false;
  }

  private Veiculo obterInstanciaVeiculo() { // Método responsável por converter a String que contém a opção da
                                            // categoria selecionada em um objeto do tipo Veiculo.
    String selecionado = (String) boxVeiculos.getSelectedItem();

    if (selecionado.equals("Hatch")) {
      return new Carro("Genérico", "Genérica", "AAA-123", CategoriaCarro.HATCH);
    } else if (selecionado.equals("SUV")) {
      return new Carro("Genérico", "Genérica", "AAA-123", CategoriaCarro.SUV);
    } else if (selecionado.equals("Sedan")) {
      return new Carro("Genérico", "Genérica", "AAA-123", CategoriaCarro.SEDAN);
    } else if (selecionado.equals("Caminhonete")) {
      return new Carro("Genérico", "Genérica", "AAA-123", CategoriaCarro.CAMINHONETE);
    } else if (selecionado.equals("Transporte > 12.000 KG")) {
      return new VeiculosTransporte("Genérica", "Genérica", "AAA-123", 12400);
    } else if (selecionado.equals("Transporte < 12.000 KG")) {
      return new VeiculosTransporte("Genérica", "Genérica", "AAA-123", 9000);
    } else if (selecionado.equals("Moto < 1000cc")) {
      return new Moto("Genérico", "Genérica", "AAA-123", 600);
    } else {
      return new Moto("Genérico", "Genérica", "AAA-123", 1100);
    }
  }

  public static void main(String[] args) {
    new TelaCatalogoServicos();
  }

}