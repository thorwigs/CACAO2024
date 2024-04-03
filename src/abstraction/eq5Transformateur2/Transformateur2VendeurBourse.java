package abstraction.eq5Transformateur2;

import abstraction.eqXRomu.bourseCacao.IVendeurBourse;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Transformateur2VendeurBourse extends Transformateur2AcheteurBourse implements IVendeurBourse {
	private Journal journalBourse;
	
	/* A faire : 
	 * --> rajouter une fonction pour la stratégie de vente (combien on propose de fève)
	 * --> notificationVente, finir la fonction
	 */
	
	
	////////////////////////////////////////////
	// Constructeur --> met à jour le journal //
	////////////////////////////////////////////
	public Transformateur2VendeurBourse() {
		super();
		this.journalBourse = new Journal(this.getNom()+" journal Bourse", this);
	}
	
	
	/////////////////////	
	// Offre proposéee //
	/////////////////////
	public double offre(Feve f, double cours) {
		//à faire : verif si cours>couts sinon pas de ventes
		//à faire : on peut vendre plsu que la stock
		//à faire : stratégie de vente sur le nombre de tonne proposé
		
		//Pas de vente si les fèves sont HQ
			if (f.getGamme() == Gamme.HQ) {
				return 0;
			}else {
			return 100;   // valeur arbitraire
			}
	}
	
	
	///////////////////////////////////////////
	// Notifs de la vente ou de la BlackList //	
	///////////////////////////////////////////
	public double notificationVente(Feve f, double quantiteEnT, double coursEnEuroParT) {
		// return de la qtté demandée
		// à faire : modifier si cette demande n'est pas respectée
		return quantiteEnT;
	}
	public void notificationBlackList(int dureeEnStep) {
		// TODO Auto-generated method stub
		journalBourse.ajouter(Filiere.LA_FILIERE.getEtape()+" : je suis blackliste pour une duree de "+dureeEnStep+" etapes");
	}
	
	
}
