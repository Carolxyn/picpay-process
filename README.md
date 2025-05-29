# PicPay Challenge - Sistema de Transações

## 📋 Sobre o Projeto

Sistema de transações financeiras desenvolvido como parte do processo seletivo PicPay para desenvolvedor back-end Java Jr. A aplicação simula um sistema de pagamentos simplificado com funcionalidades de criação de usuários e transferências entre contas.

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JPA**
- **Spring Web**
- **H2 Database** (banco em memória)
- **Lombok**
- **Maven**
- **Spring Boot DevTools**

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

```
src/
├── main/java/br/com/picpay/picpaychallange/
│   ├── Controllers/         # Controladores REST
│   ├── Dto/                # Data Transfer Objects
│   ├── domain/             # Entidades de domínio
│   │   ├── transaction/    # Entidade Transaction
│   │   └── user/          # Entidade User e UserType
│   ├── infra/             # Configurações de infraestrutura
│   ├── repository/        # Repositórios JPA
│   └── services/          # Regras de negócio
```

## 📊 Modelo de Dados

### User (Usuário)
- `id`: Identificador único
- `firstname`: Primeiro nome
- `lastname`: Sobrenome
- `document`: CPF/CNPJ (único)
- `email`: Email (único)
- `password`: Senha
- `balance`: Saldo da conta
- `userType`: Tipo do usuário (COMMON/MERCHANT)

### Transaction (Transação)
- `id`: Identificador único
- `amount`: Valor da transação
- `sender`: Usuário remetente
- `receiver`: Usuário destinatário
- `timestamp`: Data e hora da transação

## 🔧 Funcionalidades

### Usuários
- ✅ Criação de usuários (COMMON/MERCHANT)
- ✅ Listagem de todos os usuários
- ✅ Busca de usuário por ID
- ✅ Proteção de dados sensíveis nas respostas

### Transações
- ✅ Transferência entre usuários
- ✅ Validação de saldo suficiente
- ✅ Validação de tipos de usuário
- ✅ Autorização externa via API
- ✅ Notificação (simulada)

## 📝 Regras de Negócio

1. **Usuários Comerciantes (MERCHANT)**:
   - Não podem enviar dinheiro
   - Apenas recebem transferências

2. **Usuários Comuns (COMMON)**:
   - Podem enviar e receber dinheiro
   - **Não podem transferir para comerciantes**

3. **Validações de Transação**:
   - Saldo suficiente
   - Valor maior que zero
   - Não permite auto-transferência
   - Autorização externa obrigatória

## 🛠️ Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+

### Executando o Projeto

1. **Clone o repositório**:
```bash
git clone <url-do-repositorio>
cd picpaychallange
```

2. **Execute com Maven Wrapper**:
```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

3. **Ou compile e execute**:
```bash
./mvnw clean package
java -jar target/picpaychallange-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: `http://localhost:8080`

### Console do H2 Database
Acesse: `http://localhost:8080/h2-console`
- **URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (vazio)

## 📚 API Endpoints

### Usuários

#### Criar Usuário
```http
POST /users
Content-Type: application/json

{
  "firstname": "João",
  "lastname": "Silva",
  "document": "12345678901",
  "email": "joao@email.com",
  "password": "senha123",
  "balance": 1000.00,
  "userType": "COMMON"
}
```

#### Listar Usuários
```http
GET /users
```

#### Buscar Usuário por ID
```http
GET /users/{id}
```

### Transações

#### Criar Transação
```http
POST /transactions
Content-Type: application/json

{
  "senderId": 1,
  "receiverId": 2,
  "value": 100.00
}
```

## 🧪 Exemplos de Uso

### 1. Criando Usuários

**Usuário Common**:
```json
{
  "firstname": "Maria",
  "lastname": "Santos",
  "document": "11111111111",
  "email": "maria@email.com",
  "password": "123456",
  "balance": 500.00,
  "userType": "COMMON"
}
```

**Usuário Merchant**:
```json
{
  "firstname": "Loja",
  "lastname": "Exemplo",
  "document": "22222222222",
  "email": "loja@email.com",
  "password": "123456",
  "balance": 0.00,
  "userType": "MERCHANT"
}
```

### 2. Realizando Transação

```json
{
  "senderId": 1,
  "receiverId": 2,
  "value": 50.00
}
```

## ⚠️ Tratamento de Erros

A API retorna erros detalhados:

```json
{
  "error": "Saldo insuficiente. Saldo atual: R$ 100.00, Valor solicitado: R$ 200.00"
}
```

Principais validações:
- ❌ Comerciantes não podem enviar dinheiro
- ❌ Usuários comuns não podem transferir para comerciantes
- ❌ Saldo insuficiente
- ❌ Usuário não encontrado
- ❌ Valor inválido
- ❌ Auto-transferência
- ❌ Autorização externa negada

## 🔒 Segurança

- Senhas e documentos são ocultados nas respostas da API
- Validação de tipos de usuário
- Transações são atômicas (@Transactional)

## 🧪 Testes

```bash
./mvnw test
```

## 📦 Dependências Principais

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

## 🌐 APIs Externas

O sistema integra com:
- **Autorização**: `https://run.mocky.io/v3/ad30abce-b1f5-4674-87b3-0cd526154a18`
- **Notificação**: `https://run.mocky.io/v3/ab88dad1-6c90-4d17-b603-05ba3b19a210`

## 🔄 Melhorias Futuras

- [ ] Implementar autenticação JWT
- [ ] Adicionar validação de CPF/CNPJ
- [ ] Implementar histórico de transações
- [ ] Adicionar rate limiting
- [ ] Implementar cache
- [ ] Melhorar cobertura de testes
- [ ] Adicionar documentação Swagger
- [ ] Implementar rollback de transações em caso de falha

## 📄 Licença

Este projeto foi desenvolvido como parte de um desafio técnico.

## 👨‍💻 Desenvolvedor

Projeto desenvolvido para o processo seletivo PicPay - Desenvolvedor Back-end Java Jr.

---

**Nota**: Este é um projeto de demonstração. Em um ambiente de produção, seria necessário implementar medidas adicionais de segurança, como criptografia de senhas, autenticação, autorização mais robusta e validações mais rigorosas.
