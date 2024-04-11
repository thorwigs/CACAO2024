package abstraction.eq1Producteur1;

import java.util.ArrayList;
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
	public Ouvrier() {
		this.rendement=1;
		this.anciennete=0;
		this.salaire=1.8;
		this.isForme=false;
		this.isEquitable=false;
		this.estEnfant=false;	
	}
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
		
		
	
	public void formation (Ouvrier o ) {
		double ancienneteMin =10;
		double augmentationRendement=0.5;
		double augmentationSalaire=0.1*this.getSalaire();
		if (( this.getAnciennete()<= ancienneteMin) && 
				(!(this.getIsForme())) &&
				(!(this.getIsEnfant())))
		
			{
			this.setRendement(this.getRendement()+ augmentationRendement);
			this.setSalaire(this.getSalaire()+augmentationSalaire);
			this.setIsForme(true);
		     int size = this.soldeParStep.size();
		     double solde = this.soldeParStep.get(size);
		     double k =5;
		     this.soldeParStep.add(size-1, solde - k);
		    	 
		}
		
		
		
	}
	
	public double getSalaireTotal(ArrayList<Ouvrier> listeOuvrier) {
		
		double s =0;
		for (Ouvrier ouvrier : listeOuvrier) {
			s=s+ouvrier.getSalaire();
			
		}
		return s;
		
	}
	public double getSalaireEquitable(ArrayList<Ouvrier> listeOuvrier, double salaireEquitable) {
			
			double s =0;
			for (Ouvrier ouvrier : listeOuvrier) {
				if (ouvrier.isEquitable) {
					s=s+ouvrier.getSalaire();
				}
				
				
			}
			return s;
			
		}
	public double getSalaireEnfants(ArrayList<Ouvrier> listeOuvrier, double salaireEnfant) {
			
			double s =0;
			for (Ouvrier ouvrier : listeOuvrier) {
				if (ouvrier.getIsEnfant()) {
					s=s+ouvrier.getSalaire();
				}
				
				
			}
			return s;
			
		}




	public int GetNombreOuvrierEquitable(ArrayList<Ouvrier> listeOuvrier) {
		int s=0;
		for (Ouvrier ouvrier : listeOuvrier) {
			if (ouvrier.isEquitable) {
				s=s+1;
				
			}
			
		}
		return s;
		
	}
	public int GetNombreOuvrierNonEquitable(ArrayList<Ouvrier> listeOuvrier) {
		int s=GetNombreOuvrierEquitable(listeOuvrier);
		
		return listeOuvrier.size()-s;
		
	}
	
	public int getNombreOuvrierFormés(ArrayList<Ouvrier> listeOuvrier) {
		
		int s=0;
		for (Ouvrier ouvrier : listeOuvrier) {
			if (ouvrier.isForme) {
				s=s+1;
				
			}
			
			
		}
		return s;
	}
	
	public void addOuvrier(ArrayList<Ouvrier> listeOuvriers,int nombre_à_ajouter,double anciennete,double salaire,double rendement,boolean isEquitable,boolean isForme,boolean isEnfant) {
		
		for (int i = 0; i < nombre_à_ajouter; i++) {
			Ouvrier ouvrier_a_ajouter=new Ouvrier(anciennete,rendement,salaire,isForme,isEquitable,isEnfant);
			listeOuvriers.add(ouvrier_a_ajouter);
			
		}
		
	}
	public boolean equals(Object objet) {
		return (objet instanceof Ouvrier)&&
				(((Ouvrier) objet).getIsEquitable()==this.getIsEquitable())&&
				((((Ouvrier) objet).getAnciennete()==this.getAnciennete()))&&
				((((Ouvrier) objet).getIsForme()==this.getIsForme()))&&
				((((Ouvrier) objet).getSalaire()==this.getSalaire()))&&
				((((Ouvrier) objet).getRendement()==this.getRendement()));
				
	}
	public String toString() {
	    return "Ouvrier{ " +
	            " anciennete= " + anciennete +
	            " , rendement= " + rendement +
	            " , salaire= " + salaire +
	            " , isEquitable= " + isEquitable +
	            " , isForme= " + isForme +
	            '}';
	}
	
	
	
	
	

}
