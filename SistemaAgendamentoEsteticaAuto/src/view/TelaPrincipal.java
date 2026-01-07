package view;

import model.Sessao;
import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
  public TelaPrincipal() {
    // Verificação para não abrir sem usuário logado.
    if (Sessao.getUsuarioLogado() == null) {
      JOptionPane.showMessageDialog(null, "Erro: Realize login primeiro!");
      dispose();
      return; // Para a execução.
    }

    setTitle("EDS - Área do Cliente");
    setSize(500, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(null);
    setResizable(false);
    getContentPane().setBackground(Color.decode("#f0f2f5"));

    // Cabeçalho
    JPanel panelHeader = new JPanel();
    panelHeader.setBounds(0, 0, 500, 80);
    panelHeader.setBackground(Color.decode("#2c3e50"));
    panelHeader.setLayout(null);
    add(panelHeader);

    String nome = Sessao.getUsuarioLogado().getNome();
    JLabel lblBemVindo = new JLabel("Olá, " + nome + "!");
    lblBemVindo.setForeground(Color.WHITE); // Cor da letra
    lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 24));
    lblBemVindo.setBounds(20, 20, 300, 40);
    panelHeader.add(lblBemVindo);

    JLabel lblSubtitulo = new JLabel("Seu carro limpo e novo em folha.");
    lblSubtitulo.setForeground(Color.LIGHT_GRAY);
    lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    lblSubtitulo.setBounds(20, 50, 300, 20);
    panelHeader.add(lblSubtitulo);

    // Botões
    JButton btnVeiculos = criarBotao("Meus Veículos", "Cadastre ou gerencie seus carros", 50, 120);
    btnVeiculos.addActionListener(e -> {
      new TelaCadastroVeiculo(null, null).setVisible(true);
    });
    add(btnVeiculos);

    JButton btnAgendar = criarBotao("Novo Agendamento", "Marque uma data", 50, 200);
    btnAgendar.addActionListener(e -> {
      new TelaAgendamento().setVisible(true);
    });
    add(btnAgendar);

    JButton btnServicos = criarBotao("Serviços", "Lista de serviços oferecidos", 50, 280);
    btnServicos.addActionListener(e -> {
      new TelaCatalogoServicos().setVisible(true);
    });
    add(btnServicos);

    JButton btnHistorico = criarBotao(" Meus Agendamentos", "Veja o que já foi feito", 50, 360);
    btnHistorico.addActionListener(e -> {
      new TelaMeusAgendamentos().setVisible(true);
    });
    add(btnHistorico);

    JButton btnSair = new JButton("Sair da Conta");
    btnSair.setBounds(150, 450, 200, 40);
    btnSair.setBackground(Color.decode("#e74c3c"));
    btnSair.setForeground(Color.WHITE);
    btnSair.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnSair.setFocusPainted(false);
    btnSair.addActionListener(e -> {
      Sessao.logout();
      new TelaLogin().setVisible(true);
      dispose();
    });
    add(btnSair);

  }

  private JButton criarBotao(String texto, String dica, int x, int y) {
    JButton btn = new JButton(texto);
    btn.setBounds(x, y, 380, 60);
    btn.setBackground(Color.WHITE);
    btn.setForeground(Color.decode("#2c3e50"));
    btn.setFont(new Font("Segoe UI", Font.BOLD, 16));

    btn.setToolTipText(dica); // Dica ao passar o mouse sugere o que esse botão faz
    btn.setFocusPainted(false);
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Alinha o texto à esquerda com uma margem pequena
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setMargin(new Insets(0, 20, 0, 0));

    return btn;
  }
}
