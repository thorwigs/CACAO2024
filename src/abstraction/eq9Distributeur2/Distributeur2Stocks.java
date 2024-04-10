package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;

import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.ChocolatDeMarque;



public abstract class Distributeur2Stocks extends Distributeur2Acteur{
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected List<ChocolatDeMarque> chocolatsVillors;
	protected Variable totalStocksChocoMarque;  
	private List<ChocolatDeMarque>chocosProduits;
	private double coutStockage;
	
	public Distributeur2Stocks() {
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		
		this.totalStocksChocoMarque = new VariablePrivee("Eq9DStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0, 1000000.0, 0.0);

	}
	public void initialiser() {
		

		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*16;
		chocosProduits= Filiere.LA_FILIERE.getChocolatsProduits();			
		for (ChocolatDeMarque cm : chocosProduits) {
			double quantite;
			quantite= Filiere.LA_FILIERE.getVentes(cm, -24);			
			this.stockChocoMarque.put(cm, quantite);
			this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
			this.totalStocksChocoMarque.ajouter(this,  quantite, cryptogramme);}
			
		}
		
		


	public HashMap<ChocolatDeMarque, Double> getStockChocoMarque() {
		return this.stockChocoMarque;
	}
	public double getStockChocoMarque(ChocolatDeMarque cm,int crypto) {
		if(this.cryptogramme==crypto) {
		return this.stockChocoMarque.get(cm);}
		return 0.0;
	}
	
	

}
