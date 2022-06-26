package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Integer> getChromosomes() {
		String sql = "SELECT DISTINCT Chromosome FROM genes WHERE Chromosome!=0";
		List<Integer> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				result.add(res.getInt("Chromosome"));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public double getArchi(int chromosome1, int chromosome2) {
		String sql = "SELECT SUM(i.Expression_Corr) AS peso "
				+ "FROM interactions i "
				+ "WHERE i.GeneID1 IN (SELECT DISTINCT GeneID "
				+ "FROM genes WHERE Chromosome = ?) "
				+ "AND i.GeneID2 IN (SELECT DISTINCT GeneID "
				+ "FROM genes WHERE Chromosome = ?)";
		double result;
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, chromosome1);
			st.setInt(2, chromosome2);
			ResultSet res = st.executeQuery();
			
			res.first();
			result = res.getDouble("peso");
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public List<Arco> getArchi2() {
		String sql = "SELECT g1.Chromosome, g2.Chromosome, SUM(DISTINCT i.Expression_Corr) AS peso "
				+ "FROM genes g1, genes g2, interactions i "
				+ "WHERE g1.Chromosome!=g2.Chromosome "
				+ "AND g1.Chromosome!=0 AND g2.Chromosome!=0 "
				+ "AND g1.GeneID=i.GeneID1 AND g2.GeneID=i.GeneID2 "
				+ "GROUP BY g1.Chromosome, g2.Chromosome";
		List<Arco> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				result.add(new Arco(res.getInt("g1.Chromosome"), res.getInt("g2.Chromosome"), res.getDouble("peso")));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
}
