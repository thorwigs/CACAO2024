package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.filiere.Banque;
import abstraction.eqXRomu.filiere.Filiere;
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
	protected double coutAdjuvants;//cout des adjuvants pour 1 tonne de chocolat
	protected double coutMachines;//cout des machines pour 1 tonne de chocolat
	
	
	public Transformateur2MasseSalariale() {
		super();
	}
	public void initialiser() {
		super.initialiser();
		NbSalaries = 100000;
		salaire = 2000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransformation = 3.7;
		coutAdjuvants = 1200;
		coutMachines=8;
	}
	
	
	////// A FINIR /////////////////////////////////////////
	public double TonnesTransformees(Feve f, Chocolat c) {
		double tMaxTransformees = Math.min(this.getQuantiteEnStock(f, cryptogramme),this.NbSalaries/0.27); //Quantite maximale a transformer
		
		
		return tMaxTransformees*0.9; //On transforme 90% (peut etre modifie) de ce qu'on peut transformer au maximum
	}
	
	public double CoutTransformation(ChocolatDeMarque cm, double tonnes) {
		// 8 = coût machines pour une tonne par step
		// 1200 = coût adjuvants pour une tonne par step
		return tonnes*8 + tonnes*(1-cm.getPourcentageCacao())*1200 ;
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
	
	
	
	////// A FINIR ///////////////////////////////////////////
	public void next() {
		super.next();
		// Paiement des coût de la masse salariale
		double TonnesTransformees = 2;
		Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût Masse Salariale", CoutMasseSalariale(TonnesTransformees));
		
		// Paiement des coût de transformation pour chaque chocolat de marque
		/*for (ChocolatDeMarque cm : ) {
			double tonnes = 1;
			Filiere.LA_FILIERE.getBanque().payerCout(Filiere.LA_FILIERE.getActeur(getNom()), this.cryptogramme, "Coût transformation de"+tonnes+"tonnes de"+cm.getMarque(), CoutMasseSalariale(TonnesTransformees));
		}*/
	}
}
