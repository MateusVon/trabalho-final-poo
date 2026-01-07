package view;

import dao.AgendamentoDAO;
import model.Agendamento;
import model.Cliente;
import model.Sessao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TelaMeusAgendamentos extends JFrame {
  private JTable tabela;
  private DefaultTableModel modeloTabela;
  public JButton btnVoltar;
  public JButton btnCanelar;
  public Cliente clienteLogado;

  public TelaMeusAgendamentos() {
    super("Meus Agendamentos");

    // Recuperar Sessão
    clienteLogado = Sessao.getUsuarioLogado();
    if (clienteLogado == null) {
      JOptionPane.showMessageDialog(null, "Você precisa estar logado para acessar seus agendamentos!");
      dispose();
      return;
    }

    setSize(600, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(null);
    setResizable(false);
    getContentPane().setBackground(Color.decode("#f5f5f5"));

    JLabel lblTitulo = new JLabel("Histórico de Agendamentos");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setForeground(Color.decode("#2c3e50"));
    lblTitulo.setBounds(20, 20, 300, 300);
    add(lblTitulo);

    // Configuração da Tabela

    String[] colunas = { "ID", "Data", "Hora", "Prioridade", "Valor (R$)" };
    modeloTabela = new DefaultTableModel(colunas, 0) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tabela = new JTable(modeloTabela);
    tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scrollPane = new JScrollPane(tabela);
    scrollPane.setBounds(20, 70, 545, 300);
    add(scrollPane);

    // Botão de cancelar agendamento
    btnCanelar = new JButton("Cancelar Agendamento Selecionado");
    btnCanelar.setBounds(20, 390, 180, 35);
    btnCanelar.setBackground(Color.decode("#e74c3c"));
    btnCanelar.setForeground(Color.WHITE);
    btnCanelar.addActionListener(e -> cancelarAgendamento());
    add(btnCanelar);

    btnVoltar = new JButton("Voltar");
    btnVoltar.setBounds(465, 390, 100, 35);
    btnVoltar.addActionListener(e -> dispose());
    add(btnVoltar);

    carregarTabela();
    setVisible(true);
  }

  private void carregarTabela() {
    modeloTabela.setRowCount(0);

    AgendamentoDAO dao = new AgendamentoDAO();
    List<Agendamento> lista = dao.listar();

    DateTimeFormatter fmData = DateTimeFormatter.ofPattern("dd/mm/yyyy");

    boolean encontrou = false;

    for (Agendamento a : lista) {
      if (a.getCliente() != null && a.getCliente().getCpf().equals(clienteLogado.getCpf())) {

        String dataExibicao = (a.getDataEntrega() != null) ? a.getDataEntrega() : "Sem data.";
        Object[] linha = {
            a.getId(),
            dataExibicao,
            a.getHorario(),
            (a.getPrioridade() == 1 ? "Alta" : "Normal"),
            String.format("%.2f", a.calcularTotal())
        };
        modeloTabela.addRow(linha);
        encontrou = true;
      }
    }
    if (!encontrou) {
      JOptionPane.showMessageDialog(this, "Nenhum agendamento encontrado.");
    }
  }

  private void cancelarAgendamento() {
    int linhaSelecionada = tabela.getSelectedRow();

    if (linhaSelecionada == -1) {
      JOptionPane.showMessageDialog(this, "Selecione um agendamento na tabela para cancelar.");
      return;
    }

    int idAgendamento = (int) tabela.getValueAt(linhaSelecionada, 0);

    int confirmacao = JOptionPane.showConfirmDialog(this,
        "Tem certesa que deseja cancelar o agendamento #" + idAgendamento + "?",
        "Confirmar Canelamento",
        JOptionPane.YES_NO_OPTION);

    if (confirmacao == JOptionPane.YES_NO_OPTION) {
      AgendamentoDAO dao = new AgendamentoDAO();
      if (dao.deletar(idAgendamento)) {
        JOptionPane.showMessageDialog(this, "Agendamento cancelado com sucesso!");
        carregarTabela();
      } else {
        JOptionPane.showMessageDialog(this, "Erro ao cancelar. Tente novamente.");
      }
    }
  }
}
