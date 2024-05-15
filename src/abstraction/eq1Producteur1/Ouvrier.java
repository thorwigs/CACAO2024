package abstraction.eq1Producteur1;
/**@author Youssef Ben Abdeljelil*/
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/*
 *///////////////////////////////////////YOUSSEF BEN ABDELJELIL//////////////////////////////
////////////////////////////////////////////////////////////////////////////////*/

//on va faire une classe Ouvrier qui décrit chaque ouvrier ,type,rendement,salaire,équitable,formation
public class Ouvrier extends Producteur1Acteur {
	public boolean estEnfant;
	public boolean isEquitable;
	public boolean isForme;
	//constructeur par défaut: nouveau employé non équitable,adulte,salaire minimal,rendement normal=1
	public Ouvrier() {
		this.isForme=false;
		this.isEquitable=false;
		this.estEnfant=false;	
	}
	//constructeur par paramètres
	public Ouvrier(boolean estEnfant,boolean isEquitable,boolean isForme) {

		this.isForme = isForme;
		this.estEnfant=estEnfant;
		if (estEnfant) {
			this.isEquitable=false;
			this.isForme=false;

		}
		else {
			this.isEquitable=isEquitable;
			this.isForme=isForme;
		}
		
		
		//Un efnant ne peut pas produire de l'équitable
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
				
				((((Ouvrier) objet).getIsForme()==this.getIsForme()))&&
				((((Ouvrier) objet).getIsEnfant()==this.getIsEnfant()))
				
				;
		//critères d'égalités de travailleurs
	}

	public String toString() {
		return "Ouvrier{ " +
				
				" , isEquitable= " + isEquitable +
				" , isForme= " + isForme +
				" , isEnfant= " + this.estEnfant +
				'}';
	}

	
	




}
