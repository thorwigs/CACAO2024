package abstraction.eq2Producteur2;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;

/** Classe gérant les ressources humaines
 * @author Noémie
 */

public abstract class Producteur2_MasseSalariale extends Producteur2_Stocks {
	
	/** Définition des variables
	 * @author Noémie
	 */ 
	private long nb_employes ;
	private long nb_employes_equitable;
	private long nb_employes_enfants;
	
	private double salaire_enfant;
	private double salaire_adulte;
	private double salaire_adulte_equitable;
	
	protected Journal journalRH;
	abstract public double getHectaresTotal(int cryptogramme) ;
	
	
	/** Constructeur
	 * @author Noémie
	 */
	public Producteur2_MasseSalariale() {
		super();
		this.journalRH = new Journal(this.getNom()+" journal_RH", this);
	}
		
	/** Initialisation
	 * @author Noémie
	 */
	public void initialiser() {
		super.initialiser();
		this.nb_employes = 730000;  		  
		this.nb_employes_equitable = 20000; // 2% au départ
		this.nb_employes_enfants = 250000;  // 25% au départ
		
		// Salaire à la journée
		this.salaire_enfant =  1;
		this.salaire_adulte = 2;
		this.salaire_adulte_equitable = 3;
	}
	
	/** getter
	 * @author Noémie
	 */
	public long getNb_employes() {
		return nb_employes;
	}
	/** setter
	 * @author Noémie
	 */
	public void setNb_employes(long nb_employes) {
		this.nb_employes = nb_employes;
	}
	/** getter
	 * @author Noémie
	 */
	public long getNb_employes_equitable() {
		return nb_employes_equitable;
	}
	/** setter
	 * @author Noémie
	 */
	public void setNb_employes_equitable(long nb_employes_equitable) {
		this.nb_employes_equitable = nb_employes_equitable;
	}
	/** getter
	 * @author Noémie
	 */
	public long getNb_employes_enfants() {
		return nb_employes_enfants;
	}
	/** setter
	 * @author Noémie
	 */
	public void setNb_employes_enfants(long nb_employes_enfants) {
		this.nb_employes_enfants = nb_employes_enfants;
	}
	
	/** ajoute les journal_RH à la liste des autres journaux
	 * @author Noémie
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalRH);
		return jx;
	}
	
	/** Retourne le nombre total d'employés travaillant dans la plantation
	 * @author Noémie
	 */
	public double getNb_Employes_total() {
		double nb_employes = this.getNb_employes() + this.getNb_employes_enfants() + this.getNb_employes_equitable();
		return nb_employes;
	}
	
	/** Retourne le salaire de la catégorie d'employés passée en paramètre
	 * @author Noémie
	 */
	public double getSalaire(String categorie) {
		if (categorie == "enfant") {
			return salaire_enfant;
		}
		else if (categorie == "adulte équitable") {
			return salaire_adulte_equitable;
		}
		else {
			return salaire_adulte;
		}
	}
	
	/** Retourne le nombre d'employés de la catégorie passée en paramètre
	 * @author Noémie
	 */
	public long getNombreEmployes(String categorie) {
		if (categorie == "enfant") {
			return this.getNb_employes_enfants();
		}
		else if (categorie == "adulte équitable") {
			return this.getNb_employes_equitable();
		}
		else {
			return this.getNb_employes();
		}
	}
	
	/** Met à jour le nombre d'employés dans une catégorie
	 * @author Noémie
	 */
	public void setNombreEmployes(String categorie, long d) {
		if (categorie == "enfant") {
			this.setNb_employes_enfants(d);
		}
		else if (categorie == "adulte équitable") {
			this.setNb_employes_equitable(d);
		}
		else {
			this.setNb_employes(d);
		}
	}
	
	/** Licencie n personnes de la catégorie passée en paramètre
	 * @author Noémie
	 */
	public void licencie (long n, String categorie) {
		long nb_emp = getNombreEmployes(categorie);	
		
		// Si on demande à en licencier alors qu'il n'y a déjà plus personne
		if ((nb_emp - n) < 0) {
			this.setNombreEmployes(categorie, 0);
			this.journalRH.ajouter("il n y a plus d employes dans la categorie " + categorie);
		}
		else { 
			this.setNombreEmployes(categorie, nb_emp-n);
		}
	}
	
	/** Embauche n personnes de la catégorie passée en paramètre
	 * @author Noémie
	 */
	public void embauche(long n, String categorie) {
		long nb_emp = getNombreEmployes(categorie);
		this.setNombreEmployes(categorie, nb_emp+n);
	}
	
	/** Calcule le coût lié à la main d'oeuvre à chaque tour
	 * @author Noémie
	 */
	public double cout_humain_par_step() { 
		// Renvoie le coût total lié à la main d'oeuvre par step 
		double enfants = getNb_employes_enfants()* getSalaire("enfant");
		double adultes_eq = getNb_employes_equitable()*getSalaire("adulte équitable");
		double adultes = getNb_employes()*getSalaire("adulte"); 
		
		return enfants + adultes_eq + adultes;
	}
	
	/** Méthode qui licencie 5% des employés non-équitables lorsque nous produisons trop
	 * @author Noémie
	 */
	public void trop_d_employes() {
		double pourcentage_enf = this.getPourcentage_enfants();
		long a_licencier = (long) (this.getNb_Employes_total()*0.05);
		long nb_enfants = (long) pourcentage_enf/100*a_licencier;
		
		this.licencie(nb_enfants, "enfants");
		this.licencie(a_licencier-nb_enfants, getDescription());
		this.journalRH.ajouter("On a licencié " + a_licencier + " personnes");
	}			
	
	/** Retourne le pourcentage d'enfants travaillant dans la plantation
	 * @author Noémie
	 */
	public double getPourcentage_enfants(){
		double pourcentage = (this.getNb_employes_enfants()/this.getNb_Employes_total())*100;
		return pourcentage;
	}
	
	/** Retourne le pourcentage d'adultes équitables travaillant dans la plantation
	 * @author Noémie
	 */
	public double getPourcentage_equitable(){
		double pourcentage = (this.getNb_employes_equitable()/this.getNb_Employes_total())*100;
		return pourcentage;
	}
	
	
	/**  Fonction qui permet d'implémenter notre stratégie. 
	 * Si notre bénéfice  est supérieur à ce que nous coûte 2 tours de simulation
	 * en ressources humaines on peut embaucher de nouveaux employés adultes dont une part en équitable.
	 * On suppose qu'en faisant une telle action on diminue le travail infantile de 2%.
	 * @author Noémie
	 */
	public void strategie()  {
		double benef = this.getBenefice();
		if (benef > 2*cout_humain_par_step()){
			// S'il n'y a déjà plus d'enfants dans la plantation
			if (getPourcentage_enfants() < 0.02) {
				double nb_ade=0.001*this.getNb_Employes_total();
				double nb_ade_max=0.2*this.getNb_Employes_total();
				this.embauche(Math.min((long) nb_ade,(long) nb_ade_max), "adulte équitable");
			}
			
			// Si le pourcentage d'employes équitable est supérieur à 20
			else if (this.getPourcentage_equitable() < 20) {
				long nb_enf = getNb_employes_enfants();
			
				// le nombre d'employés qui changent de catégorie est égal à 2% des enfants
				long nb_employes_modif = (long) Math.round(0.02*nb_enf);
				
				// parmis ces employés, 60% passent en équitable
				long modif_equitable = (long) Math.round(nb_employes_modif*0.6);
				this.licencie(nb_employes_modif,"enfant");
				this.embauche(modif_equitable, "adulte équitable");
				this.embauche(nb_employes_modif - modif_equitable, "adulte");
			}
		}
		// S'il y a trop d'employes equitables
		if(this.getPourcentage_equitable() > 25) {
			long nb_emp_eq = this.getNb_employes_equitable();
			long nb_max_emp_eq = (long) (0.2 * this.getNb_Employes_total());
			this.licencie(nb_emp_eq - nb_max_emp_eq, "adulte équitable");
			this.embauche(nb_emp_eq - nb_max_emp_eq, "adulte");
		}
	}
	

	/** Méthode next qui appelle stratégie et met à jour le journal RH 
	 * @author Noémie
	 */
	public void next() {
		super.next();
		this.strategie();
		
		this.journalRH.ajouter("\n-------------- ETAPE " + Filiere.LA_FILIERE.getEtape() + " --------------------");
		this.journalRH.ajouter("nombre d'employes dans la plantation " + this.getNb_Employes_total());
		this.journalRH.ajouter("pourcentage d'enfants " + this.getPourcentage_enfants());
		this.journalRH.ajouter("pourcentage d'employés équitable " + this.getPourcentage_equitable());
	}
}
