/**
 * Représente un acteur producteur dans la filière, avec un focus sur la gestion de la plantation.
 */
package abstraction.eq1Producteur1;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
/**@author Abderrahmane Er-rahmaouy */
public class Producteur1Plantation extends Producteur1MasseSalariale implements IPlantation{
	protected double nombreHec = 3E6;
	Random random = new Random();
	protected double nombreHecMax = 5E6;
	protected Journal journalPlantation;
	protected HashMap<Feve, Double> plantation;
	protected boolean pesticides = true;
	protected HashMap<Feve, Double> production;
	protected HashMap<Feve, Double> prodAnnee;
	protected HashMap<Feve, Variable> stock;
	protected HashMap<Feve, Double> Stocck;
	protected static double PBQ = 0.7; 
	protected static double PMQ = 0.28;
	protected static double PHQ = 0.02;
	protected static double rendSaison = 1;



	protected HashMap<Feve,Double> ouvriers ;
	/**
	 * Constructeur pour la classe Producteur1Plantation.
	 * Initialise les attributs et le journal de la plantation.
	 */

	public Producteur1Plantation() {
		super();
		this.prodAnnee =  new HashMap<Feve, Double>();
		this.production = new HashMap<Feve, Double>();
		this.ouvriers = new HashMap<Feve,Double>();
		this.journalPlantation = new Journal(this.getNom()+"   journal Plantation",this);
		this.journalPlantation.ajouter("La part de plantation de BQ est:"+PBQ);
		this.journalPlantation.ajouter("La part de plantation de MQ est:"+PMQ);
		this.journalPlantation.ajouter("La part de plantation de HQ est:"+PHQ);
		this.maindoeuvre();

	}


	@Override
	/**
	 * Méthode pour déterminer la surface de plantation de chaque type de fève.
	 * @return Un dictionnaire avec chaque fève et sa surface de plantation associée.
	 */
	public HashMap<Feve, Double> plantation() {
		/*
		 * Retourne un dictionnaire où chaque clé est une Feve et chaque valeur est la surface associée de cette feve.
		 */
		plantation = new HashMap<Feve, Double>();
		Math.min(PBQ*this.nombreHec, PBQ*this.get_Nombre_Total());
		plantation.put(Feve.F_BQ,Math.min(PBQ*this.nombreHec, PBQ*this.get_Nombre_Total()) );
		plantation.put(Feve.F_MQ,Math.min(PMQ*this.nombreHec, PMQ*this.get_Nombre_Total()));
		plantation.put(Feve.F_HQ, Math.min(PHQ*this.nombreHec, PHQ*this.get_Nombre_Total()));
		/*
		plantation.put(Feve.F_BQ, PBQ*this.nombreHec );
		plantation.put(Feve.F_MQ, PMQ*this.nombreHec);
		plantation.put(Feve.F_HQ, PHQ*this.nombreHec);
		 */
		this.journalPlantation.ajouter("On a reussi planter");

		return plantation;
	}

	@Override
	/**
	 * Méthode pour ajuster la taille de la plantation en fonction des ajustements fournis.
	 * @param adjustments Les ajustements à appliquer à la taille de la plantation.
	 */
	public void adjustPlantationSize(HashMap<Feve, Double> adjustments) {
		/*
		 *  Si on n'a pas assez d'espace pour planter nos feves
		 */
		for (Feve feve : adjustments.keySet()) {

			double adjustment = adjustments.get(feve);
			double currentSize = plantation.getOrDefault(feve, 0.0);

			double newSize = currentSize + adjustment;

			if (newSize > nombreHecMax) {
				newSize = nombreHecMax;
			} else if (newSize < 0) {
				newSize = 0; 
			}

			this.achat(adjustment);
			plantation.put(feve, newSize);
			journalPlantation.ajouter(String.format("Adjusting %s plantation size by %.2f hectares", feve.name(), adjustment));
		}

	}


	@Override
	/**
	 * Méthode pour déterminer le nombre d'ouvriers nécessaires pour la plantation.
	 */
	public  void maindoeuvre() {
		/*
		 * return nombre d'ouvriers avec un rendement 1 necessaire
		 */

		double rendementPresent = this.get_Nombre_Enfant()+this.get_Nombre_Ouvrier_NonEquitable_NonForme()+(this.get_Nombre_Ouvrier_Equitable_Forme()*1.5+this.get_Nombre_Ouvrier_NonEquitable_Forme())*1.5+this.get_Nombre_Ouvrier_Equitable_NonForme();
		//double rendnecessaire = 0;
		/*
		if (rendementPresent < this.nombreHec) {



			if (rendementPresent < this.nombreHec) {
		        for (Feve f : this.plantation().keySet()) {
		            double requiredWorkers = f.isBio() ? 1.5 *(this.nombreHec- this.plantation().get(f)) : (this.nombreHec- this.plantation().get(f));
		            this.journalPlantation.ajouter("Nombre d'ouvrier avec un rendement 1 necessaire pour la plantation" 
		                                            + (f.isBio() ? " bio" : "") + " est :" + requiredWorkers);
		            rendnecessaire += requiredWorkers;


		        }
		    } else {
		        for (Feve f : this.plantation().keySet()) {
		        	rendnecessaire += 0;

		        }
		    }
		}
		 */

		this.recruitWorkers(this.nombreHec-rendementPresent);
	}	
	/**
	 * @author youssef
	 * methode qui tient compte des rendements selons la saison
	 * @return double qui decrit le rendement, ce rendement 
	 */
	public double effet_saison() {
		//de octobre a mars:grande récolte, 
		//avril-septembre:baisse de récolte :Saison des pluies
		int i = Filiere.LA_FILIERE.getEtape();

        int rang_step = i % 24;
        if ((rang_step >= 18 && rang_step <= 23) || (rang_step >= 0 && rang_step <= 5)) {
            return (random.nextInt((120 - 100) + 1) + 100) / 100.0; // forte saison, valeur random entre 1.00 et 1.20
        } else if (rang_step == 6 || rang_step == 17) {
            return (random.nextInt((110 - 90) + 1) + 90) / 100.0; // forte saison commence sa décroissance, valeur random entre 0.90 et 1.10
        } else if (rang_step == 7 || rang_step == 16) {
            return (random.nextInt((100 - 80) + 1) + 80) / 100.0; // forte saison encore en décroissance, valeur random entre 0.80 et 1.00
        } else {
            return (random.nextInt((80 - 55) + 1) + 55) / 100.0; // basse saison commence, valeur random entre 0.55 et 0.80
        }


}






	public void setHec(double hec) {
		this.nombreHec = hec;
	}


	/**
	 * Méthode pour embaucher des ouvriers en fonction de la demande de main-d'œuvre.
	 * @param demand La demande de main-d'œuvre pour chaque type de fève.
	 */
	public void recruitWorkers(double rend) {
		int aEmbaucher = 0;
		/*
	    for (Feve feve : Feve.keySet()) {
	        double requiredRendement = demand.get(feve);
	        aEmbaucher += ((int) requiredRendement);
	    }
		 */

		//System.out.println(true);
		aEmbaucher += ((int) (PBQ*rend)) ;


		aEmbaucher += ((int)(PMQ*rend));


		aEmbaucher += ((int)(PHQ*rend));


		//System.out.println(aEmbaucher);

		this.journalOuvrier.ajouter("On a besoin d'embaucher:" + aEmbaucher);
		double what =0.5*aEmbaucher;
		aEmbaucher = (int) Math.round(what);

		this.addQuantiteOuvrier(this.ouvrierEquitableNonForme,(int) Math.round(0.30 * aEmbaucher));
		
		aEmbaucher -= (int) Math.round(0.30 * aEmbaucher);
		this.addQuantiteOuvrier(this.ouvrierNonEquitableNonForme,(int) Math.round(0.40 * aEmbaucher));

		aEmbaucher -= (int) Math.round(0.40 * aEmbaucher);
		if (this.get_Nombre_Enfant() != 0) {
			this.addQuantiteOuvrier(this.enfant,(int) Math.round(0.30 * aEmbaucher));
			aEmbaucher -= (int) Math.round(0.30 * aEmbaucher);
		}
		this.addQuantiteOuvrier(this.ouvrierNonEquitableNonForme,aEmbaucher);
		
	
	}
	/**
	 * Méthode pour acheter une certaine quantité de terres.
	 * @param hec Le nombre d'hectares à acheter.
	 */
	public void achat(double hec) {
		/*
		 * Acheter des nouvelles hectares
		 */
		double cout = hec * 500;
		if (this.nombreHec+hec > this.nombreHecMax && this.getSolde() > cout) {
			this.journalPlantation.ajouter("On peut acheter la quantite demande; la quantite on peut acheter est: "+ (this.nombreHecMax-this.nombreHec));
			this.achat(this.nombreHecMax-this.nombreHec);
		}
		else if(this.getSolde() < cout) {
			this.journalPlantation.ajouter("On peut pas acheter la quantite demande car on n'a pas l'argent");
		}
		else if (cout > 0){
			Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Cout d'acheter des hectares",cout );
			this.setHec(this.nombreHec + hec);
		}


	}

	/**
	 * Méthode pour calculer la production annuelle de chaque type de fève.
	 * @return Un dictionnaire avec chaque fève et sa production annuelle associée.
	 */
	public HashMap<Feve, Double> prodAnnuel() {
		/*
		 * return la production annuel
		 */
		this.prodAnnee = new HashMap<Feve, Double>();
		this.prodAnnee.put(Feve.F_BQ,Math.min(PBQ*0.650*this.nombreHec, PBQ*0.650*this.get_Nombre_Total()));//0.7*0.650*this.nombreHec);
		this.prodAnnee.put(Feve.F_MQ, Math.min(PMQ*0.650*this.nombreHec, PMQ*0.650*this.get_Nombre_Total()));
		this.prodAnnee.put(Feve.F_HQ, Math.min(PHQ*0.650*this.nombreHec, PHQ*0.650*this.get_Nombre_Total()));
		this.prodAnnee.put(Feve.F_HQ_E, 0.0);
		this.prodAnnee.put(Feve.F_HQ_BE, 0.0);
		this.prodAnnee.put(Feve.F_MQ_E,0.0);
		this.journalPlantation.ajouter("Plantation Success, production par Annee:" + 0.7*0.650*this.nombreHec +"For BQ");
		this.journalPlantation.ajouter("Plantation Success, production par Annee:" + 0.28*0.650*this.nombreHec +"For MQ");
		this.journalPlantation.ajouter("Plantation Success, production par Annee:" + 0.02*0.650*this.nombreHec +"For HQ");

		return prodAnnee;
	}
	/**
	 * Méthode pour calculer la quantité de fèves produite prenant en compte les pesticides.
	 * @return Un dictionnaire avec chaque fève et sa production ajustée.
	 */
	public HashMap<Feve, Double> quantite() {
		/*
		 * return la production annuel qui prennent en compte les pesticides
		 */
		// TODO Auto-generated method stub
		this.prodAnnee = this.prodAnnuel();
		this.production = new HashMap<Feve, Double>();

		if (pesticides) {
			this.production.put(Feve.F_BQ,0.9*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.85*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.8*this.prodAnnee.get(Feve.F_HQ));
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
			this.journalPlantation.ajouter("On a decide d'utiliser des pesticicides dans notre production");
		}
		else {
			this.production.put(Feve.F_BQ,0.82*this.prodAnnee.get(Feve.F_BQ));
			this.production.put(Feve.F_MQ, 0.77*this.prodAnnee.get(Feve.F_MQ));
			this.production.put(Feve.F_HQ, 0.72*this.prodAnnee.get(Feve.F_HQ));
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
			this.journalPlantation.ajouter("On a decide de ne pas utiliser des pesticicides dans notre production");
		}
		this.journalPlantation.ajouter("Plantation Success, Quantite prevue par Annee:" + this.production.get(Feve.F_BQ) +"For BQ");
		this.journalPlantation.ajouter("Plantation Success, Quantite prevue par Annee:" + this.production.get(Feve.F_MQ) +"For MQ");
		this.journalPlantation.ajouter("Plantation Success, Quantite prevue par Annee:" + this.production.get(Feve.F_HQ) +"For HQ");
		return production;
	}

	@Override
	public void setProdTemps(HashMap<Feve, Double> d0, HashMap<Feve, Double> d1) {
		// TODO Auto-generated method stub

	}
	public HashMap<Feve, Double> ProdParStep(){
		/*
		 * retourne la production par etape
		 */
		this.prodAnnee = this.prodAnnuel();
		this.production = new HashMap<Feve, Double>();
		if (pesticides) {

			this.production.put(Feve.F_BQ,0.9*this.prodAnnee.get(Feve.F_BQ)/24);

			this.production.put(Feve.F_MQ, 0.85*this.prodAnnee.get(Feve.F_MQ)/24);

			this.production.put(Feve.F_HQ, 0.8*this.prodAnnee.get(Feve.F_HQ)/24);

			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}

		else {
			this.production.put(Feve.F_BQ,0.82*this.prodAnnee.get(Feve.F_BQ)/24);
			this.production.put(Feve.F_MQ, 0.77*this.prodAnnee.get(Feve.F_MQ)/24);
			this.production.put(Feve.F_HQ, 0.72*this.prodAnnee.get(Feve.F_HQ)/24);
			this.production.put(Feve.F_HQ_E, 0.0);
			this.production.put(Feve.F_HQ_BE, 0.0);
			this.production.put(Feve.F_MQ_E, 0.0);
		}
		this.journalPlantation.ajouter("Harvest Success, production par Step:" + this.production.get(Feve.F_BQ) +"For BQ");
		this.journalPlantation.ajouter("Harvest Success, production par Step:" + this.production.get(Feve.F_MQ) +"For MQ");
		this.journalPlantation.ajouter("Harvest Success, production par Step:" + this.production.get(Feve.F_HQ) +"For HQ");

		return production;
	}
	
	/**
	 * Méthode pour initialiser le stock de fèves pour chaque type de fève.
	 * @return Un dictionnaire avec chaque fève et sa quantité de stock associée.
	 */
	public HashMap<Feve, Variable> IniStock(){
		/*
		 * Initialise un dictionnaire feve,variable pour visualiser le stock
		 */
		this.stock = new HashMap<Feve, Variable>();
		HashMap<Feve, Double> pro = this.ProdParStep();

		for (Feve f : Feve.values()) {
			Variable v =  new Variable(this.getNom()+"Stock"+f.toString().substring(2), "<html>Stock de feves "+f+"</html>",this, 0.0, pro.get(f)*24, pro.get(f)*4);
			this.stock.put(f, v);
		}
		return stock;
	}
	/**
	 * Méthode pour initialiser le stock de fèves pour chaque type de fève.
	 * @return Un dictionnaire avec chaque fève et sa quantité de stock associée.
	 */

	@SuppressWarnings("static-access")
	public void next() {
		super.next();
		this.maindoeuvre();
		this.rendSaison = effet_saison();

		if (Filiere.LA_FILIERE.getEtape()%12 == 0) {
			this.achat((nombreHecMax-nombreHec)/2);
		}


	}
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(this.journalPlantation);
		return res;
	}

}
