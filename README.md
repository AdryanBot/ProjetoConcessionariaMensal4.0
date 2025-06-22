# 🚗 Sistema de Gestão de Concessionária

Um sistema completo de gestão para concessionárias de veículos desenvolvido em Java com interface gráfica Swing e integração com a API FIPE.

## 📋 Sobre o Projeto

O **FW Concessionária** é um sistema desktop que permite gerenciar vendas, clientes e veículos de forma integrada, com dados atualizados em tempo real através da API FIPE oficial.

## ✨ Funcionalidades

### 🏪 Gestão de Vendas
- **Nova Venda**: Registro completo de vendas com seleção de cliente e veículo
- **Histórico de Vendas**: Visualização de todas as vendas realizadas
- **Relatórios**: Consulta de vendas por cliente ou veículo

### 👥 Gestão de Clientes
- **Cadastro de Clientes**: Formulário com validação de dados
- **Listagem Completa**: Visualização de todos os clientes cadastrados
- **Remoção Segura**: Exclusão com preservação do histórico de vendas
- **Histórico por Cliente**: Consulta de vendas realizadas por cliente específico

### 🚙 Gestão de Veículos
- **Cadastro Inteligente**: Integração com API FIPE para dados precisos
- **Filtros Avançados**: Busca por marca, modelo, ano e combustível
- **Preços Atualizados**: Valores em tempo real da tabela FIPE
- **Atualização de Preços**: Ferramenta para ajuste manual de valores
- **Pesquisa Parcial**: Busca por nome do modelo

### 🔗 Integração API FIPE
- **Dados Oficiais**: Marcas, modelos e preços da tabela FIPE
- **Filtros Inteligentes**: Apenas modelos disponíveis para ano/combustível selecionado
- **Rate Limiting**: Controle automático de requisições
- **Tratamento de Erros**: Mensagens claras para o usuário

## 🛠️ Tecnologias Utilizadas

- **Java 17+** - Linguagem principal
- **Swing** - Interface gráfica
- **JPA/Hibernate** - Persistência de dados
- **PostgreSQL** - Banco de dados
- **Gson** - Processamento JSON
- **API FIPE** - Dados de veículos
- **Maven** - Gerenciamento de dependências

## 📁 Estrutura do Projeto

```
src/main/java/
├── api/                    # Clientes de APIs externas
│   └── FipeApiClient.java  # Integração com API FIPE
├── controller/             # Controladores de negócio
│   └── Cadastro.java      # Lógica de cadastros
├── entities/              # Entidades JPA
│   ├── Cliente.java       # Modelo de cliente
│   ├── Veiculo.java       # Modelo de veículo base
│   ├── Carro.java         # Modelo de carro
│   ├── Moto.java          # Modelo de moto
│   ├── Caminhao.java      # Modelo de caminhão
│   ├── Vendas.java        # Modelo de venda
│   ├── FipeMarca.java     # Modelo de marca FIPE
│   ├── FipeModelo.java    # Modelo de modelo FIPE
│   └── FipeVeiculo.java   # Modelo de veículo FIPE
├── main/                  # Classe principal
│   └── Main.java          # Ponto de entrada
├── repositories/          # Repositórios de dados
│   ├── ClienteRepository.java
│   ├── VeiculoRepository.java
│   ├── CarroRepository.java
│   ├── MotoRepository.java
│   ├── CaminhaoRepository.java
│   └── VendasRepository.java
├── services/              # Serviços de negócio
│   ├── ClienteService.java
│   ├── VeiculoService.java
│   └── VendasService.java
├── view/                  # Interface gráfica (Views)
│   ├── MainWindow.java    # Janela principal com mensagem inicial
│   ├── SidebarPanel.java  # Menu lateral com hover effects
│   ├── NavigationController.java # Controlador de navegação
│   ├── CadastroPanel.java # Telas de cadastro
│   ├── VendasPanel.java   # Telas de vendas
│   ├── ClientesPanel.java # Telas de clientes
│   ├── VeiculosPanel.java # Telas de veículos
│   └── CadastroVeiculoPanel.java # Cadastro com integração FIPE
└── utils/                 # Utilitários
    ├── JPAUtil.java       # Configuração JPA
    └── TabelaUtils.java   # Utilitários para tabelas
```

## 🚀 Como Executar

### Pré-requisitos
- Java 17 ou superior
- PostgreSQL instalado e configurado
- Maven para gerenciamento de dependências

### Configuração do Banco de Dados

1. Crie um banco PostgreSQL:
```sql
CREATE DATABASE concessionaria;
```

2. Configure as credenciais em `persistence.xml`:
```xml
<property name="javax.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/concessionaria"/>
<property name="javax.persistence.jdbc.user" value="seu_usuario"/>
<property name="javax.persistence.jdbc.password" value="sua_senha"/>
```

### Executando o Projeto

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/sistema-concessionaria.git
cd sistema-concessionaria
```

2. Compile e execute:
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="main.Main"
```

## 💡 Funcionalidades Destacadas

### 🎯 Interface Responsiva
- Layout adaptável a diferentes resoluções
- Componentes redimensionáveis
- Design moderno e intuitivo
- Mensagem de boas-vindas na tela inicial

### 🔄 Atualização em Tempo Real
- Tabelas se atualizam automaticamente após operações
- Sincronização entre diferentes telas
- Feedback visual imediato

### 🛡️ Segurança de Dados
- Validação de entrada de dados
- Preservação do histórico de vendas
- Confirmação para operações críticas

### 🎨 Experiência do Usuário
- Formatação automática de campos (datas no formato dd/mm/aaaa)
- Mensagens de erro claras e informativas
- Navegação intuitiva com menu lateral expansível
- Tela inicial com orientação ao usuário

## 📊 Principais Telas

### 🏠 Tela Principal
- Dashboard com navegação lateral expansível
- Logo da empresa "FW Concessionária" em destaque
- Menu organizado por categorias com hover effects elegantes
- Mensagem inicial orientativa: "Selecione uma opção ao lado"
- Botões sem bordas com efeito hover preto

### 📝 Cadastro de Veículos
- Seleção de tipo, ano e combustível
- Busca automática de marcas via API FIPE com retry inteligente
- Filtros avançados por disponibilidade de ano/combustível
- Exibição de preços atualizados em tempo real
- Tratamento de rate limiting da API FIPE

### 👥 Cadastro de Clientes
- Formulário com validação de dados
- Campo "Data de Nascimento" com placeholder dd/mm/aaaa
- Formatação automática de data durante digitação
- Interface intuitiva e user-friendly

### 💰 Gestão de Vendas
- Nova venda com seleção de cliente e veículo
- Histórico completo de vendas realizadas
- Preservação do histórico mesmo após remoção de itens
- Atualização em tempo real das tabelas

### 📈 Relatórios e Consultas
- Listagem de clientes e veículos cadastrados
- Remoção segura com confirmação
- Tabelas que se atualizam automaticamente
- Dados organizados e bem apresentados

## 🔧 Configurações Avançadas

### Rate Limiting da API FIPE
O sistema implementa controle inteligente de requisições:
- Retry progressivo com delays de 2s, 5s e 10s
- Máximo de 3 tentativas por requisição
- User-Agent configurado para melhor compatibilidade
- Timeout de 15 segundos para conexão e leitura

### Persistência de Dados
- Mapeamento JPA completo
- Relacionamentos entre entidades
- Transações seguras

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 👨‍💻 Autor

**Seu Nome**
- GitHub: [@seu-usuario](https://github.com/seu-usuario)
- LinkedIn: [Seu Perfil](https://linkedin.com/in/seu-perfil)

## 🙏 Agradecimentos

- API FIPE pela disponibilização gratuita dos dados
- Comunidade Java pelo suporte
- Contribuidores do projeto

---

⭐ **Se este projeto foi útil para você, considere dar uma estrela!**