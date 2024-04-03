package abstraction.eq2Producteur2;

public class Producteur2_MasseSalariale extends Producteur2_Stocks {
	private int nb_employes ;
	private int nb_employes_equitable;
	private int nb_employes_enfants;
	
	private double salaire_enfant;
	private double salaire_adulte;
	private double salaire_adulte_equitable;
		
	public void initialiser() {
		int nb_employes =  3679200;
		int nb_employes_equitable = 100800;
		int nb_employes_enfants = 1260000; //25% au départ
		
		int salaire_enfant =  1;
		int salaire_adulte = 2;
		int salaire_adulte_equitable = 3;
		
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
	
	public double cout_par_step() {
		double enfants = getNb_employes_enfants()* getSalaire("enfant");
		double adultes_eq = getNb_employes_equitable()*getSalaire("adulte équitable");
		double adultes = getNb_employes()*getSalaire("adulte"); 
		return enfants + adultes_eq + adultes;
	}
	
	public void mise_a_jour() {
		
	}
	

}
