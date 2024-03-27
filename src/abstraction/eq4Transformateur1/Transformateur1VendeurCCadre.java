package abstraction.eq4Transformateur1;

import java.awt.Color;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur1VendeurCCadre extends Transformateur1VendeurBourse implements IVendeurContratCadre {

	private static int PRIX_DEFAUT = 4500;
	
	private SuperviseurVentesContratCadre supCC;
	private List<ExemplaireContratCadre> contratsEnCours;
	private List<ExemplaireContratCadre> contratsTermines;
	protected Journal journalCC;
	
	@Override
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		
	}


	@Override
	public void next() {
		super.next();
		this.journalCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		for (ChocolatDeMarque f : stockChocoMarque.keySet()) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
			if (stockChocoMarque.get(f) - restantDu(f)>1200) { // au moins 100 tonnes par step pendant 6 mois
				this.journalCC.ajouter("   "+f+" suffisamment en stock pour passer un CC");
				double parStep = Math.max(100, (stockChocoMarque.get(f)-restantDu(f))/24); // au moins 100, et pas plus que la moitie de nos possibilites divisees par 2
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(f);
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
		// On archive les contrats termines
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			this.contratsEnCours.remove(c);
		}
		this.journalCC.ajouter("=================================");
	}
	
	public double restantDu(ChocolatDeMarque f) {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(f)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}
	
	
	
	
	

	@Override
	public List<Variable> getIndicateurs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variable> getParametres() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Journal> getJournaux() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCryptogramme(Integer crypto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationFaillite(IActeur acteur) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notificationOperationBancaire(double montant) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getNomsFilieresProposees() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Filiere getFiliere(String nom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getQuantiteEnStock(IProduit p, int cryptogramme) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean vend(IProduit produit) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double propositionPrix(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double livrer(IProduit produit, double quantite, ExemplaireContratCadre contrat) {
		// TODO Auto-generated method stub
		return 0;
	}

}
