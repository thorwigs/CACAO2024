package abstraction.eq1Producteur1;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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

	protected SuperviseurVentesContratCadre supCC;
	private HashMap<IAcheteurContratCadre, Integer> Acheteurs;
	protected List<ExemplaireContratCadre> contratsEnCours;
	protected List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCoC;

	private List<Double> echancesQua;

	public Producteur1VendeurCCadre() {
		super();
		this.contratsEnCours = new LinkedList<>();
		this.contratsTermines = new LinkedList<>();
		this.journalCoC = new Journal(this.getNom() + " journal CC", this);

		this.echancesQua = new LinkedList<>();
		this.Acheteurs = new HashMap<>();
	}

	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}

	public void next() {
		super.next();
		this.journalCoC.ajouter("=== STEP " + Filiere.LA_FILIERE.getEtape() + " ====================");
		for (Feve f : stock.keySet()) {
			if (stock.get(f).getValeur() - restantDu(f) > 1200) {
				this.journalCoC.ajouter("   " + f + " suffisamment en stock pour passer un CC");
				double parStep = Math.max(100, (stock.get(f).getValeur() - restantDu(f)) / 2);
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);
				if (acheteurs.size() > 0) {
					IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
					journalCoC.ajouter("   " + acheteur.getNom() + " retenu comme acheteur parmi " + acheteurs.size() + " acheteurs potentiels");
					ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, f, e, cryptogramme, false);
					if (contrat == null) {
						journalCoC.ajouter(Color.RED, Color.white, "   echec des negociations");
					} else {
						this.contratsEnCours.add(contrat);
						journalCoC.ajouter(Color.GREEN, acheteur.getColor(), "   contrat signe");
					}
				} else {
					journalCoC.ajouter("   pas d'acheteur");
				}
			}
		}
	}

	public double restantDu(Feve f) {
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res += c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

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
				count += 1;
			}
		}
		if (count != 0) {
			return res / count;
		}
		Gamme gamme = f.getGamme();
		boolean bio = f.isBio();
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
			if (this.stock.get(f).getValeur() > 10) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		journalCoC.ajouter("      contreProposition(" + contrat.getProduit() + " avec echeancier " + contrat.getEcheancier());
		Echeancier ec = contrat.getEcheancier();

		if ((contrat.getAcheteur().equals(Filiere.LA_FILIERE.getActeur("EQ6")))) {
			return null;
		}

		IProduit produit = contrat.getProduit();
		boolean accepted = false;
		String type = produit.getType();
		if (!type.equals("Feve")) {
			journalCoC.ajouter("Ce n'est pas une feve");
			return null;
		}

		this.echancesQua.add(ec.getQuantiteTotale());
		Feve f = (Feve) produit;

		double stockdispo = stock.get(f).getValeur() - restantDu(f);


		if (stockdispo < 20000) {
			journalCoC.ajouter("Insufficient stock: " + stockdispo);
			return null;
		}

		if (Acheteurs.keySet().contains(contrat.getAcheteur())) {
			if (Acheteurs.get(contrat.getAcheteur()) >= 3) {
				return null;
			}
		}
		
		int duree = ec.getStepFin() - ec.getStepDebut();
		if (duree < 10) {
			journalCoC.ajouter(Color.RED, Color.white,"Pas de contract avec une duree inferieure a 5 mois");
			return null;
		}
		if (Filiere.LA_FILIERE.getEtape() < 12) {
			journalCoC.ajouter(Color.RED, Color.white,"On fait pas de contract pendant la 12ere etapes");
			return null;
		}
		if (this.contratsEnCours.size() >= 5) {
			journalCoC.ajouter(Color.RED, Color.white,"Maximum number of ongoing contracts reached");
			return null;
		}
		if (ec.getStepDebut() < Filiere.LA_FILIERE.getEtape() + 8) {
			accepted = true;
		}
		if (!accepted) {
			if (ec.getQuantiteTotale() <= stock.get((Feve) produit).getValeur() - restantDu((Feve) produit)) {
				journalCoC.ajouter("      je retourne " + new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, (int) 0.7*(ec.getQuantiteTotale() / 12)));
				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, (int) (ec.getQuantiteTotale() / 12));
			} else {
				journalCoC.ajouter("      je retourne " + new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, (int) ((stock.get((Feve) produit).getValeur() - restantDu((Feve) produit) / 12))));
				return new Echeancier(Filiere.LA_FILIERE.getEtape() + 1, 12, (int) ((stock.get((Feve) produit).getValeur() - restantDu((Feve) produit) / 12)));
			}
		}
		journalCoC.ajouter("Echeancier accepted");
		return ec;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		if (!contrat.getProduit().getType().equals("Feve")) {
			return 0;
		}
		return prix((Feve) contrat.getProduit());
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		List<Double> prix = contrat.getListePrix();
		if (prix.get(prix.size() - 1) >= 0.975 * prix.get(0)) {
			journalCoC.ajouter("      contrePropose le prix demande : " + contrat.getPrix());
			return contrat.getPrix();
		} else {
			journalCoC.ajouter("      contreproposition(" + contrat.getPrix() + ") retourne " + prix.get(0) * 1.05);
			return prix.get(0) * 1.05;
		}
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCoC.ajouter("New contract: " + contrat);
		Acheteurs.put(contrat.getAcheteur(), Acheteurs.getOrDefault(contrat.getAcheteur(), 0) + 1);
		this.contratsEnCours.add(contrat);
	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		double stockActuel = stock.get(produit).getValeur((Integer) cryptogramme);
		double aLivre = Math.min(quantite, stockActuel);
		journalCoC.ajouter("   Livraison de " + aLivre + " T de " + produit + " sur " + quantite + " exigees pour contrat " + contrat.getNumero() + " avec " + contrat.getAcheteur());
		stock.get(produit).setValeur(this, stockActuel - aLivre, (Integer) cryptogramme);
		return aLivre;
	}

	public List<Journal> getJournaux() {
		List<Journal> res = super.getJournaux();
		res.add(journalCoC);
		return res;
	}
}
