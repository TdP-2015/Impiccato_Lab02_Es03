package it.polito.tdp.impiccato.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ImpiccatoModel {

	private String segreto;
	private int errori;
	private final int MAX_ERRORI = 8;
	private String maschera;

	private Set<String> provate;

	public int getErrori() {
		return errori;
	}

	public int getMAX_ERRORI() {
		return MAX_ERRORI;
	}

	public String getMaschera() {
		return maschera;
	}

	/**
	 * Costruttore, inizializza un modello vuoto
	 */
	public ImpiccatoModel() {
		segreto = null;
		errori = 0;
		provate = new HashSet<String>();
	}

	/**
	 * Inizia una nuova partita
	 * 
	 * @param segreto
	 *            La parola da indovinare
	 */
	public void startGame(String segreto) {
		this.segreto = segreto.toUpperCase();

		errori = 0;
		provate.clear();

		maschera = "";
		for (int i = 0; i < segreto.length(); i++)
			maschera = maschera + "_";
	}

	/**
	 * Controlla se la lettera specificata fa parte della parola. Lettere non
	 * esistenti oppure lettere ripetute sono valutate come errori.
	 * 
	 * @param t
	 *            La lettera da provare
	 * @return true se la lettera esiste, false se non esiste oppure se era già
	 *         stata detta
	 */
	public boolean tryLetter(String t) {
		t = t.toUpperCase();
		// già detta?
		if (provate.contains(t)) {
			errori++;
			return false;
		}

		provate.add(t);

		// presente?
		if (segreto.contains(t)) {
			// presente

			for (int i = 0; i < segreto.length(); i++) {
				if (segreto.charAt(i) == t.charAt(0)) {
					maschera = maschera.substring(0, i) + t
							+ maschera.substring(i + 1);
				}
			}

			return true;
		} else {
			// assente
			errori++;
			return false;
		}
	}

	/**
	 * Determina se la parola è stata indovinata
	 * 
	 * @return true se è indovinata, false se vi sono ancora lettere da
	 *         indovinare
	 */
	public boolean isWinner() {
		return (!maschera.contains("_"));
	}

	/**
	 * Determina se hai perso la partita
	 * 
	 * @return true se hai perso, false se sei ancora in gioco
	 */
	public boolean isLoser() {
		return (errori >= MAX_ERRORI);
	}

	/**
	 * Genera una nuova parola segreta, estraendola casualmente dal Dizionario
	 * memorizzato nel database
	 * 
	 * @return una parola casuale
	 */
	public String nuovoSegreto() {
		String url = "jdbc:mysql://localhost/dizionario?user=root";

		try {
			Connection conn = DriverManager.getConnection(url);

			String query1 = "select count(id) as numero from parola";

			Statement st1 = conn.createStatement();
			ResultSet res1 = st1.executeQuery(query1);

			res1.first();
			int numeroParole = res1.getInt("numero");

			res1.close();

			int indiceParola = (int) (Math.random() * numeroParole);

			String query2 = "select nome from parola limit " + indiceParola
					+ ", 1";

			/*
			String query2 = String.format(
					"select nome from parola limit %d, 1", 
					indiceParola) ; */
			
			Statement st2 = conn.createStatement() ;
			ResultSet res2 = st2.executeQuery(query2) ;
			
			res2.first() ;
			String segreto = res2.getString("nome") ;
			
			res2.close();
			
			conn.close();
			
			return segreto.toUpperCase() ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;

	}
}
