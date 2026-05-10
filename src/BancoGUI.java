import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BancoGUI extends JFrame {
    private List<Conta> contas;
    private Conta contaLogada;
    private JFrame loginFrame;
    private JFrame mainFrame;
    private JTabbedPane tabs;
    private JPanel saldoPanel; // referência para recriar a aba de saldo
    private Color corPrimaria = new Color(41, 128, 185);
    private Color corSecundaria = new Color(240, 248, 255);

    public BancoGUI() {
        contas = new ArrayList<>();
        mostrarTelaLogin();
    }

    // Atualiza a aba de saldo após qualquer operação
    private void atualizarAbaSaldo() {
        if (tabs != null && saldoPanel != null) {
            int index = tabs.indexOfComponent(saldoPanel);
            if (index != -1) {
                JPanel novoSaldo = criarPainelSaldo();
                tabs.setComponentAt(index, novoSaldo);
                saldoPanel = novoSaldo;
            }
        }
    }

    private void mostrarTelaLogin() {
        loginFrame = new JFrame("Login - Banco Central");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(450, 550);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(corSecundaria);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("🏦 Banco Central", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(corPrimaria);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        JLabel numLabel = new JLabel("Número da Conta:");
        numLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(numLabel, gbc);

        JTextField numeroField = new JTextField(15);
        numeroField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        numeroField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        mainPanel.add(numeroField, gbc);

        JLabel senhaLabel = new JLabel("Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(senhaLabel, gbc);

        JPasswordField senhaField = new JPasswordField(15);
        senhaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        senhaField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        mainPanel.add(senhaField, gbc);

        JButton loginBtn = new JButton("Entrar");
        loginBtn.setBackground(corPrimaria);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(loginBtn, gbc);

        JLabel cadastroLabel = new JLabel("Não tem conta? Cadastre-se");
        cadastroLabel.setForeground(corPrimaria);
        cadastroLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        cadastroLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        mainPanel.add(cadastroLabel, gbc);

        JLabel listarLabel = new JLabel("Ver contas cadastradas");
        listarLabel.setForeground(corPrimaria);
        listarLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        listarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 5;
        mainPanel.add(listarLabel, gbc);

        loginBtn.addActionListener(e -> {
            try {
                int num = Integer.parseInt(numeroField.getText().trim());
                String senha = new String(senhaField.getPassword());
                Conta encontrada = null;
                for (Conta c : contas) {
                    if (c.getNumero() == num && c.titular.validarSenha(senha)) {
                        encontrada = c;
                        break;
                    }
                }
                if (encontrada != null) {
                    contaLogada = encontrada;
                    loginFrame.dispose();
                    abrirJanelaPrincipal();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Conta ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(loginFrame, "Número da conta inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        cadastroLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginFrame.dispose();
                mostrarTelaCadastro();
            }
        });

        listarLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listarContas();
            }
        });

        loginFrame.add(mainPanel);
        loginFrame.setVisible(true);
    }

    private void listarContas() {
        if (contas.isEmpty()) {
            JOptionPane.showMessageDialog(loginFrame, "Nenhuma conta cadastrada.", "Contas", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder("Contas disponíveis:\n");
        for (Conta c : contas) {
            sb.append("Nº ").append(c.getNumero()).append(" - ").append(c.titular.getNome());
            if (c instanceof ContaCorrente) sb.append(" (Corrente)");
            else sb.append(" (Poupança)");
            sb.append("\n");
        }
        JOptionPane.showMessageDialog(loginFrame, sb.toString(), "Lista de Contas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarTelaCadastro() {
        JFrame cadastroFrame = new JFrame("Criar Conta");
        cadastroFrame.setSize(450, 600);
        cadastroFrame.setLocationRelativeTo(null);
        cadastroFrame.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(corSecundaria);
        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("Nova Conta", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(corPrimaria);
        gbc.gridy = 0;
        mainPanel.add(title, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Nome Completo:"), gbc);
        JTextField nomeField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("CPF:"), gbc);
        JTextField cpfField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(cpfField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Senha (4+ dígitos):"), gbc);
        JPasswordField senhaField = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(senhaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Tipo de Conta:"), gbc);
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Conta Corrente (cheque especial R$500)", "Conta Poupança (rendimento 0,5%)"});
        gbc.gridx = 1;
        mainPanel.add(tipoCombo, gbc);

        JButton criarBtn = new JButton("Criar Conta");
        criarBtn.setBackground(new Color(46, 204, 113));
        criarBtn.setForeground(Color.WHITE);
        criarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        criarBtn.setFocusPainted(false);
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        mainPanel.add(criarBtn, gbc);

        JButton voltarBtn = new JButton("Voltar ao Login");
        voltarBtn.setBorderPainted(false);
        voltarBtn.setContentAreaFilled(false);
        voltarBtn.setForeground(corPrimaria);
        voltarBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gbc.gridy = 6;
        mainPanel.add(voltarBtn, gbc);

        criarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String cpf = cpfField.getText().trim();
            String senha = new String(senhaField.getPassword());
            if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(cadastroFrame, "Preencha todos os campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (senha.length() < 4) {
                JOptionPane.showMessageDialog(cadastroFrame, "Senha deve ter pelo menos 4 caracteres.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Cliente cliente = new Cliente(nome, cpf, senha);
            Conta novaConta;
            if (tipoCombo.getSelectedIndex() == 0) {
                novaConta = new ContaCorrente(1, cliente);
            } else {
                novaConta = new ContaPoupanca(1, cliente);
            }
            contas.add(novaConta);
            JOptionPane.showMessageDialog(cadastroFrame, "Conta criada!\nNúmero: " + novaConta.getNumero());
            cadastroFrame.dispose();
            mostrarTelaLogin();
        });

        voltarBtn.addActionListener(e -> {
            cadastroFrame.dispose();
            mostrarTelaLogin();
        });

        cadastroFrame.add(mainPanel);
        cadastroFrame.setVisible(true);
    }

    private void abrirJanelaPrincipal() {
        mainFrame = new JFrame("Banco - Conta " + contaLogada.getNumero());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // mudado para não fechar tudo
        mainFrame.setSize(900, 650);
        mainFrame.setLocationRelativeTo(null);

        tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        saldoPanel = criarPainelSaldo();
        tabs.addTab("💰 Saldo", saldoPanel);
        tabs.addTab("⬇️ Depósito", criarPainelDeposito());
        tabs.addTab("⬆️ Saque", criarPainelSaque());
        tabs.addTab("🔄 Transferência", criarPainelTransferencia());
        tabs.addTab("📄 Extrato", criarPainelExtrato());
        tabs.addTab("🏦 Todas Contas", criarPainelListaContas());

        // Botão de logout
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(corSecundaria);
        JButton logoutBtn = new JButton("Sair da conta");
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            mainFrame.dispose();
            mostrarTelaLogin();
        });
        topPanel.add(logoutBtn);

        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.add(tabs, BorderLayout.CENTER);
        mainFrame.setVisible(true);
    }

    private JPanel criarPainelSaldo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(corSecundaria);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(40, 60, 40, 60)
        ));

        JLabel saldoLabel = new JLabel("Saldo atual", SwingConstants.CENTER);
        saldoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        BigDecimal saldo = contaLogada.saldo;
        JLabel valorLabel = new JLabel(String.format("R$ %.2f", saldo));
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valorLabel.setForeground(corPrimaria);
        valorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(saldoLabel, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);

        if (contaLogada instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) contaLogada;
            JLabel limiteLabel = new JLabel("Limite cheque especial: R$ " + String.format("%.2f", cc.getChequeEspecial()));
            limiteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            limiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
            card.add(limiteLabel, BorderLayout.SOUTH);
        }

        panel.add(card);
        return panel;
    }

    private JPanel criarPainelDeposito() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(corSecundaria);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Depositar dinheiro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(corPrimaria);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel valorLabel = new JLabel("Valor (R$):");
        valorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        card.add(valorLabel, gbc);

        JTextField valorField = new JTextField(15);
        valorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valorField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        card.add(valorField, gbc);

        JButton depositarBtn = new JButton("Confirmar Depósito");
        depositarBtn.setBackground(new Color(46, 204, 113));
        depositarBtn.setForeground(Color.WHITE);
        depositarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        depositarBtn.setFocusPainted(false);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        card.add(depositarBtn, gbc);

        depositarBtn.addActionListener(e -> {
            try {
                String text = valorField.getText().trim().replace(",", ".");
                if (text.isEmpty()) throw new IllegalArgumentException("Digite um valor");
                BigDecimal valor = new BigDecimal(text);
                contaLogada.depositar(valor);
                JOptionPane.showMessageDialog(mainFrame, "Depósito realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                valorField.setText("");
                atualizarAbaSaldo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(card, gbc);
        return panel;
    }

    private JPanel criarPainelSaque() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(corSecundaria);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Sacar dinheiro");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(corPrimaria);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel valorLabel = new JLabel("Valor (R$):");
        valorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        card.add(valorLabel, gbc);

        JTextField valorField = new JTextField(15);
        valorField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valorField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        card.add(valorField, gbc);

        JButton sacarBtn = new JButton("Confirmar Saque");
        sacarBtn.setBackground(new Color(231, 76, 60));
        sacarBtn.setForeground(Color.WHITE);
        sacarBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sacarBtn.setFocusPainted(false);
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 15, 15, 15);
        card.add(sacarBtn, gbc);

        sacarBtn.addActionListener(e -> {
            try {
                String text = valorField.getText().trim().replace(",", ".");
                if (text.isEmpty()) throw new IllegalArgumentException("Digite um valor");
                BigDecimal valor = new BigDecimal(text);
                contaLogada.sacar(valor);
                JOptionPane.showMessageDialog(mainFrame, "Saque realizado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                valorField.setText("");
                atualizarAbaSaldo();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(card, gbc);
        return panel;
    }

    private JPanel criarPainelTransferencia() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(corSecundaria);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));

        JLabel title = new JLabel("Transferência");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(corPrimaria);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel contaDestLabel = new JLabel("Conta destino:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        card.add(contaDestLabel, gbc);

        JTextField destinoField = new JTextField(15);
        destinoField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        card.add(destinoField, gbc);

        JLabel valorLabel = new JLabel("Valor (R$):");
        gbc.gridy = 2;
        gbc.gridx = 0;
        card.add(valorLabel, gbc);

        JTextField valorField = new JTextField(15);
        valorField.setBorder(new LineBorder(Color.LIGHT_GRAY));
        gbc.gridx = 1;
        card.add(valorField, gbc);

        JButton transferirBtn = new JButton("Transferir");
        transferirBtn.setBackground(new Color(241, 196, 15));
        transferirBtn.setForeground(Color.WHITE);
        transferirBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        transferirBtn.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        card.add(transferirBtn, gbc);

        transferirBtn.addActionListener(e -> {
            try {
                int numDestino = Integer.parseInt(destinoField.getText().trim());
                Conta destino = null;
                for (Conta c : contas) {
                    if (c.getNumero() == numDestino) {
                        destino = c;
                        break;
                    }
                }
                if (destino == null) {
                    JOptionPane.showMessageDialog(mainFrame, "Conta destino não encontrada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (destino.getNumero() == contaLogada.getNumero()) {
                    JOptionPane.showMessageDialog(mainFrame, "Não pode transferir para si mesmo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String text = valorField.getText().trim().replace(",", ".");
                if (text.isEmpty()) throw new IllegalArgumentException("Digite o valor");
                BigDecimal valor = new BigDecimal(text);
                contaLogada.transferir(destino, valor);
                JOptionPane.showMessageDialog(mainFrame, "Transferência realizada!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                destinoField.setText("");
                valorField.setText("");
                atualizarAbaSaldo();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Número da conta inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainFrame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(card, gbc);
        return panel;
    }

    private JPanel criarPainelExtrato() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(corSecundaria);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Data/Hora", "Tipo", "Valor (R$)", "Descrição"}, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setRowHeight(28);

        for (Transacao t : contaLogada.historico) {
            model.addRow(new Object[]{t.getDataHoraFormatada(), t.getTipo(), String.format("%.2f", t.getValor()), t.getDescricao()});
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        JButton atualizarBtn = new JButton("Atualizar Extrato");
        atualizarBtn.setBackground(corPrimaria);
        atualizarBtn.setForeground(Color.WHITE);
        atualizarBtn.setFocusPainted(false);
        atualizarBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Transacao t : contaLogada.historico) {
                model.addRow(new Object[]{t.getDataHoraFormatada(), t.getTipo(), String.format("%.2f", t.getValor()), t.getDescricao()});
            }
        });
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(atualizarBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel criarPainelListaContas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(corSecundaria);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        DefaultTableModel model = new DefaultTableModel(new String[]{"Número", "Titular", "Tipo", "Saldo"}, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setRowHeight(28);

        for (Conta c : contas) {
            String tipo = (c instanceof ContaCorrente) ? "Corrente" : "Poupança";
            model.addRow(new Object[]{c.getNumero(), c.titular.getNome(), tipo, String.format("R$ %.2f", c.saldo)});
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        JButton recarregarBtn = new JButton("Recarregar");
        recarregarBtn.setBackground(corPrimaria);
        recarregarBtn.setForeground(Color.WHITE);
        recarregarBtn.addActionListener(e -> {
            model.setRowCount(0);
            for (Conta c : contas) {
                String tipo = (c instanceof ContaCorrente) ? "Corrente" : "Poupança";
                model.addRow(new Object[]{c.getNumero(), c.titular.getNome(), tipo, String.format("R$ %.2f", c.saldo)});
            }
        });
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(recarregarBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BancoGUI());
    }
}