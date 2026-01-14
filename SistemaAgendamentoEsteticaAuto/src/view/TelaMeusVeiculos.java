package view;

import dao.VeiculoDAO;

import java.util.List;

import java.awt.*;
import java.awt.event.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import model.Carro;

import model.Cliente;
import model.Moto;
import model.Sessao;
import model.Veiculo;

public class TelaMeusVeiculos extends JDialog {

  private JPanel pnlLista;
  private Cliente clienteLogado;
  private VeiculoDAO dao;

  public TelaMeusVeiculos(Frame owner) {
    super(owner, "Meus Veículos", true);
    clienteLogado = Sessao.getUsuarioLogado();
    if (clienteLogado == null) {
      JOptionPane.showInputDialog(null, "Erro de sessão. Faça login novamente");
      return;

    }
    setSize(600, 500);
    setLocationRelativeTo(owner);
    setLayout(new BorderLayout());
    dao = new VeiculoDAO();

    JLabel lblTitulo = new JLabel("Meus Veículos");
    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
    lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    lblTitulo.setForeground(Color.decode("#2c3e50")); // Cor azul escuro
    lblTitulo.setBorder(new EmptyBorder(10, 0, 10, 0)); // Margem (Espaçamento)

    add(lblTitulo, BorderLayout.NORTH);

    pnlLista = new JPanel();

    pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(pnlLista);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setBorder(null);

    add(scrollPane, BorderLayout.CENTER);

    JPanel pnlSul = new JPanel(new FlowLayout(FlowLayout.RIGHT));

    JButton btnNovo = new JButton("Cadastrar Novo Veículo ");
    btnNovo.setBackground(Color.decode("#3498db")); // Azul
    btnNovo.setForeground(Color.WHITE);
    btnNovo.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnNovo.setCursor(new Cursor(Cursor.HAND_CURSOR));

    btnNovo.addActionListener(e -> abrirTelaCadastro(null));

    pnlSul.add(btnNovo);
    add(pnlSul, BorderLayout.SOUTH);
    atualizarLista();
  }

  private void atualizarLista() {
    pnlLista.removeAll();

    // --- MUDANÇA AQUI ---
    // Reaproveitando a lógica de filtro, mas usando o ID que é mais seguro que CPF
    List<Veiculo> veiculos = dao.listarPorCpf(clienteLogado.getCpf());
    // --------------------

    if (veiculos.isEmpty()) {
      JLabel lblVazio = new JLabel("Nenhum veículo cadastrado.");
      lblVazio.setBorder(new EmptyBorder(20, 20, 20, 20));
      pnlLista.add(lblVazio);

    } else {
      for (Veiculo v : veiculos) {
        pnlLista.add(criarCardVeiculo(v));
        pnlLista.add(Box.createRigidArea(new Dimension(0, 10)));
      }
    }
    pnlLista.revalidate();
    pnlLista.repaint();

  }

  private JPanel criarCardVeiculo(Veiculo v) {
    JPanel card = new JPanel(new BorderLayout());
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1), // Use createLineBorder para cor e espessura
        new EmptyBorder(10, 10, 10, 10) // São 4 números (top, left, bottom, right) e SEM ponto e vírgula aqui
    ));
    card.setBackground(Color.WHITE);
    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

    String detalhes = "<html><b>" + v.getMarca() + "" + v.getModelo() + "</b> (" + v.getPlaca() + ")<br/>";
    if (v instanceof Carro) {
      detalhes += "Carro -" + ((Carro) v).getCategoria();

    } else if (v instanceof Moto) {
      detalhes += "Moto - " + ((Moto) v).getCilindradas() + "cc";

    }

    private void excluirVeiculo(Veiculo v) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o veículo placa " + v.getPlaca() + "?",
                "Excluir", JOptionPane.YES_NO_OPTION);

        if(confirm == JOptionPane.YES_OPTION){
            if (dao.deletar(v.getId())) {
                JOptionPane.showMessageDialog(this, "Veículo excluído com sucesso!");
                atualizarLista();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao excluir veículo.");
            }
        }
        

    }

  }

}
