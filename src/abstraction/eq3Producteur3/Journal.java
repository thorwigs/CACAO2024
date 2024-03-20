/*Code du Journal servant a garder les traces des differents steps*/
package abstraction.eq3Producteur3;
import java.util.ArrayList;

public class Journal {
	private String nom;            // le nom du journal
	private IActeur createur;
	private ArrayList<String>liste;// la liste des messages deposes sur le journal
	private int lignesAffichables;
	private PropertyChangeSupport pcs; // Pour notifier les observers (l'ecouteur qui actualise la fenetre d'affichage du journal) des changements
	// Ils ne sont pas notifies a chaque modifications mais uniquement en fin de next.
	/**
	 * Initialise le journal avec le nom nom et une liste de messages vide   
	 * @param nom le nom du journal
	 */
	public Journal(String nom, IActeur createur) {
		this.nom = nom;
		this.createur = createur;
		this.liste=new ArrayList<String>();
		this.lignesAffichables=200;
		this.pcs = new  PropertyChangeSupport(this);
	}
	/**
	 * @return Retourne le nom du journal
	 */
	public String getNom() {
		return this.nom;
	}

	public IActeur getCreateur() {
		return this.createur;
	}
	/**
	 * @return Retourne le nombre de messages sur le journal
	 */
	public int getTaille() {
		return this.liste.size();
	}

	/**
	 * Ajoute le message s sur le journal
	 * @param s le message a ajouter sur le journal
	 */
	public void ajouter(String s) {
		this.liste.add( "Et"+entierSur6(Filiere.LA_FILIERE==null? 0 : Filiere.LA_FILIERE.getEtape())+" "+s);
	}
	
	/**
	 * Efface le journal
	 */
	public void clear() {
		this.liste.clear();
	}
}

