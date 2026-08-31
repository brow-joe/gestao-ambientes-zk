# 🏫 Sistema de Gestão e Controle de Ocupação de Espaços Acadêmicos

Aplicação web desenvolvida em Java utilizando o ZK Framework e Spring Boot, criada para atender ao case técnico de controle de uso de espaços de ensino, gestão de alunos e monitoramento de taxa de ocupação em tempo real.

---

## 🚀 Funcionalidades

- Gerenciamento de Ambientes: Cadastro, edição e listagem de espaços (Salas de Aula, Laboratórios e Salas de Estudos) com definição de capacidade máxima.
- Gerenciamento de Alunos: CRUD completo para cadastro e manutenção de estudantes.
- Controle de Presença (Ocupação): Registro de entrada e saída de alunos nos ambientes, respeitando os limites de capacidade máxima e validando o status de lotação.
- Interface Fluida (ZK MVVM): Telas reativas construídas totalmente baseadas no padrão MVVM do ZK Framework.
- Autenticação Segura: Sistema de login integrado com Spring Security e persistência de credenciais criptografadas via BCrypt no banco de dados.

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot (Core, Web, Data JPA, Security)
- ZK Framework 10 (Interface UI / MVVM)
- Flyway (Gerenciamento de migrações de banco de dados)
- H2 Database / MySQL (Persistência relacional)
- Maven (Gerenciador de dependências)

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
- JDK 17 ou superior instalado.
- Maven configurado na máquina (ou utilize o wrapper ./mvnw).

### Passos para execução
1. Clone o repositório

2. Execute a aplicação via Maven:
   mvn spring-boot:run

3. Acesse a aplicação no seu navegador:
   http://localhost:8080

---

### 🐳 Como Subir via Docker

1. Construa a imagem do container executando o comando na raiz do projeto:
   ```bash
   docker build -t gestao-zk .
   ```

2. Suba o container mapeando a porta da aplicação:
   ```bash
   docker run -d -p 8080:8080 --name gestao-zk-app gestao-zk
   ```

3. Acesse a aplicação no seu navegador:
   http://localhost:8080

---

### 🔑 Credenciais de Acesso Padrão
- Usuário: admin
- Senha: admin
