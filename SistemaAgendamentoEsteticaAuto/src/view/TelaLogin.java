package view;

import javax.swing.*; // Importa tudo do Swing (telas, botões)

import controller.LoginController;
import exceptions.AutenticacaoException;

import java.awt.*; // Importa cores e fontes

public class TelaLogin extends JFrame{
    // Declarando componentes
    private JLabel lblLogo;
    private JLabel lblUsuario;
    private JTextField txtUsuario;
    private JLabel lblSenha;
    private JPasswordField txtSenha;
    private JLabel lblSemConta;
    private JButton btnCadastrar;
    private JButton btnEntrar;


    // Construtor para montar a tela
    public TelaLogin(){
        // Configurações da Janela
        super("Tela de Login"); // Título da janela
        setSize(400, 350); // Largura 400px, Altura 350
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa quando fecha a janela 
        setResizable(false); // Impede o usuario de mudar o tamanho da janela
        setLocationRelativeTo(null); // Centraliza a janela na tela do computador
        setLayout(null);
        getContentPane().setBackground(Color.decode("#34495e"));
        
        // Configurando os Componentes

        // Logo
        lblLogo = new JLabel("EDS Estética Automotiva");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 16));
        lblLogo.setBounds(100,20,200,30); // Tamanho da logo
        lblLogo.setForeground(Color.YELLOW);

        // Campo Usuario
        lblUsuario = new JLabel("Usuário");
        lblUsuario.setBounds(50,80,80,25); // Posição do Texto
        lblUsuario.setForeground(Color.WHITE);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(100,80,200,25); // Posição da caixa de escrever

        // Campo senha
        lblSenha = new JLabel("Senha");
        lblSenha.setBounds(50,130,80,25);
        lblSenha.setForeground(Color.WHITE);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(100,130,200,25);

        // Texto
        lblSemConta = new JLabel("Não possui conta?");
        lblSemConta.setFont(new Font("Arial", Font.PLAIN, 10));
        lblSemConta.setBounds(50,180,150,20);
        lblSemConta.setForeground(Color.WHITE);

        // Botões
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBounds(50,210,130,40);
        btnCadastrar.setBackground(Color.WHITE);
        btnCadastrar.addActionListener(e -> {
            new TelaCadastroCliente().setVisible(true); // Abre a tela de cadastro
            this.dispose(); // Fecha a tela de login para não ficarem duas janelas abertas
        });

        btnEntrar = new JButton("Entrar");
        btnEntrar.setBounds(200,210,130,40);
        btnEntrar.setBackground(new Color(173, 216, 230));

        btnEntrar.addActionListener(e -> {
            String usuario = txtUsuario.getText(); // Pega o que o usuário digitou, como se fosse um scanner
            String senha = new String(txtSenha.getPassword()); // JPassowrd exige esse tratamento fazendo um casting pra String

            LoginController controller = new LoginController();
            try{
                controller.logar(usuario, senha); // Tenta logar
                new TelaPrincipal().setVisible(true); // Abre a tela principal
                this.dispose(); // Fecha a tela de login
            }catch(AutenticacaoException erro){
                JOptionPane.showMessageDialog(null, erro.getMessage(), "Falha no Login", JOptionPane.WARNING_MESSAGE);
            }catch(Exception erroGeral){
                JOptionPane.showMessageDialog(null, "Erro inesperado: " + erroGeral.getMessage());
            }
        });

        add(lblLogo);
        add(lblUsuario);
        add(txtUsuario);
        add(lblSenha);
        add(txtSenha);
        add(lblSemConta);
        add(btnCadastrar);
        add(btnEntrar);
        setVisible(true);

    }
    public static void main(String[] args) {
        new TelaLogin();
        
    }
}
