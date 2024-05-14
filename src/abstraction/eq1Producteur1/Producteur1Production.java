/**
 * Représente un gestionnaire de production pour un producteur spécifique dans la chaîne d'approvisionnement.
 * Gère la production, la transformation et le stockage des fèves de cacao.
 */

/**@author Abderrahmane Er-rahmaouy*/
package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1Production extends Producteur1Plantation{

	protected double prix_hq_F;
	protected Journal journalProduction;
	protected Journal journalStockage;
	protected HashMap<String, Integer> ageStock;
	protected ArrayList<Double> Stock_HQ;
	protected ArrayList<Double> Stock_MQ;
	protected ArrayList<Double> Stock_BQ;
	protected static double stockMax;
	protected int degrHQ = 4;
	protected int degrMQ = 8;
	protected int degrBQ = 12;

	//protected HashMap<Feve, Variable> stockag;
	/**
	 * Constructeur pour la classe Producteur1Production.
	 * Initialise les journaux, le stock et d'autres structures de données nécessaires.
	 */
	public Producteur1Production() {
		// Initialisation des journaux, du stock et d'autres structures de données
		super();
		this.journalProduction = new Journal(this.getNom()+"   journal Production",this);
		this.journalStockage = new Journal(this.getNom()+"   journal Stockage",this);
		this.ageStock = new HashMap<String, Integer>();
		this.Stock_HQ = new ArrayList<Double>();
		this.Stock_MQ = new ArrayList<Double>();
		this.Stock_BQ = new ArrayList<Double>();
		this.prodParStep = this.ProdParStep();
		this.Stocck = this.InitiStock();

		this.stock = this.IniStock();
		

		this.Stock_HQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));

	}
	/**
	 * Méthode pour initialiser le stock de fèves pour chaque type de fève.
	 * @return Un dictionnaire avec chaque fève et sa quantité de stock associée.
	 */
	public HashMap<Feve, Double> InitiStock(){
		/*
		 * Initialise un dictionnaire feve,double pour facilier la minipulation du stock
		 * On commence deja par le stock qu'on produit pendant 2 mois
		 */
		this.Stocck = new HashMap<Feve, Double>();
		HashMap<Feve, Double> pro = this.ProdParStep();
		for (Feve f : Feve.values()) {
			this.Stocck.put(f, pro.get(f)*4);
			this.journalPlantation.ajouter("On a initialise nos stocks par:" + this.Stocck.get(f));
		
		}
		return this.Stocck;
		
	}
	/**
	 * Récupère la quantité d'un produit spécifique en stock.
	 * @param p Le produit pour lequel la quantité est demandée.
	 * @param cryptogramme Le code d'identification pour accéder aux informations de stock.
	 * @return La quantité du produit spécifié en stock.
	 */
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		if (this.cryptogramme==cryptogramme && this.Stocck.containsKey(p)) { // c'est donc bien un acteur assermente qui demande a consulter la quantite en stock
			return this.stock.get(p).getValeur(cryptogramme); // A modifier
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre stock
		}
	}
	/**
	 * Convertit une partie des fèves de cacao ordinaires en fèves de cacao équitables.
	 * S'ils respectent les conditions equitables 
	 */
	public void feveToEqui() {


		int nb = this.GetNombreOuvrierEquitable();
		int nbe = this.getNombreEnfants();
		int nbt = this.GetNombreOuvrierEquitable()+ this.GetNombreOuvrierNonEquitable()+this.getNombreOuvrierFormés();

		if (nbe !=0) {
			this.journalProduction.ajouter("On ne peut pas faire de l'equitable car on employe des enfants");
		}
		if (nb > 0.10*nbt) {
			double h = this.getQuantiteEnStock(Feve.F_HQ, cryptogramme);
			double m = this.getQuantiteEnStock(Feve.F_MQ, cryptogramme);
			this.Stocck.put(Feve.F_MQ_E, 0.5*m);
			this.Stocck.put(Feve.F_HQ, 0.5*h);
			this.stock.get(Feve.F_MQ_E).setValeur(this, 0.5*m);
			this.stock.get(Feve.F_HQ_E).setValeur(this, 0.5*h);
			this.journalProduction.ajouter("On a transforme 50% of MQ to MQ_E and 50% of HQ to HQ_E");
			this.journalStockage.ajouter("On a transforme 50% of MQ to MQ_E and 50% of HQ to HQ_E");

		}
	}
	
	/**
	 * Convertit une partie des fèves de cacao en fèves de cacao biologiques.
	 * Cela n'est pas encore fait car on fait pas de bio pour l'instant.
	 */
	public void feveToBio() {
		if (this.pesticides) {
			this.journalProduction.ajouter("On ne peut pas faire du bio car on a utilise des pesticides");
		}
		else {
			if (this.prodParStep.get(Feve.F_HQ_E) !=0) {
				this.prodParStep.put(Feve.F_HQ_BE, null);
			}
		} 

	}
	/**
	 * Gère le stockage des fèves de cacao de haute qualité.
	 * Initie le transfert de stock en fonction de certaines conditions.
	 * Ca prend pas encore en consideration l'equitable et bio equitable
	 */
	public void Stockage_HQ() {

		if (this.Stock_HQ.size() >= this.degrHQ) {
			boolean check = true;
			for (int i  =this.Stock_HQ.size()-this.degrHQ; i<this.Stock_HQ.size()-1 ;i++) {
				if (this.Stock_HQ.get(i+1) <= this.Stock_HQ.get(i)) {

					check = false;
					break; // Exit the loop if any decrease in stock is found
				}


			}
			if (check) {


				double hq = this.Stocck.get(Feve.F_HQ);
				double pro = this.Stocck.get(Feve.F_MQ);
				this.stock.get(Feve.F_HQ).retirer(this, this.Stock_HQ.get(this.Stock_HQ.size()-this.degrHQ));
				this.stock.get(Feve.F_MQ).setValeur(this, pro + this.Stock_HQ.get(this.Stock_HQ.size()-this.degrHQ));
				this.Stocck.put(Feve.F_HQ, hq - this.Stock_HQ.get(this.Stock_HQ.size()-this.degrHQ));
				this.Stocck.put(Feve.F_MQ, pro + this.Stock_HQ.get(this.Stock_HQ.size()-this.degrHQ));
				this.journalStockage.ajouter("On a eu une degradation de stock de HQ vers MQ de la quantite suivante:"+this.Stock_HQ.get(this.Stock_HQ.size()-4) );
				this.Stock_HQ.remove(this.Stock_HQ.size()-this.degrHQ);

			}
		}

	}
	/**
	 * Gère le stockage des fèves de cacao de qualité moyenne.
	 * Initie le transfert de stock en fonction de certaines conditions.
	 */
	public void Stockage_MQ() {

		if (this.Stock_MQ.size() >= this.degrMQ) {
			boolean check = true;
			int j = this.Stock_MQ.size()-this.degrMQ;
			for (int i = j; i<j+7 ;i++) {
				if (this.Stock_MQ.get(i) <= this.Stock_MQ.get(i)) {
					check = false;
					break; // Exit the loop if any decrease in stock is found
				}

			}
			if (check) {

				double mq = this.Stocck.get(Feve.F_MQ);
				double pro = this.Stocck.get(Feve.F_BQ);

				this.stock.get(Feve.F_MQ).retirer(this, this.Stock_HQ.get(j));
				this.stock.get(Feve.F_BQ).setValeur(this, pro + this.Stock_HQ.get(j));
				this.Stocck.put(Feve.F_MQ, mq - this.Stock_MQ.get(j));
				this.Stocck.put(Feve.F_BQ, pro + this.Stock_MQ.get(j));
				this.journalStockage.ajouter("On a eu une degradation de stock de MQ vers BQ de la quantite suivante:"+this.Stock_MQ.get(this.Stock_MQ.size()-8) );




				this.Stock_HQ.remove(j);				
			}
		}
	}
	/**
	 * Gère le stockage des fèves de cacao de basse qualité.
	 * Initie le transfert de stock en fonction de certaines conditions.
	 */
	public void Stockage_BQ() {
		if (this.Stock_MQ.size() >= this.degrBQ) {
			boolean check = true;
			int j = this.Stock_MQ.size() - this.degrBQ;
			for (int i = j; i < j + 11; i++) {
				if (this.Stock_BQ.get(i) <= this.Stock_BQ.get(i + 1)) {
					check = false;
					break; // Exit the loop if any decrease in stock is found
				}
			}
			if (check) {

				double bq = this.stock.get(Feve.F_BQ).getValeur();
				double bqToRemove = this.Stock_BQ.get(j);
				this.stock.get(Feve.F_BQ).retirer(this, bqToRemove);
				this.Stocck.put(Feve.F_BQ, bq - this.Stock_BQ.get(j));
				this.journalStockage.ajouter("On a eu une degradation de stock de BQ de la quantite suivante:"+this.Stock_BQ.get(this.Stock_BQ.size()-12) );
				this.Stock_BQ.remove(j);
			}
		}
	}

	/**
	 * Récupère une liste d'indicateurs pertinents pour le fonctionnement du producteur.
	 * Inclut les informations sur le stock.
	 * @return Une liste d'indicateurs.
	 */
	public List<Variable> getIndicateurs() {
		List<Variable> res = super.getIndicateurs();
		res.addAll(this.stock.values());		
		return res;
	}
	/**
	 * Passe à l'étape suivante de la simulation.
	 * Exécute la production, la transformation et la gestion du stock.
	 */
	public void next() {
		super.next();
		this.feveToEqui();
		this.feveToBio();

		this.journalProduction.ajouter("On a ces quantites:"+ this.Stocck.get(Feve.F_BQ)+ "en stock pour la gamme BQ en  :"+ Filiere.LA_FILIERE.getEtape());
		this.journalProduction.ajouter("On a ces quantites:"+ this.Stocck.get(Feve.F_MQ)+ "en stock pour la gamme MQ en  :"+ Filiere.LA_FILIERE.getEtape());
		this.journalProduction.ajouter("On a ces quantites:"+ this.Stocck.get(Feve.F_HQ)+ "en stock pour la gamme HQ en  :"+ Filiere.LA_FILIERE.getEtape());

		double totalStock = 0;
		for (Feve f : Feve.values()) {
			this.stock.get(f).ajouter(this,this.getProd().get(f) );
			totalStock += this.stock.get(f).getValeur();
		}
		this.getJournaux().get(0).ajouter("Stock= "+ totalStock);
		double coutame = this.AmeliorationStockage() ? 2 : 0;
		/*
		 * Le cout de stockage par ton augmentera de 2 PM si on a decide d'ameliorer le stock
		 * 
		 */
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*(this.getCoutStockage()+coutame));
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme,"Cout Prod",this.CoutsProd());
		this.Stock_HQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));
		this.Stockage_HQ();
		this.Stock_MQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));
		this.Stockage_MQ();
		this.Stock_BQ.add(this.getQuantiteEnStock(Feve.F_BQ, cryptogramme));
		this.Stockage_BQ();
		if (this.AmeliorationStockage() || this.degrHQ > 4 || this.degrMQ > 8 || this.degrBQ > 12) {
			this.degrHQ += 2;
			this.degrMQ += 4;
			this.degrBQ += 6;
		}


	}
	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalProduction);
		return res;

	}
	/*
	 * Une fonction qui ajoute l'option d'ameliorer les condtions de stockage pour 
	 * que les feves ne pourraient pas aussi rapidement
	 * On traveillera sous la condition que si le stock depasse une quantite on aimera le reserver mieux
	 */
	public boolean AmeliorationStockage() {

		double stockHQ = this.getQuantiteEnStock(Feve.F_HQ, cryptogramme) + this.getQuantiteEnStock(Feve.F_HQ_E, cryptogramme)+this.getQuantiteEnStock(Feve.F_HQ_BE, cryptogramme);
		double stockMQ = this.getQuantiteEnStock(Feve.F_MQ, cryptogramme)+ this.getQuantiteEnStock(Feve.F_MQ_E, cryptogramme);;
		double stockBQ = this.getQuantiteEnStock(Feve.F_BQ, cryptogramme);
		boolean ame = stockHQ > 50 && stockMQ > 50 && stockBQ > 100;
		this.journalStockage.ajouter("On a decide d'ameliorer le stockae pour mieux garder les feves");
		return ame;
	}




}