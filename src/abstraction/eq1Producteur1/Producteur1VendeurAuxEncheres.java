package abstraction.eq1Producteur1;
/**author Fatima-Ezzahra*/
import java.util.ArrayList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.encheres.Enchere;
import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;

public class Producteur1VendeurAuxEncheres extends Producteur1VendeurAppelIDOffre implements IVendeurAuxEncheres {


	private double prixEnchereBq;

	private double prixEnchereHqEqui = 0;
	private double prixEnchereHqBio=0;
	private double prixEnchereHq=0;
	private double prixEnchereHqBioEqui=0;

	private double prixEnchereMq=0;
	private double prixEnchereMqEqui=0;
	private double prixEnchereMqbio=0;
	private double prixEnchereMqBioEqui=0;
	protected Journal journalEncheres;

	/**
	 * Constructeur de la classe Producteur1VendeurAuxEncheres.
	 */
	public Producteur1VendeurAuxEncheres() {
		super();
		this.journalEncheres =  new Journal(this.getNom()+" journal Encheres", this);
	}

	//public void initialiser() {
	//super.initialiser();
	//}
	/**
	 * Choisi une enchère parmi les propositions reçues.
	 * @param propositions La liste des propositions d'enchères reçues.
	 * @return L'enchère choisie ou null si aucune enchère n'a été sélectionnée.
	 */
	public Enchere choisir(List<Enchere> propositions) {
		int i = propositions.size();
		double prix = 0;
		int indice =0;
		for (int j=0; j<i; i++) { 
			if (propositions.get(j).getPrixTonne()>prix) {
				prix=propositions.get(j).getPrixTonne();
				indice=j;
			}

		}
		boolean bio =   ( (Feve  ) propositions.get(0).getProduit()).isBio();
		boolean equitable  =   ( (Feve  ) propositions.get(0).getProduit()).isEquitable();
		Gamme gamme = ( (Feve  ) propositions.get(0).getProduit()).getGamme();

		if (gamme ==  Gamme.BQ ) {
			if (prix >= prixEnchereBq ) {
				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
				return propositions.get(indice);
			}
			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
			return null;
		}

		else if (gamme == Gamme.MQ) {
			if (bio) {
				if (equitable) {
					if (prix >=  prixEnchereMqBioEqui ) {
						journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
						return propositions.get(indice);

					}
					else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
					return null;
				}
				else if (prix >= prixEnchereMqbio ) {
					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
					return propositions.get(indice);
				}
				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
				return null;

			}
			else if (equitable){
				if (prix >= prixEnchereMqEqui ) {
					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
					return propositions.get(indice);

				}
				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
				return null;

			}

			else if (prix >=  prixEnchereMq) {
				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
				return propositions.get(indice);

			}
			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
			return null;





		}

		else if (gamme == Gamme.HQ) {
			if (bio) {
				if (equitable) {
					if (prix >=  prixEnchereHqBioEqui ) {
						journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
						return propositions.get(indice);

					}
					else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
					return null;
				}
				else if (prix >= prixEnchereHqBio) {
					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
					return propositions.get(indice);
				}
				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
				return null;

			}
			else if (equitable){
				if (prix >= prixEnchereHqEqui ) {
					journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
					return propositions.get(indice);

				}
				else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
				return null;

			}

			else if (prix >=  prixEnchereHq) {
				journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +propositions.get(0).getQuantiteT() +"du produit"+ propositions.get(0).getProduit()+"au prix"+prix);
				return propositions.get(indice);

			}
			else journalEncheres.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+"On met en vente la quantité   " +0 +"du produit"+ propositions.get(0).getProduit());
			return null;





		}
		return null;

	}
	/**
	 * Renvoie les journaux de l'acteur, y compris le journal des enchères.
	 * @return Une liste contenant les journaux de l'acteur.
	 */
	public List<Journal> getJournaux() {
		List<Journal> res= super.getJournaux();
		res.add(this.journalEncheres);
		return res;
	}
	
	public void  update_quantite_vendu_bourse() {

		this.quantiteEnTBQ= this.getQuantiteEnStock(Feve.F_BQ, cryptogramme)-this.restantDu(Feve.F_BQ);

		this.quantiteEnTHQ= this.getQuantiteEnStock(Feve.F_HQ, cryptogramme)-this.restantDu(Feve.F_HQ);
		this.quantiteEnTMQ= this.getQuantiteEnStock(Feve.F_MQ, cryptogramme)-this.restantDu(Feve.F_MQ);
		
		
	}
	 
	public void next() {
		super.next();
		update_quantite_vendu_bourse();
		
	}
	
	
	
	
	
	
	
	
	
	
	

}
