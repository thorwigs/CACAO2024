package abstraction.eq8Distributeur1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import abstraction.eqXRomu.appelDOffre.IAcheteurAO;
import abstraction.eqXRomu.appelDOffre.OffreVente;
import abstraction.eqXRomu.appelDOffre.SuperviseurVentesAO;
import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.general.Journal;
import abstraction.eqXRomu.produits.ChocolatDeMarque;

public class Distributeur1AcheteurAppelOffre extends Distributeur1AcheteurContratCadre implements IAcheteurAO{
	private HashMap<ChocolatDeMarque, List<Double>> prixRetenus;
	private SuperviseurVentesAO supAO;
	protected Journal journalAO;
	
	public Distributeur1AcheteurAppelOffre(double capaciteDeVente,double[] prix,String[] marques) {
		super(capaciteDeVente,prix,marques);
		this.journalAO= new Journal (this.getNom() +"Journal AO", this);
	}
	public void initialiser() {
		super.initialiser();
		this.supAO = (SuperviseurVentesAO)(Filiere.LA_FILIERE.getActeur("Sup.AO"));
		this.prixRetenus = new HashMap<ChocolatDeMarque, List<Double>>();
		for (ChocolatDeMarque cm : this.stock_Choco.keySet()) {
			this.prixRetenus.put(cm, new LinkedList<Double>());
		}
	}
	
	
	public void next() {
		
	}
	
	public OffreVente choisirOV(List<OffreVente> propositions) {
		
		return null;
	}
	
	public List<Journal> getJournaux(){
		List<Journal> jx=super.getJournaux();
		jx.add(journalAO);
		return jx;
	}
	
	

}
