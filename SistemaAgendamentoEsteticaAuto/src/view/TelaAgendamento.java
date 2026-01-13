package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

import controller.AgendamentoController;
import exceptions.AgendamentoException;
import dao.VeiculoDAO;
import model.*;
import model.enums.FormaPagamento;
import model.enums.TiposDeServicos;

public class TelaAgendamento extends JFrame {
  
  private JComboBox<Veiculo> cbVeiculos; 
  private JComboBox<TiposDeServicos> cbServicos;
  private JComboBox<FormaPagamento> cbPagamento;
  private JFormattedTextField txtData;
  private JFormattedTextField txtHora;
  private JLabel lblPrecoEstimado;
  private JButton btnAgendar;
  private JButton btnVoltar;

  private Cliente clienteLogado;

  public TelaAgendamento() {
    super("Novo Agendamento");

    clienteLogado = Sessao.getUsuarioLogado();
    if (clienteLogado == null) {
      JOptionPane.showMessageDialog(null, "Erro de Sessão. Faça login novamente.");
      dispose();
      return;
    }

    setSize(450, 560); 
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(null);
    setResizable(false);
    getContentPane().setBackground(Color.decode("#F5F5F5"));

    // Título
    JLabel lblTitulo = new JLabel("Agendar Serviço");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
    lblTitulo.setForeground(Color.decode("#2c3e50"));
    lblTitulo.setBounds(130, 20, 200, 30);
    add(lblTitulo);

    // --- SELEÇÃO DE VEÍCULO ---
    add(criarLabel("Selecione o Veículo:", 40, 60));
    
    cbVeiculos = new JComboBox<>();
    cbVeiculos.setBounds(40, 85, 350, 30);
    cbVeiculos.setBackground(Color.WHITE);
    add(cbVeiculos);

    // Carregar os veículos do banco de dados
    carregarMeusVeiculos();

    // Quando trocar de carro, atualiza os serviços compatíveis e o preço
    cbVeiculos.addActionListener(e -> {
        atualizarServicosCompativeis();
        atualizarPrecoEstimado();
    });

    // --- SELEÇÃO DE SERVIÇO ---
    add(criarLabel("Selecione o Serviço:", 40, 130));
    cbServicos = new JComboBox<>();
    cbServicos.setBackground(Color.WHITE);
    cbServicos.setBounds(40, 155, 350, 30);
    cbServicos.addActionListener(e -> atualizarPrecoEstimado());
    add(cbServicos);

    // Preço
    lblPrecoEstimado = new JLabel("Valor Estimado: R$ 0,00");
    lblPrecoEstimado.setFont(new Font("Segoe UI", Font.BOLD, 14));
    lblPrecoEstimado.setForeground(Color.decode("#27ae60"));
    lblPrecoEstimado.setBounds(40, 190, 300, 25);
    add(lblPrecoEstimado);

    // Data e Hora
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
      maskHora.setPlaceholderCharacter('_'); 
      txtHora = new JFormattedTextField(maskHora);
    } catch (ParseException e) {
      txtHora = new JFormattedTextField();
    }
    txtHora.setBounds(230, 245, 160, 30);
    add(txtHora);

    // Pagamento
    add(criarLabel("Forma de Pagamento:", 40, 285));
    cbPagamento = new JComboBox<>(FormaPagamento.values());
    cbPagamento.setBounds(40, 310, 350, 30);
    cbPagamento.setBackground(Color.WHITE);
    add(cbPagamento);

    // Botões
    btnAgendar = new JButton("CONFIRMAR AGENDAMENTO");
    btnAgendar.setBounds(40, 370, 350, 45); 
    btnAgendar.setBackground(Color.decode("#2c3e50"));
    btnAgendar.setForeground(Color.WHITE);
    btnAgendar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnAgendar.setFocusPainted(false);
    btnAgendar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnAgendar.addActionListener(e -> realizarAgendamento());
    add(btnAgendar);

    btnVoltar = new JButton("Cancelar");
    btnVoltar.setBounds(150, 430, 130, 30); 
    btnVoltar.setBackground(Color.decode("#F5f5f5"));
    btnVoltar.setBorder(null);
    btnVoltar.setForeground(Color.GRAY);
    btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnVoltar.addActionListener(e -> dispose());
    add(btnVoltar);

    // Carrega os dados iniciais
    atualizarServicosCompativeis();
    atualizarPrecoEstimado();
    
    setVisible(true);
  }

  // --- MÉTODOS AUXILIARES ---

  private void carregarMeusVeiculos() {
      VeiculoDAO dao = new VeiculoDAO();
      List<Veiculo> lista = dao.listarPorCpf(clienteLogado.getCpf());
      
      cbVeiculos.removeAllItems();
      
      if (lista.isEmpty()) {
          JOptionPane.showMessageDialog(this, "Você não tem veículos cadastrados!\nCadastre um veículo primeiro.");
          dispose(); // Fecha a tela se não tiver carro
          return;
      }

      for (Veiculo v : lista) {
          cbVeiculos.addItem(v);
      }
  }

  private void atualizarServicosCompativeis() {
      Veiculo veiculoSelecionado = (Veiculo) cbVeiculos.getSelectedItem();
      if (veiculoSelecionado == null) return;

      cbServicos.removeAllItems();

      for (TiposDeServicos tipo : TiposDeServicos.values()) {
        // Filtro para Transporte
        if (tipo.isExclusivoTransporte() && !(veiculoSelecionado instanceof VeiculosTransporte)) {
          continue;
        }
        // Filtro para Moto
        if (tipo == TiposDeServicos.INSULFILM && (veiculoSelecionado instanceof Moto)) {
          continue;
        }
        cbServicos.addItem(tipo);
      }
  }

  private void atualizarPrecoEstimado() {
    Veiculo veiculoSelecionado = (Veiculo) cbVeiculos.getSelectedItem();
    TiposDeServicos tipoSelecionado = (TiposDeServicos) cbServicos.getSelectedItem();

    if (veiculoSelecionado == null || tipoSelecionado == null) {
        lblPrecoEstimado.setText("Valor Estimado: R$ 0,00");
        return;
    }

    double percentual = veiculoSelecionado.calcularPrecoEspecifico();
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
      Veiculo veiculoSelecionado = (Veiculo) cbVeiculos.getSelectedItem();
      TiposDeServicos tipoSelecionado = (TiposDeServicos) cbServicos.getSelectedItem();
      FormaPagamento formaPag = (FormaPagamento) cbPagamento.getSelectedItem();

      AgendamentoController controller = new AgendamentoController();

      controller.verificaAgendamento(
          clienteLogado,
          veiculoSelecionado, // Agora passamos o veículo escolhido na Combo
          tipoSelecionado,
          dataString,
          horaString,
          formaPag);

      JOptionPane.showMessageDialog(this, "Agendamento realizado com Sucesso!");
      dispose();

    } catch (AgendamentoException e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erro inesperado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
    }
  }

  private JLabel criarLabel(String texto, int x, int y) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lbl.setBounds(x, y, 200, 20);
    return lbl;
  }
}