package abstraction.eq1Producteur1;

import java.awt.Color;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1Acteur implements IActeur {
	
	protected ArrayList<Ouvrier> liste_Ouvrier;
	protected int cryptogramme;
	protected Journal journal;
	//new stuff I added : Abdo
	protected int nb_employees;
	protected int nb_enfants;
	protected int nb_equitables;
	private double coutStockage;
	//Haythem
	private double coutUnitaireProductionBQ = 1.0;
    private double coutUnitaireProductionMQ = 1.5;
    private double coutUnitaireProductionHQ = 2.0;
	//This /|\
	protected HashMap<Feve, Double> prodParStep;
	protected HashMap<Feve, Variable> stock;

	protected double croissanceEco;
	public static double soldeInitiale;
	protected ArrayList<Double> croissanceParStep;
	protected ArrayList<Double> soldeParStep ;
	protected  double labourNormal = 1.80;
	protected double labourEnfant = 0.80;
	protected  double labourEquitable = 3;
	protected double Part = 0.25;


	public Producteur1Acteur() {
		this.journal=new Journal(this.getNom()+"   journal",this);
		this.soldeParStep = new ArrayList<Double>();
		this.prodParStep = new HashMap<Feve, Double>();
		this.prodParStep.put(Feve.F_BQ,Part*10000000.0 );
		this.prodParStep.put(Feve.F_MQ,Part*10000000.0 );
		this.prodParStep.put(Feve.F_HQ, Part*10000000.0);
		this.prodParStep.put(Feve.F_MQ_E,Part*0.0 );
		this.prodParStep.put(Feve.F_HQ_E,Part*0.0 );
		this.prodParStep.put(Feve.F_HQ_BE,Part*0.0 );
	
		
		
		
	
		//Still not sure about this need to be looked into a bit more
		this.stock = new HashMap<Feve, Variable>();
		for (Feve f : Feve.values()) {
			Variable v =  new Variable(this.getNom()+"Stock"+f.toString().substring(2), "<html>Stock de feves "+f+"</html>",this, 0.0, prodParStep.get(f)*24, prodParStep.get(f)*6);
			this.stock.put(f, v);
		}
	}
	public void amelioration() {
		int etape = Filiere.LA_FILIERE.getEtape();
		int annee = Filiere.LA_FILIERE.getAnnee(etape);
		float croissement =0 ;
		int enfants = this.nb_enfants;
		int size = this.croissanceParStep.size();
		boolean croissant = this.croissanceParStep.get(size-1)>0 && this.croissanceParStep.get(size-2)>0 && this.croissanceParStep.get(size-3)>0;
		if ((annee != 0)& (annee % 5 == 0) && croissant & (this.nb_enfants>=10)  ) {
			
			this.setNbEnfant(enfants-10);
			if (this.labourNormal < 2.5 ) { 
				double nouveauSalaire = this.labourNormal*1.08;
				this.labourNormal = nouveauSalaire;
				
				}
			if (this.labourEnfant < 2 ) { 
				double nouveauSalaireE = this.labourEnfant*1.05;
				this.labourEnfant= nouveauSalaireE;
				
				}
			
			
		}
		
	}
	
	
	public HashMap<Feve, Double> getProd(){
		return this.prodParStep;
	}
	public double getCoutStockage() {
		return this.coutStockage;
	}

	public void initialiser() {
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
		this.nb_enfants = 150;
		this.nb_equitables = 30;
		this.nb_employees = 100;
		this.soldeInitiale = this.getSolde();
		this.soldeParStep.add(this.soldeInitiale);
		this.croissanceParStep = new ArrayList<Double>();
		//soldeParStep.add(this.getSolde());
	}
	public String getNom() {// NE PAS MODIFIER
		return "EQ1";
	}
	
	public void CroissanceEconomique() {
		this.croissanceParStep = new ArrayList<Double>();
		for (int i = 0; i < this.soldeParStep.size()-1; i++) {
			double crois = (this.soldeParStep.get(i+1)-this.soldeParStep.get(i))/this.soldeParStep.get(i-1);
			
			this.croissanceParStep.add(crois);
			
		}
		
		
	}
	
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		double totalStock = 0;
		for (Feve f : Feve.values()) {
			this.stock.get(f).ajouter(this,this.getProd().get(f) );
			totalStock += this.stock.get(f).getValeur();
		}
		this.getJournaux().get(0).ajouter("Etape= "+Filiere.LA_FILIERE.getEtape());
		this.getJournaux().get(0).ajouter("Coût de stockage : "+this.getCoutStockage());
		this.getJournaux().get(0).ajouter("Stock= "+ totalStock);
		this.getJournaux().get(0).ajouter("Le nombre d'employees = "+ this.nb_employees);
		this.getJournaux().get(0).ajouter("Le nombre d'employees equitable = "+ this.nb_equitables);
		this.getJournaux().get(0).ajouter("Le nombre d'enfants employees = "+ this.nb_enfants);
		/*  I added this above there is no diff in between the two functions I just think the first is more professional/|\
		this.journal.ajouter("etape= "+Filiere.LA_FILIERE.getEtape());
		this.journal.ajouter("prix stockage= "+Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		*/
		double Labor = (this.nb_employees*this.labourNormal+this.nb_enfants*this.labourEnfant+this.nb_equitables*this.labourEquitable)*15;
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*this.getCoutStockage());
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Labor",Labor );

		double diffSolde = this.getSolde()-this.soldeInitiale;
		this.getJournaux().get(0).ajouter("Le solde a l'etape " + Filiere.LA_FILIERE.getEtape() + "est augemente de :"+ this.getSolde());
		this.soldeParStep.add(this.getSolde());
		int i = this.soldeParStep.size();
		
		this.getJournaux().get(0).ajouter("La croissance economique est :"+(this.soldeParStep.get(i-1)-this.soldeParStep.get(i-2))/this.soldeParStep.get(i-2));
		this.croissanceParStep.add((this.soldeParStep.get(i-1)-this.soldeParStep.get(i-2))/this.soldeParStep.get(i-2));
		this.getJournaux().get(0).ajouter("Les nouveaux salaire sont:"+ this.labourNormal);
		this.amelioration();
		this.getJournaux().get(0).ajouter("Le cout de production total a l'etape " + Filiere.LA_FILIERE.getEtape() + "est "+ this.CoutsProd());

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
		res.addAll(this.stock.values());
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

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme && this.stock.containsKey(p)) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return this.stock.get(p).getValeur(cryptogramme); // A modifier
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}

	// Haythem
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
	
	
	
	public void embauche(int à_embaucher) {
		
	
		this.setNbOuv(à_embaucher+this.getNbEnfant());
		//ajout d'un cout d'embauche au préable
		
			}
	
	public void licensier(int à_licensier) {
		
		this.setNbEnfant(this.getNbOuv()-à_licensier);
		
		
		
	}
	
	public void formation() {
		
		
	}
	
	public int getNbEnfant() {
		return this.nb_enfants;
		
	}
	public void setNbEnfant(int nbenfants) {
		
		this.nb_enfants=nbenfants;
	}
	
	public int getNbOuv() {
		return this.nb_employees;
		
	}
	
	public int getNbOuvEq() {
		return this.nb_equitables;
		
	}
	
	
	public void setNbOuv(int nbouvrier) {
		this.nb_employees=nbouvrier;
		

	}
}
