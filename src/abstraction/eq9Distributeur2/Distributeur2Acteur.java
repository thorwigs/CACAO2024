package abstraction.eq9Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariablePrivee;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.IProduit;

public abstract class Distributeur2Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal;
	private int capaciteStockage;
	protected double coutStockage;
	
	

	public Distributeur2Acteur() {
		this.journal = new Journal(this.getNom()+" journal", this);
		this.capaciteStockage = Integer.MAX_VALUE;
		
	}
	
	public void initialiser() {
		this.coutStockage = (Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*16);
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ9";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}
	
	public int getCapaciteStockage() {
		return this.capaciteStockage;
	}
	public double getCoutStockage() {
		return this.coutStockage;
	}
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	
	public void next() {
		this.getJournaux().get(0).ajouter("Step "+Filiere.LA_FILIERE.getEtape());
		this.getJournaux().get(0).ajouter("Coût de stockage pour une tonne : "+ this.getCoutStockage());
		
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(245, 155, 185); 
	}

	public String getDescription() {
		return "Bonjour bonjour";
	}

	//Le code suivant à été mis en commentaire car on ne souhaite plus mettre les chocolats de marque en indicateurs mais simplement les différentes catégories de chocolat!
	/*
	// transforme le dictionnaire des stocks(double) en dictionnaire des stocks(Variable)
	protected HashMap<ChocolatDeMarque, Variable> variabilisation(HashMap<ChocolatDeMarque, Double> stockChocoMarque){
		HashMap<ChocolatDeMarque, Variable> stockChocoIndicateur = new HashMap();
		for (HashMap.Entry<ChocolatDeMarque, Double> entry : stockChocoMarque.entrySet()) {
			entry.getKey();
			Variable quantite = new Variable("Eq9DStockChocoMarque_"+ entry.getKey() , "<html>Quantite totale de chocolat de marque en stock</html>",this, 0.0,Double.MAX_VALUE, entry.getValue());
			stockChocoIndicateur.put(entry.getKey(),quantite);
		}
		return stockChocoIndicateur;
	}
	
	protected HashMap<ChocolatDeMarque, Variable> iniT(){
		HashMap<ChocolatDeMarque, Variable> stockChocoIndicateur = new HashMap();
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			Variable quantite = new Variable("Eq9DStockChocoMarque_"+cm.getNom() ,this);
			stockChocoIndicateur.put(cm,quantite);
		}
		return stockChocoIndicateur;
	}
	*/
	protected abstract HashMap<ChocolatDeMarque, Double> stockParType(String type);
	
	protected void MiseAJour(ArrayList<Variable> indicateurs){
		 for (Variable type : indicateurs) {
			 HashMap<ChocolatDeMarque, Double> stockParType = stockParType(type.getNom().substring(10));
			 double stockType = 0;
			 for(double quantite : stockParType.values()) {
				 stockType+=quantite;
			 }
			 type.setValeur(this, stockType);
		 }
		
		
	};
	
	
	protected ArrayList<Variable> init(){
		ArrayList<Variable> indicateurs = new ArrayList<Variable>();
		
		Variable BQ = new Variable("Eq9DStock_BQ", "<html>Quantite totale de chocolat de Basse qualité en stock</html>",this, 0.0,Double.MAX_VALUE, 0); indicateurs.add(BQ);
		Variable MQ = new Variable("Eq9DStock_MQ", "<html>Quantite totale de chocolat de Moyenne qualité en stock</html>",this, 0.0,Double.MAX_VALUE, 0); indicateurs.add(MQ);
		Variable MQ_E = new Variable("Eq9DStock_MQ_E", "<html>Quantite totale de chocolat de Moyenne qualité équitable en stock</html>",this, 0.0,Double.MAX_VALUE, 0); indicateurs.add(MQ_E);
		Variable HQ = new Variable("Eq9DStock_HQ", "<html>Quantite totale de chocolat de Haute qualité en stock</html>",this, 0.0,Double.MAX_VALUE, 0);  indicateurs.add(HQ);
		Variable HQ_E = new Variable("Eq9DStock_HQ_E", "<html>Quantite totale de chocolat de Haute qualité équitable en stock</html>",this, 0.0,Double.MAX_VALUE, 0);  indicateurs.add(HQ_E);
		Variable HQ_BE = new Variable("Eq9DStock_HQ_BE", "<html>Quantite totale de chocolat de Haute qualité bio-équitable en stock</html>",this, 0.0,Double.MAX_VALUE, 0);  indicateurs.add(HQ_BE);
		
		return indicateurs;
	}
	
	// Renvoie les indicateurs
	public abstract List<Variable> getIndicateurs() ;

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(this.journal);
		return res;
	}

	
	//Fonction appelée juste après avoir réalisé un achat
	public double coutDacheminement(double prix) {
		return 0.05*prix;
	}
	
	
	////////////////////////////////////////////////////////
	//               En lien avec la Banque               //
	////////////////////////////////////////////////////////

	// Appelee en debut de simulation pour vous communiquer 
	// votre cryptogramme personnel, indispensable pour les
	// transactions.
	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}
	

	// Appelee lorsqu'un acteur fait faillite (potentiellement vous)
	// afin de vous en informer.
	public void notificationFaillite(IActeur acteur) {
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
	}
	
	// Renvoie le solde actuel de l'acteur
	protected double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	////////////////////////////////////////////////////////
	//        Pour la creation de filieres de test        //
	////////////////////////////////////////////////////////

	// Renvoie la liste des filieres proposees par l'acteur
	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	// Renvoie une instance d'une filiere d'apres son nom
	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

}
