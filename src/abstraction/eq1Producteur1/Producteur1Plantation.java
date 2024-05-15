/**
 * Représente un acteur producteur dans la filière, avec un focus sur la gestion de la plantation.
 */
package abstraction.eq1Producteur1;

import java.util.HashMap;
import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Feve;
/**@author Abderrahmane Er-rahmaouy */
public class Producteur1Plantation extends MasseSalarialeNouveau implements IPlantation{
	protected double nombreHec = 3E6;
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

		plantation.put(Feve.F_BQ, PBQ*this.nombreHec );
		plantation.put(Feve.F_MQ, PMQ*this.nombreHec);
		plantation.put(Feve.F_HQ, PHQ*this.nombreHec);
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
		double rendementPresent = this.get_Nombre_Enfant()+this.get_Nombre_Ouvrier_NonEquitable_NonForme()+(this.get_Nombre_Ouvrier_Equitable_Forme()+this.get_Nombre_Ouvrier_NonEquitable_Forme())*1.2+this.get_Nombre_Ouvrier_Equitable_NonForme();
		if (rendementPresent < this.nombreHec) {



			for (Feve f : this.plantation().keySet()) {
				if (f.isBio()) {
					this.journalPlantation.ajouter("Nombre d'ouvrier avec un rendement 1 necessaire pour la plantation bio est :"+ 1.5*this.plantation().get(f));
					this.ouvriers.put(f, 1.5*this.plantation().get(f));
				} else {
					this.journalPlantation.ajouter("Nombre d'ouvrier avec un rendement 1 necessaire pour la plantation est :"+ this.plantation().get(f));
					this.ouvriers.put(f, this.plantation().get(f));
				}
			}
		}
		else {
			for (Feve f : this.plantation().keySet()) {
				this.ouvriers.put(f, 0.0);
			}

		}


	}	 

	public void setHec(double hec) {
		this.nombreHec = hec;
	}

	@Override
	/**
	 * Méthode pour embaucher des ouvriers en fonction de la demande de main-d'œuvre.
	 * @param demand La demande de main-d'œuvre pour chaque type de fève.
	 */
	public void recruitWorkers(HashMap<Feve, Double> demand) {
	    int aEmbaucher = 0;
	    for (Feve feve : demand.keySet()) {
	        double requiredRendement = demand.get(feve);
	        aEmbaucher += ((int) requiredRendement);
	    }
	    this.journalOuvrier.ajouter("On a besoin d'embaucher:" + aEmbaucher);
	    double what = 0.1 * aEmbaucher;
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
		this.prodAnnee.put(Feve.F_BQ,0.7*0.650*this.nombreHec);
		this.prodAnnee.put(Feve.F_MQ, 0.650*0.28*this.nombreHec);
		this.prodAnnee.put(Feve.F_HQ, 0.650*0.02*this.nombreHec);
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

	public void next() {
		super.next();
		this.maindoeuvre();
		this.recruitWorkers(this.ouvriers);
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
