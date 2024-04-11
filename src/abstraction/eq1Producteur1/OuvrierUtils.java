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
	
	public static void addOuvrier(ArrayList<Ouvrier> listeOuvriers,int nombre_à_ajouter,double anciennete,double salaire,double rendement,boolean isEquitable,boolean isForme,boolean isEnfant) {
		
		for (int i = 0; i < nombre_à_ajouter; i++) {
			Ouvrier ouvrier_a_ajouter=new Ouvrier(anciennete,rendement,salaire,isForme,isEquitable,isEnfant);
			listeOuvriers.add(ouvrier_a_ajouter);
			
		}
		
	}   //ajouter un nombre d'ouvriers avec tous les parametres d'un ouvrier
	
	
	public static double removeEmploye(ArrayList<Ouvrier> listeOuvriers, int nombreASupprimer, boolean isEquitable, boolean isForme, boolean isEnfant) {
        // Créer une liste pour stocker temporairement les ouvriers à supprimer
		int indenite_totale=0;//prime à donner
        ArrayList<Ouvrier> ouvriersASupprimer = new ArrayList<>();

        // Filtrer la liste en fonction des attributs isEquitable, isForme, et isEnfant
        for (Ouvrier ouvrier : listeOuvriers) {
            if (ouvrier.isEquitable == isEquitable && ouvrier.isForme == isForme && ouvrier.estEnfant == isEnfant) {
                ouvriersASupprimer.add(ouvrier);
            }
        }

        // Trier la liste des ouvriers à supprimer par ancienneté
        Collections.sort(ouvriersASupprimer,  new ComparateurAnciennete());

        // Supprimer le nombre spécifié d'ouvriers de la liste principale, à partir de l'ancienneté la plus basse
        for (int i = 0; i < Math.min(nombreASupprimer, ouvriersASupprimer.size()); i++) {
            listeOuvriers.remove(ouvriersASupprimer.get(i));
            double indemnité = 0;
            double anciennetéEnAnnées = ouvriersASupprimer.get(i).getAnciennete() / 365;
            double salaire = ouvriersASupprimer.get(i).getSalaire();
            if (anciennetéEnAnnées <= 10) {
                indemnité = (salaire*30 / 4) * anciennetéEnAnnées; // Un quart de mois de salaire par année pour les années jusqu'à 10
            } else {
                indemnité = (salaire*30 / 4) * 10; // Pour les premières 10 années
                indemnité += (salaire*30 / 3) * (anciennetéEnAnnées - 10); // Un tiers de mois de salaire par année d'ancienneté après 10 ans
            }
            indenite_totale+=indemnité;

            
        }
        return indenite_totale;

        //une méthode permetttant de "licensier" des ouvriers 
        //selon un nombre en parametres et leurs types de 
        //travail(equitable,enfant,formation)
        
    }
	
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
	
}
