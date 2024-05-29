package abstraction.eq3Producteur3;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import abstraction.eqXRomu.bourseCacao.IAcheteurBourse;
import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

public abstract class Producteur3Plantation extends Producteur3Acteur {
	abstract HashMap<Feve,Double> quantite();
	abstract void setProdTemps(HashMap<Feve, Double> d0,HashMap<Feve, Double> d1);
	abstract void deleteAcheteurs(IAcheteurBourse acheteur);
	abstract void deleteVendeurs(IVendeurBourse vendeur);
	
/**
 * @author Alexis et Gabin
 * les variables surfaceXQ donnent le nombre d'hectares qui produisent des feves de qualité XQ 
 * Calcul des valeurs initials par @gabin
 */
	private double surfaceHQ = 22.74*1000;
	private double surfaceHQBE = 8.42*1000;
	private double surfaceHQE = 7.58*1000;
	private double surfaceMQ = 47.57*1000;
	private double surfaceMQE = 11.89*1000;
	private double surfaceBQ = 134.775*1000;
	private HashMap<Feve, Double> surfacePlantation;
	private HashMap<Feve, Double> coutPlantation;
	
/**
 * @author Alexis
 * variable qui répertorie l'âge des plants	
 * pour chaque type de fève, il y a un dictionnaire dont les clés sont les steps de fin de vie d'une parcelle
 */
	private HashMap<Feve,HashMap<Integer,Double>> agePlant;
	
	public void initialiser() {
		super.initialiser();
		agePlant = new HashMap<Feve,HashMap<Integer,Double>>();
		HashMap<Integer,Double> feveBQ = new HashMap<Integer,Double>();
		feveBQ.put(20, surfaceBQ*0.3);
		feveBQ.put(420, surfaceBQ*0.3);
		feveBQ.put(720, surfaceBQ*0.4);
		agePlant.put(Feve.F_BQ, feveBQ);

		HashMap<Integer,Double> feveMQ = new HashMap<Integer,Double>();
		feveMQ.put(70, surfaceMQ*0.2);
		feveMQ.put(520, surfaceMQ*0.3);
		feveMQ.put(720, surfaceMQ*0.5);
		agePlant.put(Feve.F_MQ, feveMQ);
		
		HashMap<Integer,Double> feveMQE = new HashMap<Integer,Double>();
		feveMQE.put(570, surfaceMQE*0.1);
		feveMQE.put(670, surfaceMQE*0.2);
		feveMQE.put(670, surfaceMQE*0.7);
		agePlant.put(Feve.F_MQ_E, feveMQE);
		
		HashMap<Integer,Double> feveHQ = new HashMap<Integer,Double>();
		feveHQ.put(10, surfaceHQ*0.1);
		feveHQ.put(420, surfaceHQ*0.4);
		feveHQ.put(720, surfaceHQ*0.5);
		agePlant.put(Feve.F_HQ, feveHQ);

		HashMap<Integer,Double> feveHQE = new HashMap<Integer,Double>();
		feveHQE.put(620, surfaceHQE*0.7);
		feveHQE.put(720, surfaceHQE*0.3);
		agePlant.put(Feve.F_HQ_E, feveHQE);
		
		HashMap<Integer,Double> feveHQBE = new HashMap<Integer,Double>();
		feveHQBE.put(670, surfaceHQBE*0.1);
		feveHQBE.put(720, surfaceHQBE*0.9);
		agePlant.put(Feve.F_HQ_BE, feveHQBE);
		
		surfacePlantation=new HashMap<Feve, Double>();
		surfacePlantation.put(Feve.F_BQ, surfaceBQ);
		surfacePlantation.put(Feve.F_MQ, surfaceMQ);
		surfacePlantation.put(Feve.F_MQ_E, surfaceMQE);
		surfacePlantation.put(Feve.F_HQ, surfaceHQ);
		surfacePlantation.put(Feve.F_HQ_E, surfaceHQ);
		surfacePlantation.put(Feve.F_HQ_BE, surfaceHQBE);
		
		coutPlantation = new HashMap<Feve, Double>();
		coutPlantation.put(Feve.F_BQ, 500.0);
		coutPlantation.put(Feve.F_MQ, 1000.0);
		coutPlantation.put(Feve.F_MQ_E, 1000.0);
		coutPlantation.put(Feve.F_HQ, 1500.0);
		coutPlantation.put(Feve.F_HQ_E, 1500.0);
		coutPlantation.put(Feve.F_HQ_BE, 1500.0);
		
	}
	


///Gestion de la plantation
	
	/**
	 * @author Alexis
	 * @return HashMap<Feve,Double> (tableau des surfaces par feve)
	 * Le dictionnaire plantation a pour cle Feve et pour valeur la variable surfaceXQ associee du step precedent + les eventuelles extensions.
	 */
	protected HashMap<Feve, Double> plantation() {
	//on augmente la surface 
		surfacePlantation = achatPlantation(surfacePlantation);
		return surfacePlantation;
	}

	/**
	 * @author Alexis
	 * @param HashMap<Feve,Double> surfaces (tableau des surfaces par feve)
	 * @return HashMap<Feve,Double> surfaces (tableau des surfaces par feve)
	 * Cette methode determine la surface supplementaire a acheter pour chaque type de feve
	 * Elle s'appuie sur la production et les ventes du step precedent ainsi que sur la fonction aRemplacer
	 * Lorsqu'on achète des plantations pour remplacer celles qui sont en fin de vie, il faut actualiser agePlant
	 * On veut en même temps afficher dans le journal les plantations qui ont été renouvelées
	 */
	protected HashMap<Feve, Double> achatPlantation(HashMap<Feve, Double> surfaces) {
		HashMap<Feve, Double> agePlantPrec = new HashMap<Feve, Double>(); //va contenir les surfaces remplacees
		for (Feve f : prodfeve.keySet()) {
			double supp = 0; //initialisation de la surface supplementaire
			double delta = ventefeve.get(f).getValeur() - prodfeve.get(f).getValeur();// difference entre vente et production de f
			if (delta > 50) { // si on vend beaucoup plus que ce que l'on produit (en tonnes)
				supp += 100; 
			}
			if(aRemplacer(agePlant).get(f) != null) {
				if(f == Feve.F_HQ_E || f == Feve.F_HQ_BE || f == Feve.F_MQ_E) {
					supp += aRemplacer(agePlant).get(f)+10; // on tend à accroitre nos plantations de bio et d'équitable
				}
				else if(f == Feve.F_HQ || f == Feve.F_MQ) {
					supp += aRemplacer(agePlant).get(f)-10; // on diminue nos plantations conventionnelles
				}
				else {
					supp += aRemplacer(agePlant).get(f);
				}
				agePlantPrec.put(f, aRemplacer(agePlant).get(f));
				agePlant.get(f).remove(Filiere.LA_FILIERE.getEtape());
				agePlant.get(f).put(Filiere.LA_FILIERE.getEtape()+720, agePlantPrec.get(f));
				}
			surfaces.put(f, surfaces.get(f)+supp); // on augmente la surface de plantation pour le type f (en ha)
			if(supp > 0) {
				Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Coût plantation supplémentaire "+f, supp*coutPlantation.get(f));
			}
		}
		if(!agePlantPrec.isEmpty()) {
			journal.ajouter("Surfaces à remplacer pour chaque type de fève:" + agePlantPrec.toString());
		}
		return surfaces;
	}


	/**
	 * @author Alexis
	 * @param  agePlant
	 * @return HashMap<Feve,Double> replace (tableau des surfaces à remplacer par feve)
	 * Cette methode détermine la quantité de plants trop vieux à remplacer.
	 * On regarde si pour chaque plantation d'un type de feve, il y a une parcelle qui est trop vieille,
	 * ie dont le step de fin de vie correspond au step actuel
	 */
	protected HashMap<Feve, Double> aRemplacer(HashMap<Feve,HashMap<Integer,Double>> agePlant) {
		HashMap<Feve, Double> replace = new HashMap<Feve, Double>();
		for(Feve f: agePlant.keySet()) {
			LinkedList<Integer> steps = new LinkedList<Integer>();
			steps.addAll(agePlant.get(f).keySet());
			Collections.sort(steps);
			for(int step: steps) {
				if(Filiere.LA_FILIERE.getEtape() == step) {
					replace.put(f, agePlant.get(f).get(step));
				}
			}
		}
		return replace;
	}
	
///Gestion de la main d'oeuvre///

	/**
	 * @author Arthur
	 * @return HashMap<Feve,Double> ouvriers (nombre d'ouvriers par type de feve)
	 * Renvoie le nombre d'ouvriers necessaires et le type de feve selon la superficie (en ha) et le type de plantation
	*/
	protected HashMap<Feve,Double> maindoeuvre() {
		//renvoie le nombre d'ouvriers necessaires et le type de feve selon la superficie (en ha) et le type de plantation
		HashMap<Feve,Double> ouvriers = new HashMap<Feve,Double>();
		for (Feve f : surfacePlantation.keySet()) {
			if (f.isBio()) {
				//1.5 ouvriers par ha en bio
				ouvriers.put(f, 1.5*surfacePlantation.get(f));
			} else {
				//1 ouvrier par ha en conventionnel
				ouvriers.put(f, surfacePlantation.get(f));
			}
		}
		return ouvriers;
	}	 

}


