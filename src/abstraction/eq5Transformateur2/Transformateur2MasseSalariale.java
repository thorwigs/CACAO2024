package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;

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
	
	
	public Transformateur2MasseSalariale() {
		super();
	}
	public void initialiser() {
		super.initialiser();
		NbSalaries = 100000;
		salaire = 2000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransformation = 3.7;
		this.JournalMasseSalariale.ajouter(""); // ajouter les infos 

	}
	
	
	////// A FINIR /////////////////////////////////////////
	public double TonnesTransformees(Feve f) {
		// modif des stocks
		return 2;
	}
	///////////////////////////////////////////////////////
	
	public double TotauxTonnesTransformees() {
		double totaux = 0;
		for (Feve f : Feve.values()) {
			totaux += TonnesTransformees(f);
		}
		this.JournalMasseSalariale.ajouter("On a Transformé au total "+totaux+" tonnes de chocolat");
		return totaux;
	}
	
	public double CoutTransformation(ChocolatDeMarque cm, double tonnes) {
		// 8 = coût machines pour une tonne par step
		// 1200 = coût adjuvants pour une tonne par step
		return tonnes*8 + tonnes*(1-cm.getPourcentageCacao())*1200 ;
	}
	public double CoutTransformationTotal() {
		double coutTotal = 0;
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			coutTotal += CoutTransformation(cm,t);	
		}
		this.JournalMasseSalariale.ajouter("Le cout total de la transformation est de"+coutTotal);
		return coutTotal;
	}
	
	
	
	public int EmbaucheLicenciement(double TonnesTransformees) {
		double CapaciteTransfoTotale = NbSalaries * capaciteTransformation;
		
		if (TonnesTransformees >= CapaciteTransfoTotale) {
			int embauche = (int) ((TonnesTransformees - CapaciteTransfoTotale)/capaciteTransformation);
			NbSalaries += embauche;
			return embauche;
			
		}else {
			int licenciement = (int) ((TonnesTransformees - CapaciteTransfoTotale)/capaciteTransformation);
			NbSalaries += licenciement;
			return licenciement;
		}
	}
	public double CoutMasseSalariale(double TonnesTransformees) {
		double cout_salaire = NbSalaries * salaire;
		double cout_licenciement = 0;
		if (EmbaucheLicenciement(TonnesTransformees)<0) {
			cout_licenciement = -EmbaucheLicenciement(TonnesTransformees) * coutLicenciement1Salarie ;
		}
		return  cout_salaire + cout_licenciement;
	}
	
	
	public void next() {
		super.next();
		
		// Paiement des coût de la masse salariale
		double TotauxTransformees = TotauxTonnesTransformees();
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Masse Salariale", CoutMasseSalariale(TotauxTransformees));
		
		// Paiement des coût de transformation
		double TotalCout = CoutTransformationTotal();
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Transformation" , TotalCout);
		}
}


