package it.polito.tdp.dizionariograph.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.dizionariograph.db.WordDAO;


public class Model 
{

		
	//grafo orientato non pesato
	private Graph<String, DefaultEdge> grafo;
	//private Map<String, String>backVisit;
	
	
	

	public void createGraph(int numeroLettere) 
	{
		/*
		 * Creo grafo che abbia:
		 * - Vertici: tutte le parole del dizionario di una certa lunghezza
		 * - Archi: se parola differisce da un'altra parola per una lettera
		 */
		
		WordDAO dao = new WordDAO();
		List<String> wordsFixedLength = dao.getAllWordsFixedLength(numeroLettere);
		
		
		// creo oggetto grafo
		this.grafo = new SimpleDirectedGraph<>(DefaultEdge.class);
		// creo i vertici del grafo
		Graphs.addAllVertices(this.grafo, wordsFixedLength);
		
		// Debug - creo dummy grafo
		//vertici
		/*grafo.addVertex("casa");
		grafo.addVertex("cara");
		grafo.addVertex("case");
		grafo.addVertex("caro");
		grafo.addVertex("care");
		grafo.addVertex("cura");
		grafo.addVertex("fila");
		grafo.addVertex("file");
		grafo.addVertex("pile");*/
		
		//creo gli archi
		//M1 - Via SQL - troppe query 
		// 2 lettere ci mette < 1 minuti
		// 4 lettere ci può mettere 10 minuti
		/*String aus = "";
		for (String parolaInserita : grafo.vertexSet())
		{
			for (int i = 1; i <= parolaInserita.length(); i++)
			{
				 aus = parolaInserita.substring(0, i-1) + '_' + parolaInserita.substring(i, parolaInserita.length());
				
				 List<String> neighbours =dao.findNeighbours(aus);
				 
				 //creo archi
				 for (String neighbour : neighbours)
				 {
					 if (parolaInserita.compareTo(neighbour) != 0)
						 this.grafo.addEdge(parolaInserita, neighbour);
				 }
			}
		}*/
		
		//M2 - via Java - molto più veloce
		for (String parola : grafo.vertexSet())
		{
			for (String possibileVicino : grafo.vertexSet())
			{
				//devo controllare se stringe sono simile
				//controllo se non esiste già arco
				if (similari(parola, possibileVicino))
				{
					if (grafo.getAllEdges(parola, possibileVicino).size() == 0 && grafo.getAllEdges(possibileVicino, parola).size() == 0)
						this.grafo.addEdge(parola, possibileVicino);
				}
			}
		}
		
		System.out.println(grafo.edgeSet());
	}
	
	private boolean similari(String word1, String word2) 
	{
		boolean primaDifferenza = true;
	    if(word1.equals(word2))//non voglio loop nel grafo
	        return false;

        for(int i = 0; i < word1.length(); i++) 
        {
            if(word1.charAt(i) != word2.charAt(i))
            { 
            	if (primaDifferenza)
            		primaDifferenza = false;
            	else
            		return false;
            }
        }
        return true;
	}

	//voglio trovare solo i vicini della parola inserita (analisi per ampiezza)
	// POSSO SOSTITUIRE TUTTO QUESTO (VISITA IN AMPIEZZA CON UN METODO) con un metodo apposito -> vedi sol. prof
	public List<String> displayNeighbours(String parolaInserita) 
	{
		/*// visita per ampiezza
		ArrayList<String> visitaCompletaAlbero = new ArrayList<String>();
		backVisit = new HashMap<>() ;
		//visita completa - io voglio solo primo grado
		if (grafo.containsVertex(parolaInserita))
		{
			GraphIterator<String, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo, parolaInserita);
			it.addTraversalListener(new EdgeTraversedGraphListener());
			
			backVisit.put(parolaInserita, null); //devo inserire manualmente il punto di partenza (che non ha padre -> null)
			while(it.hasNext())
			{
				visitaCompletaAlbero.add(it.next()) ;
			}
			
			//soluzione tampone - vicini di grado 1
			List<String>result = new ArrayList<String>();
			
			Set<DefaultEdge> set = grafo.edgesOf(parolaInserita);
			for (DefaultEdge e : set)
			{
				result.add(grafo.getEdgeTarget(e));
			}
			return result;
		}
		else
			return null;*/
		
		//if (numeroLettere != parolaInserita.length())
		//	throw new RuntimeException("La parola inserita ha una lunghezza differente rispetto al numero inserito.");

		List<String> parole = new ArrayList<String>();

		// Ottengo la lista dei vicini di un vertice
		parole.addAll(Graphs.neighborListOf(this.grafo, parolaInserita));

		// Ritorno la lista dei vicini
		return parole;
	}

	public String findMaxDegree() 
	{
		int gradoMax = 0;
		String verticeMax = "";
		for (String v : grafo.vertexSet())
		{
			if (grafo.degreeOf(v) > gradoMax)
			{
				gradoMax = grafo.degreeOf(v);
				verticeMax = v;
			}
		}
		// POSSO SOSTITUIRE TUTTO QUESTO (VISITA IN AMPIEZZA CON UN METODO) con un metodo apposito -> vedi sol. prof
		if (verticeMax.compareTo("") != 0)
		{
			List<String>vicini = displayNeighbours(verticeMax);
			return verticeMax + " grado : " + gradoMax + "\nvicini: " + vicini;
		}
		return null;
	}
}
