package abstraction.eq9Distributeur2;

import java.util.HashMap;

import abstraction.eqXRomu.clients.ClientFinal;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.IDistributeurChocolatDeMarque;
import abstraction.eqXRomu.general.Entree;
import abstraction.eqXRomu.produits.ChocolatDeMarque;


public abstract class Distributeur2Vente extends Distributeur2Stocks implements IDistributeurChocolatDeMarque {

// Codé par Maureen Leprince 
	
	private HashMap<ChocolatDeMarque, Double> totalVentes;
	private HashMap<ChocolatDeMarque, Double> ventePrecedente;
	private int stepActuel;
	
	public Double getTotalVentes (ChocolatDeMarque cm) {
		return this.totalVentes.get(cm);
	}
	public Double getVentePrecedente (ChocolatDeMarque cm) {
		return this.ventePrecedente.get(cm);
	}
	
	public Distributeur2Vente () {
		super();
		this.totalVentes= new HashMap<ChocolatDeMarque, Double>();
		this.ventePrecedente= new HashMap<ChocolatDeMarque, Double>();
		this.stepActuel = 0;
	}
	
	public void initialiser() {
		super.initialiser();
		for (ChocolatDeMarque cm : Filiere.LA_FILIERE.getChocolatsProduits()) {
			this.totalVentes.put(cm, 0.);
			this.ventePrecedente.put(cm, 0.);
		}
	}
	
// Classe codée par Margot Lourenço Da Silva 
	@Override
	public double prix(ChocolatDeMarque choco) {
		// TODO Auto-generated method stub
		if( Filiere.LA_FILIERE.getEtape() < 1) {
			switch (choco.getChocolat()) {
			case C_HQ_BE: return 26000;
			case C_HQ_E: return 22000;
			case C_MQ_E:return 18000;
			case C_MQ :return 16000;
			case C_BQ : return 12000;
			case C_HQ : return 20000;
			default:
				return 0;}}
		if (Filiere.LA_FILIERE.prixMoyen(choco,Filiere.LA_FILIERE.getEtape()-1) > 100000  ) {
		return Filiere.LA_FILIERE.prixMoyen(choco,Filiere.LA_FILIERE.getEtape()-1);}
		switch (choco.getChocolat()) {
		case C_HQ_BE: return 26000;
		case C_HQ_E: return 22000;
		case C_MQ_E:return 18000;
		case C_MQ :return 16000;
		case C_BQ : return 12000;
		case C_HQ : return 20000;
		default:
			return 0;}}
	

	@Override
	public double quantiteEnVente(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		
		return this.getQuantiteEnStock(choco,crypto)*0.9;
		
	}

	@Override
	public double quantiteEnVenteTG(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
		return this.quantiteEnVente(choco, crypto)*ClientFinal.POURCENTAGE_MAX_EN_TG*0.99;
	}

	@Override
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant, int crypto) {
		// TODO Auto-generated method stub
		if (this.stockChocoMarque!=null && this.stockChocoMarque.keySet().contains(choco)) {
			this.stockChocoMarque.put(choco, this.stockChocoMarque.get(choco)-quantite);
			//this.totalStocksChocoMarque.retirer(this,  quantite, cryptogramme);
			// ajout de Maureen pour avoir accès aux ventes précédentes
			this.totalVentes.put(choco, this.totalVentes.get(choco)+quantite); 
			if (this.stepActuel != Filiere.LA_FILIERE.getEtape()) {
				this.ventePrecedente.put(choco, quantite); 
				this.stepActuel = Filiere.LA_FILIERE.getEtape();
			} else {
				this.ventePrecedente.put(choco, this.ventePrecedente.get(choco)+quantite); 
			}
		}
	}

	@Override
	public void notificationRayonVide(ChocolatDeMarque choco, int crypto) {
		// TODO Auto-generated method stub
	if (this.getQuantiteEnStock(choco, crypto)==0.0) {
		journal.ajouter("plus de chocolat"+choco+"");
	}
		
	}

}
