package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;


public abstract class Producteur3Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal;
	private HashMap<IProduit,Integer> stocks;
	//passable en parametre/indicateurs
	private double coutUnitaireProductionBQ = 1.0;
    private double coutUnitaireProductionMQ = 1.5;
    private double coutUnitaireProductionHQ = 2.0;
    
    abstract HashMap<Feve,Double> quantite();
    
	public Producteur3Acteur() {
		this.journal = new Journal(this.getNom()+" journal",this);
	}
	
	public void initialiser() {
		this.stocks = new HashMap<IProduit,Integer>();
		setQuantiteEnStock(Feve.F_BQ,100000); //quantite initiale de stock de BQ (a modifier)
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ3";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////


	public void next() {
		this.journal.ajouter("etape="+Filiere.LA_FILIERE.getEtape());
		this.journal.ajouter("cout de stockage: "+Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", calculerCoutsStockage ());
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", calculerCoutsProduction ());
		
	}

	public Color getColor() {// NE PAS MODIFIER
		return new Color(249, 230, 151); 
	}

	public String getDescription() {
		return "tiCao - Producteur 3";
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
		this.journal.ajouter("Faillite de l'acteur "+acteur.toString());	
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	public void notificationOperationBancaire(double montant) {
		this.journal.ajouter("Operation bancaire : "+montant+ " E");
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

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			//on renvoie la valeur (null => 0)
			if (this.stocks.get(p) != null) {
				return this.stocks.get(p);
			}
			else {
				return 0;
			}
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	
	/**
	 * @author Arthur
	 */
	protected void setQuantiteEnStock(IProduit p, double stock) {
		//on set la valeur du stock ou la modifie si elle existe deja
		this.stocks.put(p,(int)stock);
	}
	/**
	 * @author mammouYoussef
	 */
	 public double calculerCoutsStockage () {
	      double coutStockage = 0;
	      double cout=Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
	      for (Integer quantite : stocks.values()) {
	          coutStockage += quantite * cout  ;}
	      return coutStockage;
	      }
	 /**
	  * @author mammouYoussef
	  */		 

     
	 public double calculerCoutsProduction() {
		    double coutProductionBQ = 0;
		    double coutProductionMQ = 0;
		    double coutProductionHQ = 0;
	
	    HashMap<Feve, Double> quantitesProduites = quantite();

	    for (Feve f : quantitesProduites.keySet()) {
	        double quantite = quantitesProduites.get(f); // Récupération de la quantité produite

	        // Calcul du coût de production pour la gamme de qualité concernée
	        if (f.getGamme() == Gamme.BQ) {
	            coutProductionBQ += quantite * coutUnitaireProductionBQ;
	        } else if (f.getGamme() == Gamme.MQ) {
	            
	            coutProductionMQ += quantite * coutUnitaireProductionMQ;
	        } else if (f.getGamme() == Gamme.HQ) {
	          
	            coutProductionHQ += quantite * coutUnitaireProductionHQ;
	        }
	    }
	    return coutProductionBQ + coutProductionMQ + coutProductionHQ;
	
        }
	 }