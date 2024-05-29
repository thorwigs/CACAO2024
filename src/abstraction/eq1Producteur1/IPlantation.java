package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;
/**@author Yuri*/
public interface IPlantation {
	// Gestion de la plantation
    
    /**
     * Retourne un dictionnaire où chaque clé est une Feve et chaque valeur est la surface associée de cette feve.
     */
    HashMap<Feve, Double> plantation();
    
    /**
     * Ajuste la taille de la plantation en fonction des besoins ou contraintes.
     * @param adjustments Un dictionnaire contenant les ajustements à apporter à chaque type de feve
     */
    void adjustPlantationSize(HashMap<Feve, Double> adjustments);
    
    // Gestion de la main d'oeuvre
    
    /**
     * Renvoie le nombre d'ouvriers nécessaires et le type de feve 
     * selon la superficie (en ha) et le type de plantation
     */
    void maindoeuvre();
    
    
    
    // Gestion de la production
    
    /**
     * Retourne un dictionnaire où chaque clé est une Feve et chaque valeur est la quantité produite de cette feve.
     */
    HashMap<Feve, Double> quantite();


	
    
}
