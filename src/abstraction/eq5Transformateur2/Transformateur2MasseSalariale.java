package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;
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
	
	
	public Transformateur2MasseSalariale() {
		super();
	}
	public void initialiser() {
		super.initialiser();
		NbSalaries = 100000;
		salaire = 2000;
		coutLicenciement1Salarie = 4*salaire;
		capaciteTransformation = 3.7;
	}
	
	
	////// A FINIR /////////////////////////////////////////
	public double TonnesTransformees(Feve f, Chocolat c) {
		// modif des stocks
		return 2;
	}
	public double CoutTransformation(Chocolat c) {
		return 2;
	}
	///////////////////////////////////////////////////////
	
	
	
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
	
	
	
	////// A FINIR //////////////////////////////////////////
	public void next() {
		super.next();
		// Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Transformation",this.CoutTransformation(); A completer
		// Filiere.LA_FILIERE.getBanque().payerCout(this, cryptogramme, "Masse Salariale",this.CoutMasseSalariale(); A completer

		
	}
	////////////////////////////////////////////////////////

}
