package view;

import dao.AgendamentoDAO;
import dao.PagamentoDAO;
import model.Agendamento;
import model.Cliente;
import model.Pagamento;
import model.Sessao;
import model.enums.StatusPagamento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TelaMeusAgendamentos extends JFrame {
  private JTable tabela;
  private DefaultTableModel modeloTabela;
  public JButton btnVoltar;
  public JButton btnCancelar;
  public Cliente clienteLogado;
  public JButton btnPagar;

  public TelaMeusAgendamentos() {
    super("Meus Agendamentos");

    // Recuperar Sessão
    clienteLogado = Sessao.getUsuarioLogado();
    if (clienteLogado == null) {
      JOptionPane.showMessageDialog(null, "Você precisa estar logado para acessar seus agendamentos!");
      dispose();
      return;
    }

    setSize(750, 500);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(null);
    setResizable(false);
    setLocationRelativeTo(null);
    getContentPane().setBackground(Color.decode("#f5f5f5"));

    JLabel lblTitulo = new JLabel("Histórico de Agendamentos");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
    lblTitulo.setForeground(Color.decode("#2c3e50"));
    lblTitulo.setBounds(20, 20, 300, 30);
    add(lblTitulo);

    // Configuração da Tabela

    String[] colunas = { "ID", "Data", "Hora", "Pagamento", "Valor (R$)" };
    modeloTabela = new DefaultTableModel(colunas, 0) {
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tabela = new JTable(modeloTabela);
    tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    tabela.getColumnModel().getColumn(0).setPreferredWidth(30); // ID pequeno
    tabela.getColumnModel().getColumn(1).setPreferredWidth(200); // Serviços grande

    JScrollPane scrollPane = new JScrollPane(tabela);
    scrollPane.setBounds(20, 70, 690, 300);
    add(scrollPane);

    // Botão de cancelar agendamento
    btnCancelar = new JButton("Cancelar Agendamento Selecionado");
    btnCancelar.setBounds(20, 390, 180, 35);
    btnCancelar.setBackground(Color.decode("#e74c3c"));
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.addActionListener(e -> cancelarAgendamento());
    add(btnCancelar);

    // 2. Botão Pagar (NOVO)
    btnPagar = new JButton("Pagar Agora ($)");
    btnPagar.setBounds(210, 390, 150, 35);
    btnPagar.setBackground(Color.decode("#27ae60")); // Verde Sucesso
    btnPagar.setForeground(Color.WHITE);
    btnPagar.addActionListener(e -> realizarPagamentoOnline());
    add(btnPagar);

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

    boolean encontrou = false;

    // Formatador da data
    DateTimeFormatter formatadorView = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    for (Agendamento a : lista) {
      // Verificação de segurança (CPF do cliente logado)
      if (a.getCliente() != null && a.getCliente().getCpf().equals(clienteLogado.getCpf())) {
        String dataExibicao;
        if (a.getDataEntregaCliente() != null) {
          // Se tem data, formata ela para String (ex: "25/01/2026")
          dataExibicao = a.getDataEntregaCliente().format(formatadorView);
        } else {
          // Se for null, escreve o texto padrão
          dataExibicao = "Sem data";
        }

        Object[] linha = {
            a.getId(),
            dataExibicao, // Essa variável é garantidamente uma String
            a.getHorario(),
            a.getPagamento().getStatus(),
            String.format("R$ %.2f", a.calcularTotal())
        };

        modeloTabela.addRow(linha);
        encontrou = true;
      }
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

  private void realizarPagamentoOnline() {
    int linhaSelecionada = tabela.getSelectedRow();

    if (linhaSelecionada == -1) {
      JOptionPane.showMessageDialog(this, "Selecione um agendamento para pagar.");
      return;
    }

    // Pega o ID da coluna 0 (Por isso a correção no carregarTabela foi importante)
    int idAgendamento = (int) tabela.getValueAt(linhaSelecionada, 0);

    // Busca o agendamento real no banco
    AgendamentoDAO dao = new AgendamentoDAO();
    Agendamento ag = dao.buscaPorId(idAgendamento); // <--- Atenção: método é buscaPorId ou buscarPorId no seu DAO?

    if (ag == null)
      return;

    // Verifica se já está pago
    // OBS: Ajuste "PAGO" conforme você escreveu no seu Enum ou String
    if (ag.getPagamento() != null && "PAGO".equalsIgnoreCase(String.valueOf(ag.getPagamento().getStatus()))) {
      JOptionPane.showMessageDialog(this, "Este agendamento já está pago! Obrigado.");
      return;
    }

    // Simulação de Pagamento
    int confirmacao = JOptionPane.showConfirmDialog(this,
        "Valor Total: R$ " + String.format("%.2f", ag.calcularTotal()) + "\nDeseja confirmar o pagamento via App?",
        "Pagamento Online", JOptionPane.YES_NO_OPTION);

    if (confirmacao == JOptionPane.YES_NO_OPTION) {
      try {
        // Atualiza o status do objeto Pagamento
        Pagamento pag = ag.getPagamento();
        if (pag != null) {
          pag.setStatus(StatusPagamento.PAGO);

          // Salva a alteração no banco de Pagamentos
          PagamentoDAO pagDAO = new PagamentoDAO();
          pagDAO.atualizar(pag); 

          JOptionPane.showMessageDialog(this, "Pagamento confirmado com sucesso!");

          // Recarrega a tabela para ficar verde/atualizado
          carregarTabela();
        } else {
          JOptionPane.showMessageDialog(this, "Erro: Não há registro financeiro para este agendamento.");
        }

      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Erro ao processar pagamento: " + e.getMessage());
      }
    }
  }
}
