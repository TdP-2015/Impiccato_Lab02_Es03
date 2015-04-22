package it.polito.tdp.impiccato.db;

import it.polito.tdp.impiccato.model.Parola;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object per manipolare la tabella PAROLA del database DIZIONARIO
 * 
 * @author Fulvio Corno
 *
 */
public class ParolaDAO {

	/**
	 * Restituisce in numero di parole presenti nel database
	 * 
	 * @return il numero di parole presenti, oppure -1 se ci sono errori
	 *         nell'esecuzione della query
	 */
	public int contaParole() {

		Connection conn = DBConnect.getConnection();

		String query1 = "select count(id) as numero from parola";

		try {
			Statement st1 = conn.createStatement();
			ResultSet res1 = st1.executeQuery(query1);

			res1.first();
			int numeroParole = res1.getInt("numero");

			res1.close();
			conn.close();

			return numeroParole;

		} catch (SQLException e) {
			return -1;
		}

	}

	/**
	 * Restituisce la parola che si trova nel dizionario alla posizione
	 * specificata. Attenzione: la posizione indica il numero di riga, e non il
	 * campo ID.
	 * 
	 * @param pos
	 *            numero di riga della parola da cercare (compreso tra 0 e
	 *            contaParole()-1)
	 * @return la parola trovata
	 */
	public String findNomeParolaByPos(int pos) {

		Parola p= findParolaByPos(pos) ;
		return p.getNome();

	}
	
	/**
	 * 
	 * @param pos
	 * @return
	 */
	public Parola findParolaByPos(int pos) {

		Connection conn = DBConnect.getConnection();

		String query2 = "select id, nome from parola limit " + pos + ", 1";

		try {

			Statement st2 = conn.createStatement();
			ResultSet res2 = st2.executeQuery(query2);

			Parola p ;
			if (res2.first()) { 
				p = new Parola(
						res2.getInt("id"),
						res2.getString("nome"));
			} else {
				p = null ;
			}

			res2.close();
			conn.close();

			return p;

		} catch (SQLException e) {
			return null;
		}

	}

}
