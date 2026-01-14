package view;

import dao.PagamentoDAO;
import model.Agendamento;
import model.Pagamento;
import model.enums.StatusPagamento;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import java.awt.*;

public class TelaSimulacaoPagamento extends JDialog {

  private Agendamento agendamento;
  private boolean pagou = false; // Para saber se o pagamento foi concluído

  public TelaSimulacaoPagamento(Frame parent, Agendamento ag) {
    super(parent, "Finalizar Pagamento", true); // True = Modal (bloqueia a tela de trás)
    this.agendamento = ag;

    setSize(400, 450);
    setLocationRelativeTo(parent);
    setLayout(new BorderLayout());
    setResizable(false);
    getContentPane().setBackground(Color.WHITE);

    // --- CABEÇALHO ---
    JPanel pnlHeader = new JPanel(new GridLayout(3, 1));
    pnlHeader.setBackground(Color.decode("#2c3e50"));
    pnlHeader.setBorder(new EmptyBorder(15, 15, 15, 15));

    JLabel lblTitulo = new JLabel("Confirmação de Pagamento");
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

    JLabel lblValor = new JLabel("Total: R$ " + String.format("%.2f", ag.calcularTotal()));
    lblValor.setForeground(Color.decode("#2ecc71")); // Verde
    lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblValor.setHorizontalAlignment(SwingConstants.CENTER);

    // Recupera o nome do pagamento (DINHEIRO, PIX, CARTAO)
    String formaPagamento = ag.getPagamento().getFormaPagamento().name();
    JLabel lblForma = new JLabel("Método: " + formaPagamento);
    lblForma.setForeground(Color.LIGHT_GRAY);
    lblForma.setHorizontalAlignment(SwingConstants.CENTER);

    pnlHeader.add(lblTitulo);
    pnlHeader.add(lblValor);
    pnlHeader.add(lblForma);
    add(pnlHeader, BorderLayout.NORTH);

    JPanel pnlCorpo = new JPanel();
    pnlCorpo.setLayout(new BoxLayout(pnlCorpo, BoxLayout.Y_AXIS));
    pnlCorpo.setBorder(new EmptyBorder(20, 20, 20, 20));
    pnlCorpo.setBackground(Color.WHITE);

    // Verifica qual painel montar
    if (formaPagamento.contains("PIX")) {
      montarTelaPix(pnlCorpo);
    } else if (formaPagamento.contains("CARTAO") || formaPagamento.contains("CREDITO")
        || formaPagamento.contains("DEBITO")) {
      montarTelaCartao(pnlCorpo);
    } else if (formaPagamento.contains("DINHEIRO")) {
      montarTelaDinheiro(pnlCorpo);
    } else {
      pnlCorpo.add(new JLabel("Método não reconhecido. Pague no balcão."));
    }

    add(pnlCorpo, BorderLayout.CENTER);

    JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.CENTER));
    pnlSul.setBackground(Color.WHITE);
    pnlSul.setBorder(new EmptyBorder(10, 0, 10, 0));

    JButton btnConfirmar = new JButton("Confirmar Pagamento");
    btnConfirmar.setBackground(Color.decode("#27ae60"));
    btnConfirmar.setForeground(Color.WHITE);
    btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnConfirmar.setPreferredSize(new Dimension(180, 40));

    btnConfirmar.addActionListener(e -> processarPagamento());

    JButton btnCancelar = new JButton("Cancelar");
    btnCancelar.setBackground(Color.decode("#e74c3c"));
    btnCancelar.setForeground(Color.WHITE);
    btnCancelar.addActionListener(e -> dispose());

    pnlSul.add(btnCancelar);
    pnlSul.add(btnConfirmar);
    add(pnlSul, BorderLayout.SOUTH);
  }

  // --- MÉTODOS AUXILIARES DE TELA ---

  private void montarTelaPix(JPanel panel) {
    JLabel lblInstrucao = new JLabel("Escaneie o QR Code ou copie a chave:");
    lblInstrucao.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField txtChave = new JTextField("00020126360014BR.GOV.BCB.PIX0114+553199999999");
    txtChave.setEditable(false);
    txtChave.setHorizontalAlignment(JTextField.CENTER);
    txtChave.setMaximumSize(new Dimension(300, 30));

    // Simulação visual de QR Code (Um painel cinza quadrado)
    JPanel pnlQrCode = new JPanel();
    pnlQrCode.setBackground(Color.LIGHT_GRAY);
    pnlQrCode.setMaximumSize(new Dimension(150, 150));
    pnlQrCode.setPreferredSize(new Dimension(150, 150));
    pnlQrCode.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    pnlQrCode.add(new JLabel("<html><br><br>[ QR CODE ]<br>SIMULADO</html>"));

    panel.add(lblInstrucao);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(pnlQrCode);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(txtChave);
  }

  private void montarTelaCartao(JPanel panel) {
    panel.add(new JLabel("Número do Cartão:"));
    try {
      JFormattedTextField txtNum = new JFormattedTextField(new MaskFormatter("#### #### #### ####"));
      txtNum.setMaximumSize(new Dimension(300, 30));
      panel.add(txtNum);
    } catch (Exception e) {
    }

    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(new JLabel("Nome no Cartão:"));
    JTextField txtNome = new JTextField();
    txtNome.setMaximumSize(new Dimension(300, 30));
    panel.add(txtNome);

    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    // Validade e CVV na mesma linha
    JPanel pnlLinha = new JPanel(new FlowLayout(FlowLayout.LEFT));
    pnlLinha.setBackground(Color.WHITE);

    try {
      pnlLinha.add(new JLabel("Validade:"));
      pnlLinha.add(new JFormattedTextField(new MaskFormatter("##/##")));

      pnlLinha.add(Box.createHorizontalStrut(15));

      pnlLinha.add(new JLabel("CVV:"));
      pnlLinha.add(new JFormattedTextField(new MaskFormatter("###")));
    } catch (Exception e) {
    }

    panel.add(pnlLinha);
  }

  private void montarTelaDinheiro(JPanel panel) {
    JLabel lbl = new JLabel("Pagamento em Espécie");
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel lblTroco = new JLabel("Precisa de troco para quanto?");
    lblTroco.setAlignmentX(Component.CENTER_ALIGNMENT);

    JTextField txtTroco = new JTextField();
    txtTroco.setMaximumSize(new Dimension(150, 30));

    JCheckBox chkTroco = new JCheckBox("Não preciso de troco");
    chkTroco.setBackground(Color.WHITE);
    chkTroco.setAlignmentX(Component.CENTER_ALIGNMENT);
    chkTroco.addActionListener(e -> txtTroco.setEnabled(!chkTroco.isSelected()));

    panel.add(lbl);
    panel.add(Box.createRigidArea(new Dimension(0, 20)));
    panel.add(lblTroco);
    panel.add(txtTroco);
    panel.add(chkTroco);
  }

  private void processarPagamento() {
    JOptionPane.showMessageDialog(this, "Conectando ao banco...", "Processando", JOptionPane.INFORMATION_MESSAGE);

    try {
      // Atualiza Status
      Pagamento pag = agendamento.getPagamento();
      if (pag != null) {
        pag.setStatus(StatusPagamento.PAGO); //

        // Salva no DAO
        PagamentoDAO dao = new PagamentoDAO();
        dao.atualizar(pag);

        JOptionPane.showMessageDialog(this, "Pagamento Aprovado com Sucesso!");
        this.pagou = true;
        dispose(); // Fecha a janela
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erro ao processar: " + e.getMessage());
    }
  }

  // Método para a tela anterior saber se deu certo
  public boolean isPagamentoRealizado() {
    return pagou;
  }
}