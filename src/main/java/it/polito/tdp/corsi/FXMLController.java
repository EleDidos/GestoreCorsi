/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.corsi;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.corsi.model.Corso;
import it.polito.tdp.corsi.model.Model;
import it.polito.tdp.corsi.model.Studente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPeriodo"
    private TextField txtPeriodo; // Value injected by FXMLLoader

    @FXML // fx:id="txtCorso"
    private TextField txtCorso; // Value injected by FXMLLoader

    @FXML // fx:id="btnCorsiPerPeriodo"
    private Button btnCorsiPerPeriodo; // Value injected by FXMLLoader

    @FXML // fx:id="btnNumeroStudenti"
    private Button btnNumeroStudenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnStudenti"
    private Button btnStudenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnDivisioneStudenti"
    private Button btnDivisioneStudenti; // Value injected by FXMLLoader

    @FXML // fx:id="txtRisultato"
    private TextArea txtRisultato; // Value injected by FXMLLoader

    @FXML
    void corsiPerPeriodo(ActionEvent event) {
    	
    	txtRisultato.clear();
    	
    	//IN UNA TEXT AREA ottengo per forza UNA STRINGA
    	//in questo caso quella stringa = periodo DIDATTICO
    	String periodoStringa = txtPeriodo.getText();
    	//devo provare a convertirla in un numero
    	Integer periodo;
    	
    	// CONTROLLO DELL'INPUT
    	try {
    		periodo = Integer.parseInt(periodoStringa);
    	}catch (NumberFormatException ne) {
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}catch (NullPointerException npe) { //se campo è nullo
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}
    	
    	if(periodo < 1 || periodo > 2) { 
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}
    	
    	List<Corso> corsi = this.model.getCorsiByPeriodo(periodo);
    	
    	/*for(Corso c : corsi) {
    		txtRisultato.appendText(c.toString() + "\n");
    	}*/
    	
    	
    	/************INCOLONNA con stringBuilder e Append***************/
    	StringBuilder sb = new StringBuilder ();
    	for(Corso ci: corsi) {
    		sb.append(String.format("%-8s", ci.getCodins()));
    		// % : segue un PLACE HOLDER
    		// - : quello sche scrive va allineato a sx
    		// n : numero di spazi (larghezza colonna del placeholder)
    		// s : segue una stringa (d : digit/intero)
    		sb.append(String.format("%-8s ", ci.getCodins()));
    		sb.append(String.format("%-4d ", ci.getCrediti()));
    		sb.append(String.format("%-50s ", ci.getNome()));
    		sb.append(String.format("%-4d\n", ci.getPd()));
    		// spazio dopo ognuno per spaziarli e \n sull'ultima
    	}
    	
    	txtRisultato.appendText(sb.toString());
    }

    @FXML
    void numeroStudenti(ActionEvent event) {
    	
    	txtRisultato.clear();
    	
    	String periodoStringa = txtPeriodo.getText();
    	Integer periodo;
    	
    	try {
    		periodo = Integer.parseInt(periodoStringa);
    	}catch (NumberFormatException ne) {
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}catch (NullPointerException npe) {
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}
    	
    	if(periodo < 1 || periodo > 2) {
    		txtRisultato.setText("Devi inserire un numero (1 o 2) per il periodo didattico");
    		return;
    	}
    	
    	Map<Corso, Integer> corsiIscrizioni = this.model.getIscrittiByPeriodo(periodo);
    	for(Corso c : corsiIscrizioni.keySet()) {
    		txtRisultato.appendText(c.toString());
    		Integer n = corsiIscrizioni.get(c);
    		txtRisultato.appendText("\t" + n + "\n");
    	}
    }

    @FXML
    void stampaDivisione(ActionEvent event) {
    	txtRisultato.clear();
    	
    	String codice = txtCorso.getText();
    	if(!model.esisteCorso(codice)) {
    		txtRisultato.appendText("Il corso non esiste");
    		return;
    	}
    	
    	Map <String,Integer> divisione = model.getDivisioneCDS(codice);
    	for(String cds: divisione.keySet()) {
    		txtRisultato.appendText(String.format("%-8s %4d\n", cds,divisione.get(cds)));
    	}
    	
    	
    	
    }

    @FXML
    void stampaStudenti(ActionEvent event) {
    	txtRisultato.clear();
    	
    	String codice = txtCorso.getText();
    	if(!model.esisteCorso(codice)) {
    		txtRisultato.appendText("Il corso non esiste");
    	}
    	
    	List <Studente> studenti = model.getStudentiByCorso(codice);
    	
    	if(studenti.size()==0) {
    		txtRisultato.appendText("Il corso non ha iscritti");
    		return;
    	}
    	
    	for (Studente si: studenti) {
    		txtRisultato.appendText(si+"\n");
    	}
    		
    	
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPeriodo != null : "fx:id=\"txtPeriodo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtCorso != null : "fx:id=\"txtCorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCorsiPerPeriodo != null : "fx:id=\"btnCorsiPerPeriodo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnNumeroStudenti != null : "fx:id=\"btnNumeroStudenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnStudenti != null : "fx:id=\"btnStudenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDivisioneStudenti != null : "fx:id=\"btnDivisioneStudenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtRisultato != null : "fx:id=\"txtRisultato\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	/************ Per allineare definisco STILE SPECIFICO della text Area *******/
    	txtRisultato.setStyle("-fx-font-family: monospace");
    }
    
    
   
    
}