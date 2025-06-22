package controller;

import repositories.*;
import services.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import jakarta.persistence.*;
import entities.*;
import utils.JPAUtil;

/**
 * Controlador responsável pelas operações de cadastro, consulta e remoção
 * de veículos, clientes e vendas do sistema.
 */
public class Cadastro {

    /**
     * Adiciona um veículo ao sistema baseado no tipo especificado.
     * @param tipoVeiculo Tipo do veículo (cars, motorcycles, trucks)
     * @param marca Marca do veículo
     * @param modelo Modelo do veículo
     * @param ano Ano do veículo
     * @param codigoFipe Código FIPE
     * @param preco Preço do veículo
     * @param combustivel Tipo de combustível
     * @param acronCombustivel Acrônimo do combustível
     * @param mesReferencia Mês de referência da tabela FIPE
     */
    public void adicionarVeiculo(String tipoVeiculo, String marca, String modelo, String ano, String codigoFipe, String preco, String combustivel, String acronCombustivel, String mesReferencia){
        if (tipoVeiculo.equals("cars")) {
            Carro carro = new Carro(1, preco, marca, modelo, ano, combustivel, codigoFipe, mesReferencia, acronCombustivel);
            new CarroRepository().salvar(carro);
        } else if (tipoVeiculo.equals("motorcycles")) {
            Moto moto = new Moto(2, preco, marca, modelo, ano, combustivel, codigoFipe, mesReferencia, acronCombustivel);
            new MotoRepository().salvar(moto);
        } else {
            Caminhao caminhao = new Caminhao(3, preco, marca, modelo, ano, combustivel, codigoFipe, mesReferencia, acronCombustivel);
            new CaminhaoRepository().salvar(caminhao);
        }

        mostrarVeiculos(); // Exibe todos os veículos após adicionar
    }

    /**
     * Atualiza o preço de um veículo específico.
     */
    public void atualizarPreco() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o ID do veículo que deseja atualizar o preço: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // limpa o buffer

        VeiculoService veiculoServ = new VeiculoService();
        Veiculo veiculo = veiculoServ.buscarPorId(id);

        if (veiculo != null) {
            System.out.print("Digite o novo preço com as siglas (ex: R$ 99.999): ");
            String novoPreco = scanner.nextLine();
            veiculo.setPreco(novoPreco);

            // Atualiza no banco de dados com merge
            EntityManager em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(veiculo);
            em.getTransaction().commit();
            em.close();

            System.out.println("Preço atualizado com sucesso!");
        } else {
            System.out.println("Veículo com ID " + id + " não encontrado.");
        }
    }

    /**
     * Busca veículos por nome parcial do modelo.
     */
    public void pesquisaParcial(){
        Scanner scanner = new Scanner(System.in);
        VeiculoService service = new VeiculoService();
        System.out.println("Digite o nome completo ou parcial do veiculo que deseja procurar: ");
        String pesquisa = scanner.nextLine();
        List<Veiculo> resultados = service.buscarPorModelo(pesquisa);

        // Exibe cada veículo encontrado
        for (Veiculo v : resultados) {
            System.out.println("--------------------------------------------------------");
            System.out.println("Id: " + v.getId());
            System.out.println("Tipo: " + (v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão"));
            System.out.println("Marca: " + v.getMarca());
            System.out.println("Modelo: " + v.getModelo());
            System.out.println("Ano: " + v.getAno());
            System.out.println("Preço: " + v.getPreco());
            System.out.println("Combustível: " + v.getCombustivel());
            System.out.println("Código Fipe: " + v.getCodigoFipe());
            System.out.println("Mês de Referência: " + v.getMesReferencia());
            System.out.println("Acrônimo Combustível: " + v.getAcronCombustivel());
        }
    }

    /**
     * Lista todos os veículos cadastrados no sistema.
     */
    public void mostrarVeiculos() {
        VeiculoService vs = new VeiculoService();
        List<Veiculo> listaVeiculos = vs.listarTodos();

        if (listaVeiculos.isEmpty()) {
            System.out.println("Nenhum veiculo adicionado ainda!");
        } else {
            System.out.println("Total de veículos cadastrados: " + vs.contarVeiculos()); // Exibe total de veículos
            for (Veiculo v : listaVeiculos) {
                System.out.println("--------------------------------------------------------");
                System.out.println("Id: " + v.getId());
                System.out.println("Tipo: " + (v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão"));
                System.out.println("Marca: " + v.getMarca());
                System.out.println("Modelo: " + v.getModelo());
                System.out.println("Ano: " + v.getAno());
                System.out.println("Preço: " + v.getPreco());
                System.out.println("Combustível: " + v.getCombustivel());
                System.out.println("Código Fipe: " + v.getCodigoFipe());
                System.out.println("Mês de Referência: " + v.getMesReferencia());
                System.out.println("Acrônimo Combustível: " + v.getAcronCombustivel());
            }
        }
    }

    /**
     * Remove um veículo do sistema pelo ID.
     */
    public void removerVeiculo(){
        mostrarVeiculos(); // Exibe todos os veículos antes de remover
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do veículo que deseja remover: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            EntityManager em = JPAUtil.getEntityManager();
            Veiculo veiculo = em.find(Veiculo.class, id);

            if (veiculo != null) {
                em.getTransaction().begin();
                em.remove(veiculo);
                em.getTransaction().commit();
                System.out.println("Veículo removido com sucesso!");
            } else {
                System.out.println("Veículo com ID " + id + " não encontrado.");
            }

            em.close();

        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
        } catch (Exception e) {
            System.out.println("Erro ao remover veículo: " + e.getMessage());
        }
    }

    /**
     * Adiciona um novo cliente ao sistema.
     * @param cpf CPF do cliente
     * @param dataNascimento Data de nascimento
     * @param nomeCliente Nome do cliente
     */
    public void adicionarCliente(String cpf, String dataNascimento, String nomeCliente){
        Cliente cliente = new Cliente(nomeCliente, cpf, dataNascimento);
        new ClienteRepository().salvar(cliente);
        mostrarClientes(); // Mostra todos os clientes após cadastro
    }

    /**
     * Lista todos os clientes cadastrados.
     */
    public void mostrarClientes(){
        ClienteService vs = new ClienteService();
        List<Cliente> listaClientes = vs.listarTodos();

        if(listaClientes.isEmpty()){
            System.out.println("Nenhum cliente adicionado ainda!");
        } else {
            System.out.println("Total de clientes cadastrados: " + vs.contarClientes()); // Exibe total
            for(Cliente c: listaClientes){
                System.out.println("--------------------------------------------------------");
                System.out.println("Id: " + c.getId());
                System.out.println("Nome: " + c.getNome());
                System.out.println("cpf: " + c.getCpf());
                System.out.println("Data de nascimento: " + c.getDateB());
            }
        }
    }

    /**
     * Remove um cliente do sistema pelo ID.
     */
    public void deletarCliente(){
        mostrarClientes();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite o ID do cliente que deseja remover: ");

        try {
            Long id = Long.parseLong(scanner.nextLine());
            EntityManager em = JPAUtil.getEntityManager();
            Cliente cliente = em.find(Cliente.class, id);

            if (cliente != null) {
                em.getTransaction().begin();
                em.remove(cliente);
                em.getTransaction().commit();
                System.out.println("Cliente removido com sucesso!");
            } else {
                System.out.println("Cliente com ID " + id + " não encontrado.");
            }

            em.close();

        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Por favor, digite um número.");
        } catch (Exception e) {
            System.out.println("Erro ao remover Cliente: " + e.getMessage());
        }
    }

    /**
     * Registra uma nova venda associando cliente e veículo.
     */
    public void adicionarVenda() {
        Scanner scanner = new Scanner(System.in);

        mostrarClientes(); // Lista clientes para escolher
        System.out.print("Digite o ID do cliente: ");
        Long idCliente = Long.parseLong(scanner.nextLine());

        mostrarVeiculos(); // Lista veículos para escolher
        System.out.print("Digite o ID do veículo: ");
        Long idVeiculo = Long.parseLong(scanner.nextLine());

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Cliente cliente = em.find(Cliente.class, idCliente);
            Veiculo veiculo = em.find(Veiculo.class, idVeiculo);

            if (cliente == null || veiculo == null) {
                System.out.println("Cliente ou veículo não encontrado.");
                return;
            }

            // Cria objeto de venda e preenche os dados
            Vendas venda = new Vendas();
            venda.setCliente(cliente);
            venda.setVeiculo(veiculo);
            venda.setModeloVeiculo(veiculo.getModelo());
            venda.setMarcaVeiculo(veiculo.getMarca());
            venda.setPrecoVeiculo(veiculo.getPreco());
            venda.setNomeCliente(cliente.getNome());

            // Persiste no banco
            em.getTransaction().begin();
            em.persist(venda);
            em.getTransaction().commit();

            System.out.println("Venda registrada com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao registrar venda: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    // Lista todas as vendas realizadas
    public void mostrarVendas() {
        VendasService vendaService = new VendasService();
        List<Vendas> listaVendas = VendasService.listarTodas();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        if (listaVendas.isEmpty()) {
            System.out.println("Nenhuma venda registrada.");
        } else {
            System.out.println("Total de vendas realizadas: " + vendaService.contarVendas());
            for (Vendas v : listaVendas) {
                System.out.println("--------------------------------------------------------");
                System.out.println("ID da Venda: " + v.getId());
                System.out.println("Cliente: " + v.getNomeCliente());
                System.out.println("Veículo: " + v.getModeloVeiculo() + " - " + v.getMarcaVeiculo());
                System.out.println("Preço: " + v.getPrecoVeiculo());
                System.out.println("Data: " + v.getDataVenda().format(formatter));
            }
        }
    }

    // Pesquisa vendas dentro de um intervalo de datas
    public void vendaPorData(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        Scanner scanner = new Scanner(System.in);
        VendasService service = new VendasService();

        // Coleta da data inicial
        System.out.println("Data de inicio do periodo:");
        System.out.print("Dia: "); int DiaI = scanner.nextInt(); scanner.nextLine();
        System.out.print("Mes: "); int MesI = scanner.nextInt(); scanner.nextLine();
        System.out.print("Ano: "); int AnoI = scanner.nextInt(); scanner.nextLine();

        // Coleta da data final
        System.out.println("Data do fim do periodo:");
        System.out.print("Dia: "); int DiaF = scanner.nextInt(); scanner.nextLine();
        System.out.print("Mes: "); int MesF = scanner.nextInt(); scanner.nextLine();
        System.out.print("Ano: "); int AnoF = scanner.nextInt(); scanner.nextLine();

        // Cria objetos de data
        LocalDateTime dataInicio = LocalDate.of(AnoI, MesI, DiaI).atStartOfDay();
        LocalDateTime dataFim = LocalDate.of(AnoF, MesF, DiaF).atTime(LocalTime.MAX);

        // Busca vendas no período
        List<Vendas> vendas = service.buscarPorPeriodo(dataInicio, dataFim);

        // Exibe as vendas
        System.out.println("Vendas entre " + dataInicio + " e " + dataFim + ":");
        for (Vendas v : vendas) {
            System.out.println("--------------------------------------------------------");
            System.out.println("ID da Venda: " + v.getId());
            System.out.println("Cliente: " + v.getNomeCliente());
            System.out.println("Veículo: " + v.getModeloVeiculo() + " - " + v.getMarcaVeiculo());
            System.out.println("Preço: " + v.getPrecoVeiculo());
            System.out.println("Data: " + v.getDataVenda().format(formatter));
        }
    }

    // Mostra vendas feitas por um cliente específico


    // Mostra clientes que compraram um veículo específico
    public void mostrarClientesPorVeiculo() {
        VendasService vendasService = new VendasService();
        vendasService.pedirIdVeiculo();
    }
}
