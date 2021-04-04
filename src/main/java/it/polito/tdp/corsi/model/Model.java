package it.polito.tdp.corsi.model;

import java.util.HashMap;
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
	
	public List <Studente> getStudentiByCorso(String codice){
		//posso passare solo ciò che mi serve
		return corsoDao.getStudentiByCorso(new Corso(codice,null,null,null));
	}
	
	
	// 1° SOLUZIONE per PUNTO 3: senza passare dal DAO
	// LIMITE: performance + rischio errori
	/*public Map <String,Integer> getDivisioneCDS(String codice) {
		
		Map <String,Integer> divisione = new HashMap <String,Integer> ();
		// 1. recupero studenti da precedente metodo
		List <Studente> studenti = this.getStudentiByCorso(codice);
		// 2. scorro studenti, se CDS c'è già incremento, sennò ne creo uno nuovo con cnt=1
		for(Studente si: studenti) {
			if(si.getCDS()!=null && !si.getCDS().equals("")) { //se ci sono studenti non assegnati a nessun corso non li metto
				if(divisione.get(si.getCDS())==null) {
					divisione.put(si.getCDS(), 1);
				} else {
					divisione.put(si.getCDS(),divisione.get(si.getCDS()+1));
				}
			}
		} //for
		return divisione;
	}*/
	
	// 2° SOLUZIONE per PUNTO 3: passando dal DB 
	// più veloce e codice standard
	public Map <String,Integer> getDivisioneCDS(String codice) {
		return corsoDao.getDivisioneStudenti(new Corso(codice,null,null,null));
	}
	

	
	public boolean esisteCorso(String codice) {
		
		return corsoDao.esisteCorso(new Corso(codice,null,null,null));
	}
	
}
