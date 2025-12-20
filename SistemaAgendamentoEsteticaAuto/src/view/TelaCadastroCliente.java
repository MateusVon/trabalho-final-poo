package view;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import controller.CadastroController;


import java.awt.*;
import java.text.ParseException;

public class TelaCadastroCliente extends JFrame{
    // Componentes
    private JTextField txtNome;
    private JFormattedTextField txtCpf;
    private JFormattedTextField txtTelefone;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnSalvar;
    private JButton btnVoltar;

    public TelaCadastroCliente(){
        super("Crie sua Conta");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.decode("#F5F5F5"));

        JLabel lblTitulo = new JLabel("Novo Cadastro");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.decode("#2c3e50"));
        lblTitulo.setBounds(110, 20, 200, 30);
        add(lblTitulo);

        // Nome
        add(criarLabel("Nome Completo:", 40, 70));
        txtNome = new JTextField();
        txtNome.setBounds(40,95,300,30);
        add(txtNome);

        // Cpf
        add(criarLabel("CPF", 40, 135));
        try{
            MaskFormatter maskCpf = new MaskFormatter("###.###.###-##"); // Define o formato da mascara
            maskCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(maskCpf);
        }catch (ParseException e){
            txtCpf = new JFormattedTextField();
        }
        txtCpf.setBounds(40,160,300,30);
        add(txtCpf);

        // Telefone
        add(criarLabel("Celular:", 40, 200));
        try {
            MaskFormatter maskTel = new MaskFormatter("(##) #####-####");
            maskTel.setPlaceholderCharacter('_');
            txtTelefone = new JFormattedTextField(maskTel);
        } catch (ParseException e) {
            txtTelefone = new JFormattedTextField();
        }
        txtTelefone.setBounds(40, 225, 300, 30);
        add(txtTelefone);

        // Login
        add(criarLabel("Crie um Login:", 40, 265));
        txtLogin = new JTextField();
        txtLogin.setBounds(40, 290, 300, 30);
        add(txtLogin);

        // Senha
        add(criarLabel("Crie uma Senha:", 40, 330));
        txtSenha = new JPasswordField();
        txtSenha.setBounds(40, 355, 300, 30);
        add(txtSenha);

        // --- BOTÕES ---

        btnSalvar = new JButton("CRIAR CONTA");
        btnSalvar.setBounds(40, 410, 300, 40);
        btnSalvar.setBackground(Color.decode("#27ae60")); // Verde
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> cadastrarCliente());
        add(btnSalvar);

        // Botão VOLTAR
        btnVoltar = new JButton("Voltar ao Login");
        btnVoltar.setBounds(40, 460, 300, 30);
        btnVoltar.setBackground(Color.decode("#F5F5F5"));
        btnVoltar.setForeground(Color.GRAY);
        btnVoltar.setBorder(null); // Sem borda, estilo link
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ação do Botão Voltar
        btnVoltar.addActionListener(e -> {
            new TelaLogin().setVisible(true);
            dispose();
        });
        add(btnVoltar);
    }

    private void cadastrarCliente() {
        
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String tel = txtTelefone.getText();
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        // Verifica se está vazio
        if (nome.isEmpty() || login.isEmpty() || senha.isEmpty() || cpf.contains("_") || tel.contains("_")) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        CadastroController cadastroC = new CadastroController();

        try {
            // Cria o objeto Cliente
            cadastroC.cadastrarCliente(nome, cpf, tel, login, senha);
            JOptionPane.showMessageDialog(this, "Conta criada com sucesso!\nFaça login para continuar.");
            // Volta para a tela de login
            new TelaLogin().setVisible(true);
            this.dispose();
        } catch (IllegalArgumentException erro) {
            // Se o Model reclamar do CPF ou Telefone inválido, mostra aqui
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Erro no Cadastro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception erroGeral) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + erroGeral.getMessage());
        }
    }

    // Método auxiliar para criar Labels rapidinho
    private JLabel criarLabel(String texto, int x, int y){
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setBounds(x,y,200,20);
        return lbl;
    }
}
