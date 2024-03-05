package abstraction.eqXRomu.acteurs;

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

/**
 * Producteur additionnel trivial (*) representant une faible part
 * de marche dont le but est de vendre des Feves, permettant aux transformateurs
 * d'en acheter sans attendre que les realisations des equipes 1 a 3 soient operationnelles.
 * 
 * (*) trop trivial puisqu'il ne respecte pas toutes les contraintes imposees,
 * notamment en termes de couts, de replantation, de maladies, de pe
 * et c'est pourquoi sa part de marche initiale est faible.
 */
public class ProducteurXActeur  implements IActeur {
	
	protected HashMap<Feve,Double> prodParStep;
	protected HashMap<Feve,Variable> stock;
	protected int cryptogramme;
	private static final double PART=0.1;  // La part de marche initiale 
	private double coutStockage;

	public ProducteurXActeur() {

		// Nous pourrions nous adapter au marche car il est possible de connaitre les consommations
		// faites par les clients finaux lors de la precedente annee, mais dans cette version basique
		// on ne s'appuie que sur des moyennes
		this.prodParStep = new HashMap<Feve, Double>();
		this.prodParStep.put(Feve.F_HQ_BE, PART*20830.0);
		this.prodParStep.put(Feve.F_HQ_E, PART*41600.0);
		this.prodParStep.put(Feve.F_MQ_E, PART*10400.0);
		this.prodParStep.put(Feve.F_MQ, PART*52000.0);
		this.prodParStep.put(Feve.F_BQ, PART*83320.0);
		
		this.stock = new HashMap<Feve, Variable>();
		for (Feve f : Feve.values()) {
		    this.stock.put(f, new VariableReadOnly(this+"Stock"+f.toString().substring(2), "<html>Stock de feves "+f+"</html>",this, 0.0, prodParStep.get(f)*24, prodParStep.get(f)*6));
		}
	}
	
	public void initialiser() {
		this.coutStockage = Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
	}

	public String getNom() {
		return "EQXP";
	}
	
	public String toString() {
		return this.getNom();
	}

	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////

	public void next() {
		// on ajoute la production, laquelle est trivialement la meme tout au long de l'annee, sans alea, ...
		double totalStock=0.0;
		for (Feve f : Feve.values()) {
			this.stock.get(f).ajouter(this, this.prodParStep.get(f), cryptogramme);
			if (this.stock.get(f).getValeur(cryptogramme)>10*this.prodParStep.get(f)) { // on jette si trop de stock
				this.stock.get(f).setValeur(this, 10*this.prodParStep.get(f), cryptogramme);
			}
			totalStock+=this.stock.get(f).getValeur();
		}
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*this.coutStockage);

		// On payera la main d'oeuvre et les autres frais quand les regles seront claires a ce sujet...
	}

	public Color getColor() {
		return new Color(165,165,165);
	}

	public String getDescription() {
		return "Producteur X";
	}

	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		res.addAll(this.stock.values());
		return res;
	}

	public List<Variable> getParametres() {
		List<Variable> res=new ArrayList<Variable>();
		
		return res;
	}

	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		return res;
	}

	public void setCryptogramme(Integer crypto) {
		this.cryptogramme = crypto;
	}

	public void notificationFaillite(IActeur acteur) {
	}

	public void notificationOperationBancaire(double montant) {
	}
	
	protected double getSolde() {
		return Filiere.LA_FILIERE.getBanque().getSolde(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme);
	}

	public List<String> getNomsFilieresProposees() {
		ArrayList<String> filieres = new ArrayList<String>();
		return(filieres);
	}

	public Filiere getFiliere(String nom) {
		return Filiere.LA_FILIERE;
	}

	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme && this.stock.keySet().contains(p)) { 
			return this.stock.get(p).getValeur((Integer)cryptogramme);
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
}
