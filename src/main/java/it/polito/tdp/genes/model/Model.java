package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private GenesDao dao;
	
	private List<Integer> best;
	
	public Model() {
		this.dao = new GenesDao();
	}
	
	public String creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getChromosomes());
		
//		for(Integer c1 : grafo.vertexSet()) {
//			for(Integer c2 : grafo.vertexSet()) {
//				if(c1!=c2) {
//					double peso = dao.getArchi(c1, c2);
//					if(peso != 0.0) {
//						Graphs.addEdge(grafo, c1, c2, peso);
//					}
//				}
//			}
//		}
		
		for(Arco a : dao.getArchi2()) {
			Graphs.addEdge(grafo, a.getChromosome1(), a.getChromosome2(), a.getPeso());
		}
		
		return "Grafo creato: "+grafo.vertexSet().size()+" vertici, "
		+grafo.edgeSet().size()+" archi";
	}
	
	public double getPesoMin() {
		double min = 100;
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) < min) {
				min = grafo.getEdgeWeight(e);
			}
		}
		return min;
	}
	
	public double getPesoMax() {
		double max = -100;
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) > max) {
				max = grafo.getEdgeWeight(e);
			}
		}
		return max;
	}
	
	public int getArchiPesoMinore(double soglia) {
		int count = 0;
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) < soglia) {
				count++;
			}
		}
		return count;
	}
	
	public int getArchiPesoMaggiore(double soglia) {
		int count = 0;
		for(DefaultWeightedEdge e : grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e) > soglia) {
				count++;
			}
		}
		return count;
	}
	
	public List<Integer> ricercaCammino(double soglia) {
		this.best = new ArrayList<>();
		List<Integer> parziale = new ArrayList<>();
		for(Integer i : grafo.vertexSet()) {
			parziale.add(i);
			cerca(parziale, soglia);
			parziale.clear();
		}
		return best;
	}

	private void cerca(List<Integer> parziale, double soglia) {
		if(calcolaLunghezza(parziale)>calcolaLunghezza(best)) {
			best = new ArrayList<>(parziale);
		}
		
		for(Integer c : Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			if(grafo.getEdge(parziale.get(parziale.size()-1), c)!=null && grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1), c)) > soglia && !parziale.contains(c)) {
				parziale.add(c);
				cerca(parziale, soglia);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	private double calcolaLunghezza(List<Integer> parziale) {
		double l = 0.0;
		for(int i=0; i<parziale.size()-1; i++) {
			Integer c1 = parziale.get(i);
			Integer c2 = parziale.get(i+1);
			l += grafo.getEdgeWeight(grafo.getEdge(c1, c2));
		}
		return l;
	}
	
//	private List<DefaultWeightedEdge> archiValidi(double soglia) {
//		List<DefaultWeightedEdge> archi = new ArrayList<>();
//		for(DefaultWeightedEdge e : grafo.edgeSet()) {
//			if(grafo.getEdgeWeight(e) > soglia) {
//				archi.add(e);
//			}
//		}
//		return archi;
//	}
	
}