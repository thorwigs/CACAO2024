
/**
 * Représente un gestionnaire de la masse salariale pour un acteur producteur spécifique dans la filière.
 * Gère les salaires, les effectifs et les formations du personnel ouvrier.
 * Code par Youssef en globalite

 */
package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;

/**@author youssef ben abdeljelil*/


public class Producteur1MasseSalariale extends Producteur1Acteur {
	double indemniteTotal = 0;
	protected Journal journalOuvrier;

	////c'est une classe qui contient des fonctions utiles à opérer sur une liste de type Ouvrier//
	protected ArrayList<Ouvrier> listeOuvrier;
	protected ArrayList<Ouvrier> liste_Ouvrier;
	protected HashMap<Ouvrier, Integer> listeEmployee;
	protected Ouvrier enfant;
	protected Ouvrier Equitable;
	protected Ouvrier Normal;
	protected Ouvrier forme;
	protected Ouvrier EquiForme;
	/**
	 * Constructeur pour la classe Producteru1MasseSalariale.
	 * Initialise le journal des ouvriers et la liste des ouvriers.
	 */
	public Producteur1MasseSalariale() {
		this.journalOuvrier = new Journal(this.getNom()+"   journal Ouvrier",this);
		this.listeEmployee = new HashMap<Ouvrier, Integer>();
		this.enfant = new Ouvrier(0, 1, 0.8, false, false, true);
		this.Equitable = new Ouvrier(0, 1, 3, true, false, false);
		this.Normal = new Ouvrier(0, 1, 1.8, false, false, false);
		this.forme = new Ouvrier(0, 1, 2.2, false, true, false);
		this.EquiForme = new Ouvrier(0, 1, 3.5, true, true, false);
		this.nb_enfants = 150;
		this.nb_normal = 100;
		this.nb_equitable = 30;
		this.listeEmployee.put(enfant, nb_enfants);
		this.listeEmployee.put(Equitable, nb_equitable);
		this.listeEmployee.put(Normal, nb_normal);
		this.listeEmployee.put(EquiForme, 0);
		this.listeEmployee.put(forme, 0);

/*
		this.listeOuvrier=new ArrayList<Ouvrier>();
		this.addOuvrier(this.nb_enfants, this.labourEnfant, false, false, true);
		this.addOuvrier(this.nb_equitable, this.labourEquitable, true, false, false);
		this.addOuvrier(this.nb_normal, this.labourEnfant, false, false, false);
		liste_Ouvrier = listeOuvrier;
		*/
	}
	/**
	 * Renvoie la liste des ouvriers.
	 * @return La liste des ouvriers.
	 */
	//public ArrayList<Ouvrier> getListeOuvrier() {
	//	return this.listeOuvrier;
	//}


	/**
	 * Calcule le salaire total de tous les ouvriers.
	 * @return Le salaire total.
	 */
	/**@author Haythem*/
	public double getSalaireTotal() {
		double s = 0;
		for (Ouvrier o : this.listeEmployee.keySet()) {
			s += o.getSalaire()* this.listeEmployee.get(o);
		}
		

		return s;//retourne le salaire total à partir de notre liste d'ouvriers

	}

	/**
	 * Renvoie le nombre total d'enfants parmi les ouvriers.
	 * @return Le nombre d'enfants.
	 */
	public int getNombreEnfants() {
		int s = 0;
		for (Ouvrier o : this.listeEmployee.keySet()) {
			s += (o.getIsEnfant() ? 1 : 0 )*this.listeEmployee.get(o);
		}
		return s;//retourne le nombre d'enfants

	}
	/**
	 * Renvoie le nombre d'ouvriers travaillant dans le domaine équitable.
	 * @return Le nombre d'ouvriers équitables.
	 */
	public int GetNombreOuvrierEquitable() {
		int s = 0;
		for (Ouvrier o : this.listeEmployee.keySet()) {
			s += (o.getIsEquitable() ? 1 : 0 )*this.listeEmployee.get(o);
		}
		return s;//retourne le nombre de travailleurs dans l'équitable

	}
	/**
	 * Renvoie le nombre d'ouvriers travaillant dans le domaine non équitable.
	 * @return Le nombre d'ouvriers non équitables.
	 */
	public int GetNombreOuvrierNonEquitable() {
		int s1=this.GetNombreOuvrierEquitable();
		int s2=this.getNombreEnfants();

		return this.listeOuvrier.size()-s1-s2;//retourne le nombre de travailleurs noramux(non équitable)

	}
	/**
	 * Renvoie le nombre d'ouvriers ayant suivi une formation.
	 * @return Le nombre d'ouvriers formés.
	 */
	public int getNombreOuvrierFormés() {

		int s=0;
		
		for (Ouvrier o : this.listeEmployee.keySet()) {
			s += (o.getIsForme() ? 1 : 0 )*this.listeEmployee.get(o);
		}
		return s;
		//retourne le nombre d'ouvriers ayant fait une formation
	}
	/**
	 * Ajoute un certain nombre d'ouvriers à la liste.
	 * @param nombre_à_ajouter Le nombre d'ouvriers à ajouter.
	 * @param salaire Le salaire des nouveaux ouvriers.
	 * @param isEquitable Indique si les nouveaux ouvriers travaillent dans le domaine équitable.
	 * @param isForme Indique si les nouveaux ouvriers ont suivi une formation.
	 * @param isEnfant Indique si les nouveaux ouvriers sont des enfants.
	 */
	public void addOuvrier(int nombre_à_ajouter, double salaire, boolean isEquitable, boolean isForme, boolean isEnfant) {


		// Ajout des nouveaux ouvriers à la nouvelle liste
		if (isEquitable) {
			this.listeEmployee.put(Equitable, nombre_à_ajouter);
		}
		if (isEnfant) {
			this.listeEmployee.put(enfant, nombre_à_ajouter);
		}
		if (!(isEquitable) && !(isEnfant)) {
			this.listeEmployee.put(Normal, nombre_à_ajouter);
		}
		if (isEquitable && isForme) {
			this.listeEmployee.put(EquiForme, nombre_à_ajouter);
		}
		if (!(isEquitable) && isForme) {
			this.listeEmployee.put(forme, nombre_à_ajouter);
		}
	


	}

	/**
	 * Supprime un certain nombre d'ouvriers de la liste en fonction de certains critères.
	 * @param nombreASupprimer Le nombre d'ouvriers à supprimer.
	 * @param isEquitable Indique si les ouvriers à supprimer travaillent dans le domaine équitable.
	 * @param isForme Indique si les ouvriers à supprimer ont suivi une formation.
	 * @param isEnfant Indique si les ouvriers à supprimer sont des enfants.
	 * 	//une méthode permetttant de "licensier" des ouvriers 
	//selon un nombre en parametres et leurs types de 
	//travail(equitable,enfant,formation)
	//ResultatSuppression resultat = removeEmploye(listeOuvriers, 3, true, false, true);

	// si on veut retourner la liste des oruvriers après supression , on fait:ArrayList<Ouvrier> listeMiseAJour = resultat.listeMiseAJour;
	//si on veut retounrer l'indemnite:double indemniteTotale = resultat.indemniteTotale;
	 */
	/**@author youssef ben abdeljelil*/
	public void removeEmploye(int nombreASupprimer, boolean isEquitable, boolean isForme, boolean isEnfant) {
		// Créer une liste pour stocker temporairement les ouvriers à supprimer
		ArrayList<Ouvrier> ouvriersASupprimer = new ArrayList<>();


		// Filtrer la liste en fonction des attributs isEquitable, isForme, et isEnfant
		for (Ouvrier ouvrier : this.listeOuvrier) {
			if (ouvrier.isEquitable == isEquitable && ouvrier.isForme == isForme && ouvrier.estEnfant == false) {
				ouvriersASupprimer.add(ouvrier);
			}
		}

		// Trier la liste des ouvriers à supprimer par ancienneté
		Collections.sort(ouvriersASupprimer, (o1, o2) -> Double.compare(o1.getAnciennete(), o2.getAnciennete()));



		// Supprimer le nombre spécifié d'ouvriers de la liste principale, à partir de l'ancienneté la plus basse
		for (int i = 0; i < Math.min(nombreASupprimer, ouvriersASupprimer.size()); i++) {
			Ouvrier ouvrier = ouvriersASupprimer.get(i);
			this.listeOuvrier.remove(ouvrier);
			double indemnite =0;
			double anciennetéEnAnnées = ouvrier.getAnciennete() / 365;
			double salaire = ouvrier.getSalaire();

			double indemnité = anciennetéEnAnnées <= 10 ? (salaire * 30 / 4) * anciennetéEnAnnées : (salaire * 30 / 4) * 10 + (salaire * 30 / 3) * (anciennetéEnAnnées - 10);
			this.indemniteTotal += indemnité;

			if (anciennetéEnAnnées <= 10) {
				indemnite = salaire * 0.30 * anciennetéEnAnnées;
			} else if (anciennetéEnAnnées <= 15) {
				// Calcul pour les 10 premières années
				indemnite = salaire * 0.30 * 10;
				// Calcul pour les années entre 10 et 15
				indemnite += salaire * 0.35 * (anciennetéEnAnnées - 10);
			} else {
				// Calcul pour les 10 premières années
				indemnite = salaire * 0.30 * 10;
				// Calcul pour les 5 années suivantes (10 à 15 ans)
				indemnite += salaire * 0.35 * 5;
				// Calcul pour les années au-delà de 15 ans
				indemnite += salaire * 0.40 * (anciennetéEnAnnées - 15);
			}

			this.indemniteTotal+=indemnite;


		}
		this.journalOuvrier.ajouter("On a licence "+ Math.min(nombreASupprimer, ouvriersASupprimer.size())+ "personnes et on a due paye un total d'indemnite de:"+ this.indemniteTotal);

	}


	
	/**
	 * Met à jour l'ancienneté de chaque ouvrier dans la liste.
	 * Met également à jour les caractéristiques des ouvriers en fonction de leur ancienneté.
	/**@author Haythem*/
	public void UpdateAnciennete() {


		for (Ouvrier ouvrier : this.listeOuvrier) {
			double anciennete_step_precedent=ouvrier.getAnciennete();
			ouvrier.setAnciennete(anciennete_step_precedent+15.0);
			if ((ouvrier.getAnciennete()>3650) && (ouvrier.getIsEnfant())) {
				ouvrier.setIsEnfant(false);
				ouvrier.setSalaire(1.8);
				//après 10 ans, un enfant devient adule et son salire devient le salaire minimal d'un adulte

			}	//méthode pour mettre a jour l'anciennete chaque next par 
			//ajout de 15 jours à chaque ancienneté

		}
	}
	/**
	 * Procède à une formation pour un certain nombre d'ouvriers.
	 * @param nbr_à_former Le nombre d'ouvriers à former.
	 */
	public void formation (int nbr_à_former) {
		double cout_formation=0;//dépend de l'ancienneté
		double ancienneteMin =720;// ancienneté au moins de 2 ans pour faire une formation
		double augmentationRendement=0.5;// le rendement augmente de 0.5
		for (Ouvrier ouvrier : this.listeOuvrier) {
			double augmentationSalaire=0.2*ouvrier.getSalaire(); //le salaire augmente de 0.2
			if (( ouvrier.getAnciennete()>= ancienneteMin) && 
					(!(ouvrier.getIsForme())) &&
					(!(ouvrier.getIsEnfant())))

			{
				ouvrier.setRendement(ouvrier.getRendement()+ augmentationRendement);
				ouvrier.setSalaire(ouvrier.getSalaire()+augmentationSalaire);
				ouvrier.setIsForme(true);
				/*
				int size = ouvrier.soldeParStep.size();
				double solde = ouvrier.soldeParStep.get(size);
				double k =5;
				ouvrier.soldeParStep.add(size-1, solde - k);
				 */
			}

		}




	}
	/**@author youssef ben abdeljelil*/
	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(this.journalOuvrier);
		return res;

	}
	public void next() {
		super.next();
		double Labor = this.getSalaireTotal();

		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Labor",Labor );
		this.journalOuvrier.ajouter("Le nombre d'employees noramux = "+ this.GetNombreOuvrierNonEquitable());

		this.journalOuvrier.ajouter("Le nombre d'employees normaux = "+ this.GetNombreOuvrierNonEquitable());

		this.journalOuvrier.ajouter("Le nombre d'employees equitable = "+ this.GetNombreOuvrierEquitable());
		this.journalOuvrier.ajouter("Le nombre d'enfants employees = "+ this.getNombreEnfants());
		this.formation(100);
		this.UpdateAnciennete();
		if (this.indemniteTotal > 0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "indemniteTotale = ",indemniteTotal );
			this.indemniteTotal = 0;
			}
		this.amelioration();

	}
	/*Ecrit par Fatima-ezzahra*/
	public void amelioration() {
		int etape = Filiere.LA_FILIERE.getEtape();
		int annee = Filiere.LA_FILIERE.getAnnee(etape);
	
		int enfants = this.getNombreEnfants();
		int size = this.croissanceParStep.size();
		boolean croissant = this.croissanceParStep.get(size-1)>0 && this.croissanceParStep.get(size-2)>0 && this.croissanceParStep.get(size-3)>0;

		if ((annee != 0)& (annee % 5 == 0) && croissant   ) {


			this.removeEmploye(Math.min(10, enfants), false, false, true);//remove 10 enfants



			if (this.labourNormal < 2.5 ) { 
				double nouveauSalaire = this.labourNormal*1.08;
				this.labourNormal = nouveauSalaire;

			}

			if (this.labourEnfant < 2 ) { 
				double nouveauSalaireE = this.labourEnfant*1.05;
				this.labourEnfant= nouveauSalaireE;

			}


		}
	}

}
