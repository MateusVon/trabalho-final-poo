package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;

import dao.AgendamentoDAO;
import model.*;
import model.enums.FormaPagamento;
import model.enums.StatusPagamento;
import model.enums.TiposDeServicos;

public class TelaAgendamento extends JFrame {
  private JComboBox<TiposDeServicos> cbServicos;
  private JComboBox<FormaPagamento> cbPagamento;
  private JFormattedTextField txtData;
  private JFormattedTextField txtHora;
  private JLabel lblPrecoEstimado;
  private JLabel lblVeiculoInfo;
  private JButton btnAgendar;
  private JButton btnVoltar;

  private Cliente clienteLogado;
  private Veiculo veiculoCliente;

  public TelaAgendamento() {
    super("Novo Agendamento");

    clienteLogado = Sessao.getUsuarioLogado();
    if (clienteLogado == null) {
      JOptionPane.showMessageDialog(null, "Erro de Sessão. Faça login novqamente.");
      dispose();
      return;
    }

    veiculoCliente = clienteLogado.getVeiculo();

    setSize(450, 520);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(null);
    setResizable(false);
    getContentPane().setBackground(Color.decode("#F5F5F5"));

    // Título

    JLabel lblTitulo = new JLabel("Agendar Servico");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    lblTitulo.setForeground(Color.decode("#2c3e50"));
    lblTitulo.setBounds(130, 20, 200, 30);
    add(lblTitulo);

    JLabel lblVeiculoTitulo = criarLabel("Veículo Selecionado:", 40, 60);
    add(lblVeiculoTitulo);

    String infoVeiculo = (veiculoCliente != null)
        ? veiculoCliente.getModelo() + " - " + veiculoCliente.getPlaca()
        : "Nenhum veículo cadastrado!";

    lblVeiculoInfo = new JLabel(infoVeiculo);
    lblVeiculoInfo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
    lblVeiculoInfo.setForeground(Color.BLUE);
    lblVeiculoInfo.setBounds(40, 85, 350, 25);
    add(lblVeiculoInfo);

    if (veiculoCliente == null) {
      JLabel lblAlerta = new JLabel("Cadastre um veículo antes de agendar!");
      lblAlerta.setForeground(Color.RED);
      lblAlerta.setBounds(40, 105, 300, 20);
      add(lblAlerta);
    }

    add(criarLabel("Selecione o serviço:", 40, 130));
    cbServicos = new JComboBox<>(TiposDeServicos.values());
    cbServicos.setBounds(40, 155, 350, 30);
    cbServicos.setBackground(Color.WHITE);

    cbServicos.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        atualizarPrecoEstimado();
      }
    });
    add(cbServicos);

    lblPrecoEstimado = new JLabel("Valor Estimado: R$ 0,00");
    lblPrecoEstimado.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblPrecoEstimado.setForeground(Color.decode("#27ae60"));
    lblPrecoEstimado.setBounds(40, 190, 300, 25);
    add(lblPrecoEstimado);

    add(criarLabel("Data:", 40, 220));
    try {
      MaskFormatter maskData = new MaskFormatter("##/##/####");
      maskData.setPlaceholderCharacter('_');
      txtData = new JFormattedTextField(maskData);
    } catch (ParseException e) {
      txtData = new JFormattedTextField();
    }
    txtData.setBounds(40, 245, 160, 30);
    add(txtData);

    add(criarLabel("Horário:", 230, 220));
    try {
      MaskFormatter maskHora = new MaskFormatter("##:##");
      maskHora.setPlaceholderCharacter('-');
      txtHora = new JFormattedTextField(maskHora);
    } catch (ParseException e) {
      txtHora = new JFormattedTextField();
    }
    txtHora.setBounds(230, 245, 160, 30);
    add(txtHora);

    add(criarLabel("Forma de Pagamento:", 40, 285));
    cbPagamento = new JComboBox<>(FormaPagamento.values());
    cbPagamento.setBounds(40, 310, 350, 30);
    cbPagamento.setBackground(Color.WHITE);
    add(cbPagamento);

    btnAgendar = new JButton("CONFIRMAR AGENDAMENTO");
    btnAgendar.setBounds(40, 360, 350, 45);
    btnAgendar.setBackground(Color.decode("#2c3e50"));
    btnAgendar.setForeground(Color.WHITE);
    btnAgendar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnAgendar.setFocusPainted(false);
    btnAgendar.setCursor(new Cursor(Cursor.HAND_CURSOR));

    if (veiculoCliente == null)
      btnAgendar.setEnabled(false);

    btnAgendar.addActionListener(e -> realizarAgendamento());
    add(btnAgendar);

    btnVoltar = new JButton("Cancelar");
    btnVoltar.setBounds(150, 420, 130, 30);
    btnVoltar.setBackground(Color.decode("#F5f5f5"));
    btnVoltar.setBorder(null);
    btnVoltar.setForeground(Color.GRAY);
    btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnVoltar.addActionListener(e -> dispose());
    add(btnVoltar);

    atualizarPrecoEstimado();
    setVisible(true);
  }

  private JLabel criarLabel(String texto, int x, int y) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lbl.setBounds(x, y, 200, 20);
    return lbl;
  }

  private void atualizarPrecoEstimado() {
    if (veiculoCliente == null)
      return;
    TiposDeServicos tipoSelecionado = (TiposDeServicos) cbServicos.getSelectedItem();
    double percentual = veiculoCliente.calcularPrecoEspecifico();
    Servicos servicoTemp = new Servicos(tipoSelecionado, percentual);
    lblPrecoEstimado.setText(String.format("Valor Estimado: R$ %.2f", servicoTemp.getPreco()));
  }

  private void realizarAgendamento() {
    String dataString = txtData.getText();
    String horaString = txtHora.getText();

    if (dataString.contains("_") || horaString.contains("_")) {
      JOptionPane.showMessageDialog(this, "Preencha a Data e a Hora corretamente!");
      return;
    }
    try {

      // Conversão de Hora
      LocalTime horario = LocalTime.parse(horaString, DateTimeFormatter.ofPattern("HH:mm"));

      // Preparação dos Objetos
      TiposDeServicos tipoSelecionado = (TiposDeServicos) cbServicos.getSelectedItem();
      FormaPagamento formaPag = (FormaPagamento) cbPagamento.getSelectedItem();

      // Calcula o preço final
      double percentual = veiculoCliente.calcularPrecoEspecifico();
      Servicos servicoFinal = new Servicos(tipoSelecionado, percentual);

      // Cria Pagamento
      Pagamento pagamento = new Pagamento(servicoFinal.getPreco(), formaPag, StatusPagamento.PENDENTE);

      // Cria Agendamento
      Agendamento novoAgendamento = new Agendamento(clienteLogado, veiculoCliente, servicoFinal, horario);

      // Adiciona o serviço na lista do agendamento
      novoAgendamento.adicionarServico(servicoFinal);

      // Define a data de entrega que vem do input
      novoAgendamento.setDataEntrega(dataString);

      // Vincula Agendamento
      novoAgendamento.setPagamento(pagamento);

      // Salva no DAO(Arquivo)
      AgendamentoDAO dao = new AgendamentoDAO();

      dao.salvar(novoAgendamento);

      JOptionPane.showMessageDialog(this, "Agendamento realizado com Sucesso!\nPrazo estimado: "
          + veiculoCliente.calcularPrazoEstimado(servicoFinal) + " dias.");
      dispose();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erro ao agendar: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
