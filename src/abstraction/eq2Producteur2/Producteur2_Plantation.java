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
	private static final int START_YEARS = 2024;
	private static final double nb_hectares_initiaux = 3000000;
	private static final double nb_max_hectares = 7000000;
	private static final int DUREE_VIE = 40;
	
	protected static final int PRIX_HECTARE_BQ = 500;
	protected static final int PRIX_HECTARE_MQ = 1000;
	protected static final int PRIX_HECTARE_HQ = 1500;
	
	protected double nb_nouveaux_hectares; // hectares nouvellement plantés sur 2 semaines


	// Simplement pour l'initialisation de la plantation
	protected int pourcentage_HQ;
	protected int pourcentage_MQ;
	protected int pourcentage_BQ;
	
	protected double rend_pest_BQ = 0.9;
	protected double rend_pest_MQ = 0.85;
	protected double rend_pest_HQ = 0.80;

	protected double rend_no_pest_HQ = 0.72;
	protected HashMap <Feve, HashMap<Integer, Double>> plantation;
	protected int annee_actuelle;
	double cout_du_tour;
	
	
	protected Journal journalPlantation;
	
	/** Constructeur de classe
	 * @author Anthony
	 */
	public Producteur2_Plantation() {
		this.journalPlantation =new Journal(this.getNom()+" journal Plantation",this);
		this.plantation = new HashMap <Feve, HashMap<Integer, Double>>();
	}
	
	/** Initialisation
	 * @author Anthony
	 */
	public void initialiser() {
		super.initialiser();
		this.setPourcentage_HQ(2);
		this.setPourcentage_MQ(38);
		this.setPourcentage_BQ(60);	
		this.annee_actuelle = 2024;	
		this.init_simu_feve();
	}
	
	/** Initialise la HashMap des hectares pour débuter la simulation
	 * @author Anthony
	 */
	public void init_simu_feve() {
		
		//création de la HashMap à l'intérieur de plantation pour hectare BQ
        HashMap<Integer, Double> hectare_BQ = new HashMap<>();

        for(int i  = 0; i < 40; i++) {
        	hectare_BQ.put(Producteur2_Plantation.START_YEARS + i,(nb_hectares_initiaux / 40) * getPourcentage_BQ()/100);
        }
        plantation.put(Feve.F_BQ,hectare_BQ);


        //création de la HashMap à l'intérieur de plantation pour hectare HQ
        HashMap<Integer, Double> hectare_HQ = new HashMap<>();
        for(int i  = 0; i < 40; i++) {

        	hectare_HQ.put(Producteur2_Plantation.START_YEARS + i,(nb_hectares_initiaux / 40) * getPourcentage_HQ()/100);
        }
        plantation.put(Feve.F_HQ,hectare_HQ);

        HashMap<Integer, Double> hectare_MQ = new HashMap<>();

        for(int i  = 0; i < 40; i++) {

        	hectare_MQ.put(Producteur2_Plantation.START_YEARS + i,(nb_hectares_initiaux / 40) * getPourcentage_MQ()/100);
        }
        plantation.put(Feve.F_MQ,hectare_MQ);
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
	public void planter(double nb_hectares, Feve f) {
		// Il n'y a pas d'hectares MQ_E ou HQ_E donc il faut définir une nouvelle variable
		Feve qualite;
		if (f == Feve.F_HQ || f == Feve.F_HQ_E) {
			qualite = Feve.F_HQ;
		}
		else if (f == Feve.F_MQ || f == Feve.F_MQ_E) {
			qualite = Feve.F_MQ;
		}
		else {
			qualite = Feve.F_BQ;
		}
		
		if (this.plantation.containsKey(annee_actuelle + DUREE_VIE)) {
			double deja_achetes_cette_annee = this.plantation.get(qualite).get(annee_actuelle + DUREE_VIE);
			plantation.get(qualite).put(annee_actuelle + DUREE_VIE, deja_achetes_cette_annee + nb_hectares);
		}
		else {
			plantation.get(qualite).put(annee_actuelle + DUREE_VIE, nb_hectares);
		}
		if (qualite == Feve.F_HQ) {
			// On ne fait que de la haute qualité équitable
			this.embauche((int) nb_hectares, "adulte équitable");
			cout_du_tour = cout_du_tour + PRIX_HECTARE_HQ * nb_hectares; 
		}
		else {
			this.embauche((int) nb_hectares, "adulte");
			if (qualite == Feve.F_BQ) {
				cout_du_tour = cout_du_tour + PRIX_HECTARE_BQ * nb_hectares; 
			}
			else {
				qualite = Feve.F_BQ;
			}
			
			if (this.plantation.containsKey(annee_actuelle + DUREE_VIE)) {
				double deja_achetes_cette_annee = this.plantation.get(qualite).get(annee_actuelle + DUREE_VIE);
				plantation.get(qualite).put(annee_actuelle + DUREE_VIE, deja_achetes_cette_annee + nb_hectares);
			}
			else {
				plantation.get(qualite).put(annee_actuelle + DUREE_VIE, nb_hectares);
			}
			if (qualite == Feve.F_HQ) {
				// On ne fait que de la haute qualité équitable
				this.embauche((int) nb_hectares, "adulte équitable");
				cout_du_tour = cout_du_tour + PRIX_HECTARE_HQ * nb_hectares; 
			}
			else {
				this.embauche((int) nb_hectares, "adulte");
				if (qualite == Feve.F_BQ) {
					cout_du_tour = cout_du_tour + PRIX_HECTARE_BQ * nb_hectares; 
				}
				else {
					cout_du_tour = cout_du_tour + PRIX_HECTARE_MQ * nb_hectares; 
				}
			}
		}
	} 

	/** retourne la production actuelle de cacao sur 2 semaines en tonnes
	 * @author Anthony
	 */
	public void production_cacao() {
		double nb_hectares_BQ = getHectaresPlantes(Feve.F_BQ, this.cryptogramme);
		double nb_hectares_MQ = getHectaresPlantes(Feve.F_MQ, this.cryptogramme);
		double nb_hectares_HQ = getHectaresPlantes(Feve.F_HQ, this.cryptogramme);
		double total_hectare = this.getHectaresTotal(this.cryptogramme);
		
		double production_BQ;
		double production_MQ;
		double production_HQ;
		double production_HQ_E;
		
		// Il faut 3 enfants pour s'occuper de 2 hectares
		double main_oeuvre_BQ_MQ = this.getNb_employes_enfants()*2/3 +this.getNb_employes()-this.getNb_employes_equitable();
		double nb_employes_equitable = this.getNb_employes_equitable();
		
		// Si on n'a pas assez de main d'oeuvre pour s'occuper de tous les hectares de notre plantation
		if (nb_employes_equitable +  main_oeuvre_BQ_MQ  < total_hectare) {

			// Production en tonnes
			// 48 = 0.5 tonnes de fèves par an par hectare / 24 steps
			this.embauche((long)(total_hectare - nb_employes_equitable +  main_oeuvre_BQ_MQ) , "adulte");
			double peut_produire_BQ = main_oeuvre_BQ_MQ*0.75;
			double peut_produire_MQ = main_oeuvre_BQ_MQ - peut_produire_BQ;
			production_BQ = peut_produire_BQ/48*rend_pest_BQ;
			production_MQ = peut_produire_MQ/48*rend_pest_MQ;
			
			// HQ et HQE
			double prod_HQ_E = Math.max(nb_employes_equitable, nb_hectares_HQ);
			production_HQ_E = prod_HQ_E/48*rend_pest_HQ;
			
			// On ne fait pas de HQ non equitable sauf s'il n'y a pas assez d'employes en équitable
			production_HQ = 0;
			if (nb_employes_equitable < nb_hectares_HQ) {
				double peut_produire_HQ = Math.max(0, main_oeuvre_BQ_MQ - peut_produire_BQ - peut_produire_MQ);
				production_HQ = (peut_produire_HQ /48*rend_pest_HQ);
			}
		}
		
		else {
			// Production en tonnes
			production_BQ = nb_hectares_BQ/48*rend_pest_BQ;
			production_MQ = nb_hectares_MQ/48*rend_pest_MQ;
			
			// HQ et HQE			
			production_HQ_E = nb_employes_equitable/48*rend_pest_HQ;
		
			// On ne fait pas de HQ non equitable sauf s'il n'y a pas assez d'employes en équitable
			production_HQ = 0;
			if (nb_employes_equitable < nb_hectares_HQ) {
				
				production_HQ = (nb_hectares_HQ - nb_employes_equitable /48*rend_pest_HQ);
			}
			else {
				this.planter(nb_hectares_HQ*0.05, Feve.F_HQ);
			}
		}
		
		ajout_stock(Feve.F_BQ, production_BQ);
		ajout_stock(Feve.F_MQ, production_MQ);
		ajout_stock(Feve.F_HQ, production_HQ);
		ajout_stock(Feve.F_HQ_E, production_HQ_E);
		
		
		this.prodParStep.put(Feve.F_BQ, production_BQ);
		this.prodParStep.put(Feve.F_MQ, production_MQ);
		this.prodParStep.put(Feve.F_HQ, production_HQ);
	    this.prodParStep.put(Feve.F_HQ_E, production_HQ_E);
	    
	    ajout_plantation_journal(production_HQ_E,production_MQ,production_BQ);
	}
	
	/** Retourne le coût de plantation par rapport au nombre de nouveaux hectares plantés
	 * @author Anthony
	 */
	public double cout_plantation() {
		double cout = cout_du_tour;
		// On remet à 0 pour le prochain tour
		this.cout_du_tour = 0;
		return cout;
	}
	
	/** Retourne le nombre d'hectares pour un type de fève
	 * @author Quentin
	*/
	public double getHectaresPlantes(Feve qualite_feve, int cryptogramme) {
		if(this.cryptogramme==cryptogramme) { // c'est donc bien un acteur assermente qui demande a consulter le nombre d'hectares
			double somme = 0;
			if (qualite_feve == Feve.F_HQ ||qualite_feve == Feve.F_HQ_E) {
				for(Integer annee : this.getPlantation().get(Feve.F_HQ).keySet()) {
					somme += this.getPlantation().get(Feve.F_HQ).get(annee);
				}
			}
			else if (qualite_feve == Feve.F_MQ ||qualite_feve == Feve.F_MQ_E) {
				for(Integer annee : this.getPlantation().get(Feve.F_MQ).keySet()) {
					somme += this.getPlantation().get(Feve.F_MQ).get(annee);
				}
			}
			else {
				for(Integer annee : this.getPlantation().get(Feve.F_BQ).keySet()) {
					somme += this.getPlantation().get(Feve.F_BQ).get(annee);
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
	
	/** Supprime les hectares qui ont dépassé leur âge limite de 40 ans
	 * @author Quentin
	 */
	public void suppressionVieuxHectares() {
		this.plantation.get(Feve.F_BQ).remove(annee_actuelle);
		this.plantation.get(Feve.F_MQ).remove(annee_actuelle);
		this.plantation.get(Feve.F_HQ).remove(annee_actuelle);	
	}
	
	/** Ajoute les nouvelles informations sur les plantations au journal des plantations
	 * @author Anthony
	*/
	public void ajout_plantation_journal(double prod_HQ_E, double prod_MQ, double prod_BQ){
		this.journalPlantation.ajouter(" ");
		this.journalPlantation.ajouter("------------ ETAPE " + Filiere.LA_FILIERE.getEtape() + " ---------------");
		//this.journalPlantation.ajouter("Cout de la plantation : " + cout_plantation());
		if (nb_nouveaux_hectares == 0) {
			this.journalPlantation.ajouter("Pas de nouveaux hectares achetés ");
		}
		else {
			this.journalPlantation.ajouter("Nouveau hectares achetes : " + nb_nouveaux_hectares);
		}
		this.journalPlantation.ajouter(" ");
		this.journalPlantation.ajouter("a ce tour on a produit :");
		this.journalPlantation.ajouter(" .      " + prod_BQ + " tonnes de fèves de basse qualité");
		this.journalPlantation.ajouter(" .      " + prod_MQ + " tonnes de fèves de moyenne qualité");
		this.journalPlantation.ajouter(" .      " + prod_HQ_E + " tonnes de fèves de haute qualité equitable");
	}
	
	/** Méthode permettant de savoir quand il faut embaucher et planter en fonction de la quantité de stock restante
	 * @author Noémie
	 */
	public void besoin_embauche() {
		double stock_BQ = this.getQuantiteEnStock(Feve.F_BQ, this.cryptogramme);
		double stock_MQ = this.getQuantiteEnStock(Feve.F_MQ, this.cryptogramme);
		double stock_HQ_E = this.getQuantiteEnStock(Feve.F_HQ_E, this.cryptogramme);
		
		if (stock_BQ < 1000) {
			this.embauche((int) (this.getNb_Employes_total()*0.02), "adulte");
			this.planter((int) (this.getNb_Employes_total()*0.02), Feve.F_BQ);	
		}
		if (stock_MQ < 1000) {
			this.embauche((int) (this.getNb_Employes_total()*0.02), "adulte");
			this.planter((int) (this.getNb_Employes_total()*0.02), Feve.F_MQ);
		}
		
		if (stock_HQ_E < 1000 && this.getPourcentage_equitable() < 20 ) {
			this.embauche((int) (this.getNb_Employes_total()*0.02), "adulte équitable");
			this.planter((int) (this.getNb_Employes_total()*0.02), Feve.F_HQ);
		}
	}
	
	
	/**Cette fonction next sert à implémenter notre stratégie.
	 * Si notre stock est vide à la fin d'un tour cela implique que nous avons vendu l'intégralité
	 * de notre production. Il peut être utile d'acheter de nouveaux hectares pour produire plus de cacao.
	 * @author Noémie, Quentin
	 */
	public void next(){
		super.next();
		production_cacao();
		ajout_stock_journal(); //pour avoir le journal mis à jour après la production de fèves
		for (Feve f : Feve.values()) {
			if (f != Feve.F_HQ_BE) {
				this.prod_step.get(f).setValeur(this, this.prodParStep.get(f));
			}
		}
		
		if((this.START_YEARS + (int)(Filiere.LA_FILIERE.getEtape()/24)) != this.annee_actuelle) {
			suppressionVieuxHectares();
			this.annee_actuelle = this.START_YEARS +(int)(Filiere.LA_FILIERE.getEtape()/24);
		}
		this.besoin_embauche();
	}
} 