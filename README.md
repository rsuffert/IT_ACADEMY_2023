## Estrutura de arquivos e diretórios

- `src`: folder que contém os códigos-fonte.
- `bin`: folder que contém os códigos-fonte compilados (bytecodes).
- `docs`: folder que contém os arquivos HTML com a documentação do programa, gerados no formato Javadoc.
- `.vscode`: folder com as configurações do ambiente.
- `DNIT-Distancias.csv`: arquivo com a base de dados - representada por uma matriz - das distâncias entre as capitias do Brasil.

## Executando o programa

Para executar o programa, deve ser executado o arquivo `App.java`.

## Outras informações sobre o programa:
- É explorado o conceito do paradigma orientado a objetos, com a criação de três classes: 
`App`, `Item` e `Transportation`. A primeira é a classe principal do programa, que 
deve ser executada para rodá-lo. Nela, estão os métodos (funções) para ler o arquivo 
CSV, interagir com o usuário e simular o problema proposto. A segunda classe é apenas 
uma representação dos itens que compõem os objetos da terceira classe, que, por sua 
vez, representa os transportes das companhias de carga. Enquanto a segunda classe 
conta apenas com métodos (funções) de acesso para as variáveis (getters), a terceira 
apresenta métodos (funções) mais robustos, como para definir qual a distribuição de 
caminhões (pequenos, médios e grandes) mais econômica para cada situação de 
transporte e calcular o custo total dos transportes.
- A interação com o usuário (entrada e saída) é feita exclusivamente por meio de interface gráfica do Java, sem nenhuma informação relevante ao usuário ser exibida no terminal.

## PARA MAIS INFORMAÇÕES, VER O ARQUIVO PDF DO RELATÓRIO DA SOLUÇÃO PROPOSTA.
