package abstraction.eq3Producteur3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
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
	protected Journal journal_bourse; 
	protected Journal journal_contrat_cadre;
	private HashMap<IProduit,Integer> stocks;
	//passable en parametre/indicateurs
	private double coutUnitaireProductionBQ = 1.0;
    private double coutUnitaireProductionMQ = 1.5;
    private double coutUnitaireProductionHQ = 2.0;
    //creation d'un tableau de variables qui donne la production pour chaque type de feve
    //@alexis
    protected HashMap<Feve, Variable> prodfeve;
    //creation d'un tableau de variables qui donne les ventes pour chaque type de feve 
    //@alexis
    protected HashMap<Feve, Variable> ventefeve;
    //creation d'un tableau de variables qui donne les stocks pour chaque type de feve 
    //@alexis
    protected HashMap<Feve, Variable> stockfeve;
    
    protected HashMap<Feve,Variable> plantations; //variable qui suit la surface de plantation HQ_BE @author Arthur
  
    
    protected HashMap<Feve, Double> ventefevebourse;
    protected HashMap<Feve, Double> ventefevecadre;
    protected HashMap<Feve,HashMap<Integer,Double>> stockGammeStep;
    protected HashMap<Feve,HashMap<Integer,Double>> coutGammeStep;
    //abstract
    abstract void deleteAcheteurs(IAcheteurBourse acheteur);
    abstract void deleteVendeurs(IVendeurBourse vendeur);
    abstract HashMap<Feve,Double> quantite();
    abstract void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1);
    abstract HashMap<Feve,Double> maindoeuvre();
	protected abstract HashMap<Feve,Double> newQuantite();
	
	private HashMap<Feve,Double> production_initial = new HashMap<Feve,Double>();
	
	
	public Producteur3Acteur() {
		this.journal = new Journal(this.getNom()+" journal",this);
		this.journal_bourse = new Journal(this.getNom()+" journal bourse",this);
		this.journal_contrat_cadre = new Journal(this.getNom()+" journal contrat cadre",this);
		//@Alexis
		this.prodfeve = new HashMap<Feve,Variable>();
		this.ventefeve = new HashMap<Feve,Variable>();
		this.stockfeve = new HashMap<Feve,Variable>();
		this.ventefevebourse = new HashMap<Feve, Double>();
		this.ventefevecadre = new HashMap<Feve, Double>();
		this.plantations = new HashMap<Feve,Variable>();
		//VALEURS INITIALES
		//BQ
		this.ventefeve.put(Feve.F_BQ,  new Variable("Eq3Vente "+Feve.F_BQ, this, 3600));
		this.stockfeve.put(Feve.F_BQ,  new Variable("Eq3Stock "+Feve.F_BQ, this, 22372));
		this.prodfeve.put(Feve.F_BQ,  new Variable("Eq3Prod "+Feve.F_BQ, this, 3790));
		this.ventefevebourse.put(Feve.F_BQ, 3600.0);
		this.ventefevecadre.put(Feve.F_BQ, 0.0);
		this.plantations.put(Feve.F_BQ, new Variable("Plantation "+Feve.F_BQ,this,134775));
		//MQ
		this.ventefeve.put(Feve.F_MQ,  new Variable("Eq3Vente "+Feve.F_MQ, this, 2024));
		this.stockfeve.put(Feve.F_MQ,  new Variable("Eq3Stock "+Feve.F_MQ, this, 3900));
		this.prodfeve.put(Feve.F_MQ,  new Variable("Eq3Prod "+Feve.F_MQ, this, 1263));
		this.ventefevebourse.put(Feve.F_MQ, 2024*0.8);
		this.ventefevecadre.put(Feve.F_MQ, 2024*0.2);
		this.plantations.put(Feve.F_MQ, new Variable("Plantation "+Feve.F_MQ,this,47570));
		//MQ_E
		this.ventefeve.put(Feve.F_MQ_E,  new Variable("Eq3Vente "+Feve.F_MQ_E, this, 290));
		this.stockfeve.put(Feve.F_MQ_E,  new Variable("Eq3Stock "+Feve.F_MQ_E, this, 986));
		this.prodfeve.put(Feve.F_MQ_E,  new Variable("Eq3Prod "+Feve.F_MQ_E, this, 315));
		this.ventefevebourse.put(Feve.F_MQ_E, 290.0);
		this.ventefevecadre.put(Feve.F_MQ_E, 0.0);
		this.plantations.put(Feve.F_MQ_E, new Variable("Plantation "+Feve.F_MQ_E,this,11890));
		//HQ
		this.ventefeve.put(Feve.F_HQ,  new Variable("Eq3Vente "+Feve.F_HQ, this, 502));
		this.stockfeve.put(Feve.F_HQ,  new Variable("Eq3Stock "+Feve.F_HQ, this, 750));
		this.prodfeve.put(Feve.F_HQ,  new Variable("Eq3Prod "+Feve.F_HQ, this, 568));
		this.ventefevebourse.put(Feve.F_HQ, 502.0);
		this.ventefevecadre.put(Feve.F_HQ, 0.0);
		this.plantations.put(Feve.F_HQ, new Variable("Plantation "+Feve.F_HQ,this,22740));	
		//HQ_E
		this.ventefeve.put(Feve.F_HQ_E,  new Variable("Eq3Vente "+Feve.F_HQ_E, this, 189));
		this.stockfeve.put(Feve.F_HQ_E,  new Variable("Eq3Stock "+Feve.F_HQ_E, this, 250));
		this.prodfeve.put(Feve.F_HQ_E,  new Variable("Eq3Prod "+Feve.F_HQ_E, this, 189.5));
		this.ventefevebourse.put(Feve.F_HQ_E, 189.0);
		this.ventefevecadre.put(Feve.F_HQ_E, 0.0);
		this.plantations.put(Feve.F_HQ_E, new Variable("Plantation "+Feve.F_HQ_E,this,7580));	
		//HQ_E
		this.ventefeve.put(Feve.F_HQ_BE,  new Variable("Eq3Vente "+Feve.F_HQ_BE, this, 189));
		this.stockfeve.put(Feve.F_HQ_BE,  new Variable("Eq3Stock "+Feve.F_HQ_BE, this, 277));
		this.prodfeve.put(Feve.F_HQ_BE,  new Variable("Eq3Prod "+Feve.F_HQ_BE, this, 189.45));
		this.ventefevebourse.put(Feve.F_HQ_BE, 189.0);
		this.ventefevecadre.put(Feve.F_HQ_BE, 0.0);
		this.plantations.put(Feve.F_HQ_BE, new Variable("Plantation "+Feve.F_HQ_BE,this,8420));	
		 
	}
	
/*************************************************************************************************/
	
	public void initialiser() {
		this.stocks = new HashMap<IProduit,Integer>();
		//On set les stocks
		/**
		 *Initialisation basée sur les quantités produites actuellement au Pérou
		 *surface:
		HQ_BE : 8 420 ha 
		HQ : 22 740 ha ; HQ_E : 7 580 ha  (Non Bio) 
		MQ : 47 570 ha ; MQ_E : 11 890 ha 
		BQ : 134 775 ha 
		 * @author Gabin
		 
		 *Modification valeurs:
		 *On initialise à une valeur correspondant à la production pendant 2 steps
		 HQ : 33 kg/(ha.2steps)
		 MQ : 83 kg/(ha.2steps)
		 BQ : 166 kg/(ha.2steps)
		 Les quantités sont en tonnes
		 *@author Alexis
		 */
		setQuantiteEnStock(Feve.F_BQ, 22372);
		setQuantiteEnStock(Feve.F_MQ, 3900);
		setQuantiteEnStock(Feve.F_MQ_E, 986);
		setQuantiteEnStock(Feve.F_HQ, 750);
		setQuantiteEnStock(Feve.F_HQ_E, 250);
		setQuantiteEnStock(Feve.F_HQ_BE, 277);
		
		//On set les variables coutGammeStep
		this.coutGammeStep = new HashMap<Feve,HashMap<Integer,Double>>();
		HashMap<Integer,Double> bq0 = new HashMap<Integer,Double>();
		bq0.put(0, 7.58);
		this.coutGammeStep.put(Feve.F_BQ, bq0);
		HashMap<Integer,Double> mq0 = new HashMap<Integer,Double>();
		mq0.put(0, 1.26);
		this.coutGammeStep.put(Feve.F_MQ, mq0);
		HashMap<Integer,Double> mqE0 = new HashMap<Integer,Double>();
		mqE0.put(0, 0.316);
		this.coutGammeStep.put(Feve.F_MQ_E, mqE0);
		HashMap<Integer,Double> hq0 = new HashMap<Integer,Double>();
		hq0.put(0, 0.5685);
		this.coutGammeStep.put(Feve.F_HQ, hq0);
		HashMap<Integer,Double> hqE0 = new HashMap<Integer,Double>();
		hqE0.put(0, 0.19);
		this.coutGammeStep.put(Feve.F_HQ_E, hqE0);
		HashMap<Integer,Double> hqBE0 = new HashMap<Integer,Double>();
		hqBE0.put(0, 0.19);
		this.coutGammeStep.put(Feve.F_HQ_BE, hqBE0);
		//On set les variables stockGammeStep
		this.stockGammeStep = new HashMap<Feve,HashMap<Integer,Double>>();
		HashMap<Integer,Double> bq00 = new HashMap<Integer,Double>();
		bq00.put(0, 7.58);
		this.stockGammeStep.put(Feve.F_BQ, bq00);
		HashMap<Integer,Double> mq00 = new HashMap<Integer,Double>();
		mq00.put(0, 1.26);
		this.stockGammeStep.put(Feve.F_MQ, mq00);
		HashMap<Integer,Double> mqE00 = new HashMap<Integer,Double>();
		mqE00.put(0, 0.316);
		this.stockGammeStep.put(Feve.F_MQ_E, mqE00);
		HashMap<Integer,Double> hq00 = new HashMap<Integer,Double>();
		hq00.put(0, 0.5685);
		this.stockGammeStep.put(Feve.F_HQ, hq00);
		HashMap<Integer,Double> hqE00 = new HashMap<Integer,Double>();
		hqE00.put(0, 0.19);
		this.stockGammeStep.put(Feve.F_HQ_E, hqE00);
		HashMap<Integer,Double> hqBE00 = new HashMap<Integer,Double>();
		hqBE00.put(0, 0.19);
		this.stockGammeStep.put(Feve.F_HQ_BE, hqBE00);		
	}
	
/*************************************************************************************************/
	
	public String getNom() {// NE PAS MODIFIER
		return "EQ3";
	}
	
	public String toString() {// NE PAS MODIFIER
		return this.getNom();
	}

/*************************************************************************************************/
	
	////////////////////////////////////////////////////////
	//         En lien avec l'interface graphique         //
	////////////////////////////////////////////////////////
	
	public void next() {
		//On gere nos intrants de production @Arthur
		gestionStock(); 
		//On met a jour les variables GammeStep @Arthur
		majGammeStep();
		//On prend en compte les peremptions (ATTENTION A EXECUTER APRES majGammeStep() @Arthur
		peremption();
		/**
		 * Journal des opérations réalisées et des transactions
		 * @author Gabin (modification youssef)
		 */
		  this.journal.ajouter("Étape " + Filiere.LA_FILIERE.getEtape() + " - Détails des opérations de production, stockage et coûts associés.");
		  this.journal.ajouter("Coûts de production : " + calculerCoutsProduction()+ " €");
          this.journal.ajouter("Coûts de stockage : " + calculerCoutsStockage() + " €");
	      this.journal.ajouter("Coûts de main-d'œuvre : " + coutMaindoeuvre() + " €");
	      this.journal.ajouter("Donc Total des coûts à payer : " + calculerCouts() + " €");
		
		//On paie les couts lies a la production et au stockage @Youssef
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Production&Stockage", calculerCouts());
		
		//MaJ des quantites produites pour chaque type de feve: quantite() donne ce qui est produit et pret a la vente, @alexis 
		for (Feve f : Feve.values()) {
			this.prodfeve.get(f).setValeur(this, quantite().get(f));
			this.ventefeve.get(f).setValeur(this, ventefevecadre.get(f)+ventefevebourse.get(f));
			this.stockfeve.get(f).setValeur(this, this.getQuantiteEnStock(f, cryptogramme)) ;
		//Détail des transactions pour chaque type de fève, @Youssef
		    this.journal.ajouter("Feve " + f.name() + ": Prod=" + quantite().get(f) + "t, VenteCadre=" + ventefevecadre.get(f) + "t, VenteBourse=" + ventefevebourse.get(f) + "t, Stock=" + this.getQuantiteEnStock(f, cryptogramme) + "t");
		}
		

	}
	
/*************************************************************************************************/

	public Color getColor() {// NE PAS MODIFIER
		return new Color(249, 230, 151); 
	}

	public String getDescription() {
		return "tiCao - Producteur 3";
	}

	// Renvoie les indicateurs @Alexis
	public List<Variable> getIndicateurs() {
		List<Variable> res = new ArrayList<Variable>();
		for (Feve f : Feve.values()) {
			res.add(stockfeve.get(f));
			res.add(prodfeve.get(f));
			res.add(plantations.get(f));
		}
		return res;
	}

	

	// Renvoie les parametres
	public List<Variable> getParametres() {
		List<Variable> res = new ArrayList<Variable>();
		return res;
	}

	// Renvoie les journaux @Gabin
	public List<Journal> getJournaux() {
		List<Journal> res = new ArrayList<Journal>();
		res.add(this.journal);
		res.add(this.journal_bourse);
		res.add(this.journal_contrat_cadre);
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
		if (acteur instanceof IVendeurBourse) {
			deleteVendeurs((IVendeurBourse)acteur);
		} else if (acteur instanceof IAcheteurBourse) {
			deleteAcheteurs((IAcheteurBourse)acteur);
		}
	}

	// Apres chaque operation sur votre compte bancaire, cette
	// operation est appelee pour vous en informer
	//@Arthur
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
	
/*************************************************************************************************/

	/**
	 * @author modifications par Arthur
	 * @param IProduit p, int cryptogramme
	 * @return double stock
	 * Retourn le stock si non null (sinon 0) du produit p
	 */
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
	 * @param IProduit, double stock
	 * Setter de la quantité de stock pour un produit p (ATTENTION A SON UTILISATION)
	 */
	protected void setQuantiteEnStock(IProduit p, double stock) {
		//on set la valeur du stock ou la modifie si elle existe deja
		this.stocks.put(p,(int)stock);
	}
	
	/**
	 * @author mammouYoussef
	 * @return double coutStockage
	 * Calcule les couts de stockage
	 */
	 protected double calculerCoutsStockage() {
	      double coutStockage = 0;
	      double cout=Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur();
	      for (Integer quantite : stocks.values()) {
	          coutStockage += quantite * cout  ;}
	      return coutStockage;
	      }
	
	 /**
	  * @author mammouYoussef
	  * @return double coutProduction
	  * Calcule les couts de production
	  */		 
	 protected double calculerCoutsProduction() {
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
	 
	 /**
	  * @author mammouYoussef
	  * @return coutMaindoeuvre
	  * Calcule les couts de main d'oeuvre
	  */	
	 
	 protected double coutMaindoeuvre() {
		    // Calcule le coût de la main-d'œuvre en tenant compte des salaires des ouvriers

		    HashMap<Feve, Double> ouvriers = maindoeuvre();
		    double coutMaindoeuvre = 0;

		    // Pour chaque type de fève, calculer le coût de la main-d'œuvre en fonction du nombre d'ouvriers
		    // et ajuster le salaire selon que la fève est équitable ou non
		    for (Feve f : ouvriers.keySet()) {
		        double nbOuvriers = ouvriers.get(f);
		        double salaireOuvrier; 

		        // Déterminer le salaire en fonction du type de fève
		        if (f.isEquitable()) {
		            salaireOuvrier = 3.9 * 15; // Salaire pour l'equitable (bio ou non) (*15 comme c'est 3.9 par jour et le step comporte 15 jours) 
		        } else { 
		            salaireOuvrier = 2.6 * 15; // Salaire standard pour les non équitable 
		        }

		        // Calculer le coût total pour tous les types de fève
		        coutMaindoeuvre += nbOuvriers * salaireOuvrier;
		    }

		    return coutMaindoeuvre;
		}
	
	 /**
	  * @author mammouYoussef
	  * @return double coutTotal
	  * Calcule les couts totaux
	  */
	 protected double calculerCouts() {
		 return calculerCoutsProduction()+calculerCoutsStockage()+coutMaindoeuvre();
		 
	 }
	 
	 /**
	  * @author Arthur
	  * gestion des stocks pour les inputs de production (les outputs sont geres par les fonctions de ventes)
	  * L'appel a cette fonction entraine la production (A N'APPELLER QU'UNE FOIS)
	  */
	 protected void gestionStock() {
		 HashMap<Feve,Double> prod = quantite();
		 for (Feve f : prod.keySet()) {
			 this.setQuantiteEnStock(f, this.getQuantiteEnStock(f, this.cryptogramme)+prod.get(f));
		 }
	 }
	 
	 /**
	  * @author Arthur
	  * Dans le but de s'assurer de ne pas vendre a perte et de gerer la peremption, on regarde les couts de chaque feve par step et leurs quantites
	  * Cette fonction met a jour les variables associees
	  */
	 protected void majGammeStep() {
		 for (Feve f : stockGammeStep.keySet()) {
			//on ajoute la production du step
			 stockGammeStep.get(f).put(Filiere.LA_FILIERE.getEtape(), quantite().get(f));
		 //on ajoute les couts du step
			 if (f.isEquitable()) {
				 coutGammeStep.get(f).put(Filiere.LA_FILIERE.getEtape(), maindoeuvre().get(f)*3.9*15);
			 } else {
				 coutGammeStep.get(f).put(Filiere.LA_FILIERE.getEtape(), maindoeuvre().get(f)*2.6*15);
			 }
		//on regarde tous les steps pour prendre en compte les ventes sur les stocks et rapport de couts
			LinkedList<Integer> steps = new LinkedList<Integer>();
			steps.addAll(stockGammeStep.get(f).keySet());
			Collections.sort(steps);
			double venteF = ventefeve.get(f).getValeur();
			for (Integer step : steps) {
				double stockStep = stockGammeStep.get(f).get(step);
				//on met a jour les stocks en destockant les plus vieilles feves
				//on fait de meme avec les couts proportionnellement 
				if (stockStep > venteF) {
					stockGammeStep.get(f).put(step, stockStep-venteF);
					coutGammeStep.get(f).put(step, (stockStep-venteF)/stockStep*coutGammeStep.get(f).get(step));
				} else {
					venteF -= stockStep;
					stockGammeStep.get(f).remove(step);
					coutGammeStep.get(f).remove(step);
				}
			}
			for (Integer step : steps) {
				//On ajoute les frais de stockage si on a des stocks a ce step
				if (coutGammeStep.get(f).containsKey(step)) {
					coutGammeStep.get(f).put(step, coutGammeStep.get(f).get(step)+Filiere.LA_FILIERE.getParametre("cout moyen stockage producteur").getValeur()*stockGammeStep.get(f).get(step));			}
				}	
		 }
	 }
	 
	 /**
	  * @author Arthur
	  * @param Feve f, double quantiteDem
	  * @return double coutDeRevient
	  * La fonction renvoie le cout de revient pour une gamme et une quantite donnee par tonnes
	  * On vend en priorite les vieilles feves
	  */
	 protected double coutRevient(Feve f,double quantiteDem) {
		 if (quantiteDem <= 0) {
			 return 0.0;
		 } else {
			 double accu = 0.0;
			 double quantiteDemQ = quantiteDem;
			 //On veut destocker step par step
			 LinkedList<Integer> steps = new LinkedList<Integer>();
			 steps.addAll(stockGammeStep.get(f).keySet());
			 Collections.sort(steps);
			 for (Integer step : steps) {
				 double stockStep = stockGammeStep.get(f).get(step);
				 //On ajoute les couts de revient en proportion de la quantite demandee
				 if (stockStep > quantiteDemQ) {
					 accu += quantiteDemQ/stockStep * coutGammeStep.get(f).get(step);
					 break;
				 } else {
					 accu += coutGammeStep.get(f).get(step);
					 quantiteDemQ -= stockStep;
				 }
			 }
			 return accu/quantiteDem;
		 }
	 }
	 
	 /**
	  * @author Arthur
	  * Degrade la qualite des feves selon la duree 
	  * A EXECUTER APRES majGammeStep()
	  */
	 protected void peremption() {
		//apres 2 mois, HQ_BE devient MQ_E (garde le label equitable)
		 Set<Integer> stepsHQBE = new HashSet<Integer>();
		 stepsHQBE.addAll(stockGammeStep.get(Feve.F_HQ_BE).keySet());
		for (Integer step : stepsHQBE) {
			if (Filiere.LA_FILIERE.getEtape() - step > 4) {
				stockGammeStep.get(Feve.F_MQ_E).put(Filiere.LA_FILIERE.getEtape(),stockGammeStep.get(Feve.F_HQ_BE).get(step)+((stockGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape())!=null)?stockGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape()):0));
				stockGammeStep.get(Feve.F_HQ_BE).remove(step);
				coutGammeStep.get(Feve.F_MQ_E).put(Filiere.LA_FILIERE.getEtape(), coutGammeStep.get(Feve.F_HQ_BE).get(step)+((coutGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape())!=null)?coutGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape()):0));
				coutGammeStep.get(Feve.F_HQ_BE).remove(step);
			}
		}
		//apres 2 mois, HQ_E devient MQ_E (garde le label equitable)
		 Set<Integer> stepsHQE = new HashSet<Integer>();
		 stepsHQE.addAll(stockGammeStep.get(Feve.F_HQ_E).keySet());
		for (Integer step : stepsHQE) {
			if (Filiere.LA_FILIERE.getEtape() - step > 4) {
				stockGammeStep.get(Feve.F_MQ_E).put(Filiere.LA_FILIERE.getEtape(),stockGammeStep.get(Feve.F_HQ_E).get(step)+((stockGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape())!=null)?stockGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape()):0));
				stockGammeStep.get(Feve.F_HQ_E).remove(step);
				coutGammeStep.get(Feve.F_MQ_E).put(Filiere.LA_FILIERE.getEtape(), coutGammeStep.get(Feve.F_HQ_E).get(step)+((coutGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape())!=null)?coutGammeStep.get(Feve.F_MQ_E).get(Filiere.LA_FILIERE.getEtape()):0));
				coutGammeStep.get(Feve.F_HQ_E).remove(step);			
			}
		} 
		//apres 2 mois, HQ devient MQ
		 Set<Integer> stepsHQ = new HashSet<Integer>();
		 stepsHQ.addAll(stockGammeStep.get(Feve.F_HQ).keySet());
		for (Integer step : stepsHQ) {
			if (Filiere.LA_FILIERE.getEtape() - step > 4) {
				stockGammeStep.get(Feve.F_MQ).put(Filiere.LA_FILIERE.getEtape(),stockGammeStep.get(Feve.F_HQ).get(step)+((stockGammeStep.get(Feve.F_MQ).get(Filiere.LA_FILIERE.getEtape())!=null)?stockGammeStep.get(Feve.F_MQ).get(Filiere.LA_FILIERE.getEtape()):0));
				stockGammeStep.get(Feve.F_HQ).remove(step);
				coutGammeStep.get(Feve.F_MQ).put(Filiere.LA_FILIERE.getEtape(), coutGammeStep.get(Feve.F_HQ).get(step)+((coutGammeStep.get(Feve.F_MQ).get(Filiere.LA_FILIERE.getEtape())!=null)?coutGammeStep.get(Feve.F_MQ).get(Filiere.LA_FILIERE.getEtape()):0));
				coutGammeStep.get(Feve.F_HQ).remove(step);			
			}
		} 
		//apres 4 mois, MQ devient BQ 
		 Set<Integer> stepsMQ = new HashSet<Integer>();
		 stepsMQ.addAll(stockGammeStep.get(Feve.F_MQ).keySet());
		for (Integer step : stepsMQ) {
			if (Filiere.LA_FILIERE.getEtape() - step > 8) {
				stockGammeStep.get(Feve.F_BQ).put(Filiere.LA_FILIERE.getEtape(),stockGammeStep.get(Feve.F_MQ).get(step)+((stockGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape())!=null)?stockGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape()):0));
				stockGammeStep.get(Feve.F_MQ).remove(step);
				coutGammeStep.get(Feve.F_BQ).put(Filiere.LA_FILIERE.getEtape(), coutGammeStep.get(Feve.F_MQ).get(step)+((coutGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape())!=null)?coutGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape()):0));
				coutGammeStep.get(Feve.F_MQ).remove(step);			
			}
		} 
		//apres 4 mois, MQ_E devient BQ (et perd son label)
		 Set<Integer> stepsMQE = new HashSet<Integer>();
		 stepsMQE.addAll(stockGammeStep.get(Feve.F_MQ_E).keySet());
		for (Integer step : stepsMQE) {
			if (Filiere.LA_FILIERE.getEtape() - step > 8) {
				stockGammeStep.get(Feve.F_BQ).put(Filiere.LA_FILIERE.getEtape(),stockGammeStep.get(Feve.F_MQ_E).get(step)+((stockGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape())!=null)?stockGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape()):0));
				stockGammeStep.get(Feve.F_MQ_E).remove(step);
				coutGammeStep.get(Feve.F_BQ).put(Filiere.LA_FILIERE.getEtape(), coutGammeStep.get(Feve.F_MQ_E).get(step)+((coutGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape())!=null)?coutGammeStep.get(Feve.F_BQ).get(Filiere.LA_FILIERE.getEtape()):0));
				coutGammeStep.get(Feve.F_MQ_E).remove(step);				
			}
		}
		//apres 6 mois, BQ perime
		 Set<Integer> stepsBQ = new HashSet<Integer>();
		 stepsBQ.addAll(stockGammeStep.get(Feve.F_BQ).keySet());
		for (Integer step : stepsBQ) {
			if (Filiere.LA_FILIERE.getEtape() - step > 8) {
				stockGammeStep.get(Feve.F_BQ).remove(step);
				coutGammeStep.get(Feve.F_BQ).remove(step);
			}
		} 		
	 }
}