package abstraction.eq3Producteur3;

import java.util.HashMap;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public abstract class Producteur3Production extends Producteur3Plantation { 
	abstract void deleteAcheteurs(IAcheteurBourse acheteur);
	abstract void deleteVendeurs(IVendeurBourse vendeur);
	
	//tonne produite par hectare lors d'un step
	protected double T_ha_BQ = 28.125/1000 ;	//avec pesticide
	protected double T_ha_MQ = 26.5625/1000 ;
	protected double T_ha_HQ = 25.0/1000 ;
	protected double T_ha_HQ_BE = 22.5/1000 ;   //sans pesticide (bio équitable)
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps = new HashMap<Integer,HashMap<Feve,Double>>(); //variable qui sert a prendre en compte le temps de séchage
	protected Journal journal_Production;
	public void initialiser() {
		super.initialiser();
		//On set les productions
		//@Gabin
		HashMap<Feve,Double> d01 = new HashMap<Feve,Double>();
		d01.put(Feve.F_BQ, 3.79);
		d01.put(Feve.F_MQ, 2.527);		//80% de HQ est non équitable
		d01.put(Feve.F_MQ_E, 0.63);        //20% de MQ est équitable
		d01.put(Feve.F_HQ, 1.137);			//60% de HQ est ni bio ni équitable
		d01.put(Feve.F_HQ_E, 0.379);		//20% de HQ est équitable
		d01.put(Feve.F_HQ_BE, 0.3789);		//20% de HQ est bio équitable
		setProdTemps(d01,d01);
	}
	
	/**
	 * @author Gabin
	 * @return HashMap<Feve,Double> (tableau des récoltes selon les fèves)
	 * Dictionnaire renvoyant la quantité produite pour chaque type de cacao (et types bio/équitable). 
	 * Prend en compte les surfaces de plantation et les tonnes produites par hectare lors d'un step.
	 * !!! nouveauté : prend en compte la saisonnalité
	 */
	protected HashMap<Feve,Double> newQuantite() {
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		HashMap<Feve,Double> plant = plantation();
		double k=coeff(Filiere.LA_FILIERE.getEtape());
		for(Feve f1 : plant.keySet()) {
			if (f1.getGamme() == Gamme.BQ) {            		// FEVE BASSE QUALITE (NI BIO NI EQUITABLE)
				quantite.put(f1, T_ha_BQ*c*k*plant.get(f1));
			}			
			else if (f1.getGamme() == Gamme.MQ) { 
				if (f1.isEquitable() == false) {				// FEVE MOYENNE QUALITE (NI BIO NI EQUITABLE)
					quantite.put(f1, T_ha_MQ*c*k*plant.get(f1));
				} 
				else {											// FEVE MOYENNE QUALITE (EQUITABLE , NON BIO )
					quantite.put(f1, T_ha_MQ*c*k*plant.get(f1));
				}
			}	
			else if (f1.getGamme() == Gamme.HQ) {      
			
				if ( f1.isBio() == false) {
					if (f1.isEquitable()== false) { 			// HAUTE QUALITE (NI BIO NI EQUITABLE)
						quantite.put(f1, T_ha_HQ*c*k*plant.get(f1));
					}
					else {										// HAUTE QUALITE (EQUITABLE, NON BIO)
						quantite.put(f1, T_ha_HQ*c*k*plant.get(f1));
					}
				}
				else {											// HAUTE QUALITE (BIO EQUITABLE)
					quantite.put(f1, T_ha_HQ_BE*c*k*plant.get(f1)); 
				}

			}
		}
		return quantite;
		
		
	}	

	/**
	 * @author Gabin
	 * @return HashMap<Integer,HashMap<Feve,Double>> prodTemps (quantités produites par fèves sur ce step et le précédent)
	 * Stockage dans le temps de 2 dictionnaires.
	 * Nécessaire pour prendre en compte le temps de séchage.
	 * Le premier dictionnaire est celui du step t-1 (clé 0), soit la quantité produite 1 step avant (2 semaines avant)
	 * Le deuxième (clé 1) correspond au step t (actuel), soit la quantité produite à ce step
	 */
	protected HashMap< Integer , HashMap<Feve,Double> > prodTemps() {
		prodTemps.put(0, prodTemps.get(1)); //step précédent
		prodTemps.put(1, newQuantite());    //step actuel
		return prodTemps;
	}
	
	/**
	 * @author Gabin
	 * @param HashMap<Feve,Double> d0, d1 (production pour les différents types de fèves au temps 0 et 1
	 * Initialise la variable prodTemps qui sert a prendre en compte le temps de séchage
	 */
	protected void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1) {
		prodTemps.put(0, d0);
		prodTemps.put(1, d1);
	}	
	
	/**
	 * @author Gabin
	 * @return HashMap<Feve,Double> quantite (quantités produites par fèves)
	 * Permet de savoir la production que l'on peut effectivement vendre (après le temps de séchage)
	 */
	protected HashMap<Feve,Double> quantite(){
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		quantite=prodTemps().get(0);
		return quantite;
	}
	
	/**Renvoie la quantité de fèves disponibles après séchage au prochain step
	 * Elle est utile pour l'établissement d'un contrat cadre
	 * 
	 * @author Alexis (royalties Gabin : j'ai tout plagia)
	 */
	protected HashMap<Feve,Double> quantiteFuture(){
		HashMap<Feve,Double> quantite = new HashMap<Feve,Double>();
		quantite = prodTemps.get(1);
		return quantite;
	}
	
	/**Palier p pour influer sur la quantité de rendement
	 * Coefficient c pour obtenir la quantité max de fève en haute saison
	 * @author Gabin
	 */
	private double p = 0.7/6;
	private double c = 1/0.65;
	
	
	/**Fonction pour trouver le mois et prendre en compte la saisonnalité
	 * @author Gabin 
	 * @return le coefficient du mois. on se base sur une approche avec des paliers et variations linéaires
	 */
	
	protected double coeff(int i) {
		int reste;
		reste = i%24;
		///haute saison
		if (reste >= 0 && reste <= 4) {   ///de janvier (step 0) à fin février, début mars(step 4)
			return 1;			
		}
		else if (reste >= 22 && reste <= 24 ) {  /// novembre à décembre
			return 1;
		}
		///basse saison de mai à début août
		else if (reste >= 10 && reste <=16 )
			return 0.3;
		///dégression linéaire en transition de saison de début mars à début mai: 3 mois-->6 steps
		else if (reste >= 5 && reste <=9 ) {
			int j = reste-4;
			double k = 1-j*p;
			return k;
		}
		///augmentation linéaire en transition de saison de début septembre à début novembre: 3 mois-->6 steps
		else if (reste >= 17 && reste<=21 ) {
			int j = 22-reste;
			double k = 1-j*p;
			return k;
		}
		return 0;	
	}
	
}
