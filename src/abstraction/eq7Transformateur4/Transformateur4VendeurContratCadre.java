package abstraction.eq7Transformateur4;

//fichier codé par Anaïs, Eliott et Pierrick
//Eliott : méthode vend()
//Pierrick : méthode initialiser()
//Anaïs : tout le reste



import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.contratsCadres.Echeancier;
import abstraction.eqXRomu.contratsCadres.ExemplaireContratCadre;
import abstraction.eqXRomu.contratsCadres.IAcheteurContratCadre;
import abstraction.eqXRomu.contratsCadres.IVendeurContratCadre;
import abstraction.eqXRomu.contratsCadres.SuperviseurVentesContratCadre;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {
	private SuperviseurVentesContratCadre supCC;
	

	protected Journal journalVCC;

	public Transformateur4VendeurContratCadre() {
		super();
		this.journalVCC = new Journal(this.getNom()+" journal CC vente", this);
	}
	
	//////////////je me permet de rajouter ça sinon on initialise pas supCC
	public void initialiser() {
		super.initialiser();
		this.supCC = (SuperviseurVentesContratCadre)(Filiere.LA_FILIERE.getActeur("Sup.CCadre"));
	}
	//////////////
	
	//codé par Eliott		
	public boolean vend(IProduit produit) { 

		for (ChocolatDeMarque c : this.chocolatCocOasis) {
			if ((produit.getType().equals("ChocolatDeMarque")) && (c == (ChocolatDeMarque)produit) && (stockChocoMarque.get(produit)>25000) ) {
				return true;
			}
		}
		for (ChocolatDeMarque c : this.chocolatDistributeur) {
			if ((produit.getType().equals("ChocolatDeMarque")) &&(c == (ChocolatDeMarque)produit) && (stockChoco.get(((ChocolatDeMarque)produit).getChocolat()) > 25000 )) {
				return true;
			}
		}
		return false ; 
		
		//à modifier selon ce qu'on veut vendre et dans quelles circonstances
	}

	//Négociations
	
	public Echeancier contrePropositionDuVendeur(ExemplaireContratCadre contrat) {
		///à modifier selon comment on veut nos échéanciers
		if (!(contrat.getProduit().getType().equals("ChocolatDeMarque"))) {
			return null;
		}

		//si ce sont des mirages , codé par Anaïs et Pierrick
		if ( ((ChocolatDeMarque)contrat.getProduit()).getMarque() == "Mirage") {
			if (stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit()))-restantALivrer((ChocolatDeMarque)(contrat.getProduit()))-contrat.getEcheancier().getQuantiteTotale()>100) {
			if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<11
					|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>8) {
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, contrat.getEcheancier().getQuantiteTotale()/12 );
			} else { // les volumes sont corrects, la duree et le debut aussi
				return contrat.getEcheancier();
			}
		} else {
			double marge = - 25000 + stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit())) - restantALivrer((ChocolatDeMarque)(contrat.getProduit()));
			if (marge<100) {
				return null;
			} else {
				double quantite = 25000 + Filiere.random.nextDouble()*(marge-100); // un nombre aleatoire entre 25000 et la marge
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, quantite/12 );
				}
			}
			
		//si ce ne sont pas des Mirage; codé par Eliott
		} else {		
			if (stockChoco.get( ((ChocolatDeMarque)(contrat.getProduit())).getChocolat() )-restantALivrer((ChocolatDeMarque)(contrat.getProduit()))-contrat.getEcheancier().getQuantiteTotale()>100) {
			if (contrat.getEcheancier().getStepFin()-contrat.getEcheancier().getStepDebut()<11
					|| contrat.getEcheancier().getStepDebut()-Filiere.LA_FILIERE.getEtape()>8) {
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, contrat.getEcheancier().getQuantiteTotale()/12 );
			} else { // les volumes sont corrects, la duree et le debut aussi
				return contrat.getEcheancier();
			}
		} else {
			double marge = - 25000 + stockChoco.get( ((ChocolatDeMarque)(contrat.getProduit())).getChocolat() ) - restantALivrer((ChocolatDeMarque)(contrat.getProduit()));
			if (marge<100) {
				return null;
			} else {
				double quantite = 25000 + Filiere.random.nextDouble()*(marge-100); // un nombre aleatoire entre 25000 et la marge
				return new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, quantite/12 );
				}
			}
		}
		
	}

	public double propositionPrix(ExemplaireContratCadre contrat) {
		return contrat.getQuantiteTotale()*5500;
	}//à modifier selon variation du nb d'employer

	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		return contrat.getPrix();
	}//à modifier selon notre prix minimal accepté

	//Après finalisation contrat 
	
	public double livrer(IProduit p, double quantite, ExemplaireContratCadre contrat) {
	
		//Si la livraison concerne des choco Mirage, codé par Anaïs et Pierrick
		if ( ((ChocolatDeMarque)p).getMarque() == "Mirage") {
				double stockMarque = (double)stockChocoMarque.get((ChocolatDeMarque)(contrat.getProduit()));
				
				if (quantite <= stockMarque) {
					journalVCC.ajouter("Livraison de "+quantite+" T de "+p+" du contrat "+contrat.getNumero());
					stockChocoMarque.put((ChocolatDeMarque)p, stockChocoMarque.get((ChocolatDeMarque)p)-quantite);
					totalStocksChocoMarque.retirer(this, quantite, cryptogramme);
					return quantite;
				}
				else {
					journalVCC.ajouter("Livraison de "+stockMarque+" T de "+p+" du contrat "+contrat.getNumero());
					stockChocoMarque.put((ChocolatDeMarque)p, stockChocoMarque.get((ChocolatDeMarque)p)-stockMarque);
					totalStocksChocoMarque.retirer(this, stockMarque, cryptogramme);
					return stockMarque;
				}
				
		//Si ce sont des chocos distributeurs, codé par Eliott
		} else {
			double stockDistrib = (double)stockChoco.get( ((ChocolatDeMarque)(contrat.getProduit())).getChocolat() );
			
			if (quantite <= stockDistrib) {
				journalVCC.ajouter("Livraison de "+quantite+" T de "+p+" du contrat "+contrat.getNumero());
				stockChoco.put(((ChocolatDeMarque)p).getChocolat(), stockChoco.get(((ChocolatDeMarque)p).getChocolat())-quantite);
				totalStocksChoco.retirer(this, quantite, cryptogramme);
				return quantite;
			}
			else {
				journalVCC.ajouter("Livraison de "+stockDistrib+" T de "+p+" du contrat "+contrat.getNumero());
				stockChoco.put(((ChocolatDeMarque)p).getChocolat(), stockChoco.get(((ChocolatDeMarque)p).getChocolat())-stockDistrib);
				totalStocksChoco.retirer(this, stockDistrib, cryptogramme);
				return stockDistrib;
			}
		}
		
		
		
		
		
		
	
		
		
	}//s'inspirer de AcheteurCC
	
	public void notificationNouveauContratCadre(ExemplaireContratCadre contrat) {
		if (contrat.getVendeur().equals(this)) {
			journalVCC.ajouter("Nouveau contrat :"+contrat);
			this.contratsEnCours.add(contrat);
		}
		else {
			super.notificationNouveauContratCadre(contrat);
		}
	}
	
	//Honorer le contrat
	
	public double restantALivrer(ChocolatDeMarque choco) {
		double res=0;			
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getProduit().equals(choco)) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}

	public double restantPayeARecevoir() {
		double res=0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			res+=c.getMontantRestantARegler();
		}
		return res;
	}
		
	//Next
		
	public void next() { 
		super.next();
		this.journalVCC.ajouter("=== STEP "+Filiere.LA_FILIERE.getEtape()+" ====================");
		
			//Pour les chocos de la marque CocOasis, codé par Anaïs et Pierrick
				for (ChocolatDeMarque choco : this.chocolatCocOasis) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
					if ((stockChocoMarque.get(choco) - restantALivrer(choco)>=30000) || (stockChocoMarque.get(choco) >= 100*12)) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000 + stockChocoMarque.get(choco) - restantALivrer(choco))/12); // au moins 100
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);	
						List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(choco);
						if (acheteurs.size()>0) {
							IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							
							journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
							
		
							ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, choco, e, cryptogramme, true);
							if (contrat==null) {
								journalVCC.ajouter(Color.RED, Color.white,"   echec des negociations");
							} else {
								this.contratsEnCours.add(contrat);
								journalVCC.ajouter(Color.GREEN, Color.BLACK, "   contrat " + contrat.getNumero() + " signé avec l'équipe " + contrat.getAcheteur()+ " pour un total de " + contrat.getPrix() + "euros et d'un stock de " + contrat.getQuantiteTotale() + " de " + contrat.getProduit());
							}
						} else {
							journalVCC.ajouter("  pas d'acheteur");
						}
					} else {
						journalVCC.ajouter(" quantité de " + choco + "  insuffisnate pour passer un contrat cadre");
					}
				}
				
				
			//Pour les chocos de marque distributeur, codé par Eliott
				
				for(ChocolatDeMarque choco : this.chocolatDistributeur) {
					
						
					if ((stockChoco.get(choco.getChocolat()) - restantALivrer(choco)>=30000) || (stockChoco.get(choco.getChocolat()) >= 100*12)) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000 + stockChoco.get(choco.getChocolat()) - restantALivrer(choco))/12); // au moins 100
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
						List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(choco);
						
						if (acheteurs.size()>0) {
							IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
							
							ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, choco, e, cryptogramme, true);
							
							if (contrat==null) {
								journalVCC.ajouter(Color.RED, Color.white,"   echec des negociations");
							} else {
								this.contratsEnCours.add(contrat);
								journalVCC.ajouter(Color.GREEN, Color.BLACK, "   contrat " + contrat.getNumero() + " signé avec l'équipe " + contrat.getAcheteur()+ " pour un total de " + contrat.getPrix() + "euros et d'un stock de " + contrat.getQuantiteTotale() + " de " + contrat.getProduit());
							}
							journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");		
						} else {
							journalVCC.ajouter("   pas d'acheteur");
						}
					} else {
						journalVCC.ajouter(" quantité de " + choco + "  insuffisnate pour passer un contrat cadre");
					}	
				}
				
				
		// On archive les contrats terminés  (pas à modifier)
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if (c.getQuantiteRestantALivrer()==0.0 && c.getMontantRestantARegler()<=0.0) {
				this.contratsTermines.add(c);
			}
		}
		for (ExemplaireContratCadre c : this.contratsTermines) {
			journalVCC.ajouter("Archivage du contrat "+c);
			this.contratsEnCours.remove(c);
		}
		this.journalVCC.ajouter("=====================================");
	}
	
	public List<Journal> getJournaux() {
		List<Journal> jx=super.getJournaux();
		jx.add(journalVCC);
		return jx;
	}

}
