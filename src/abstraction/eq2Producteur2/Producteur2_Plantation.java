package abstraction.eq2Producteur2;

public class Producteur2_Plantation extends Producteur2_MasseSalariale {
	protected double nb_hectares_max;
	protected double nb_hectares_actuel;
	protected double prix_plantation_hectare;
	
	protected int qualitee;
	protected double pourcentage_HI = 0.02;
	protected double pourcentage_MID = 0.38;
	protected double pourcentage_LOW = 0.6;
	
	
	public void initialiser() {
		double nb_hectares_max = 5000000;
		double nb_hectares_actuel = 5000000;
		double prix_plantation_hectare = 0; // à définir
		return;
	}

	public double getNb_hectares_max() {
		return nb_hectares_max;
	}

	public void setNb_hectares_max(double nb_hectares_max) {
		this.nb_hectares_max = nb_hectares_max;
	}

	public double getNb_hectares_actuel() {
		return nb_hectares_actuel;
	}

	public void setNb_hectares_actuel(double nb_hectares_actuel) {
		this.nb_hectares_actuel = nb_hectares_actuel;
	}

	public double getPrix_plantation_hectare() {
		return prix_plantation_hectare;
	}

	public void setPrix_plantation_hectare(double prix_plantation_hectare) {
		this.prix_plantation_hectare = prix_plantation_hectare;
	}
	
	public double getPourcentage_HI() {
		return pourcentage_HI;
	}

	public void setPourcentage_HI(double pourcentage_HI) {
		this.pourcentage_HI = pourcentage_HI;
	}

	public double getPourcentage_MID() {
		return pourcentage_MID;
	}

	public void setPourcentage_MID(double pourcentage_MID) {
		this.pourcentage_MID = pourcentage_MID;
	}

	public double getPourcentage_LOW() {
		return pourcentage_LOW;
	}

	public void setPourcentage_LOW(double pourcentage_LOW) {
		this.pourcentage_LOW = pourcentage_LOW;
	}
	
	
	
	public void planter(int quantitee) {
		if (getNb_hectares_actuel() + quantitee > getNb_hectares_max()) { //achat impossible
			return;
		}
		else { 
			setNb_hectares_actuel(getNb_hectares_actuel() + quantitee);
			// il reste à effectuer la transaction banquaire
		}
	}
	
	public double production_cacao() { // retourne la production actuelle de cacao sur 2 semaines en kg
		return getNb_hectares_actuel() * 500 / 26;
	}
	
	public double production_HI() { // retourne la productoin de cacao de haute qualité sur 2 semaines en kg
		return production_cacao() * getPourcentage_HI();
	}
	
	public double production_LOW() { // retourne la productoin de cacao de basse qualité sur 2 semaines en kg
		return production_cacao() * getPourcentage_LOW();
	}
	public double production_MID() { // retourne la productoin de cacao de moyenne qualité sur 2 semaines en kg
		return production_cacao() * getPourcentage_MID();
	}
	
}
// 1hectare = 500kg / an cacao
// implémenter la qualité 
