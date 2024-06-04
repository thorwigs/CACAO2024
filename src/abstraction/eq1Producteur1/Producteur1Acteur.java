package abstraction.eq1Producteur1;
/**@author Abderrahmane ER-RAHMAOUY*/
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


public class Producteur1Acteur implements IActeur {

	protected int cryptogramme;
	protected Journal journal;
	
	private double coutStockage;
	
	private double coutUnitaireProductionBQ = 1;
    private double coutUnitaireProductionMQ = 1.5;
    private double coutUnitaireProductionHQ = 2.0;
	protected HashMap<Feve, Double> prodParStep;
	protected HashMap<Feve, Double> stockIni;

	protected double croissanceEco;
	public static double soldeInitiale;
	protected ArrayList<Double> croissanceParStep;
	protected ArrayList<Double> soldeParStep ;
	protected  double labourNormal = 1.80;
	protected double labourEnfant = 0.80;
	protected  double labourEquitable = 3;
	protected double Part = 0.25;
	protected int nb_enfants;
	protected int nb_normal;
	protected int nb_equitable;
	
	 /**
     * Retourne le coût unitaire de production pour une fève donnée.
     * 
     * @param f la fève pour laquelle le coût unitaire de production est demandé.
     * @return le coût unitaire de production de la fève.
     */
	public double getCoutUnitaireProduction(Feve f) {
		if(f.getGamme()== Gamme.HQ) {
			return this.coutUnitaireProductionHQ;
		}
		else if (f.getGamme()== Gamme.MQ) {
			return this.coutUnitaireProductionMQ;
		}
		else if (f.getGamme()== Gamme.BQ) {
			return this.coutUnitaireProductionBQ;
		}
		else {
			return 0;
		}
		
	}


	
	 /**
     * Constructeur de la classe Producteur1Acteur.
     */
	public Producteur1Acteur() {
		this.journal=new Journal(this.getNom()+"   journal",this);
		this.soldeParStep = new ArrayList<Double>();
		

	}
	
		
	
	
	/**
     * Retourne la production par étape.
     * 
     * @return un HashMap contenant la production par étape.
     */
	public HashMap<Feve, Double> getProd(){
		return this.prodParStep;
	}
	 /**
     * Retourne le coût de stockage.
     * 
     * @return le coût de stockage.
     */
	public double getCoutStockage() {
		return this.coutStockage;
	}
	 /**
     * Initialise l'acteur Producteur1.
     */
	@SuppressWarnings("static-access")
	public void initialiser() {
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
	
		
		this.soldeInitiale = this.getSolde();
		this.soldeParStep.add(this.soldeInitiale);
		this.croissanceParStep = new ArrayList<Double>();
	
	}
	public String getNom() {// NE PAS MODIFIER
		return "EQ1";
	}
	 /**
     * Calcule la croissance économique.
     */
	public void CroissanceEconomique() {
		this.croissanceParStep = new ArrayList<Double>();
		
		for (int i = 0; i < this.soldeParStep.size()-1; i++) {
			double crois = (this.soldeParStep.get(i+1)-this.soldeParStep.get(i))/this.soldeParStep.get(i);
			
			this.croissanceParStep.add(crois);
			
		}
		
		
	}
	
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	/**
     * Effectue les opérations nécessaires à chaque étape de la simulation.
     */
	public void next() {
		
       int etape = Filiere.LA_FILIERE.getEtape();
		this.journal.ajouter("Etape= "+etape);

		
		
		
		
	
		this.journal.ajouter("Le solde a l'etape " + etape + "est  :"+ this.getSolde());
		this.soldeParStep.add(this.getSolde());
		this.CroissanceEconomique();
		
		if (etape>1) {
		this.journal.ajouter("La croissance economique est :"+this.croissanceParStep.get(etape-2));
		}
		
		this.journal.ajouter("Les nouveaux salaire sont:"+ this.labourNormal);
	
		this.journal.ajouter("Le cout de production total a l'etape " + etape+ "est "+ this.CoutsProd());
	}

	

	public Color getColor() {// NE PAS MODIFIER
		return new Color(243, 165, 175); 
	}

	public String getDescription() {
		return "AfriKakao-Producteur Cacao";
	}

	// Renvoie les indicateurs
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		//res.addAll(this.stock.values());
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



	/**@author Haythem */
	/**
     * Calcule les coûts de production.
     * 
     * @return le coût total de production.
     */
	protected double CoutsProd() {
		double CoutProdBQ=0;
		double CoutProdMQ=0;
		double CoutProdHQ=0;
		HashMap<Feve, Double> QuantiteDeProd =prodParStep;
		for (Feve f : QuantiteDeProd.keySet()) {
	        double quantite = QuantiteDeProd.get(f); 

	        // Calcul du coût de production pour la gamme de qualité concernée
	        if (f.getGamme() == Gamme.BQ) {
	            CoutProdBQ += quantite * coutUnitaireProductionBQ;
	        } else if (f.getGamme() == Gamme.MQ) {
	            CoutProdMQ += quantite * coutUnitaireProductionMQ;
	        } else if (f.getGamme() == Gamme.HQ) {
	            CoutProdHQ += quantite * coutUnitaireProductionHQ;
	        }
	    }
	    return CoutProdBQ + CoutProdMQ + CoutProdHQ;
	    
	}
	
	
	

	@Override
	/**
     * Retourne la quantité en stock d'un produit donné.
     * 
     * @param p le produit.
     * @param cryptogramme le cryptogramme.
     * @return la quantité en stock du produit.
     */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		return 0.0;
		
	}
	
	
}
