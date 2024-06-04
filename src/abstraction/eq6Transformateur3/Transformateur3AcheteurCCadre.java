package abstraction.eq6Transformateur3;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.bourseCacao.BourseCacao;
import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IActeur;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.general.Variable;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;
import abstraction.eq4Transformateur1.*;
import abstraction.eq5Transformateur2.*;
import abstraction.eq7Transformateur4.*;
import abstraction.eq8Distributeur1.*;
import abstraction.eq9Distributeur2.*;


public class Transformateur3AcheteurCCadre extends PrévisionAide implements IAcheteurContratCadre {
	protected SuperviseurVentesContratCadre supCC;
	protected List<ExemplaireContratCadre> contratsEnCours;
	protected List<ExemplaireContratCadre> contratsTermines;
	protected HashMap<Feve, Double > coûtMoyenAchatFeve;
	protected HashMap<Feve, Integer > nbAchatFeve;
	protected Journal journalCC6;

	/**
	 * @author Mahel
	 */
	public Transformateur3AcheteurCCadre() {
		super();
		this.contratsEnCours= new LinkedList<ExemplaireContratCadre>();
		this.contratsTermines= new LinkedList<ExemplaireContratCadre>();
		this.journalCC6 = new Journal(this.getNom()+" journal CC6", this);
		this.coûtMoyenAchatFeve = new HashMap<Feve, Double > ();
		this.nbAchatFeve = new HashMap<Feve, Integer >();
		
	}

	/**
	 * @author Mahel
	 */
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
		coûtMoyenAchatFeve.put(Feve.F_BQ,0.0);
		coûtMoyenAchatFeve.put(Feve.F_MQ,0.0);
		coûtMoyenAchatFeve.put(Feve.F_MQ_E,0.0);
		coûtMoyenAchatFeve.put(Feve.F_HQ,0.0);
		coûtMoyenAchatFeve.put(Feve.F_HQ_E,0.0);
		coûtMoyenAchatFeve.put(Feve.F_HQ_BE,0.0);
		
		nbAchatFeve.put(Feve.F_BQ,0);
		nbAchatFeve.put(Feve.F_MQ,0);
		nbAchatFeve.put(Feve.F_MQ_E,0);
		nbAchatFeve.put(Feve.F_HQ,0);
		nbAchatFeve.put(Feve.F_HQ_E,0);
		nbAchatFeve.put(Feve.F_HQ_BE,0);
	
	
	
	
	
	
	}
	/**
	 * @author Mahel
	 * 
	 */
	public void next() {
		super.next();
		this.journalCC6.ajouter(Color.BLUE, Color.white,"=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		this.journalCC6.ajouter("=== Partie Achat fèves ====================");
		HashMap<Feve, Integer> Decision = super.Decision();
		super.addhistorique(coûtMoyenAchatFeve, stockChoco, stockChocoMarque);
		for(Feve f : Decision.keySet()) {
			if(Decision.get(f)>10) {
				this.journalCC6.ajouter("   "+f+" suffisamment de vente pour passer un CC");
				double parStep = Decision.get(f);
				Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
				List<IVendeurContratCadre> vendeurs = supCC.getVendeurs(f);
				for(IVendeurContratCadre v : vendeurs) {
					if (v instanceof Transformateur3Acteur || 
							v instanceof Transformateur1Acteur ||
							v instanceof Transformateur2Acteur ||
							v instanceof Distributeur1Acteur ||
							v instanceof Distributeur2Acteur
							){
						vendeurs.remove(v);
					}
				}
				if (vendeurs.size()>0) {
					for(IVendeurContratCadre vendeur : vendeurs) {
						journalCC6.ajouter("   "+vendeur.getNom()+" retenu comme vendeur parmi "+vendeurs.size()+" vendeurs potentiels");
						ExemplaireContratCadre contrat = supCC.demandeAcheteur(this, vendeur, f, e, cryptogramme, false);
						if (contrat==null) {
							journalCC6.ajouter(Color.RED, Color.white,"   echec des negociations");
						} 
						else {
							if ( nbAchatFeve.get(f) == 0) {
								nbAchatFeve.put(f, nbAchatFeve.get(f)+1);
								coûtMoyenAchatFeve.put(f, (contrat.getPrix()));
							}
							else{
								double m = coûtMoyenAchatFeve.get(f) * nbAchatFeve.get(f);
								nbAchatFeve.put(f, nbAchatFeve.get(f)+1);
								coûtMoyenAchatFeve.put(f, (m+ contrat.getPrix())/nbAchatFeve.get(f));
							}
	
							journalCC6.ajouter("liste des contrats en cours");
							for(ExemplaireContratCadre c : this.contratsEnCours) {
								journalCC6.ajouter("Contrat numéro : " + c.getNumero());
							}
							
							journalCC6.ajouter("liste des contrats terminés");
							for(ExemplaireContratCadre c : this.contratsTermines) {
								journalCC6.ajouter("Contrat numéro : " + c.getNumero());
							}
							
							this.contratsEnCours.add(contrat);
							journalCC6.ajouter(Color.GREEN, Color.WHITE, "   contrat signe : #"+contrat.getNumero()+
									" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()
									+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());
							break;
						}
					
					}
				
					
				} else {
					journalCC6.ajouter("   pas de vendeur");
				}
			}
			// On archive les contrats termines
			for (ExemplaireContratCadre c : this.contratsEnCours) {
				if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
					this.contratsTermines.add(c);
				}
			}
			for (ExemplaireContratCadre c : this.contratsTermines) {
				journalCC6.ajouter("Archivage du contrat "+c);
				this.contratsEnCours.remove(c);
			}
			this.journalCC6.ajouter("Nombre de contrats en cours : "+this.contratsEnCours.size());
			this.journalCC6.ajouter("Nombre de contrats termines : "+this.contratsTermines.size());
		}
	}
			
	/**
	 * @author Thomas
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
	 * @author Thomas
	 */
	public double restantAPayer() {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			res+=c.getMontantRestantARegler();
		}
		return res;
	}
	/**
	 * @author Thomas
	 */
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalCC6);
		return jx;
	}
	/**
	 * @author Mahel
	 */
	public boolean achete(IProduit produit) {
		if( ! produit.getType().equals("Feve")) {
			return false;
		}
		for(Feve f : stockFeves.keySet()) {
			if(f==produit) {
				return stockFeves.get(produit)+restantDu((Feve)produit)<20000;
			}
		}
		return false;
	}
	/**
	 * @author Thomas
	 */
	public Echeancier contrePropositionDeLAcheteur(ExemplaireContratCadre contrat) { /////////FCTION A SIMPLIFIER////////
		if (!contrat.getProduit().getType().equals("Feve")) {
			return null;
		}

		if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<12
				|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>6) {
			return contrat.getEcheancier();
		}
		else{
			return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12 , contrat.getEcheancier().getQuantiteTotale()/12 );
		}
	} 
	/**
	 * @author Thomas
	 */
	public double contrePropositionPrixAcheteur(ExemplaireContratCadre contrat) { /////////FCTION A SIMPLIFIER////////
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		double solde = Filiere.LA_FILIERE.getBanque().getSolde(this, cryptogramme)-restantAPayer();
		double prixSansDecouvert = solde / contrat.getQuantiteTotale();
		if (prixSansDecouvert<bourse.getCours(Feve.F_BQ).getValeur()) {
			return 0.0; // nous ne sommes pas en mesure de fournir un prix raisonnable
		}
		if (((Feve)contrat.getProduit()).isEquitable()) {
			
			return Math.min(0.8 * prixSansDecouvert, bourse.getCours(Feve.F_MQ).getValeur()*2);
			
			
		}
		else {
			double cours = bourse.getCours((Feve)contrat.getProduit()).getValeur();
			if (contrat.getPrix()<cours) {
				return Math.min(0.8*prixSansDecouvert, contrat.getPrix());
			}
			else {
				return Math.min(0.8* prixSansDecouvert, 1.04*cours);
			}
		}
	}
	/**
	 * @author Thomas
	 */
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		journalCC6.ajouter("Nouveau contrat accepté : "+"#"+contrat.getNumero()+" | Acheteur : "+contrat.getAcheteur()+" | Vendeur : "+contrat.getVendeur()+" | Produit : "+contrat.getProduit()+" | Quantité totale : "+contrat.getQuantiteTotale()+" | Prix : "+contrat.getPrix());	
		this.contratsEnCours.add(contrat);
	}
	/**
	 * @author Arthur and Mahel
	 */
	public void receptionner(IProduit p, double quantiteEnTonnes, ExemplaireContratCadre contrat) {
		if(contrat.getAcheteur().getNom().equals("EQ6")) {
			this.journalCC6.ajouter("=== Partie réception Fèves ====================");
			journalCC6.ajouter("Contrat numéro : " + contrat.getNumero()+ "à la reception");
			journalCC6.ajouter("Reception de "+quantiteEnTonnes+" T de "+p+" du contrat "+contrat.getNumero()+ "(avec équipe" + contrat.getVendeur().getNom()+")");
			stockFeves.put((Feve)p, stockFeves.get((Feve)p)+quantiteEnTonnes);
			totalStocksFeves.ajouter(this, quantiteEnTonnes, cryptogramme);
		}
	}

}
