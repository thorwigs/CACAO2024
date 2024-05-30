package abstraction.eq2Producteur2;

import java.awt.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.general.VariableReadOnly;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit; 

/** Classe principale de l'acteur
 * @author Quentin, Noémie, Maxime
 */

public abstract class Producteur2Acteur implements IActeur {
	protected int cryptogramme;
	protected Journal journal;
	protected Journal journal_prix;

	protected HashMap<Feve,Double> stock; //Feve = qualite et Variable = quantite
	protected HashMap<Feve,Double> prodParStep;
	private static final double PART=0.1;
	protected HashMap <Feve, Variable> stock_variable;
	protected HashMap <Feve, Variable> prod_step;
	protected ArrayList<Double> solde;
	protected Variable tonnes_venduesCC;
	protected Variable tonnes_venduesBourse;
	
	protected abstract double getNbTonnesVenduesBourse();
	protected abstract double getNbTonnesVenduesCC();
	protected abstract double prix(Feve f);
	
	/** Constructeur de classe
	 * @author Quentin, Noémie
	 */
	public Producteur2Acteur() {
		this.journal = new Journal(this.getNom()+" journal", this);
		this.journal_prix = new Journal(this.getNom()+" journal prix", this);
		this.stock = new HashMap<Feve, Double>();
		this.prodParStep= new HashMap<Feve, Double>();
		this.stock_variable= new HashMap<Feve, Variable>();
		
		this.prod_step = new HashMap<Feve, Variable>();
		this.solde=new ArrayList<Double>();
		this.tonnes_venduesCC =  new Variable("Tonnes livrées CC ", this);
		this.tonnes_venduesBourse =  new Variable("Tonnes livrées Bourse ", this);

		this.init_stock(Feve.F_BQ, 102384.55); //103846153.8
		this.init_stock(Feve.F_MQ, 38678.61); //62115384.62
		this.init_stock(Feve.F_HQ_E, 0); //3076923.076
		
		this.lot_to_hashmap();
		
		prodParStep.put(Feve.F_HQ_E, 0.0);
		prodParStep.put(Feve.F_HQ, 0.0);
		prodParStep.put(Feve.F_MQ_E, 0.0);
		prodParStep.put(Feve.F_MQ, 0.0);
		prodParStep.put(Feve.F_BQ, 0.0);
		
		for (Feve f : Feve.values()) {
			if(f == Feve.F_BQ) {
				this.stock_variable.put(f,  new Variable("EQ2 Stock "+f, this, 102384.55));
			}
			else if(f == Feve.F_MQ) {
				this.stock_variable.put(f,  new Variable("EQ2 Stock "+f, this, 38678.61));
			}
			else if(f == Feve.F_HQ_E) {
				this.stock_variable.put(f,  new Variable("EQ2 Stock "+f, this, 0));
			}
			else if (f != Feve.F_HQ_BE){
				this.stock_variable.put(f,  new Variable("EQ2 Stock "+f, this, 0));
			}
			if (f != Feve.F_HQ_BE){
				this.prod_step.put(f,  new Variable("EQ2 Production par step "+f, this, 0));
			}
		}	
	}
	
	/** Définition de méthodes abstraites
	 * @author Noémie
	 */
	public abstract void init_stock(Feve type_feve, double quantite);
	public abstract void lot_to_hashmap();
	
	public void initialiser() {
		solde.add(this.getSolde()); //initialisation solde initial
		// les initialisations sont faites dans le constructeur
	}
	
	/** getBenefice
	 * 
	 * @author Maxime
	 */
	public double getBenefice() {
		int n=solde.size();
		if(n<=1) {
			return solde.get(n-1);
		}
		else{
			return solde.get(n-1)-solde.get(n-2);
		}
	}

	/** getCoursBourse
	 * 
	 * @author Maxime
	 */
	public double getCoursBourse(Feve f) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		Variable cours = bourse.getCours(f);
		return cours.getValeur();
		}
	
	/** Getter
	 * @author Noémie
	 */
	public HashMap<Feve, Variable> getStock_variable() {
		return stock_variable;
	}
	
	/** Setter
	 * @author Noémie
	 */
	public void setStock_variable(HashMap<Feve, Variable> stock_variable) {
		this.stock_variable = stock_variable;
	}

	public String getNom() {// NE PAS MODIFIER
		return "EQ2";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	
	/** Next et journal principal
	 * @author Noémie, Maxime
	 */
	public void next() {
		this.DebiteCoutParStep();
		this.journal.ajouter("--------------- étape = " + Filiere.LA_FILIERE.getEtape()+ " -----------------------------");
		this.journal.ajouter("cout de stockage moyen de la filiere " + Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur());
		this.journal.ajouter("\n Argent sortant : " + this.getCoutTotalParStep());
		this.journal.ajouter("Solde après débit : " + this.getSolde()+"\n");
		
		this.journal_prix.ajouter("--------------- étape = " + Filiere.LA_FILIERE.getEtape()+ " -----------------------------");
		this.journal_prix.ajouter("prix de la bourse feve BQ : " + this.getCoursBourse(Feve.F_BQ));
		this.journal_prix.ajouter("prix d'achat lors d'un contrat cadre feve BQ: " + this.prix(Feve.F_BQ));	
		
		for (Feve f : Feve.values()) {
			if (f != Feve.F_HQ_BE) {
				this.stock_variable.get(f).setValeur(this, this.getQuantiteEnStock(f, this.cryptogramme));
				this.prod_step.get(f).setValeur(this, this.prodParStep.get(f));
			}
		}
		solde.add(this.getSolde());
		
		if (solde.size() > 2) {
			solde.remove(0);
		}
		
		this.tonnes_venduesCC.setValeur(this, this.getNbTonnesVenduesCC());
		this.tonnes_venduesBourse.setValeur(this, this.getNbTonnesVenduesBourse());
	}
	
	public Color getColor() { //NE PAS MODIFIER
		return new Color(244, 198, 156); 
	}

	/** Renvoie la description de l'acteur
	 * @author Maxime
	 */
	public String getDescription() {
		return "Nous sommes CacaoLand, producteur au sein de la filière du cacao. Notre objectif est de produire du cacao de haute qualité de manière équitable avec également du cacao de basse et moyenne qualité en quantité.";
	}

	/** Renvoie les indicateurs
	 * @author Noémie
	 */
	public List<Variable> getIndicateurs() {
		
		List<Variable> res = new ArrayList<Variable>();
		
		for (Feve f: Feve.values()) {
			if (f != Feve.F_HQ_BE) {
				res.add(this.prod_step.get(f));
			}
		}
		for (Feve f: Feve.values()) {
			if (f != Feve.F_HQ_BE) {
				res.add(this.stock_variable.get(f));
			}
		}
		
		res.add(this.tonnes_venduesCC);
		res.add(this.tonnes_venduesBourse);
		return res;
	}

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		return res;
	}

	/**  Renvoie les journaux
	 * @author Maxime
	 */
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.add(journal);
		res.add(journal_prix);
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
		return Filiere.LA_FILIERE.getBanque().getSolde(this, cryptogramme);
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

	/** Retourne la quantité stockée pour un type de produit (un type de fève)
	 * @author Quentin
	 */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			double quantite_stockee_prod = this.stock.get(p);
			return quantite_stockee_prod;
			//return this.getStockTotal(cryptogramme);
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	
	/** Retourne la quantité totale de fèves stockée
	 * @param cryptogramme
	 * @author Quentin
	 */
	public double getStockTotal(int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			double quantite_stockee = 0;
			for(Feve f : this.stock.keySet()) {
				quantite_stockee += this.stock.get(f);
			}
			return quantite_stockee;
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	
	/** Définition de méthodes abstraites
	 * @author Noémie
	 */
	public abstract double cout_total_stock();
	public abstract double cout_humain_par_step();
	public abstract double cout_plantation();
	
	/** Calcule le montant total à payer à chaque étape
	 * @author Noémie
	 */
	public double getCoutTotalParStep() {
		double somme = this.cout_total_stock() + this.cout_humain_par_step() + this.cout_plantation();
		return somme;
	}
	
	/** Débite de notre compte l'argent utilisé à chaque étape 
	 * @author Noémie
	 */
	public void DebiteCoutParStep() {
		retireArgent(this.cout_total_stock(), "coût des stocks");	
		retireArgent(this.cout_humain_par_step(), "coût humain");
		retireArgent(this.cout_plantation(), "coût de la plantation");	
	}

	/** Permet de retirer de l'argent de la banque
	 * @param montant 
	 * @param raison
	 * @author Noémie
	 */
	public void retireArgent(double montant, String raison) {
		if (montant>0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, this.cryptogramme, raison, montant);
		}		
	}
}