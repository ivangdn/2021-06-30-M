package it.polito.tdp.genes.model;

public class Arco {
	private int chromosome1;
	private int chromosome2;
	private double peso;
	
	public Arco(int chromosome1, int chromosome2, double peso) {
		super();
		this.chromosome1 = chromosome1;
		this.chromosome2 = chromosome2;
		this.peso = peso;
	}

	public int getChromosome1() {
		return chromosome1;
	}

	public void setChromosome1(int chromosome1) {
		this.chromosome1 = chromosome1;
	}

	public int getChromosome2() {
		return chromosome2;
	}

	public void setChromosome2(int chromosome2) {
		this.chromosome2 = chromosome2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

}
