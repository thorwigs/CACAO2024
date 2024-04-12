package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/*
 *///////////////////////////////////////YOUSSEF BEN ABDELJELIL//////////////////////////////
////////////////////////////////////////////////////////////////////////////////*/
public class Ouvrier extends Producteur1Acteur {
	public double anciennete;
	public double rendement;
	public double salaire;
	public boolean isEquitable;
	public boolean isForme;
	public boolean estEnfant;
	//constructeur par défaut: nouveau employé non équitable,adulte,salaire minimal,rendement normal=1
	public Ouvrier() {
		this.rendement=1;
		this.anciennete=0;
		this.salaire=1.8;
		this.isForme=false;
		this.isEquitable=false;
		this.estEnfant=false;	
	}
	//constructeur par paramètres
	public Ouvrier(double anciennete,double rendement,double salaire,boolean isForme,boolean isEquitable,boolean estEnfant) {
		
		this.rendement=rendement;
		this.anciennete=anciennete;
		this.salaire=salaire;
		this.isForme = isForme;
		this.isEquitable=isEquitable;
		this.estEnfant=estEnfant;
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
	public void setIsEquitable(boolean IsEquitable) {
		this.isEquitable=IsEquitable;
		
	}
	public boolean getIsForme() {
		return this.isForme;
	}
	public void setIsForme(boolean bool) {
		 this.isForme=bool;
	}
	public boolean getIsEnfant() {
		return this.estEnfant;
	}
	
	public void setIsEnfant(boolean estEnfant) {
		this.estEnfant=estEnfant;
	}
	//cette méthode peut sembler bizarre mais on va mettre à jour l'age des enfants,
	//après 10ans, un enfant devient un adulte
		
	public boolean equals(Object objet) {
		return (objet instanceof Ouvrier)&&
				(((Ouvrier) objet).getIsEquitable()==this.getIsEquitable())&&
				((((Ouvrier) objet).getAnciennete()==this.getAnciennete()))&&
				((((Ouvrier) objet).getIsForme()==this.getIsForme()))&&
				((((Ouvrier) objet).getSalaire()==this.getSalaire()))&&
				((((Ouvrier) objet).getRendement()==this.getRendement()));
				//critères d'égalités de travailleurs
	}
	
	public String toString() {
	    return "Ouvrier{ " +
	            " anciennete= " + anciennete +
	            " , rendement= " + rendement +
	            " , salaire= " + salaire +
	            " , isEquitable= " + isEquitable +
	            " , isForme= " + isForme +
	            " , isEnfant= " + this.estEnfant +
	            '}';
	}
		
	public static class ComparateurAnciennete implements Comparator<Ouvrier> {
	    @Override
	    public int compare(Ouvrier o1, Ouvrier o2) {
	        return Double.compare(o1.anciennete, o2.anciennete);
	    }
	}
	//méthode pour baser la comparaison entre deux ouvriers SELON L4ANCIENNETE , 
	//en effet, lors du licensiemnt, on licensie selon une ancienneté croissante
	
	
	

}
