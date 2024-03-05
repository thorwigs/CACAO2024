package abstraction.eqXRomu.encheres;

import java.util.HashMap;



public class ExempleAcheteurAuxEncheres extends ExempleAbsAcheteurAuxEncheres implements IAcheteurAuxEncheres {

	protected HashMap<IVendeurAuxEncheres, Double> prix;
	
	public ExempleAcheteurAuxEncheres(double prixInit) {
		super(prixInit);
		this.prix=new HashMap<IVendeurAuxEncheres, Double>();
	}

	public double proposerPrix(MiseAuxEncheres offre) {
		journal.ajouter("ProposerPrix("+offre+"):");
			double px = this.prixInit;
			if (this.prix.keySet().contains(offre.getVendeur())) {
				px = this.prix.get(offre.getVendeur());
			}
			journal.ajouter("   je propose "+px);
			return px;
	}

	public void notifierAchatAuxEncheres(Enchere propositionRetenue) {
		double stock = (this.stock.keySet().contains(propositionRetenue.getMiseAuxEncheres().getProduit())) ?this.stock.get(propositionRetenue.getMiseAuxEncheres().getProduit()) : 0.0;
		this.stock.put(propositionRetenue.getMiseAuxEncheres().getProduit(), stock+ propositionRetenue.getMiseAuxEncheres().getQuantiteT());
		this.prix.put(propositionRetenue.getMiseAuxEncheres().getVendeur(), propositionRetenue.getPrixTonne()-1000.0);
		journal.ajouter("   mon prix a ete accepte. Mon prix pour "+propositionRetenue.getMiseAuxEncheres().getVendeur()+" passe a "+(propositionRetenue.getPrixTonne()-1000.0));
	}

	public void notifierEnchereNonRetenue(Enchere propositionNonRetenue) {
		this.prix.put(propositionNonRetenue.getMiseAuxEncheres().getVendeur(), propositionNonRetenue.getPrixTonne()+100.);
		journal.ajouter("   mon prix a ete refuse. Mon prix pour "+propositionNonRetenue.getMiseAuxEncheres().getVendeur()+" passe a "+(propositionNonRetenue.getPrixTonne()+100.));
	}
}
