package it.polito.tdp.dizionariograph;

import java.util.List;

import it.polito.tdp.dizionariograph.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioGraphController 
{
	private Model model;
	int numeroLettere;

    @FXML
    private TextField txtNumeroLettere;

    @FXML
    private TextField txtParola;

    @FXML
    private Button btnGeneraGrafo;

    @FXML
    private Button btnTrovaVicini;

    @FXML
    private Button btnTrovaGradoMax;

    @FXML
    private TextArea txtResult;

    @FXML
    private Button btnReset;

    @FXML
    void doGeneraGrafo(ActionEvent event) 
    {
    	if (txtNumeroLettere.getText() != "" && txtNumeroLettere.getText().matches("^[0-9]+$"))
    	{
    		try 
    		{
    		numeroLettere = Integer.parseInt(txtNumeroLettere.getText());
    		model.createGraph(numeroLettere);
    		}
    		catch (NumberFormatException nfe) 
    		{
    			txtResult.setText("Inserire un numero corretto di lettere!");
    		}
    	}
    	else
    		txtResult.setText("Numero caratteri non valido!");
    }

    @FXML
    void doReset(ActionEvent event) 
    {
    	txtResult.clear();
    	txtNumeroLettere.clear();
    	txtParola.clear();
    }

    @FXML
    void doTrovaGradoMax(ActionEvent event) 
    {
    	String gradoMax = model.findMaxDegree();
    	txtResult.appendText("\n"+gradoMax);
    }

    @FXML
    void doTrovaVicini(ActionEvent event) 
    {
    	String parola = txtParola.getText();
    	if (parola.length() == numeroLettere)
    	{
    		List<String> vicini = model.displayNeighbours(parola);
    		txtResult.appendText("Lista parole direttamente collegate a " + parola +
    				"\n" + vicini +
    				"\n" + vicini.size() + " elementi compatibili");
    	}
    	else
    		txtResult.appendText("Lunghezza parola errata!");
    }

	public void setModel(Model model) 
	{
		this.model = model;
		
	}

}
