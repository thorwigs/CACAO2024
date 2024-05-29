
/**
 * Représente un gestionnaire de la masse salariale pour un acteur producteur spécifique dans la filière.
 * Gère les salaires, les effectifs et les formations du personnel ouvrier.
 * Code par Youssef en globalite

 */
package abstraction.eq1Producteur1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;




public class Producteur1MasseSalariale extends Producteur1Acteur {

	int ordre=0;
	double indemnite_licensiement; 
	protected Journal journalOuvrier;
	protected HashMap<Ouvrier, Integer> masseSalariale; // map ayant pour cle un type douvrier et valeur le nombre
	protected ArrayList<Ouvrier> types_ouvriers;//liste ayant tous les types d'ouvriers possibles
	protected ArrayList<Double> salaire;//liste pour le salaire de chaque type d'ouvrier
	protected ArrayList<ArrayList<Integer>> anciennete;// liste  de listes ayant 5 élements listes 
	//(qui sont les types d'ouvriers), chaque liste contient 245 élements et chaque élément  presente
	//le nombre d'employé ayant l'anciennete =indice*15, 
	//par exemple si la première miste de cette liste anciennete est =[10,20,0,15,16,200]
	//il y a 10 ouvriers de premier type (ouvrierEquitableForme) ayant une ancennete de 15*0=0jours
	//il y a 20 ouvriers du premier type ayant une ancennetede 15 jours
	//il y a 0 ouvriers du prmier type ayant une ancennete de 30 jours
	//il y a 15 ouvriers du prmier type ayant une ancennete de 45 jours
	//le choix de 245 éléments est pour prendre en compte de cette anciennete pour les idnemnites de licensiement,
	//et l'augmentation du salaire en fonction de l'anciennete
	//l'élement d'indice 244(ordre 245) est pour le nombre des ouvriers ayant >10 années d'anceinnete
	//On n'a pas pu faire une plus courte liste qui comporte respectivement comme année d'anciennete 0-5-10
	//car on doit mettre à jour l'anciennte chaque tour

	protected Ouvrier enfant;
	protected Ouvrier ouvrierEquitableForme;
	protected Ouvrier ouvrierEquitableNonForme;
	protected Ouvrier ouvrierNonEquitableForme;
	protected Ouvrier ouvrierNonEquitableNonForme;
	protected static double coutFormation = 2 ; 
	/**
	 * @author haythem
	 */
	public Producteur1MasseSalariale() {
		this.journalOuvrier = new Journal(this.getNom()+"   journal Ouvrier",this);//initialisation journal
		this.nb_enfants = 150;
		this.nb_normal = 100;
		this.nb_equitable = 30;

		enfant = new Ouvrier(true, false, false);
		ouvrierEquitableForme = new Ouvrier(false,true, true);
		ouvrierEquitableNonForme = new Ouvrier(false, true, false);
		ouvrierNonEquitableForme = new Ouvrier(false, false, true);
		ouvrierNonEquitableNonForme = new Ouvrier(false, false, false);

		/*Dans l'ordre suivant
		 * enfant
		 * equitable
		 * forme
		 */


		this.salaire=new ArrayList<Double>();

		this.types_ouvriers=new ArrayList<Ouvrier>();
		this.masseSalariale=new HashMap<Ouvrier, Integer>();
		this.anciennete=new ArrayList<ArrayList<Integer>>();

		this.salaire.add(0.8);
		this.salaire.add(4.0);
		this.salaire.add(3.0);
		this.salaire.add(2.4);
		this.salaire.add(1.8);




		this.types_ouvriers.add(enfant);
		this.types_ouvriers.add(ouvrierEquitableForme);
		this.types_ouvriers.add(ouvrierEquitableNonForme);
		this.types_ouvriers.add(ouvrierNonEquitableForme);
		this.types_ouvriers.add(ouvrierNonEquitableNonForme);



		masseSalariale.put(enfant, this.nb_enfants);

		masseSalariale.put(ouvrierEquitableNonForme, this.nb_equitable);
		masseSalariale.put(ouvrierEquitableForme, 0); 
		masseSalariale.put(ouvrierNonEquitableForme, 0);
		masseSalariale.put(ouvrierNonEquitableNonForme, this.nb_normal);

		masseSalariale.put(ouvrierEquitableNonForme, this.nb_equitable);
		masseSalariale.put(ouvrierEquitableForme, 0); // 
		masseSalariale.put(ouvrierNonEquitableForme, 0);
		masseSalariale.put(ouvrierNonEquitableNonForme, this.nb_normal);


		for (int i = 0; i < 5; i++) {
			// Créer une nouvelle liste d'entiers pour chaque élément de la liste principale
			ArrayList<Integer> innerList = new ArrayList<>();

			// Remplir chaque liste interne avec 245 zéros (au debut, l'ancienente=0 pour tous les ouvriers)
			for (int j = 0; j < 245; j++) {
				innerList.add(0);
			}

			// Ajouter la liste interne à la liste principale
			anciennete.add(innerList);
		}

		//mettre a jour l'anciennete initiale avec les nombres d'ouvriers appropriés
		this.anciennete.get(0).set(0, this.nb_enfants);
		this.anciennete.get(2).set(0, this.nb_equitable);
		this.anciennete.get(4).set(0, this.nb_normal);

	}


	/**
	 * @author haythem
	 * @return masse salariale
	 */
	public HashMap<Ouvrier,Integer> getMasseSalariale() {
		return this.masseSalariale;
	}


	public Integer get_Nombre_Enfant() { 
		return masseSalariale.get(enfant); 

	}
	public Integer get_Nombre_Ouvrier_Equitable_Forme() { 
		return masseSalariale.get(ouvrierEquitableForme); 
	}

	public Integer get_Nombre_Ouvrier_Equitable_NonForme() { 
		return masseSalariale.get(ouvrierEquitableNonForme); 
	}

	public Integer get_Nombre_Ouvrier_NonEquitable_Forme() { 
		return masseSalariale.get(ouvrierNonEquitableForme); 
	}

	public Integer get_Nombre_Ouvrier_NonEquitable_NonForme() { 

		return masseSalariale.get(ouvrierNonEquitableNonForme); 
	}

	public int get_Nombre_Total() {
		return this.get_Nombre_Enfant()+this.get_Nombre_Ouvrier_Equitable_Forme()+this.get_Nombre_Ouvrier_Equitable_NonForme()+this.get_Nombre_Ouvrier_NonEquitable_Forme()+this.get_Nombre_Ouvrier_NonEquitable_NonForme();
	}


	/**
	 * @author haythem
	 * @param ouvrier de type Ouvrier
	 * @param quantite quantite à ajouter
	 */
	public void addQuantiteOuvrier(Ouvrier ouvrier, int quantite) {
		// Récupère la quantité actuelle ou initialise à 0 si l'ouvrier n'existe pas
		Integer current = masseSalariale.getOrDefault(ouvrier, 0);
		// Met à jour le HashMap en ajoutant la quantité spécifiée à la quantité actuelle
		masseSalariale.put(ouvrier, current + quantite);

		// ajouter dans la liste d'anciennete le nombre d'ouvriers avec anciennete=0
		int index = types_ouvriers.indexOf(ouvrier);//indice de la liste correspondante au type d'ouvrier

		ArrayList<Integer> listeOuvrier = anciennete.get(index);//liste d'anciennete pour ce type

		listeOuvrier.set(0, listeOuvrier.get(0) + quantite);  //met a jour le premier element qui 
		//correspond à anciennete= 0 avec le nombre actuel+nombre a ajouter



	}

	/**
	 * @param type_ouvrier :le numéro du type d'ouvrier 
	 * @param quantite:quantite à ajouter
	 * @author haythem
	 */
	public void addQuantiteOuvrier(int type_ouvrier, int quantite) {//on peut utiliser cette méthode 
		//aussi en fournissant le numero de type d'ouvriers qu'on veut ajouter
		// 0 correspond à enfant
		//1 correspond à ouvrierEquitableForme
		//2 correspond à ouvrierEquitableNonForme
		//3 correspond à ouvrierNonEquitableForme
		//4 correspond à ouvrierNonEquitableNonForme
		addQuantiteOuvrier(this.types_ouvriers.get(type_ouvrier), quantite);


	}

	/**
	 * 
	 * @return salaire total en tenant compte de l'anciennete >10 ans correspond 
	 * à une augmentation de 15% du salaire
	 * @author haythem
	 */
	public double getSalaireTotal() {
		double total = 0;



		// Calcul du salaire total en multipliant le nombre d'ouvriers par leur salaire correspondant
		total += masseSalariale.get(enfant) * salaire.get(0);
		total += masseSalariale.get(ouvrierEquitableForme) * salaire.get(1);
		total += masseSalariale.get(ouvrierEquitableNonForme) * salaire.get(2);
		total += masseSalariale.get(ouvrierNonEquitableForme) * salaire.get(3);
		total += masseSalariale.get(ouvrierNonEquitableNonForme) * salaire.get(4);
		//les ouvriers ayant une ancienente >=1 années ont une augmentation du salaire =15%

		for (int i = 1; i < this.anciennete.size(); i++) {//pour chaque liste qui 
			//correspond à l'anciennete de chaque type d'ouvrier
			total+=this.anciennete.get(i).get(244)*salaire.get(i)*0.15;


		}
		return total;
	}






	/**@author youssef ben abdeljelil*/





	public void updateAnciennete() {
		ordre =0;
		for (ArrayList<Integer> innerList : this.anciennete) {

			if (ordre==0) {/*mettre a jour enfnant->ouvrier_NonEquitable_NonFormé*/

				Integer enfants_devenant_adulte=innerList.get(244);
				Integer current_enfant=this.masseSalariale.get(enfant);
				Integer current_adulte=this.masseSalariale.get(ouvrierNonEquitableNonForme);
				this.masseSalariale.put(ouvrierNonEquitableNonForme, enfants_devenant_adulte+current_adulte);//mettre a jour la masse salariale
				this.masseSalariale.put(enfant, current_enfant-enfants_devenant_adulte);

				//mise a jour de l'anciennete
				innerList.set(244, 0);			//le sernier elemnt de la liste de l'anciennete des enfants qui correpond aux enfants ayant 10 ans d'anciennete devient nulle;								
				ArrayList<Integer> nouvelle_liste = this.anciennete.get(4);
				nouvelle_liste.set(244, nouvelle_liste.get(244) + enfants_devenant_adulte);

				// Sauvegarder la dernière valeur pour ajustement après décalage

				// Décalage des éléments vers la droite
			}
			for (int i = innerList.size() - 1; i >= 0; i--) {
				if (i==244) {
					int derniere_valeur=innerList.get(i);
					innerList.set(i,derniere_valeur+innerList.get(i-1) );//mettre a jour le dernier element


				}

				else if (i==0) {
					innerList.set(0,0);// nombre d'employes ayant 0 ans d'anciennte devient 0 
				}

				else {
					innerList.set(i, innerList.get(i - 1));//décalage à droite
				}


			}
			ordre+=1;

		}





	}





	public void removeeEmploye(Ouvrier ouvrier,int nombreAsuprrimer) {
		double moyenne_salaire=0;
		for (Double salaire :this.salaire) {
			moyenne_salaire+=salaire;
		}
		moyenne_salaire=moyenne_salaire/this.salaire.size();
		indemnite_licensiement=0;


		int indice_type_ouvrier=types_ouvriers.indexOf(ouvrier);
		int nbr_reel_a_supprimer=Math.min(nombreAsuprrimer, this.masseSalariale.get(ouvrier));

		this.masseSalariale.put(ouvrier, this.masseSalariale.get(ouvrier)-nbr_reel_a_supprimer);

		ArrayList<Integer> anciennete_a_modifer=this.anciennete.get(indice_type_ouvrier);
		int restant=nbr_reel_a_supprimer;
		int index=0;


		while (restant > 0 && index < anciennete_a_modifer.size()) {
			// Quantité actuelle avant modification

			int supprimes=Math.min(anciennete_a_modifer.get(index), restant);
			restant=restant-supprimes;
			double pourcentageIndemnite = 0;
			if (index <= 122) { // Jusqu'à la cinquième année incluse
				pourcentageIndemnite = 0.30;
			} else if (index <= 243) { // De la sixième à la dixième année incluse
				pourcentageIndemnite = 0.35;
			} else { // Au-delà de la dixième année
				pourcentageIndemnite = 0.40;
			}

			// Calcul de l'indemnité pour les employés supprimés à cet index
			indemnite_licensiement += (int)(supprimes * pourcentageIndemnite * this.salaire.get(indice_type_ouvrier)); 
			if (indemnite_licensiement>0){
				Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "indemnite_licensiement",indemnite_licensiement );
			}

			anciennete_a_modifer.set(index, anciennete_a_modifer.get(index)-supprimes);

			// Incrémentation de l'index pour passer à l'année d'ancienneté suivante
			index++;
		}
		this.anciennete.set(indice_type_ouvrier, anciennete_a_modifer);
		//soustractio du solde bancaire


	}	
	public void removeEmploye(int type_to_remove,int nombreAsuprrimer) {

		removeeEmploye(this.types_ouvriers.get(type_to_remove), nombreAsuprrimer);
	}

	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(this.journalOuvrier);
		return res;

	}

	/**
	 * @author fatima
	 */
	public void amelioration() {
		if (this.croissanceParStep.size() > 5) {
			int etape = Filiere.LA_FILIERE.getEtape();
			int annee = Filiere.LA_FILIERE.getAnnee(etape);

			int size = this.croissanceParStep.size();
			boolean croissant = this.croissanceParStep.get(size-1)>0 && this.croissanceParStep.get(size-2)>0 && this.croissanceParStep.get(size-3)>0;

			if ((annee != 0)& (annee % 5 == 0) && croissant   ) {


				this.removeEmploye(0,10);//remove 10 enfants



				if (this.salaire.get(1) < 2.5 ) { 
					this.salaire.set(1, this.salaire.get(1)*1.08);
					this.salaire.set(2, this.salaire.get(2)*1.08);
					this.salaire.set(3, this.salaire.get(3)*1.08);
					this.salaire.set(4, this.salaire.get(4)*1.08);


				}

				if (this.salaire.get(0) < 2 ) { 
					this.salaire.set(0, this.salaire.get(0)*1.05);

				}


			}
		}
	}
	/**
	 * @author youssef
	 */

	@SuppressWarnings("static-access")
	public void formation (int nbr_à_former,boolean equitable) {
		//on suppose qu'un ouvrier peut encore travailler lors de sa formation
		int quantite_max_a_former_par_step=(int)0.1*(this.get_Nombre_Ouvrier_NonEquitable_NonForme()+this.get_Nombre_Ouvrier_Equitable_NonForme());
		double coutTotalFormation = 0;
		int nbr_max_a_former=nbr_à_former;

		if (nbr_à_former>quantite_max_a_former_par_step) {

			nbr_max_a_former=quantite_max_a_former_par_step;

		}

		//changer la masse salariale
		if (equitable ) {
			Integer a =this.masseSalariale.get(ouvrierEquitableNonForme);
			int nbr_reel_a_former=Math.min(a,nbr_max_a_former);
			coutTotalFormation = nbr_reel_a_former*this.coutFormation;

			this.masseSalariale.put(ouvrierEquitableForme, this.masseSalariale.get(ouvrierEquitableForme)+nbr_reel_a_former);
			this.masseSalariale.put(ouvrierEquitableNonForme, this.masseSalariale.get(ouvrierEquitableNonForme)-nbr_reel_a_former);
			int restant=nbr_reel_a_former;
			ArrayList<Integer> anciennete_initiale=this.anciennete.get(2);
			ArrayList<Integer> anciennete_finale=this.anciennete.get(1);


			int i=anciennete_initiale.size()-1;
			while (restant>0 && i<anciennete_initiale.size()) {
				int supprimes=Math.min(anciennete_initiale.get(i), restant);
				anciennete_initiale.set(i, anciennete_initiale.get(i)-supprimes);
				anciennete_finale.set(i, anciennete_finale.get(i)+supprimes);
				restant=restant-supprimes;
				i--;




			}




		}
		else {

			Integer a =this.masseSalariale.get(ouvrierNonEquitableNonForme);
			int nbr_reel_a_former=Math.min(a,nbr_max_a_former);
			coutTotalFormation = nbr_reel_a_former*this.coutFormation;
			this.masseSalariale.put(ouvrierNonEquitableForme, this.masseSalariale.get(ouvrierNonEquitableForme)+nbr_reel_a_former);
			this.masseSalariale.put(ouvrierNonEquitableNonForme, this.masseSalariale.get(ouvrierNonEquitableNonForme)-nbr_reel_a_former);
			int restant=nbr_reel_a_former;
			ArrayList<Integer> anciennete_initiale=this.anciennete.get(4);
			ArrayList<Integer> anciennete_finale=this.anciennete.get(3);
			int i=anciennete_initiale.size()-1;
			while (restant>0 && i<anciennete_initiale.size()) {
				int supprimes=Math.min(anciennete_initiale.get(i), restant);
				anciennete_initiale.set(i, anciennete_initiale.get(i)-supprimes);
				anciennete_finale.set(i, anciennete_finale.get(i)+supprimes);
				restant=restant-supprimes;
				i--;
			}
		}

		if (coutTotalFormation > 0) {
			Filiere.LA_FILIERE.getBanque().payerCout(this, this.cryptogramme, "Cout Total de formation des ouvirers", coutTotalFormation);
			this.journalOuvrier.ajouter("On a paye le cout suivant pour augmenter le rendement de certains ouvriers:"+ coutTotalFormation);
		}
	}










	public void next() {
		super.next();



		double Labor = this.getSalaireTotal();
		Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Labor",Labor );
		this.journalOuvrier.ajouter("on paie un salaire total de " + Labor);


		this.journalOuvrier.ajouter("Le nombre d'enfants = " + this.get_Nombre_Enfant());

		// Ajout d'une entrée de journal pour les ouvriers équitables formés
		this.journalOuvrier.ajouter("Le nombre d'ouvriers équitables formés = " + this.get_Nombre_Ouvrier_Equitable_Forme());

		// Ajout d'une entrée de journal pour les ouvriers équitables non formés
		this.journalOuvrier.ajouter("Le nombre d'ouvriers équitables non formés = " + this.get_Nombre_Ouvrier_Equitable_NonForme());

		// Ajout d'une entrée de journal pour les ouvriers non équitables formés
		this.journalOuvrier.ajouter("Le nombre d'ouvriers non équitables formés = " + this.get_Nombre_Ouvrier_NonEquitable_Forme());

		// Ajout d'une entrée de journal pour les ouvriers non équitables non formés
		this.journalOuvrier.ajouter("Le nombre d'ouvriers non équitables non formés = " + this.get_Nombre_Ouvrier_NonEquitable_NonForme());
		this.journalOuvrier.ajouter("indemnite à payer" + this.indemnite_licensiement);
		indemnite_licensiement=0;
		ordre=0;

		this.updateAnciennete();
		if (Filiere.LA_FILIERE.getEtape() % 10 ==0) {
			this.formation((int) (this.get_Nombre_Ouvrier_Equitable_NonForme()*0.2), true);
			this.formation((int) (this.get_Nombre_Ouvrier_Equitable_NonForme()*0.2), false);
		}

		this.amelioration();
	}






}