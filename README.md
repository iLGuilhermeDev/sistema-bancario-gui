# Sistema Bancário em Java com Interface Gráfica (Swing)

Aplicação desktop desenvolvida em Java para gerenciamento de contas bancárias, oferecendo operações essenciais como depósito, saque, transferência e extrato. O sistema possui interface gráfica amigável construída com Java Swing e segue princípios de Programação Orientada a Objetos.

---

## Funcionalidades

- Cadastro de clientes com nome, CPF e senha
- Criação de contas Corrente (com cheque especial de R$ 500,00) ou Poupança (rendimento de 0,5% ao mês)
- Autenticação por número da conta e senha
- Depósito, saque e transferência entre contas
- Consulta de saldo e extrato detalhado com data/hora
- Listagem de todas as contas cadastradas para facilitar transferências
- Persistência em memória (dados não são salvos após o encerramento – versão atual)

---

## Tecnologias Utilizadas

- Java 17+ (JDK)
- Swing (interface gráfica)
- BigDecimal (para operações monetárias precisas)
- Git (controle de versão)

---

## Como Executar

### Pré-requisitos
- JDK 17 ou superior instalado
- Git (opcional, para clonar o repositório)

### Passos

1. Clone o repositório:
   ```bash
   git clone https://github.com/iLGuilhermeDev/sistema-bancario-gui.git
Acesse o diretório do projeto:

bash
cd sistema-bancario-gui
Compile os arquivos:

bash
javac *.java
Execute a classe principal:

bash
java BancoGUI
Alternativamente, importe o projeto em uma IDE (IntelliJ, Eclipse, NetBeans) e execute BancoGUI.java.

Estrutura do Projeto
text
├── Cliente.java         
├── Transacao.java        
├── Conta.java            
├── ContaCorrente.java    
├── ContaPoupanca.java    
└── BancoGUI.java         
Interface Gráfica
As telas a seguir ilustram o funcionamento do sistema. As imagens estão localizadas na pasta screenshots.

Tela	Descrição

Tela de cadastro.png	Cadastro de novo cliente e escolha do tipo de conta
Área de saque	Operação de saque com validação de saldo
Área de extrato.png	Exibição do histórico completo de transações
Área de saldo.png	Consulta de saldo atual e limite de cheque especial
Histórico de contas.png	Listagem de todas as contas registradas

Observações Técnicas
Precisão monetária: todas as operações utilizam BigDecimal para evitar erros de arredondamento com ponto flutuante.

Tratamento de exceções: o sistema exibe mensagens claras ao usuário em caso de valores inválidos, saldo insuficiente ou conta destino não encontrada.

Atualização dinâmica: após depósitos, saques ou transferências, a exibição do saldo é recarregada automaticamente.

Extrato: requer atualização manual via botão para recarregar a tabela (melhorias futuras podem automatizar).

Limitações Conhecidas
Os dados são armazenados apenas em memória; ao encerrar a aplicação, todas as informações são perdidas.

O rendimento da poupança não é calculado automaticamente (deve ser invocado por método específico).

Não há persistência em arquivo ou banco de dados (previsto para versões futuras).

Próximos Passos
Implementar persistência dos dados (arquivo ou JDBC)

Adicionar rotina automática de rendimento para contas poupança

Melhorar o tratamento de erros com mensagens mais detalhadas

Incluir opção de alteração de senha

Adicionar ícones aos botões para melhor experiência visual

