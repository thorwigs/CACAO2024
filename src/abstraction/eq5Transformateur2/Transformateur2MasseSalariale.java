package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

/** 
 * Cette classe a pour objectif de :
 *   - Déterminer le nombre de tonne à transformer
 *   - Calculer le coût de transformation
 *   - déterminer le besoin d'embauche / de licenciement
 *   - Calculer le coût de la masse Salariale
 */
public class Transformateur2MasseSalariale extends Transformateur2Acteur {
	protected int NbSalaries;
	protected double salaire;//salaire par step
	protected double coutLicenciement1Salarie;
	protected double capaciteTransformation;// tonnes transformées par 1 salarié par step
	protected Journal JournalMasseSalariale;
	protected double coutAdjuvants;//cout des adjuvants pour 1 tonne de chocolat
	protected double coutMachines;//cout des machines pour 1 tonne de chocolat
	
	
	public Transformateur2MasseSalariale() {
		super();
		this.JournalMasseSalariale=new Journal(this.getNom()+" journal Masse Salariale", this);
	}
	public void initialiser() {
		super.initialiser();
		NbSalaries = 100000;
		salaire = 2000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransformation = 3.7;
		coutAdjuvants = 1200;
		coutMachines = 8;
		
		this.JournalMasseSalariale.ajouter(""); // ajouter les infos 
	}
	
	/////////////////////////////////////
	// Nombres de tonnes Transformées  //
	/////////////////////////////////////
	public double TonnesTransformees(Feve f) {
		double tMaxTransformees = Math.min(this.getQuantiteEnStock(f, cryptogramme),this.NbSalaries/0.27); //Quantite maximale a transformer
		double tonnesTransformees =0.9*tMaxTransformees; //On transforme 90% (peut etre modifie) de ce qu'on peut transformer au maximum
		Chocolat c = Chocolat.get(f.getGamme(), f.isBio(), f.isEquitable());
		this.stockChoco.put(c, this.getQuantiteEnStock(c,cryptogramme)+tonnesTransformees); //Modifie le stock de tablettes
		this.stockFeves.put(f, this.getQuantiteEnStock(f,cryptogramme)-tonnesTransformees); //Modifie le stock de feves
		return tonnesTransformees; 
	}
	public double TotauxTonnesTransformees() {
		double totaux = 0;
		for (Feve f : Feve.values()) {
			totaux += this.TonnesTransformees(f);
		}
		this.JournalMasseSalariale.ajouter("On a Transformé au total "+totaux+" tonnes de chocolat");
		return totaux;
	}
	
	/////////////////////////////////////
	//        Coûts de Transfo         //
	/////////////////////////////////////
	public double CoutTransformation(ChocolatDeMarque cm, double tonnes) {
		return tonnes*coutMachines + tonnes*(1-cm.getPourcentageCacao())*coutAdjuvants ;
	}
	public double CoutTransformationTotal() {
		double coutTotal = 0;
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			double t = 100; // à modif
			coutTotal += this.CoutTransformation(cm,t);	
		}
		this.JournalMasseSalariale.ajouter("Le cout total de la transformation est de"+coutTotal);
		return coutTotal;
	}
	
	
	/////////////////////////////////////
	//     Embauche/Licenciement       //
	/////////////////////////////////////
	/* Stratégie Embauche/Licenciement :
	 * - Pas de Licenciement pour l'instant
	 * - On embauche seulement si le nombre de tonnes a transformées dépasse la capacité de transfo actuelle
	 * - On caclul ensuite le total
	 * 
	 *  Pour la V2 :
	 * - créer stratégie de licenciement
	 * - rajouter intérimaire
	 * - prendre en compte l'historique
	 */
	public int EmbaucheLicenciement(double TonnesTransformees) {
		double CapaciteTransfoTotale = NbSalaries * capaciteTransformation;
		
		if (TonnesTransformees >= CapaciteTransfoTotale) {
			int embauche = (int) ((TonnesTransformees - CapaciteTransfoTotale)/capaciteTransformation);
			NbSalaries += embauche;
			return embauche;
			
		}
		/*
		 * else {
		 * int licenciement = (int) ((TonnesTransformees - CapaciteTransfoTotale)/capaciteTransformation);
			NbSalaries += licenciement;
			return licenciement;
			}
		 */
		return 0;
	}
	public double CoutMasseSalariale(double TonnesTransformees) {
		double cout_salaire = NbSalaries * salaire;
		double cout_licenciement = 0;
		if (this.EmbaucheLicenciement(TonnesTransformees)<0) {
			cout_licenciement = this.EmbaucheLicenciement(TonnesTransformees) * coutLicenciement1Salarie ;
		}
		return  cout_salaire + cout_licenciement;
	}
	
	
	//////////////////////////////////////////////////////
	//   Next : permet de payer les coûts à la banque   //
	//////////////////////////////////////////////////////
	public void next() {
		super.next();
		// Paiement des coût de la masse salariale
		double TotauxTransformees = this.TotauxTonnesTransformees();
		if (this.CoutMasseSalariale(TotauxTransformees)>=0) {
			Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Masse Salariale", this.CoutMasseSalariale(TotauxTransformees));
		}
		
		// Paiement des coût de transformation
		double TotalCout = this.CoutTransformationTotal();
		if (TotalCout>=0) {
			Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Transformation" , TotalCout);
		}
	}

}



