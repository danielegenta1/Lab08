package it.polito.tdp.dizionariograph.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDAO {

	/*
	 * Ritorna tutte le parole di una data lunghezza
	 */
	public List<String> getAllWordsFixedLength(int length)
	{
		String sql = "SELECT nome FROM parola WHERE LENGTH(nome) = ?;";
		List<String> parole = new ArrayList<String>();

		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, length);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				parole.add(res.getString("nome"));
			}

			conn.close();
			return parole;

		} catch (SQLException e) 
		{
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> findNeighbours(String aus)
	{
		String sql = "SELECT nome FROM parola WHERE nome LIKE ?";
		List<String> vicini = new ArrayList<String>();

		try 
		{
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, aus);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				vicini.add(res.getString("nome"));
			}

			conn.close();
			return vicini;

		} catch (SQLException e) 
		{
			e.printStackTrace();
			throw new RuntimeException("Error Connection Database");
		}
	}

}
