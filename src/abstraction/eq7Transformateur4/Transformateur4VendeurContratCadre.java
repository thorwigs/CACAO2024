package abstraction.eq7Transformateur4;

//fichier codé par Anaïs, Eliott et Pierrick
//Eliott : méthode vend()
//Pierrick : méthode initialiser()
//Anaïs : tout le reste




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
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.Chocolat;
import abstraction.eqXRomu.produits.ChocolatDeMarque;
import abstraction.eqXRomu.produits.Feve;
import abstraction.eqXRomu.produits.IProduit;

public class Transformateur4VendeurContratCadre extends Transformateur4AcheteurContratCadre implements IVendeurContratCadre {
	
	private IAcheteurContratCadre acheteurPrecedent;
	private SuperviseurVentesContratCadre supCC;
	

	protected Journal journalVCC;
	private HashMap<ExemplaireContratCadre, Double> prixPrecedent;

	public Transformateur4VendeurContratCadre() {
		super();
		this.journalVCC = new Journal(this.getNom()+" journal CC vente", this);
		this.prixPrecedent = new HashMap<ExemplaireContratCadre,Double>();
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
			//System.out.println("   ---"+c+" "+(produit.getType().equals("ChocolatDeMarque"))+" "+((produit.getType().equals("ChocolatDeMarque"))?(c.equals((ChocolatDeMarque)produit)):"...")+" "+((produit.getType().equals("ChocolatDeMarque")&&(c.equals((ChocolatDeMarque)produit)))?(stockChocoMarque.get(produit)>25000):"..."));
			if ((produit.getType().equals("ChocolatDeMarque")) && (c.equals((ChocolatDeMarque)produit)) && (stockChocoMarque.get(produit)>25000) ) {
				//System.out.println(" on vend "+produit);
				return true;
			}
		}
		for (ChocolatDeMarque c : this.chocolatDistributeur) {
			if ((produit.getType().equals("ChocolatDeMarque")) &&(c.equals((ChocolatDeMarque)produit)) && (stockChoco.get(((ChocolatDeMarque)produit).getChocolat()) > 25000 )) {
				//System.out.println(" on vend "+produit);
				return true;
			}
		}
		//System.out.println(" on ne vend pas "+produit);
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

	public double getPrixFèves(IProduit p) {
		BourseCacao bourse = (BourseCacao)(Filiere.LA_FILIERE.getActeur("BourseCacao"));
		ChocolatDeMarque p2 = (ChocolatDeMarque)p ;
		Feve feve_utilise = Feve.F_HQ_BE; 
		double prix_F = 0.0 ;
		
		if (p2.getChocolat() == Chocolat.C_HQ_BE || p2.getChocolat() == Chocolat.C_HQ_E) { 
			if (p2.getChocolat() == Chocolat.C_HQ_BE) {
				feve_utilise = Feve.F_HQ_BE;  
			}
			if (p2.getChocolat() == Chocolat.C_HQ_E) {
				feve_utilise = Feve.F_HQ_E;
			}
			
			double prixtot = 0.0;
			double qtetot = 0.0;
			for (ExemplaireContratCadre c : this.contratsEnCours) { //on base le prix des fèves sur la moyenne pondérale des prix de contrat cadre par lesquels on les achète
				if (c.getProduit().equals(feve_utilise)) {
					prixtot += c.getPrix();
					qtetot += c.getQuantiteTotale();
				}
			}
			if (qtetot == 0.0) { //si on a pas de contrat cadre (au début) pour les fèves, on se base sur le cours de la bourse pour des F_HQ
				prix_F = bourse.getCours(Feve.F_HQ).getValeur();
			}
			else {
				prix_F = prixtot/qtetot;
			}
			
		}
		
		else {
			if (p2.getChocolat() == Chocolat.C_HQ) {
				feve_utilise = Feve.F_HQ;
			}
			if (p2.getChocolat() == Chocolat.C_MQ_E) {
				feve_utilise = Feve.F_MQ_E;
			}
			if (p2.getChocolat() == Chocolat.C_MQ) {
				feve_utilise = Feve.F_MQ;
			}
			if (p2.getChocolat() == Chocolat.C_BQ) {
				feve_utilise = Feve.F_BQ;
			}
			prix_F = bourse.getCours(feve_utilise).getValeur(); //on base le prix des fèves sur le cours de la bourse par laquel on les achète
		}
		
		double prix = prix_F/this.pourcentageTransfo.get(feve_utilise).get(p2.getChocolat());
		return prix;
	}
	
	public double propositionPrix(ExemplaireContratCadre contrat) {
		double prixPropose = 0.0;
		if (coutproduction_tonne_marque_step.isEmpty()){
			prixPropose = this.coutmachine + this.coutadjuvant*0.2 + getPrixFèves(contrat.getProduit()) + (1000*this.nbemployeCDI + 658)/(this.nbemployeCDI*this.tauxproductionemploye) ;
		} else {
			prixPropose = 1.05*(coutproduction_tonne_marque_step.get(contrat.getProduit()) + getPrixFèves(contrat.getProduit()));
		}
		prixPrecedent.put(contrat, prixPropose);
		return prixPropose;
	}//prend compte des coûts de production en ajoutant une marge de 10%
	
	
	public double contrePropositionPrixVendeur(ExemplaireContratCadre contrat) {
		double pPropose = contrat.getPrix();
		double pPrecedent = 0.0;
		if (prixPrecedent.get(contrat)==null) {
			pPrecedent = propositionPrix(contrat);
		}
		else {
			pPrecedent = prixPrecedent.get(contrat);
		}
		double qte = contrat.getQuantiteTotale();
		
		///////début//////modif 07/05 Pierrick
		double coutProd = 0.0; //ce truc vaut la même chose que propositionPrix(contrat) mais sans la marge de x%
		if (coutproduction_tonne_marque_step.isEmpty()){
			coutProd = this.coutmachine + this.coutadjuvant*0.2 + getPrixFèves(contrat.getProduit()) + (1000*this.nbemployeCDI + 658)/(this.nbemployeCDI*this.tauxproductionemploye) ;
		}
		else {
			coutProd = coutproduction_tonne_marque_step.get(contrat.getProduit()) + getPrixFèves(contrat.getProduit());
		}
		//////fin/////////
		
		if (pPropose <  pPrecedent) {
			if ((pPropose+pPrecedent)/2 >= coutProd*1.02){
				prixPrecedent.put(contrat, (pPropose+pPrecedent)/2);
				return (pPropose+pPrecedent)/2;
			}
			if ((pPropose+2*pPrecedent)/3 >= coutProd*1.02){
				prixPrecedent.put(contrat, (pPropose+2*pPrecedent)/3);
				return (pPropose+2*pPrecedent)/3;
			}
			if ((pPropose+3*pPrecedent)/4 >= coutProd*1.02){
				prixPrecedent.put(contrat, (pPropose+3*pPrecedent)/4);
				return (pPropose+3*pPrecedent)/4;
			}
			else {
				prixPrecedent.put(contrat, coutProd*1.02);
				return coutProd*1.02;
			}
		}
		else {
			return pPropose;
		}
	}//négocie le prix en nous garantissant une marge minimale de 2% (on enregistre notre précédente proposition dans prixPrecedent)

	
	
	
	
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
	
	public double restantALivrerDeType (Chocolat choco) { //permet d'obtenir le nombre de chocolat d'un type à livrer en CC, utile pour les CC de marque distributeur
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if ((c.getProduit() instanceof ChocolatDeMarque) && ((ChocolatDeMarque)c.getProduit()).getChocolat() == choco) {
				res+=c.getQuantiteRestantALivrer();
			}
		}
		return res;
	}
	
	public double ALivrerDeTypeAuStep (Chocolat choco) { //permet d'obtenir le nombre de chocolat d'un type à livrer en CC, utile pour les CC de marque distributeur
		double res = 0;
		for (ExemplaireContratCadre c : this.contratsEnCours) {
			if ((c.getProduit() instanceof ChocolatDeMarque) && ((ChocolatDeMarque)c.getProduit()).getChocolat() == choco) {
				res+=c.getQuantiteALivrerAuStep();
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
		
		this.journalVCC.ajouter("===== phase de demande chocolat Mirage ======== ");
		
			//Pour les chocos de la marque CocOasis, codé par Anaïs et Pierrick

				for (ChocolatDeMarque choco : this.chocolatCocOasis) { // pas forcement equitable : on avise si on lance un contrat cadre pour tout type de feve
				if (vend(choco)) {

					if ((stockChocoMarque.get(choco) - restantALivrer(choco)>=30000) || (stockChocoMarque.get(choco) >= 100*12)) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000 + stockChocoMarque.get(choco) - restantALivrer(choco))/12); // au moins 100
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);	
						List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(choco);
						if (acheteurs.size()>0) {
							for (IAcheteurContratCadre acheteur : acheteurs) {
								journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
							
		
								ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, choco, e, cryptogramme, true);
								if (contrat==null) {
									journalVCC.ajouter(Color.RED, Color.white,"   echec des negociations");
								} else {
									this.contratsEnCours.add(contrat);
									journalVCC.ajouter(Color.GREEN, Color.BLACK, "   contrat " + contrat.getNumero() + " signé avec l'équipe " + contrat.getAcheteur()+ " pour un total de " + contrat.getMontantRestantARegler() + "euros et d'un stock de " + contrat.getQuantiteTotale() + " de " + contrat.getProduit());
								}
							}
							//IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							
							
						} else {
							journalVCC.ajouter("  pas d'acheteur");
						}
					} else {
						journalVCC.ajouter(" quantité de " + choco + "  insuffisante pour passer un contrat cadre");
					}
				}
				
				
				}
				
				journalVCC.ajouter(" === fin de phase de demande chocolat Mirage =========");
				this.journalVCC.ajouter("===== phase de demande chocolat Distributeur ======== ");
			//Pour les chocos de marque distributeur, codé par Eliott
				
				for(ChocolatDeMarque choco : this.chocolatDistributeur) {
				if (vend(choco)) {
						
					if ((stockChoco.get(choco.getChocolat()) - restantALivrer(choco)>=30000) || (stockChoco.get(choco.getChocolat()) >= 100*12)) { 
						this.journalVCC.ajouter("   "+choco+" suffisamment trop en stock/contrat pour passer un CC");
						double parStep = Math.max(100, (-20000 + stockChoco.get(choco.getChocolat()) - restantALivrer(choco))/12); // au moins 100
						Echeancier e = new Echeancier(Filiere.LA_FILIERE.getEtape()+1, 12, parStep);
						List<IAcheteurContratCadre> acheteurs = supCC.getAcheteurs(choco);
						
						if (acheteurs.size()>0) {

							for (IAcheteurContratCadre acheteur : acheteurs) {
									journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");

							/*IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							
							if (acheteur == acheteurPrecedent) {
								acheteurs.remove(acheteur);
								acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
							}
							
							acheteurPrecedent = acheteur;
							
							journalVCC.ajouter("   "+acheteur.getNom()+" retenu comme acheteur parmi "+acheteurs.size()+" acheteurs potentiels");
							*/
						
							
									ExemplaireContratCadre contrat = supCC.demandeVendeur(acheteur, this, choco, e, cryptogramme, true);
							
									if (contrat==null) {
										journalVCC.ajouter(Color.RED, Color.white,"   echec des negociations");
									} else {
										this.contratsEnCours.add(contrat);
										journalVCC.ajouter(Color.GREEN, Color.BLACK, "   contrat " + contrat.getNumero() + " signé avec l'équipe " + contrat.getAcheteur()+ " pour un total de " + contrat.getPrix() + "euros et d'un stock de " + contrat.getQuantiteTotale() + " de " + contrat.getProduit());
									}
								}
							//IAcheteurContratCadre acheteur = acheteurs.get(Filiere.random.nextInt(acheteurs.size()));
						} else {
							journalVCC.ajouter("   pas d'acheteur");
						}
					} else {
						journalVCC.ajouter(" quantité de " + choco + "  insuffisnate pour passer un contrat cadre");
					}	
				}
				
				}
				journalVCC.ajouter(" === fin de phase de demande chocolat Distributeur =========");
				
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
