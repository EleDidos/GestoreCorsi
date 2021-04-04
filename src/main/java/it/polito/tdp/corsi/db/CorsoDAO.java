package it.polito.tdp.corsi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Studente;

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
	
	public List <Studente> getStudentiByCorso(Corso corso){
		String sql = "SELECT s.matricola,s.cognome,s.nome,s.CDS "
				+ "FROM studente s, iscrizione i "
				+ "WHERE s.matricola=i.matricola AND i.codins= ?";
		List <Studente> result = new LinkedList <Studente> ();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Studente s = new Studente (rs.getInt("matricola"),rs.getString("nome"),rs.getString("cognome"),rs.getString("CDS"));
				result.add(s);
			}
			
			rs.close();
			st.close();
			conn.close();
			
			return result;
			
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean esisteCorso(Corso corso) {
		String sql = "SELECT * FROM corso WHERE codins=?";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				//c'è un risultato --> corso esiste
				conn.close();
				return true;
			}else {
				conn.close();
				return false;
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	public Map<String, Integer> getDivisioneStudenti(Corso corso){
		String sql = "SELECT s.CDS, COUNT(*) AS tot "
				+ "FROM studente s, iscrizione i "
				+ "WHERE s.matricola = i.matricola AND i.codins = ? AND s.cds <> '' "
				+ "GROUP BY s.CDS";
		Map<String, Integer> result = new HashMap<String,Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				result.put(rs.getString("CDS"), rs.getInt("tot"));
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
