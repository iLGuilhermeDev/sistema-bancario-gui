import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaPoupanca extends Conta {
    private static final BigDecimal RENDIMENTO_MENSAL = new BigDecimal("0.005"); // 0,5%

    public ContaPoupanca(int agencia, Cliente titular) {
        super(agencia, titular);
    }

    public void aplicarRendimento() {
        if (this.saldo.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal juros = this.saldo.multiply(RENDIMENTO_MENSAL);
            juros = juros.setScale(2, RoundingMode.HALF_EVEN);
            this.saldo = this.saldo.add(juros);
            registrarTransacao("RENDIMENTO", juros, "Rendimento da poupança aplicado");
            System.out.println("Rendimento de R$ " + juros + " creditado na poupança.");
        } else {
            System.out.println("Conta sem saldo, nenhum rendimento aplicado.");
        }
    }
}