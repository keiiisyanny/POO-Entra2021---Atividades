package model.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Banco;
import model.entidade.Pesquisador;
import model.entidade.Vacina;

public class PesquisadorRepository {

	public Pesquisador inserir(Pesquisador novoPesquisador) {
		Connection conexao = Banco.getConnection();
		
		String sql = " INSERT INTO vacina(NOME, CPF, "
					 + " MATRICULA, DATA_NASCIMENTO) "
				     + " VALUES (?, ?, ?, ?) ";
		PreparedStatement query = Banco.getPreparedStatementWithPk(conexao, sql);
		try {
			query.setString(1, novoPesquisador.getNome());
			query.setString(2, novoPesquisador.getCpf());
			query.setInt(3, novoPesquisador.getMatricula());
			query.setDate(4, new java.sql.Date(novoPesquisador.getDataNascimento().getTime()));
			query.execute();
			
			ResultSet chavesGeradas = query.getGeneratedKeys();
			if(chavesGeradas.next()) {
				novoPesquisador.setId(chavesGeradas.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao inserir vacina.\nCausa: " + e.getCause());
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return novoPesquisador;
	}
	
	public Pesquisador consultaPorId (int id) {
		Pesquisador pesquisadorBuscada = null;
		Connection conexao = Banco.getConnection();
		String sql = " SELECT * FROM vacina "
				   + " WHERE idVacina = ? ";
		
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			stmt.setInt(1, id);
			ResultSet resultado = stmt.executeQuery();
			
			if(resultado.next()) {
				pesquisadorBuscada = new Pesquisador();
				pesquisadorBuscada.setId(resultado.getInt("idPesquisador"));
				pesquisadorBuscada.setNome(resultado.getString("nome"));
				pesquisadorBuscada.setCpf(resultado.getString("cpf"));
				pesquisadorBuscada.setMatricula(resultado.getInt("matricula"));
				pesquisadorBuscada.setDataNascimento(resultado.getDate("data_nascimento"));
							}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar vacina com id = " + id + " .\nCausa: " 
								+ e.getCause());
		} finally {
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conexao);
		}
		
		return pesquisadorBuscada;
	}
	//UPDATE
	public boolean atualizar(Pesquisador pesquisador) {
		Connection conexao = Banco.getConnection();
		boolean atualizou = false;
	String sql = " UPDATE pesquisador SET " 
				+ " NOME = ?, CPF = ?, "
				+ "MATRICULA = ?, DATA_NASCIMENTO = ? "
				+ "WHERE IDPESQUISADOR = ? ";
	PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
	
	try {
		query.setString(1, pesquisador.getNome());
		query.setString(2, pesquisador.getCpf());
		query.setInt(3, pesquisador.getMatricula());
		query.setDate(4, new java.sql.Date(pesquisador.getDataNascimento().getTime()));
		query.setInt(5, pesquisador.getId());
		
		int linhasAfetadas = query.executeUpdate();
		atualizou = linhasAfetadas > 0;
	} catch (SQLException e) {
		System.out.println("Erro ao atualizar pesquisador.\nCausa: " + e.getCause());
	}finally {
		Banco.closePreparedStatement(query);
		Banco.closeConnection(conexao);
	}
	
	return atualizou;
}
	//Delete
		public boolean excluir(int idPesquisador) {
			boolean excluiu = false;
			
			//Conectar no banco
			Connection conexao = Banco.getConnection();
			String sql = " DELETE FROM pesquisador "
					+ " WHERE idPesquisador = ? ";
			//Obter o preparedStatement
			PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
			try {
				//Executar
				stmt.setInt(1, idPesquisador);
				int registrosExcluidos = stmt.executeUpdate();
				excluiu = (registrosExcluidos > 0);
			} catch (SQLException e) {
				System.out.println("Erro ao excluir vacina.\nCausa: " + e.getCause());
			} finally {
				Banco.closePreparedStatement(stmt);
				Banco.closeConnection(conexao);
			}
			
			return excluiu;
		}
		
	}
	
