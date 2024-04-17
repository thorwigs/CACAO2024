package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.Collections;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;



public class OuvrierUtils extends Ouvrier {
	
	////c'est une classe qui contient des fonctions utiles à opérer sur une liste de type Ouvrier//

	private static IActeur Producteur1Acteur;

//////////////youssef ben abdeljelil////////////////////////
public static double getSalaireTotal(ArrayList<Ouvrier> listeOuvrier) {
		
		double s =0;
		for (Ouvrier ouvrier : listeOuvrier) {
			s=s+ouvrier.getSalaire();
			
		}
		return s;//retourne le salaire total à partir de notre liste d'ouvriers
		
	}
	

	public static int getNombreEnfants(ArrayList<Ouvrier> listeOuvrier) {
		int s=0;
		for (Ouvrier ouvrier : listeOuvrier) {
			if (ouvrier.getIsEnfant()) {
				s=s+1;
				
			}
			
		}
		return s;//retourne le nombre d'enfants
		
	}

	public static int GetNombreOuvrierEquitable(ArrayList<Ouvrier> listeOuvrier) {
		int s=0;
		for (Ouvrier ouvrier : listeOuvrier) {
			if (ouvrier.isEquitable) {
				s=s+1;
				
			}
			
		}
		return s;//retourne le nombre de travailleurs dans l'équitable
		
	}
	public static int GetNombreOuvrierNonEquitable(ArrayList<Ouvrier> listeOuvrier) {
		int s=GetNombreOuvrierEquitable(listeOuvrier);
		
		return listeOuvrier.size()-s;//retourne le nombre de travailleurs noramux(non équitable)
		
	}
	
	public static int getNombreOuvrierFormés(ArrayList<Ouvrier> listeOuvrier) {
		
		int s=0;
		for (Ouvrier ouvrier : listeOuvrier) {
			if (ouvrier.isForme) {
				s=s+1;
				
			}
			
			
		}
		return s;//retourne le nombre d'ouvriers ayant fait une formation
	}
	
	public static ArrayList<Ouvrier> addOuvrier(ArrayList<Ouvrier> listeOuvriers, int nombre_à_ajouter, double anciennete, double salaire, double rendement, boolean isEquitable, boolean isForme, boolean isEnfant) {
	    // Création d'une nouvelle liste qui est une copie de la liste originale
	    ArrayList<Ouvrier> nouvelleListe = new ArrayList<>(listeOuvriers);

	    // Ajout des nouveaux ouvriers à la nouvelle liste
	    for (int i = 0; i < nombre_à_ajouter; i++) {
	        Ouvrier ouvrier_a_ajouter = new Ouvrier(anciennete, rendement, salaire, isForme, isEquitable, isEnfant);
	        nouvelleListe.add(ouvrier_a_ajouter);
	    }

	    // Retourner la nouvelle liste
	    return nouvelleListe;
	}
		
	  //ajouter un nombre d'ouvriers avec tous les parametres d'un ouvrier
	
	public static class ResultatSuppression {
	    public ArrayList<Ouvrier> listeMiseAJour;
	    public double indemniteTotale;

	    public ResultatSuppression(ArrayList<Ouvrier> listeMiseAJour, double indemniteTotale) {
	        this.listeMiseAJour = listeMiseAJour;
	        this.indemniteTotale = indemniteTotale;
	    }
	}
	
	
	public static ResultatSuppression removeEmploye(ArrayList<Ouvrier> listeOuvriers, int nombreASupprimer, boolean isEquitable, boolean isForme, boolean isEnfant) {
        // Créer une liste pour stocker temporairement les ouvriers à supprimer
		ArrayList<Ouvrier> copieListe = new ArrayList<>(listeOuvriers);
	    ArrayList<Ouvrier> ouvriersASupprimer = new ArrayList<>();
	    double indemniteTotale = 0;

        // Filtrer la liste en fonction des attributs isEquitable, isForme, et isEnfant
	    for (Ouvrier ouvrier : copieListe) {
	        if (ouvrier.isEquitable == isEquitable && ouvrier.isForme == isForme && ouvrier.estEnfant == isEnfant) {
	            ouvriersASupprimer.add(ouvrier);
	        }
	    }

        // Trier la liste des ouvriers à supprimer par ancienneté
	    Collections.sort(ouvriersASupprimer, (o1, o2) -> Double.compare(o1.getAnciennete(), o2.getAnciennete()));

        // Supprimer le nombre spécifié d'ouvriers de la liste principale, à partir de l'ancienneté la plus basse
	    for (int i = 0; i < Math.min(nombreASupprimer, ouvriersASupprimer.size()); i++) {
	    	Ouvrier ouvrier = ouvriersASupprimer.get(i);
	    	copieListe.remove(ouvrier);
            
            double anciennetéEnAnnées = ouvrier.getAnciennete() / 365;
            double salaire = ouvrier.getSalaire();
            double indemnité = anciennetéEnAnnées <= 10 ? (salaire * 30 / 4) * anciennetéEnAnnées : (salaire * 30 / 4) * 10 + (salaire * 30 / 3) * (anciennetéEnAnnées - 10);
            indemniteTotale += indemnité;


            
	    }

	    return new ResultatSuppression(copieListe, indemniteTotale);
	}

        //une méthode permetttant de "licensier" des ouvriers 
        //selon un nombre en parametres et leurs types de 
        //travail(equitable,enfant,formation)
	//ResultatSuppression resultat = removeEmploye(listeOuvriers, 3, true, false, true);

	// si on veut retourner la liste des oruvriers après supression , on fait:ArrayList<Ouvrier> listeMiseAJour = resultat.listeMiseAJour;
        //si on veut retounrer l'indemnite:double indemniteTotale = resultat.indemniteTotale;
    
	
	public static void UpdateAnciennete(ArrayList<Ouvrier> liste_ouvriers) {
		
		
		for (Ouvrier ouvrier : liste_ouvriers) {
			double anciennete_step_precedent=ouvrier.getAnciennete();
			ouvrier.setAnciennete(anciennete_step_precedent+15.0);
			if ((ouvrier.getAnciennete()>3650) && (ouvrier.getIsEnfant())) {
				ouvrier.setIsEnfant(false);
				ouvrier.setSalaire(1.8);
				
			}	//méthode pour mettre a jour l'anciennete chaque next par 
				//ajout de 15 jurs à chaque ancienneté
			if (ouvrier.getIsEnfant()==false) 
			{
				ouvrier.setRendement(ouvrier.getRendement()+0.2/(5*365/15));//augmente le rendement par 0.2 chaque 5 ans d'anciennete, c'est à dire de 0.2/(365*5/15) chaque step
				ouvrier.setSalaire(ouvrier.getSalaire()+0.18/(5*365/5));////augmente le salaire par 10% du salaire de base  chaque 5 ans d'anciennete, c'est à dire de 0.18/(365*5/15) chaque step
			}
		}
	}
	
	public static void formation (ArrayList<Ouvrier> liste_ouvriers, int nbr_à_former) {
		double cout_formation=0;//dépend de l'ancienneté
		double ancienneteMin =720;// ancienneté au moins de 2 ans pour faire une formation
		double augmentationRendement=0.5;// le rendement augmente de 0.5
		for (Ouvrier ouvrier : liste_ouvriers) {
			double augmentationSalaire=0.2*ouvrier.getSalaire(); //le salaire augmente de 0.2
			if (( ouvrier.getAnciennete()>= ancienneteMin) && 
					(!(ouvrier.getIsForme())) &&
					(!(ouvrier.getIsEnfant())))
			
				{
				ouvrier.setRendement(ouvrier.getRendement()+ augmentationRendement);
				ouvrier.setSalaire(ouvrier.getSalaire()+augmentationSalaire);
				ouvrier.setIsForme(true);
			     int size = ouvrier.soldeParStep.size();
			     double solde = ouvrier.soldeParStep.get(size);
			     double k =5;
			     ouvrier.soldeParStep.add(size-1, solde - k);
			    	 
			}
			
		}
		
		
		
		
	}
	
}
