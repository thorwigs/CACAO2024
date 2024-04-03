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
import abstraction.eqXRomu.produits.IProduit;
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1Acteur implements IActeur {
	
	protected int cryptogramme;
	protected Journal journal;
	//new stuff I added : Abdo
	protected int nb_employees;
	protected int nb_enfants;
	protected int nb_equitables;
	private double coutStockage;
	//This /|\
	protected HashMap<Feve, Double> prodParStep;
	protected HashMap<Feve, Variable> stock;
	protected static double LabourNormal = 1.80;
	protected static double LabourEnfant = 0.80;
	protected static double LabourEquitable = 3;
	protected static double Part = 0.25;

	public Producteur1Acteur() {
		this.journal=new Journal(this.getNom()+"   journal",this);
		
		this.prodParStep = new HashMap<Feve, Double>();
		this.prodParStep.put(Feve.F_BQ,10000.0 );
		this.prodParStep.put(Feve.F_MQ,10000.0 );
		this.prodParStep.put(Feve.F_HQ, 000.0);
		this.prodParStep.put(Feve.F_MQ_E,0.0 );
		this.prodParStep.put(Feve.F_HQ_E,0.0 );
		this.prodParStep.put(Feve.F_HQ_BE,0.0 );
		
		
	
		//Still not sure about this need to be looked into a bit more
		this.stock = new HashMap<Feve, Variable>();
		for (Feve f : Feve.values()) {
			Variable v =  new Variable(this.getNom()+"Stock"+f.toString().substring(2), "<html>Stock de feves "+f+"</html>",this, 0.0, prodParStep.get(f)*24, prodParStep.get(f)*6);
			this.stock.put(f, v);
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
	}
	public String getNom() {// NE PAS MODIFIER
		return "EQ1";
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
		double Labor = (this.nb_employees*this.LabourNormal+this.nb_enfants*this.LabourEnfant+this.nb_equitables*this.LabourEquitable)*15;
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*this.getCoutStockage());
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Labor",Labor );
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
}
