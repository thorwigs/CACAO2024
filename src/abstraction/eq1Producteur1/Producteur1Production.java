/**
 * Représente un gestionnaire de production pour un producteur spécifique dans la chaîne d'approvisionnement.
 * Gère la production, la transformation et le stockage des fèves de cacao.
 */
package abstraction.eq1Producteur1;

import java.util.ArrayList;
import abstraction.eq1Producteur1.*;
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

	//protected HashMap<Feve, Variable> stockag;
	/**
    * Constructeur pour la classe Producteur1Production.
    * Initialise les journaux, le stock et d'autres structures de données nécessaires.
    */
	public Producteur1Production() {
		// Initialisation des journaux, du stock et d'autres structures de données
		super();
		this.journalProduction = new Journal(this.getNom()+"   journal Production",this);
		this.journalProduction = new Journal(this.getNom()+"   journal Stockage",this);
		this.ageStock = new HashMap<String, Integer>();
		this.Stock_HQ = new ArrayList<Double>();
		this.Stock_MQ = new ArrayList<Double>();
		this.Stock_BQ = new ArrayList<Double>();
		this.prodParStep = this.ProdParStep();
		this.Stocck = this.InitiStock();
		
		this.stock = this.IniStock();
		for (Feve feve : this.stock.keySet()) {
			String s = feve.toString()+"/" +this.getQuantiteEnStock(feve, cryptogramme);
			this.ageStock.put(s, 0);
			//this.journalProduction.ajouter(s+ " a l'age suivant"+ this.ageStock.get(s));
		}
		
		this.Stock_HQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));

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
		
		
		int nb = this.liste_Ouvrier.GetNombreOuvrierEquitable();
		int nbe = this.liste_Ouvrier.getNombreEnfants();
		int nbt = this.liste_Ouvrier.GetNombreOuvrierEquitable()+ this.liste_Ouvrier.GetNombreOuvrierNonEquitable()+this.liste_Ouvrier.getNombreOuvrierFormés();
		
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
     */
	public void feveToBio() {
		if (this.pesticides) {
			this.journalProduction.ajouter("On ne peut pas faire du bio car on a utilise des pesticides");
		}
		else {	boolean bio = true;
		if (this.prodParStep.get(Feve.F_HQ_E) !=0) {
			this.prodParStep.put(Feve.F_HQ_BE, null);
		}
		}

	}
	/**
     * Gère le stockage des fèves de cacao de haute qualité.
     * Initie le transfert de stock en fonction de certaines conditions.
     */
	public void Stockage_HQ() {

		if (this.Stock_HQ.size() >= 4) {
			boolean check = true;
			for (int i  =this.Stock_HQ.size()-4; i<this.Stock_HQ.size()-1 ;i++) {
				if (this.Stock_HQ.get(i+1) <= this.Stock_HQ.get(i)) {
					
					check = false;
					break; // Exit the loop if any decrease in stock is found
				}


			}
			if (check) {
				
				
				double hq = this.Stocck.get(Feve.F_HQ);
				double pro = this.Stocck.get(Feve.F_MQ);
				this.stock.get(Feve.F_HQ).retirer(this, this.Stock_HQ.get(this.Stock_HQ.size()-4));
				this.stock.get(Feve.F_MQ).setValeur(this, pro + this.Stock_HQ.get(this.Stock_HQ.size()-4));
				this.Stocck.put(Feve.F_HQ, hq - this.Stock_HQ.get(this.Stock_HQ.size()-4));
				this.Stocck.put(Feve.F_MQ, pro + this.Stock_HQ.get(this.Stock_HQ.size()-4));
				this.journalStockage.ajouter("On a eu une degradation de stock de HQ vers MQ de la quantite suivante:"+this.Stock_HQ.get(this.Stock_HQ.size()-4) );
				this.Stock_HQ.remove(this.Stock_HQ.size()-4);

			}
		}

	}
	/**
     * Gère le stockage des fèves de cacao de qualité moyenne.
     * Initie le transfert de stock en fonction de certaines conditions.
     */
	public void Stockage_MQ() {
		
		if (this.Stock_MQ.size() >= 8) {
			boolean check = true;
			int j = this.Stock_MQ.size()-8;
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
	    if (this.Stock_MQ.size() >= 12) {
	        boolean check = true;
	        int j = this.Stock_MQ.size() - 12;
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
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Stockage", totalStock*this.getCoutStockage());
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme,"Cout Prod",this.CoutsProd());
		this.Stock_HQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));
		this.Stockage_HQ();
		this.Stock_MQ.add(this.getQuantiteEnStock(Feve.F_HQ, cryptogramme));
		this.Stockage_MQ();
		this.Stock_BQ.add(this.getQuantiteEnStock(Feve.F_BQ, cryptogramme));
		this.Stockage_BQ();

	}
	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalProduction);
		return res;

	}




}