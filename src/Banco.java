import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Banco {
    private List<Conta> contas;
    private Scanner scanner;
    private Conta contaAcessada;   // conta logada no momento
    private boolean logado;

    public Banco() {
        this.contas = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.logado = false;
    }

    public void iniciar() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     SISTEMA BANCÁRIO DO ZÉ          ║");
        System.out.println("║   (Versão raiz - sem firula)        ║");
        System.out.println("╚══════════════════════════════════════╝");

        int opcao;
        do {
            if (!logado) {
                exibirMenuPrincipal();
                opcao = lerInteiro("Opção: ");
                switch (opcao) {
                    case 1: criarConta(); break;
                    case 2: acessarConta(); break;
                    case 3: listarContas(); break;
                    case 0: System.out.println("Saindo... até logo!"); break;
                    default: System.out.println("Opção inválida. Tente de novo.");
                }
            } else {
                exibirMenuConta();
                opcao = lerInteiro("Opção: ");
                switch (opcao) {
                    case 1: fazerDeposito(); break;
                    case 2: fazerSaque(); break;
                    case 3: fazerTransferencia(); break;
                    case 4: verSaldo(); break;
                    case 5: verExtrato(); break;
                    case 6:
                        logado = false;
                        contaAcessada = null;
                        System.out.println("Logout efetuado.");
                        break;
                    default: System.out.println("Opção inválida.");
                }
            }
        } while (opcao != 0 || logado);  // Só sai se não estiver logado e escolheu 0
        scanner.close();
    }

    private void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1 - Criar nova conta");
        System.out.println("2 - Acessar conta (número + senha)");
        System.out.println("3 - Listar todas contas (apenas números)");
        System.out.println("0 - Sair");
    }

    private void exibirMenuConta() {
        System.out.println("\n--- CONTA: " + contaAcessada.getNumero() + " ---");
        System.out.println("Bem-vindo, " + contaAcessada.titular.getNome());
        System.out.println("1 - Depositar");
        System.out.println("2 - Sacar");
        System.out.println("3 - Transferir");
        System.out.println("4 - Ver saldo");
        System.out.println("5 - Extrato completo");
        System.out.println("6 - Sair da conta (logout)");
    }

    private void criarConta() {
        System.out.println("\n=== CRIAR NOVA CONTA ===");
        System.out.print("Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("CPF (apenas números): ");
        String cpf = scanner.nextLine();
        System.out.print("Defina uma senha numérica (4 dígitos): ");
        String senha = scanner.nextLine();

        Cliente cliente = new Cliente(nome, cpf, senha);

        System.out.println("Tipo de conta:");
        System.out.println("1 - Conta Corrente (com cheque especial de R$500)");
        System.out.println("2 - Conta Poupança (rendimento mensal 0,5%)");
        int tipo = lerInteiro("Escolha: ");

        Conta novaConta;
        int agencia = 1;  // agência fixa pra simplificar
        if (tipo == 1) {
            novaConta = new ContaCorrente(agencia, cliente);
            System.out.println("Conta Corrente criada!");
        } else if (tipo == 2) {
            novaConta = new ContaPoupanca(agencia, cliente);
            System.out.println("Conta Poupança criada!");
        } else {
            System.out.println("Opção inválida, criando Corrente por padrão.");
            novaConta = new ContaCorrente(agencia, cliente);
        }

        contas.add(novaConta);
        System.out.println("Número da conta: " + novaConta.getNumero());
        System.out.println("Guarde bem o número e sua senha!\n");
    }

    private void acessarConta() {
        System.out.print("Número da conta: ");
        int num = lerInteiro("");
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        for (Conta c : contas) {
            if (c.getNumero() == num && c.titular.validarSenha(senha)) {
                contaAcessada = c;
                logado = true;
                System.out.println("Acesso autorizado!\n");
                return;
            }
        }
        System.out.println("Conta ou senha inválidos. Tente novamente.\n");
    }

    private void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada ainda.");
            return;
        }
        System.out.println("Contas existentes:");
        for (Conta c : contas) {
            String tipo = (c instanceof ContaCorrente) ? "Corrente" : "Poupança";
            System.out.println("  Conta " + c.getNumero() + " | " + tipo + " | Titular: " + c.titular.getNome());
        }
    }

    private void fazerDeposito() {
        System.out.print("Valor para depósito: R$ ");
        BigDecimal valor = lerBigDecimal();
        try {
            contaAcessada.depositar(valor);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void fazerSaque() {
        System.out.print("Valor para saque: R$ ");
        BigDecimal valor = lerBigDecimal();
        try {
            contaAcessada.sacar(valor);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void fazerTransferencia() {
        System.out.print("Número da conta destino: ");
        int numDestino = lerInteiro("");
        Conta destino = null;
        for (Conta c : contas) {
            if (c.getNumero() == numDestino) {
                destino = c;
                break;
            }
        }
        if (destino == null) {
            System.out.println("Conta destino não encontrada.");
            return;
        }
        if (destino.getNumero() == contaAcessada.getNumero()) {
            System.out.println("Não pode transferir para a mesma conta.");
            return;
        }
        System.out.print("Valor da transferência: R$ ");
        BigDecimal valor = lerBigDecimal();
        try {
            contaAcessada.transferir(destino, valor);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void verSaldo() {
        System.out.println("\nSaldo atual: R$ " + String.format("%.2f", contaAcessada.saldo));
        if (contaAcessada instanceof ContaCorrente) {
            ContaCorrente cc = (ContaCorrente) contaAcessada;
            System.out.println("Limite cheque especial: R$ " + String.format("%.2f", cc.getChequeEspecial()));
        }
        System.out.println();
    }

    private void verExtrato() {
        contaAcessada.exibirExtrato();
    }

    // Métodos auxiliares para ler dados do teclado evitando erros
    private int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            System.out.println("Valor inválido. Digite um número inteiro.");
            scanner.next(); // descarta a entrada errada
            System.out.print(mensagem);
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpa o buffer
        return valor;
    }

    private BigDecimal lerBigDecimal() {
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Valor inválido. Use números separados por ponto (ex: 12.50)");
            scanner.next();
        }
        BigDecimal valor = scanner.nextBigDecimal();
        scanner.nextLine();
        return valor;
    }

    public static void main(String[] args) {
        Banco banco = new Banco();
        banco.iniciar();
    }
}