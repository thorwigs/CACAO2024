 package abstraction.eq2Producteur2;

import java.util.HashMap;
import java.util.List;
 
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

//Toutes les variables de poids de cacao sont en TONNES 

/** Classe permettant de gérer les plantations
 * @author Anthony
 */
 
public abstract class Producteur2_Plantation extends Producteur2_MasseSalariale {
	
	/** Définition des variables
	 * @author Anthony
	 */
	protected double nb_hectares_max;
	protected double nb_hectares_actuel;
	protected double prix_plantation_hectare;
	protected double nb_nouveaux_hectares; // hectares nouvellement plantés sur 2 semaines

	//protected int qualite;
	protected int pourcentage_HQ;
	protected int pourcentage_MQ;
	protected int pourcentage_BQ;
	
	protected double rend_pest_BQ = 0.9;
	protected double rend_pest_MQ = 0.85;
	protected double rend_pest_HQ = 0.80;

	protected double rend_no_pest_HQ = 0.72;
	protected HashMap <Feve, HashMap<Integer, Double>> plantation;
	
	protected Journal journalPlantation;
	
	/** Constructeur de classe
	 * @author Anthony
	 */
	
	public void init_simu_feve() { //initialise la HashMap pour débuter la simulation
		
	}
	
	public Producteur2_Plantation() {

		this.nb_hectares_actuel=5000000.0;
		this.nb_hectares_max=5000000.0*2;
		this.prix_plantation_hectare=500.0;
		this.journalPlantation =new Journal(this.getNom()+" journal Plantation",this);
	}
	
	/** Getter
	 * @author Anthony
	 */
	public double getNb_hectares_max() {
		return this.nb_hectares_max;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setNb_hectares_max(double nb_hectares_max) {
		this.nb_hectares_max = nb_hectares_max;
	}
	
	/** Getter
	 * @author Anthony
	 */
	public double getNb_hectares_actuel() {
		return nb_hectares_actuel;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setNb_hectares_actuel(double nb_hectares_actuel) {
		this.nb_hectares_actuel = nb_hectares_actuel;
	}

	/** Getter
	 * @author Anthony
	 */
	public double getPrix_plantation_hectare() {
		return this.prix_plantation_hectare;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setPrix_plantation_hectare(double prix_plantation_hectare) {
		this.prix_plantation_hectare = prix_plantation_hectare;
	}
	
	/** Getter
	 * @author Anthony
	 */
	public int getPourcentage_HQ() {
		return this.pourcentage_HQ;
	}
	
	/** Setter
	 * @author Anthony
	 */
	public void setPourcentage_HQ(int pourcentage_HQ) {
		this.pourcentage_HQ = pourcentage_HQ;
	}

	/** Getter
	 * @author Anthony
	 */
	public int getPourcentage_MQ() {
		return pourcentage_MQ;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setPourcentage_MQ(int pourcentage_MQ) {
		this.pourcentage_MQ = pourcentage_MQ;
	}

	/** Getter
	 * @author Anthony
	 */
	public int getPourcentage_BQ() {
		return pourcentage_BQ;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setPourcentage_BQ(int pourcentage_BQ) {
		this.pourcentage_BQ = pourcentage_BQ;
	}
	
	/** Getter
	 * @author Anthony
	 */
	public double getNb_nouveau_hectares() {
		return nb_nouveaux_hectares;
	}

	/** Setter
	 * @author Anthony
	 */
	public void setNb_nouveau_hectares(double nb_nouveau_hectares) {
		this.nb_nouveaux_hectares = nb_nouveau_hectares;
	}
	
	/** Getter
	 * @author Quentin
	 */
	public HashMap <Feve, HashMap<Integer, Double>> getPlantation(){
		return this.plantation;
	}
	
	/** Setter
	 * @author Quentin
	 */
	public void setPlantation(HashMap <Feve, HashMap<Integer, Double>> plantation) {
		this.plantation = plantation;
	}
	
	/** Initialisation
	 * @author Anthony
	 */
	public void initialiser() {
		super.initialiser();
		this.setPourcentage_HQ(2);
		this.setPourcentage_MQ(38);
		this.setPourcentage_BQ(60);
		
	}
	
	/** Méthode pour obtenir les journaux
	 * @author Anthony
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalPlantation);
		return jx;
	}
	
	/** Méthode permettant de planter des cacaoyers sur un nombre d'hectares donné
	 * @param nb_hectares
	 * @author Anthony
	 */
	public void planter(double nb_hectares) {
		long nb_hectares_possible =	this.getNb_employes() + this.getNb_employes_equitable() + 2/3*this.getNb_employes_enfants();
		
		//Si on a assez de terrain mais pas assez d'employes pour gérer la plantation
		if (nb_hectares_possible < this.getNb_hectares_actuel()) {
			this.embauche((int)(this.getNb_hectares_actuel() - nb_hectares_possible),"adulte");
		}
		
		//Si on a assez de personnel mais pas assez de terrain
		else {
			if (getNb_hectares_actuel() + nb_hectares > getNb_hectares_max()) { //achat impossible
				this.journal.ajouter("on ne peut pas acheter plus de terrain.");
				return;
			}
			else { 
		 		setNb_hectares_actuel(getNb_hectares_actuel() + nb_hectares);
				// On embauche le même nombre de personnes que d'hectares car 1 hectare = 1 employé
				// 90% des personnes embauchées sont embauchées en tant qu'employé non-équitable
				int nb_employes = (int)(nb_hectares*0.9);
				this.embauche((int) nb_employes, "adulte");
				this.embauche((int) nb_hectares - nb_employes, "adulte équitable");
			}
		}
	}

	/** retourne la production actuelle de cacao sur 2 semaines en tonnes
	 * @author Anthony
	 */
	public long production_cacao() {
		
	// nb_hectares_possible représente le nombre d'hectares dont peuvent s'occuper les employés 
		long nb_hectares_possible =	this.getNb_employes() + this.getNb_employes_equitable() + 2/3*this.getNb_employes_enfants();
		long nb_hectares_production = Math.min(nb_hectares_possible, (long) this.getNb_hectares_actuel());
		long quantite_feve = nb_hectares_production/48; // 48 = 0.5 (tonnes) / 24 (tours de jeu en un an)
		return quantite_feve;
	}
	
	/** Retourne la production de cacao de haute qualité sur 2 semaines en tonnes
	 * @author Anthony
	 */
	public double production_HQ() {
		return production_cacao()* getPourcentage_HQ()/100;
	}
	
	/** Retourne la production de cacao de basse qualité sur 2 semaines en tonnes
	 * @author Anthony
	 */
	public double production_BQ() {
		return production_cacao() * getPourcentage_BQ()/100;
	}
	
	/** Retourne la production de cacao de moyenne qualité sur 2 semaines en tonnes
	 * @author Anthony
	 */
	public double production_MQ() {
		return production_cacao() * getPourcentage_MQ()/100;
	}
	
	/** Retourne la production de cacao HQ_BE après calculs des rendements en tonnes
	 * @author Anthony
	 */
	public double get_prod_no_pest_HQ() { 
		return this.production_HQ() * rend_no_pest_HQ; //feve HQ_BE
	}
	
	/** Retourne la production de cacao HQ_E après calculs des rendements en tonnes
	 * @author Anthony
	 */
	public double get_prod_pest_HQ() {
		return this.production_HQ() * rend_pest_HQ; //feve HQ_E et HQ=0
	}
	
	/** Retourne la production de cacao MQ après calculs des rendements en tonnes
	 * @author Anthony
	 */
	public double get_prod_pest_MQ() {
		return this.production_MQ() * rend_pest_MQ; //feve MQ et MQ_E=0
	}
	
	/** Retourne la production de cacao BQ après calculs des rendements en tonnes
	 * @author Anthony
	 */
	public double get_prod_pest_BQ() {
		return this.production_BQ() * rend_pest_BQ; //feve BQ
	}
	
	/** Ajoute la production sur 2 semaines aux stocks
	 * @author Anthony
	 */
	public void nouveau_stock() { // ajoute la production sur 2 semaines aux stocks
		ajout_stock(Feve.F_BQ, this.get_prod_pest_BQ());
		ajout_stock(Feve.F_MQ, this.get_prod_pest_MQ());
		ajout_stock(Feve.F_HQ_E, this.get_prod_pest_HQ());
	}	
	
	/** Permet d'ajouter à la liste la production obtenue par étape
	 * @author Anthony
	 */
	public void modifie_prodParStep() {
	    this.prodParStep.put(Feve.F_HQ_E, this.get_prod_pest_HQ());
	    this.prodParStep.put(Feve.F_MQ, this.get_prod_pest_MQ());
	    this.prodParStep.put(Feve.F_BQ, this.get_prod_pest_BQ());
	}
	
	/** Retourne le coût de plantation par rapport au nombre de nouveaux hectares plantés
	 * @author Anthony
	 */
	public double cout_plantation() {
		double res = getNb_nouveau_hectares() * getPrix_plantation_hectare();
		setNb_nouveau_hectares(0);
		return res;
	}
	
	/** Simule la quantité de plantations que l'on perd à chaque étape
	 * @author Anthony
	 */
	public void perte_plantation() {
		double pourcentage_perte = 0.00104;
		double perte_un_next = pourcentage_perte * getNb_hectares_actuel();
		setNb_hectares_actuel(getNb_hectares_actuel() - perte_un_next);
		this.planter(perte_un_next);
	}
	
	/** Met à jour les pourcentages de BQ, MQ et HQ en fonction des catégories d'employés
	 * @author Noémie
	 */
	public void maj_pourcentage() {
		int pourcentage_equitable = (int) Math.round(this.getPourcentage_equitable());
		int diff = pourcentage_equitable - getPourcentage_HQ();
		// On choisit la catégorie de fève qu'on produit le plus entre MQ et BQ pour baisser son pourcentage
		if (getPourcentage_MQ() < getPourcentage_BQ()) {
			if (getPourcentage_BQ()-diff > 0) {
				this.setPourcentage_BQ(getPourcentage_BQ()-diff);
				this.setPourcentage_HQ(pourcentage_equitable);
			}
		}
		else {
			if (getPourcentage_MQ()-diff > 0) {
				this.setPourcentage_MQ(getPourcentage_MQ()-diff);
				this.setPourcentage_HQ(pourcentage_equitable);
			}
		}
	}
	
	/** Retourne le nombre d'hectares pour un type de produit (un type de fève)
	 * @author Quentin
	*/
	public double getHectaresPlantes(IProduit p, int cryptogramme) {
		if(this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter le nombre d'hectares
			double somme = 0;
			for(Feve f : this.getPlantation().keySet()) {
				if(f == p) {
					for(Integer annee : this.getPlantation().get(f).keySet()) {
						somme += this.getPlantation().get(f).get(annee);
					}
				}
			}
			return somme;
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre notre nombre d'hectares par produit
		}	
	}
	
	/** Retourne la quantité totale d'hectares plantés
	 * @param cryptogramme
	 * @author Quentin
	 */
	public double getHectaresTotal(int cryptogramme) {
		if (this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter le nombre d'hectares total
			double somme = 0;
			for(Feve f : this.getPlantation().keySet()) {
				for(Integer annee : this.getPlantation().get(f).keySet()) {
					somme += this.getPlantation().get(f).get(annee);
				}
			}
			return somme;
		} else {
			return 0; // Les acteurs non assermentes n'ont pas a connaitre le nombre d'hectares total
		}
	}
	
	/** Ajoute les nouvelles informations sur les plantations au journal des plantations
	 * @author Anthony
	 */
	public void ajout_plantation_journal() {
		this.journalPlantation.ajouter(" ");
		this.journalPlantation.ajouter("------------ ETAPE " + Filiere.LA_FILIERE.getEtape() + " ---------------");
		this.journalPlantation.ajouter("Cout de la plantation : " + cout_plantation());
		if (nb_nouveaux_hectares == 0) {
			this.journalPlantation.ajouter("Pas de nouveaux hectares achetés ");
		}
		else {
			this.journalPlantation.ajouter("Nouveau hectares achetes : " + nb_nouveaux_hectares);
		}
		this.journalPlantation.ajouter(" ");
		this.journalPlantation.ajouter("a ce tour on a produit :");
		this.journalPlantation.ajouter(" .      " + production_BQ() + " tonnes de fèves de basse qualité");
		this.journalPlantation.ajouter(" .      " + production_MQ() + " tonnes de fèves de moyenne qualité");
		this.journalPlantation.ajouter(" .      " + production_HQ() + " tonnes de fèves de haute qualité");
	}
	
	
	/**Cette fonction next sert à implémenter notre stratégie.
	 * Si notre stock est vide à la fin d'un tour cela implique que nous avons vendu l'intégralité
	 * de notre production. Il peut être utile d'acheter de nouveaux hectares pour produire plus de cacao.
	 * @author Noémie
	 */
	public void next() {
		super.next();
		modifie_prodParStep();
		for (Feve f : Feve.values()) {
			this.prod_step.get(f).setValeur(this, this.prodParStep.get(f));
		}
		
		//On place dans le stock tout ce qu'on produit en un tour
		this.nouveau_stock();
		
		ajout_plantation_journal();
		perte_plantation(); //Perte quotidienne d'arbres
		maj_pourcentage();

		//On décide si on achète de nouveaux hectares
		if (getStockTotal(this.cryptogramme) <= 0.0) {
			if (nb_hectares_actuel * 1.02 > nb_hectares_max) {
				planter(nb_hectares_max - nb_hectares_actuel);
				setNb_nouveau_hectares(nb_hectares_max - nb_hectares_actuel);
			}
			else {
				planter(nb_hectares_actuel * 0.02); //On replante 2% de la plantation actuelle
				setNb_nouveau_hectares(nb_hectares_actuel * 0.02);
			}
		}
	}
} 