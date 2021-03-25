package it.polito.tdp.corsi.model;

import java.util.List;
import java.util.Map;

import it.polito.tdp.corsi.db.CorsoDAO;

/**
 * INTERFACCIA GRAFICA --> CONTROLLORE --> MODELLO --> DAO (per JDBC functions)
 * @author elena
 *
 */

public class Model {
	
	//RIFERIMENTO AL DAO creato nel costruttore del modello
	private CorsoDAO corsoDao;
	
	public Model() {
		corsoDao = new CorsoDAO();
	}
	
	//FUNZIONI che usano le query JDBC
	public List<Corso> getCorsiByPeriodo(Integer pd){
		return corsoDao.getCorsiByPeriodo(pd);
	}
	
	public Map<Corso,Integer> getIscrittiByPeriodo(Integer pd){
		return corsoDao.getIscrittiByPeriodo(pd);
	}
	
}
