package view;

import dao.VeiculoDAO;
import exceptions.VeiculoException;

import java.awt.*;//(Abstract Window Toolkit) é um pacote padrão do Java usado para interface gráfica.  ela contem cores e fontes entre outros
import javax.swing.*;
import model.Carro;
import model.Moto;
import model.Veiculo;
import model.enums.CategoriaCarro;

public class TelaCadastroVeiculo extends JDialog {// extends JFrame significa que essa classe É uma janela do Swing.

  private Veiculo veiculoEdicao = null;
  private boolean atualizou = false;
  private JComboBox<String> cbTipoVeiculo;// Caixa de seleção suspensa para veiculos
  // Campos de texto para o usuário digitar marca, modelo e placa:
  private JTextField txtMarca;
  private JTextField txtModelo;
  private JTextField txtPlaca;

  // Aqui o JComobox mostra as categorias do enum "CategoriaCarro"
  private JLabel lblCategoria;
  private JComboBox<CategoriaCarro> cbCategoria;

  // campo se for moto
  private JLabel lblCilindradas;
  private JTextField txtCilindradas;
  // campo para salvar ou cancelar
  private JButton btnSalvar;
  private JButton btnCancelar;

  public TelaCadastroVeiculo(Frame owner, Veiculo veiculoParaEditar) {
    super(owner, "Cadastro de Veículo", true);
    this.veiculoEdicao = veiculoParaEditar;
    setSize(400, 550);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(owner);
    setLayout(null);
    getContentPane().setBackground(Color.decode("#F5F5F5"));

    add(criarLabel("Tipo de Veículo:", 40, 70));
    cbTipoVeiculo = new JComboBox<>(new String[] { "Carro", "Moto" });
    cbTipoVeiculo.setBounds(40, 95, 300, 30);
    cbTipoVeiculo.setBackground(Color.WHITE);
    cbTipoVeiculo.addActionListener(e -> alternarCampos());
    add(cbTipoVeiculo);

    add(criarLabel("Marca:", 40, 135));
    txtMarca = new JTextField();
    txtMarca.setBounds(40, 160, 300, 30);
    add(txtMarca);

    add(criarLabel("Modelo:", 40, 200));
    txtModelo = new JTextField();
    txtModelo.setBounds(40, 225, 300, 30);
    add(txtModelo);

    add(criarLabel("Placa:", 40, 265));
    txtPlaca = new JTextField();
    txtPlaca.setBounds(40, 290, 300, 30);
    add(txtPlaca);

    lblCategoria = criarLabel("Categoria:", 40, 330);
    add(lblCategoria);
    cbCategoria = new JComboBox<>(CategoriaCarro.values());
    cbCategoria.setBounds(40, 355, 300, 30);
    cbCategoria.setBackground(Color.WHITE);
    add(cbCategoria);

    lblCilindradas = criarLabel("Cilindradas (cc):", 40, 330);
    add(lblCilindradas);
    txtCilindradas = new JTextField();
    txtCilindradas.setBounds(40, 355, 300, 30);
    add(txtCilindradas);

    btnSalvar = new JButton("SALVAR");
    btnSalvar.setBounds(40, 410, 300, 40);
    btnSalvar.setBackground(Color.decode("#27ae60"));
    btnSalvar.setForeground(Color.WHITE);
    btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnSalvar.addActionListener(e -> salvarVeiculo());
    add(btnSalvar);

    btnCancelar = new JButton("Cancelar");
    btnCancelar.setBounds(40, 460, 300, 30);
    btnCancelar.setBackground(Color.decode("#F5F5F5"));
    btnCancelar.setForeground(Color.GRAY);
    btnCancelar.setBorder(null);
    btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btnCancelar.addActionListener(e -> dispose());
    add(btnCancelar);

    if (veiculoEdicao != null) {
      preencherDadosEdicao();

    }
  }

  private void preencherDadosEdicao() {
    txtMarca.setText(veiculoEdicao.getMarca());
    txtModelo.setText(veiculoEdicao.getModelo());
    txtPlaca.setText(veiculoEdicao.getPlaca());
    cbTipoVeiculo.setEnabled(false);

    if (veiculoEdicao instanceof Carro) {
      cbTipoVeiculo.setSelectedItem("Carro");
      Carro c = (Carro) veiculoEdicao;
      cbCategoria.setSelectedItem(c.getCategoria());
    } else if (veiculoEdicao instanceof Moto) {
      cbTipoVeiculo.setSelectedItem("Moto");
      Moto m = (Moto) veiculoEdicao;
      txtCilindradas.setText(String.valueOf(m.getCilindradas()));
    }
    alternarCampos();

  }

  private void alternarCampos() {
    String tipo = (String) cbTipoVeiculo.getSelectedItem();
    boolean isCarro = "Carro".equals(tipo);

    lblCategoria.setVisible(isCarro);
    cbCategoria.setVisible(isCarro);
    lblCilindradas.setVisible(!isCarro);
    txtCilindradas.setVisible(!isCarro);
  }

  private void salvarVeiculo() {
    String marca = txtMarca.getText().trim();
    String modelo = txtModelo.getText().trim();
    String placa = txtPlaca.getText().trim();
    String tipo = (String) cbTipoVeiculo.getSelectedItem();

    if (marca.isEmpty() || modelo.isEmpty() || placa.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!");
      return;
    }

    try {
      VeiculoDAO dao = new VeiculoDAO();
      Veiculo existente = dao.buscarPorPlaca(placa);

      if (veiculoEdicao == null && existente != null) {
        JOptionPane.showMessageDialog(this, "Erro: Placa já cadastrada!", "Erro", JOptionPane.ERROR_MESSAGE);
        return;
      }

      Veiculo veiculoSalvar;

      if ("Carro".equals(tipo)) {
        CategoriaCarro cat = (CategoriaCarro) cbCategoria.getSelectedItem();
        veiculoSalvar = new Carro(marca, modelo, placa, cat);
      } else {
        if (txtCilindradas.getText().trim().isEmpty()) {
          JOptionPane.showMessageDialog(this, "Informe as cilindradas!");
          return;
        }
        int cc = Integer.parseInt(txtCilindradas.getText().trim());
        veiculoSalvar = new Moto(marca, modelo, placa, cc);
      }
      if (veiculoEdicao != null) {
        veiculoSalvar.setId(veiculoEdicao.getId());
        if (dao.atualizar(veiculoSalvar)) {
          JOptionPane.showMessageDialog(this, "Veículo atualizado com sucesso!");
          atualizou = true;
          dispose();

        } else {
          JOptionPane.showMessageDialog(this, "Erro ao atualizar.");

        }
      } else {
        // É UM NOVO CADASTRO
        if (dao.salvar(veiculoSalvar)) { // Lembrando que seu salvar retorna boolean agora? Se não, ajuste.
          JOptionPane.showMessageDialog(this, "Veículo salvo com sucesso!");
          atualizou = true; // Marca sucesso
          dispose();
        }
      }
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Cilindradas deve ser número!");
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
    }
  }

  private JLabel criarLabel(String texto, int x, int y) {
    JLabel lbl = new JLabel(texto);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lbl.setBounds(x, y, 200, 20);
    return lbl;
  }
}
