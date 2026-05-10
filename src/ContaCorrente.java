import java.math.BigDecimal;

public class ContaCorrente extends Conta {
    private BigDecimal chequeEspecial;
    private static final BigDecimal TAXA_MANUTENCAO = new BigDecimal("12.90");

    public ContaCorrente(int agencia, Cliente titular) {
        super(agencia, titular);
        this.chequeEspecial = new BigDecimal("500.00");
    }

    // Sobrescreve o saque para permitir cheque especial
    @Override
    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }
        BigDecimal saldoDisponivel = this.saldo.add(this.chequeEspecial);
        if (saldoDisponivel.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo + cheque especial insuficiente.");
        }
        this.saldo = this.saldo.subtract(valor);
        registrarTransacao("SAQUE", valor, "Saque (c/ cheque especial se necessário)");
        System.out.println("Saque de R$ " + valor + " efetuado.");
    }

    public void cobrarManutencao() {
        if (this.saldo.compareTo(TAXA_MANUTENCAO) >= 0) {
            this.saldo = this.saldo.subtract(TAXA_MANUTENCAO);
            registrarTransacao("TAXA", TAXA_MANUTENCAO, "Cobrança de tarifa mensal");
            System.out.println("Tarifa de manutenção de R$ " + TAXA_MANUTENCAO + " debitada.");
        } else {
            System.out.println("Saldo insuficiente para cobrar tarifa mensal.");
        }
    }

    public BigDecimal getChequeEspecial() {
        return chequeEspecial;
    }
}