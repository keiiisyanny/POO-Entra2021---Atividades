package model.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Banco;
import model.entidade.Pesquisador;
import model.entidade.Vacina;

public class VacinaRepository {
	
	public Vacina inserir(Vacina novaVacina) {
		Connection conexao = Banco.getConnection();
		
		String sql = " INSERT INTO vacina(PAIS_ORIGEM, ESTAGIO_PESQUISA, "
					 + "                  DATA_INICIO_PESQUISA, IDRESPONSAVEL) "
				     + " VALUES (?, ?, ?, ?) ";
		PreparedStatement query = Banco.getPreparedStatementWithPk(conexao, sql);
		try {
			query.setString(1, novaVacina.getPaisOrigem());
			query.setInt(2, novaVacina.getEstagioPesquisa());
			query.setDate(3, new java.sql.Date(novaVacina.getDataInicioPesquisa().getTime()));
			query.setInt(4, novaVacina.getResponsavel().getId());
			query.execute();
			
			ResultSet chavesGeradas = query.getGeneratedKeys();
			if(chavesGeradas.next()) {
				novaVacina.setId(chavesGeradas.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao inserir vacina.\nCausa: " + e.getCause());
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return novaVacina;
	}
	
	public boolean atualizar(Vacina vacina) {
		Connection conexao = Banco.getConnection();
		boolean atualizou = false;
		
		String sql = " UPDATE VACINA SET "
				   + " PAIS_ORIGEM = ?, ESTAGIO_PESQUISA = ?, "
				   + " DATA_INICIO_PESQUISA = ?, IDRESPONSAVEL = ? "
				   + " WHERE ID = ? ";
		PreparedStatement query = Banco.getPreparedStatement(conexao, sql);
		
		try {
			query.setString(1, vacina.getPaisOrigem());
			query.setInt(2, vacina.getEstagioPesquisa());
			query.setDate(3, new java.sql.Date(vacina.getDataInicioPesquisa().getTime()));
			query.setInt(4, vacina.getResponsavel().getId());
			query.setInt(5, vacina.getId());
			
			int linhasAfetadas = query.executeUpdate();
			atualizou = linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar vacina.\nCausa: " + e.getCause());
		}finally {
			Banco.closePreparedStatement(query);
			Banco.closeConnection(conexao);
		}
		
		return atualizou;
	}
	
	//Delete
	public boolean excluir(int id) {
		boolean excluiu = false;
		
		//Conectar no banco
		Connection conexao = Banco.getConnection();
		String sql = " DELETE FROM vacina "
				+ " WHERE idVacina = ? ";
		//Obter o preparedStatement
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			//Executar
			stmt.setInt(1, id);
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
	
	//Retrieve
	public Vacina pesquisarPorId(int id) {
		Vacina vacinaBuscada = null;
		Connection conexao = Banco.getConnection();
		String sql = " SELECT * FROM vacina "
				   + " WHERE idVacina = ? ";
		
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		try {
			stmt.setInt(1, id);
			ResultSet resultado = stmt.executeQuery();
			
			if(resultado.next()) {
				vacinaBuscada = new Vacina();
				vacinaBuscada.setId(resultado.getInt("idVacina"));
				vacinaBuscada.setEstagioPesquisa(resultado.getInt("estagio_pesquisa"));
				vacinaBuscada.setPaisOrigem(resultado.getString("pais_origem"));
				vacinaBuscada.setDataInicioPesquisa(resultado.getDate("data_inicio_pesquisa"));
				
				int idResponsavel = resultado.getInt("idResponsavel");
				PesquisadorRepository pesquisadorRepository = new PesquisadorRepository();
				Pesquisador responsavelbuscado = pesquisadorRepository.consultaPorId(idResponsavel);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar vacina com id = " + id + " .\nCausa: " 
								+ e.getCause());
		} finally {
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conexao);
		}
		
		return vacinaBuscada;
	}
	
        public ArrayList<Vacina> pesquisarTodas(){
        Connection conexao = Banco.getConnection();
        String sql = "SELECT * FROM vacina ";
        ArrayList<Vacina> todasVacinas = new ArrayList<Vacina>();
                
		PreparedStatement stmt = Banco.getPreparedStatement(conexao, sql);
		
		try {
			ResultSet resultado = stmt.executeQuery();
			
			while(resultado.next()) {
				Vacina vacinaBuscada = new Vacina();
				vacinaBuscada.setId(resultado.getInt("idVacina"));
				vacinaBuscada.setEstagioPesquisa(resultado.getInt("estagio_pesquisa"));
				vacinaBuscada.setPaisOrigem(resultado.getString("pais_origem"));
				vacinaBuscada.setDataInicioPesquisa(resultado.getDate("data_inicio_pesquisa"));
				todasVacinas.add(vacinaBuscada);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return todasVacinas
				;
	}

	private void vacinaBuscadas() {
		// TODO Auto-generated method stub
		
	}
	
	//
	
}
