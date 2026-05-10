import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
    protected static int contadorContas = 1000; // número inicial (de propósito, não começa do 1)

    protected int numero;
    protected int agencia;
    protected BigDecimal saldo;
    protected Cliente titular;
    protected List<Transacao> historico;

    public Conta(int agencia, Cliente titular) {
        this.numero = contadorContas++;
        this.agencia = agencia;
        this.titular = titular;
        this.saldo = BigDecimal.ZERO;
        this.historico = new ArrayList<>();
    }

    // Métodos públicos
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo.");
        }
        this.saldo = this.saldo.add(valor);
        registrarTransacao("DEPOSITO", valor, "Depósito em conta");
        System.out.println("Depósito de R$ " + valor + " realizado com sucesso.");
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para saque.");
        }
        this.saldo = this.saldo.subtract(valor);
        registrarTransacao("SAQUE", valor, "Saque realizado");
        System.out.println("Saque de R$ " + valor + " efetuado. Retire o dinheiro.");
    }

    public void transferir(Conta destino, BigDecimal valor) {
        if (destino == null) {
            throw new IllegalArgumentException("Conta destino inválida.");
        }
        // Primeiro tenta sacar da origem
        this.sacar(valor);
        // Se o saque passou, deposita no destino
        destino.depositar(valor);
        // Registra transações específicas de transferência
        registrarTransacao("TRANSFERENCIA_ENVIO", valor,
                "Transferência enviada para conta " + destino.getNumero());
        destino.registrarTransacao("TRANSFERENCIA_RECEBIMENTO", valor,
                "Transferência recebida da conta " + this.numero);
        System.out.println("Transferência de R$ " + valor + " para conta " + destino.getNumero() + " concluída.");
    }

    public void exibirExtrato() {
        System.out.println("\n=== EXTRATO DA CONTA " + numero + " ===");
        System.out.println("Titular: " + titular.getNome());
        System.out.println("Saldo atual: R$ " + String.format("%.2f", saldo));
        System.out.println("\nHistórico de movimentações:");
        if (historico.isEmpty()) {
            System.out.println("  Nenhuma movimentação registrada.");
        } else {
            for (Transacao t : historico) {
                System.out.println("  " + t.toString());
            }
        }
        System.out.println("===================================\n");
    }

    // Método interno (package-private) para registrar transações
    void registrarTransacao(String tipo, BigDecimal valor, String descricao) {
        Transacao transacao = new Transacao(tipo, valor, descricao);
        this.historico.add(transacao);
    }

    // Getters
    public int getNumero() {
        return numero;
    }

    public int getAgencia() {
        return agencia;
    }
}