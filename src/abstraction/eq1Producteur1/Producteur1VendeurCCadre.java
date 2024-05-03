/**@authors ER-RAHMAOUY Abderrahmane & Haythem*/
package abstraction.eq1Producteur1;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.acteurs.TransformateurX;
import abstraction.eqXRomu.bourseCacao.BourseCacao;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.Gamme;
import abstraction.eqXRomu.produits.IProduit;

public class Producteur1VendeurCCadre extends Producteur1VendeurBourse implements IVendeurContratCadre {

	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;

	public Producteur1VendeurCCadre() {
		super();
		this.contratsEnCours=new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines=new LinkedList<ExemplaireContratCadre>();
		this.journalCC = new Journal(this.getNom()+" journal CC", this);
	}

	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	/**
	 * Gère les actions à effectuer à chaque étape pour le vendeur en contrat cadre.
	 */
	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (Feve f : stock.keySet()) {
			if (stock.get(f).getValeur()-restantDu(f)>100 ) { // au moins 100 tonnes par step pendant 6 mois
				this.journalCC.ajouter("   "+f+" suffisamment en stock pour passer un CC");
				double parStep = Math.max(100, (stock.get(f).getValeur()-restantDu(f))/24); // au moins 100, et pas plus que la moitie de nos possibilites divisees par 2
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);
				
				acheteurs.remove(acheteurs.size()-1);
				
				if (acheteurs.size()>0) {
					
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					journalCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, f, e, cryptogramme, false);
					if (contrat==null) {
						journalCC.ajouter(Color.RED, Color.white,"   echec des negociations");
					} else {
						this.contratsEnCours.add(contrat);
						journalCC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
					}
				} else {
					journalCC.ajouter("   pas d'acheteur");
				}
			}

		}
	}
	/**
	 * Calcule la quantité restante à livrer pour un type de fève donné.
	 * @param f Le type de fève.
	 * @return La quantité restante à livrer.
	 */
	public double restantDu(Feve f) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}
	/**
	 * Calcule le prix des fèves en fonction des contrats en cours et terminés.
	 * @param f Le type de fève.
	 * @return Le prix des fèves.
	 */
	public double prix(Feve f) {
		double res = 0;
		int count = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res += c.getPrix();
				count += 1;
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			if (c.getProduit().equals(f)) {
				res += c.getPrix();
				count +=1;
			}
		}
		if (count != 0) {return res/count;}
		Gamme gamme = f.getGamme();
		boolean bio =f.isBio();
		boolean equitable = f.isEquitable();
		double prime = 0;
		
		if (bio) {
			prime += 100;
		}
		if (equitable) {
			prime += 60;
		}
		if (gamme == Gamme.HQ) {
			prime += 300;
		}
		res += prime + 1472;
		return res;

	}

	@Override
	public boolean vend(IProduit produit) {
		String s = produit.getType();
		if (s.equals("Feve")) {
			Feve f = (Feve) produit;
			this.stock.get(f);
			if (this.stock.get(f).getValeur()>10) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		journalCC.ajouter("      contreProposition("+contrat.getProduit()+" avec echeancier "+contrat.getEcheancier());
		Echeancier ec = contrat.getEcheancier();
		IProduit produit = contrat.getProduit();
		//Echeancier res = ec;
		boolean accepted = false;
		String type = produit.getType();
		if (type != "Feve") {
			journalCC.ajouter("Ce n'est pas une feve");
			return null;
		}
		Feve f = (Feve) produit;
		Double stockdispo = stock.get((Feve) produit).getValeur()-restantDu((Feve) produit);
		if (stockdispo < 600) { //Au moins 50 tonnes par step
			journalCC.ajouter("Je n'ai que" +stockdispo);
			return null;
		}
		int duree = ec.getStepFin()-ec.getStepDebut();
		if (duree < 0) {
			journalCC.ajouter("Pas de contract avec une duree inferieure a 5 mois");
			return null;
		}
		if (Filiere.LA_FILIERE.getEtape() < 0) {
			journalCC.ajouter("On fait pas de contract pendant les 6ers mois");
			return null;
		}
		if (this.contratsEnCours.size() >=10) {
			journalCC.ajouter("On fait pas plus que de 3 contracts en meme temps");
			return null;
		}
		if (ec.getStepDebut()<Filiere.LA_FILIERE.getEtape()+8) {
			accepted = true;
		}
		if (accepted = false){
			if (ec.getQuantiteTotale()<=stock.get((Feve)produit).getValeur()-restantDu((Feve)produit)) {
				journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12)));
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)(ec.getQuantiteTotale()/12));
			} else {
				journalCC.ajouter("      je retourne "+new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stock.get((Feve)produit).getValeur()-restantDu((Feve)produit)/12))));
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12,  (int)((stock.get((Feve)produit).getValeur()-restantDu((Feve)produit)/12)));
			}
		}
		journalCC.ajouter("Echencier accepted");
		return ec;		

	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat){
		// TODO Auto-generated method stub
		if (!contrat.getProduit().getType().equals("Feve")) {
			return 0;  
		}
		return prix((Feve) contrat.getProduit());



	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		List<Double> prix = contrat.getListePrix();
		if (prix.get(prix.size()-1)>=0.95*prix.get(0)) {
			journalCC.ajouter("      contrePropose le prix demande : "+contrat.getPrix());
			return contrat.getPrix();
		} else {
			double p = prix.get(0)/prix.get(1);
			journalCC.ajouter("      contreproposition("+contrat.getPrix()+") retourne "+prix.get(0)*1.05);
			return prix.get(0)*1.05;
		}

	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		this.journal.ajouter("Nouveau contract sur le marche :" + contrat);
	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		double stockActuel = stock.get(produit).getValeur((Integer)cryptogramme);
		double aLivre = Math.min(quantite, stockActuel);
		journalCC.ajouter("   Livraison de "+aLivre+" T de "+produit+" sur "+quantite+" exigees pour contrat "+contrat.getNumero());
		stock.get(produit).setValeur(this, aLivre, (Integer)cryptogramme);
		return aLivre;

	}
	/**
	 * Renvoie les journaux de l'acteur, y compris le journal des contrats cadre.
	 * @return Une liste contenant les journaux de l'acteur.
	 */
	public List<Journal> getJournaux() {
		List<Journal> res=super.getJournaux();
		res.add(journalCC);
		return res;
	}
}
