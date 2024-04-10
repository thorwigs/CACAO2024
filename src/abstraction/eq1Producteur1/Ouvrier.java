package abstraction.eq1Producteur1;

public class Ouvrier extends Producteur1Acteur {
	public double anciennete;
	public double rendement;
	public double salaire;
	public boolean isEquitable;
	public Ouvrier(double anciennete,double rendement,double salaire) {
		
		this.rendement=rendement;
		this.anciennete=anciennete;
		this.salaire=salaire;
	}
	public double getAnciennete() {
		return anciennete;
	}
	public void setAnciennete(double anciennete) {
		this.anciennete = anciennete;
	}
	public double getRendement() {
		return rendement;
	}
	public void setRendement(double rendement) {
		this.rendement = rendement;
	}
	public double getSalaire() {
		return salaire;
	}
	public void setSalaire(double salaire) {
		this.salaire = salaire;
	}
	public boolean getIsEquitable() {
		return this.isEquitable;
		
	}
	public void setIsEquitable() {
		
	}
	
	

}
