package abstraction.eq9Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.acteurs.Romu;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
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

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

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

	
	//Fonction à appeler juste après avoir réalisé un achat (maxime)
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
