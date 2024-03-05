package abstraction.eqXRomu.encheres;

import java.util.List;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.produits.Chocolat;

public class ExempleVendeurAuxEncheres extends ExempleAbsVendeurAuxEncheres implements IVendeurAuxEncheres {
	protected SuperviseurVentesAuxEncheres superviseur;

	public ExempleVendeurAuxEncheres(Chocolat choco, String marque, double stock, double prixMin) {
		super(choco, marque, stock, prixMin);
	}
	
	public void initialiser() {
		this.superviseur = (SuperviseurVentesAuxEncheres)(Filiere.LA_FILIERE.getActeur("Sup.Encheres"));
		journal.ajouter("PrixMin=="+this.prixMin);

	}
	
	public void next() {
		journal.ajouter("Etape="+Filiere.LA_FILIERE.getEtape());
		if (Filiere.LA_FILIERE.getEtape()>=1) {
			if (this.stock.getValeur()>200) {
				Enchere retenue = superviseur.vendreAuxEncheres(this, cryptogramme, getChocolatDeMarque(), 200.0);
				if (retenue!=null) {
					this.stock.setValeur(this, this.stock.getValeur()-retenue.getMiseAuxEncheres().getQuantiteT());
					journal.ajouter("vente de "+retenue.getMiseAuxEncheres().getQuantiteT()+" T a "+retenue.getAcheteur().getNom());
				} else {
					journal.ajouter("pas d'offre retenue");
				}
			}
		}
	}

	public Enchere choisir(List<Enchere> encheres) {
		this.journal.ajouter("encheres : "+encheres);
		if (encheres==null) {
			return null;
		} else {
			Enchere retenue = encheres.get(0);
			if (retenue.getPrixTonne()>this.prixMin) {
				this.journal.ajouter("  --> je choisis "+retenue);
				return retenue;
			} else {
				this.journal.ajouter("  --> je ne retiens rien");
				return null;
			}
		}
	}

}
