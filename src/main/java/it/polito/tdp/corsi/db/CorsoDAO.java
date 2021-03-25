package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;

/**
 * METODI che usano le QUERY JDBC
 * di Sequel Pro
 * 
 * @author elena
 *
 */

public class CorsoDAO {

	// La query mi restituisce i corsi in quel periodo, che io metterò nella LIST result
	public List<Corso> getCorsiByPeriodo(Integer periodo){
		String sql = "select * "
				+ "from corso "
				+ "where pd = ?"; //il periodo didattico viene messo dallo user
		List<Corso> result = new ArrayList<Corso>();
		
		//SEMPRE POSSIBILE OTTENERE UN'ECCEZIONE in DAO
		try {
			//Creo connessione
			Connection conn = DBConnect.getConnection();
			//Preparo l'esecuzione dello statement 
			PreparedStatement st = conn.prepareStatement(sql);
			//Al posto dell'1 della query imposto il mio unico
			// parametro (?), ovvero il PERIODO
			st.setInt(1, periodo);
			ResultSet rs = st.executeQuery();
			
			//ITERARE con WHILE il RESULT SET
			//salvare i vari corsi in result
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), 
						rs.getString("nome"), rs.getInt("pd"));
				result.add(c);
			}
				
			//chiudo la connessione 
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e); //PROPAGO L'ECCEZIONE
		}
		
		return result;
	}
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer periodo){
		String sql = "SELECT c.codins, c.nome, c.crediti, c.pd, COUNT(*) as tot "
				+ "FROM corso c, iscrizione i "
				+ "WHERE c.codins = i.codins AND c.pd = ? "
				+ "GROUP BY c.codins, c.nome, c.crediti, c.pd";
		Map<Corso,Integer> result = new HashMap<Corso, Integer>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, periodo);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				Corso c = new Corso(rs.getString("codins"), rs.getInt("crediti"), 
						rs.getString("nome"), rs.getInt("pd"));
				Integer n = rs.getInt("tot");
				result.put(c,n);
			}
			
			rs.close();
			st.close();
			conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	
	
}
