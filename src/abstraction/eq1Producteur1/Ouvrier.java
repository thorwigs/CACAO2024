package abstraction.eq1Producteur1;

/**
 * Cette classe représente un ouvrier avec des attributs décrivant leur type, productivité, salaire,
 * s'ils sont équitables et leur statut de formation.
 * Elle étend la classe Producteur1Acteur.
 * 
 * <p>Auteur: Youssef Ben Abdeljelil</p>
 * 
 * <p>///////////////////////////////////////YOUSSEF BEN ABDELJELIL//////////////////////////////
 * ////////////////////////////////////////////////////////////////////////////////</p>
 */
public class Ouvrier extends Producteur1Acteur {
    
    /**
     * Indique si l'ouvrier est un enfant.
     */
    public boolean estEnfant;
    
    /**
     * Indique si l'ouvrier est équitable.
     */
    public boolean isEquitable;
    
    /**
     * Indique si l'ouvrier est formé.
     */
    public boolean isForme;

    /**
     * Constructeur par défaut : crée un nouvel ouvrier qui n'est pas équitable, est adulte,
     * a un salaire minimal et une productivité normale (productivité = 1).
     */
    public Ouvrier() {
        this.isForme = false;
        this.isEquitable = false;
        this.estEnfant = false;  
    }

    /**
     * Constructeur par paramètres : crée un nouvel ouvrier avec les attributs spécifiés.
     * 
     * @param estEnfant boolean indiquant si l'ouvrier est un enfant.
     * @param isEquitable boolean indiquant si l'ouvrier est équitable.
     * @param isForme boolean indiquant si l'ouvrier est formé.
     */
    public Ouvrier(boolean estEnfant, boolean isEquitable, boolean isForme) {
        this.isForme = isForme;
        this.estEnfant = estEnfant;
        if (estEnfant) {
            this.isEquitable = false;
            this.isForme = false;
        } else {
            this.isEquitable = isEquitable;
            this.isForme = isForme;
        }
    }

    /**
     * Retourne si l'ouvrier est équitable.
     * 
     * @return true si l'ouvrier est équitable, false sinon.
     */
    public boolean getIsEquitable() {
        return this.isEquitable;
    }

    /**
     * Définit le statut équitable de l'ouvrier.
     * 
     * @param isEquitable boolean indiquant le statut équitable de l'ouvrier.
     */
    public void setIsEquitable(boolean isEquitable) {
        this.isEquitable = isEquitable;
    }

    /**
     * Retourne si l'ouvrier est formé.
     * 
     * @return true si l'ouvrier est formé, false sinon.
     */
    public boolean getIsForme() {
        return this.isForme;
    }

    /**
     * Définit le statut de formation de l'ouvrier.
     * 
     * @param isForme boolean indiquant le statut de formation de l'ouvrier.
     */
    public void setIsForme(boolean isForme) {
        this.isForme = isForme;
    }

    /**
     * Retourne si l'ouvrier est un enfant.
     * 
     * @return true si l'ouvrier est un enfant, false sinon.
     */
    public boolean getIsEnfant() {
        return this.estEnfant;
    }

    /**
     * Définit le statut d'enfant de l'ouvrier.
     * 
     * @param estEnfant boolean indiquant si l'ouvrier est un enfant.
     */
    public void setIsEnfant(boolean estEnfant) {
        this.estEnfant = estEnfant;
    }

    /**
     * Vérifie si cet ouvrier est égal à un autre objet.
     * Deux ouvriers sont considérés comme égaux s'ils ont le même statut équitable,
     * le même statut de formation et le même statut d'enfant.
     * 
     * @param objet l'objet à comparer.
     * @return true si l'objet spécifié est égal à cet ouvrier, false sinon.
     */
    @Override
    public boolean equals(Object objet) {
        return (objet instanceof Ouvrier) &&
               (((Ouvrier) objet).getIsEquitable() == this.getIsEquitable()) &&
               ((((Ouvrier) objet).getIsForme() == this.getIsForme())) &&
               ((((Ouvrier) objet).getIsEnfant() == this.getIsEnfant()));
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères de l'ouvrier.
     * 
     * @return une chaîne représentant les attributs de l'ouvrier.
     */
    @Override
    public String toString() {
        return "Ouvrier{ " +
               "isEquitable= " + isEquitable +
               ", isForme= " + isForme +
               ", isEnfant= " + this.estEnfant +
               '}';
    }
}
