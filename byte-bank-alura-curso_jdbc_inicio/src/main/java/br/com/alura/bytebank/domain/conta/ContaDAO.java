package br.com.alura.bytebank.domain.conta;


import br.com.alura.bytebank.domain.cliente.Cliente;
import br.com.alura.bytebank.domain.cliente.DadosCadastroCliente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ContaDAO {

    private Connection conn;

    ContaDAO(Connection connection){
        this.conn = connection;
    }

    public void salvar(DadosAberturaConta dadosDaConta){

        var cliente = new Cliente(dadosDaConta.dadosCliente());
        var conta = new Conta(dadosDaConta.numero(), BigDecimal.ZERO, cliente);

        String sql = "INSERT INTO conta (numero, saldo, cliente_nome, cliente_cpf, cliente_email) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatament = conn.prepareStatement(sql);

            preparedStatament.setInt(1, conta.getNumero());
            preparedStatament.setBigDecimal(2, BigDecimal.ZERO);
            preparedStatament.setString(3, dadosDaConta.dadosCliente().nome());
            preparedStatament.setString(4, dadosDaConta.dadosCliente().cpf());
            preparedStatament.setString(5, dadosDaConta.dadosCliente().email());

            preparedStatament.execute();
            preparedStatament.close();;
            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Conta> listar(){

        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<Conta> contas = new HashSet<>();

        String sql = "SELECT * FROM CONTA";

        try {

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery(sql);

            while (rs.next()){

                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);

                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, saldo, cliente));

            }

            ps.close();
            rs.close();
            conn.close();

        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }

        return  contas;

    }

    public Set<Conta> listar(String nomeInformado){

        PreparedStatement ps = null;
        ResultSet rs = null;
        Set<Conta> contas = new HashSet<>();

        String sql;

        if (nomeInformado != "" && nomeInformado != null) {
            sql = "SELECT * FROM CONTA WHERE CLIENTE_NOME LIKE '" + nomeInformado + "%'";
        }
        else {
            sql = "SELECT * FROM CONTA";
        }

        try {

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery(sql);

            while (rs.next()){

                Integer numero = rs.getInt(1);
                BigDecimal saldo = rs.getBigDecimal(2);
                String nome = rs.getString(3);
                String cpf = rs.getString(4);
                String email = rs.getString(5);

                DadosCadastroCliente dadosCadastroCliente = new DadosCadastroCliente(nome, cpf, email);

                Cliente cliente = new Cliente(dadosCadastroCliente);

                contas.add(new Conta(numero, saldo, cliente));

            }

            ps.close();
            rs.close();
            conn.close();

        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }

        return  contas;

    }

    public void alterar(Integer numero, BigDecimal valor){

        PreparedStatement ps;

        String sql = "UPDATE CONTA SET SALDO = ? WHERE NUMERO = ?";

        try {
            ps = conn.prepareStatement(sql);

            ps.setBigDecimal(1, valor);
            ps.setInt(2, numero);

            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }




    public Conta buscarContaPorNumero(Integer numero) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Conta conta = null;

        String sql = "SELECT * FROM CONTA WHERE NUMERO = " + numero;

        try {

            ps = conn.prepareStatement(sql);

            rs = ps.executeQuery(sql);

            while (rs.next()){

                conta = new Conta(
                        rs.getInt(1),
                        rs.getBigDecimal(2),
                        new Cliente(
                                new DadosCadastroCliente(
                                        rs.getString(3),
                                        rs.getString(4),
                                        rs.getString(5)
                                )
                        )

                );

                conta.setSaldo(BigDecimal.valueOf(Double.valueOf(rs.getString(2))));

            }

            ps.close();
            rs.close();
            conn.close();

        } catch (SQLException e) {
            throw  new RuntimeException(e);
        }

        return  conta;

    }

    public void removeConta(Conta conta) {

        String sql = "DELETE FROM CONTA WHERE NUMERO = ?";

        try {
            PreparedStatement preparedStatament = conn.prepareStatement(sql);

            preparedStatament.setInt(1, conta.getNumero());

            preparedStatament.execute();
            preparedStatament.close();;
            conn.close();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
