package abstraction.eq9Distributeur2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;

import abstraction.eqXRomu.filiere.Filiere;

import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;


// Classe codée par Margot Lourenço Da Silva( pour la mise à jour du journal voir next dans la classe Distributeur2Acteur
public abstract class Distributeur2Stocks extends Distributeur2Acteur{
	protected HashMap<ChocolatDeMarque, Double> stockChocoMarque;
	protected List<ChocolatDeMarque> chocolatsVillors;
	protected Variable totalStocksChocoMarque;  
	private List<ChocolatDeMarque>chocosProduits;
	
	public Distributeur2Stocks() {
		this.chocosProduits = new LinkedList<ChocolatDeMarque>();
		
		this.totalStocksChocoMarque = new VariablePrivee("Eq9DStockChocoMarque", "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0,Double.MAX_VALUE, 0.0);

	}
	
	public void initialiser() {
		super.initialiser();
		this.stockChocoMarque=new HashMap<ChocolatDeMarque,Double>();
		chocosProduits= Filiere.LA_FILIERE.getChocolatsProduits();		

		for (ChocolatDeMarque cm : chocosProduits) {
			double quantite;
			quantite= Filiere.LA_FILIERE.getVentes(cm, -24);			
			this.stockChocoMarque.put(cm, quantite);
			this.journal.ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+cm+")->"+this.stockChocoMarque.get(cm));
			this.totalStocksChocoMarque.ajouter(this,  quantite, cryptogramme);}
		
		}
	public void next() {
		super.next();
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			this.getJournaux().get(0).ajouter(Romu.COLOR_LLGRAY, Romu.COLOR_BROWN," stock("+cm+")->"+ this.getQuantiteEnStock(cm,this.cryptogramme));}
		}
		


	public HashMap<ChocolatDeMarque, Double> getStockChocoMarque() {
		return this.stockChocoMarque;
	}
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {//modifié par maxime car il y avait deux fonctions différentes réalisant la même tache
		if(this.cryptogramme==cryptogramme) {
		return this.stockChocoMarque.get(p);}
		else{return 0.0;}
	}
	
	

}
