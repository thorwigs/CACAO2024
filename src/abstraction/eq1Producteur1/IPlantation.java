package abstraction.eq1Producteur1;

import java.util.HashMap;

import abstraction.eqXRomu.produits.Feve;

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
    
    /**
     * Recrute de la main-d'œuvre en fonction des besoins et des conditions du marché.
     * @param demand Une estimation de la demande en main-d'œuvre pour chaque type de feve
     */
    void recruitWorkers(HashMap<Feve, Double> demand);
    
    // Gestion de la production
    
    /**
     * Retourne un dictionnaire où chaque clé est une Feve et chaque valeur est la quantité produite de cette feve.
     */
    HashMap<Feve, Double> quantite();
    
    /**
     * Met à jour la production au fil du temps.
     * @param d0 Dictionnaire de la production au temps t0
     * @param d1 Dictionnaire de la production au temps t1
     */
    void setProdTemps(HashMap<Feve, Double> d0, HashMap<Feve, Double> d1);
    
}
