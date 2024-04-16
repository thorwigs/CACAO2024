package abstraction.eq2Producteur2;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;

// Classe faite par Noémie

public abstract class Producteur2_MasseSalariale extends Producteur2_Stocks {
	private int nb_employes ;
	private int nb_employes_equitable;
	private int nb_employes_enfants;
	
	private double salaire_enfant;
	private double salaire_adulte;
	private double salaire_adulte_equitable;
	
	protected Journal journalRH;
	
	public Producteur2_MasseSalariale() {
		super();
		this.journalRH = new Journal(this.getNom()+" journal_RH", this);
	}
		
	public void initialiser() {
		super.initialiser();
		this.nb_employes = 3679200;  		  
		this.nb_employes_equitable = 100800; // 2% au départ
		this.nb_employes_enfants = 1260000;  // 25% au départ
		
		// Salaire à la journée
		this.salaire_enfant =  1;
		this.salaire_adulte = 2;
		this.salaire_adulte_equitable = 3;
	}
	
	public int getNb_employes() {
		return nb_employes;
	}
	public void setNb_employes(int nb_employes) {
		this.nb_employes = nb_employes;
	}
	public int getNb_employes_equitable() {
		return nb_employes_equitable;
	}
	public void setNb_employes_equitable(int nb_employes_equitable) {
		this.nb_employes_equitable = nb_employes_equitable;
	}
	public int getNb_employes_enfants() {
		return nb_employes_enfants;
	}
	public void setNb_employes_enfants(int nb_employes_enfants) {
		this.nb_employes_enfants = nb_employes_enfants;
	}
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalRH);
		return jx;
	}
	
	public double getNb_Employes_total() {
		double nb_employes = this.getNb_employes() + this.getNb_employes_enfants() + this.getNb_employes_equitable();
		return nb_employes;
	}
	
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

	public int getNombreEmployes(String categorie) {
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
	
	public void setNombreEmployes(String categorie, int d) {
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
	
	public void licencie (int n, String categorie) {
		int nb_emp = getNombreEmployes(categorie);	
		
		// Si on demande à en licencier alors qu'il n'y a déjà plus personne
		if ((nb_emp - n) < 0) {
			this.setNombreEmployes(categorie, 0);
			this.journalRH.ajouter("il n y a plus d employes dans la categorie " + categorie);
	
		}
		else { 
			this.setNombreEmployes(categorie, nb_emp-n);
		}
	}
	
	public void embauche(int n, String categorie) {
		int nb_emp = getNombreEmployes(categorie);
		this.setNombreEmployes(categorie, nb_emp+n);
	}
	
	public double cout_humain_par_step() { 
		// Renvoie le coût total lié à la main d'oeuvre par step 
		double enfants = getNb_employes_enfants()* getSalaire("enfant");
		double adultes_eq = getNb_employes_equitable()*getSalaire("adulte équitable");
		double adultes = getNb_employes()*getSalaire("adulte"); 
		
		return enfants + adultes_eq + adultes;
	}
	
	public void trop_d_employes() {
		double pourcentage_enf = this.getPourcentage_enfants();
		int a_licencier = (int) (this.getNb_Employes_total()*0.05);
		int nb_enfants = (int) pourcentage_enf/100*a_licencier;
		
		this.licencie(nb_enfants, "enfants");
		this.licencie(a_licencier-nb_enfants, getDescription());
		this.journalRH.ajouter("On a licencié " + a_licencier + " personnes");
	}			
				
	public double getPourcentage_enfants(){
		double pourcentage = (this.getNb_employes_enfants()/this.getNb_Employes_total())*100;
		return pourcentage;
	}
	
	public double getPourcentage_equitable(){
		double pourcentage = (this.getNb_employes_equitable()/this.getNb_Employes_total())*100;
		return pourcentage;
	}
	
	/* Fonction qui permet d'implémenter notre stratégie. 
	Si notre solde est supérieur à ce que nous coûte 10 tours de simulation
	en ressources humaines on peut embaucher de nouveaux employés adultes dont une part en équitable.
	On suppose qu'en faisant une telle action on diminue le travail infantile de 3%.
	*/
	
	public void strategie()  {
		double solde = this.getSolde();
		if (solde > 10*cout_humain_par_step()){
			int nb_enf = getNb_employes_enfants();
			
			// le nombre d'employés qui changent de catégorie est égale à 3% des enfants
			int nb_employes_modif = (int) Math.round(0.03*nb_enf);
			
			// parmis ces employés, 80% passent en équitable
			int modif_equitable = (int) Math.round(nb_employes_modif*0.8);
			
			this.licencie(nb_employes_modif,"enfant");
			this.embauche(modif_equitable, "adulte équitable");
			this.embauche(nb_employes_modif - modif_equitable, "adulte");
		}
	}
	
	public void next() {
		super.next();
		this.strategie();
		this.journalRH.ajouter("\n-------------- ETAPE " + Filiere.LA_FILIERE.getEtape() + " --------------------");
		/*this.journalRH.ajouter("nombre d'employes équitable :" + this.getNb_employes_equitable());
		this.journalRH.ajouter("nombre d'employes adultes : "+ this.getNb_employes());
		this.journalRH.ajouter("nombre d'employes enfants " + this.getNb_employes_enfants());*/
		this.journalRH.ajouter("nombre d'employes dans la plantation " + this.getNb_Employes_total());
		this.journalRH.ajouter("pourcentage d'enfants " + this.getPourcentage_enfants());
		this.journalRH.ajouter("pourcentage d'employés équitable " + this.getPourcentage_equitable());
	}
	

}
