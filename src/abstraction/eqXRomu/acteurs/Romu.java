package abstraction.eqXRomu.acteurs;


import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Romu extends RomuActeur {

	public void next() {
		this.journal.clear();
		super.next();
		// Afficher les ventes
		this.journal.ajouter("=== VENTES DE CHOCOLAT DE MARQUE ================================");
		String st="________________________";
		for (int etape=Filiere.LA_FILIERE.getEtape(); etape>=Math.max(0, Filiere.LA_FILIERE.getEtape()-300); etape--) {
			st+="etape "+Journal.entierSur6( etape) +" ";
		}
		this.journal.ajouter(st);
		for (ChocolatDeMarque c : Filiere.LA_FILIERE.getChocolatsProduits()) {
			String s = Journal.texteSurUneLargeurDe(c.getNom(), 20)+" -> ";
			for (int etape=Filiere.LA_FILIERE.getEtape(); etape>=Math.max(0, Filiere.LA_FILIERE.getEtape()-300); etape--) {
				s+=Journal.doubleSur(Filiere.LA_FILIERE.getVentes(c, etape), 9, 2) +" ";
			}
			this.journal.ajouter(s);
		}
			this.journal.ajouter("=============================================================");
	}
}
