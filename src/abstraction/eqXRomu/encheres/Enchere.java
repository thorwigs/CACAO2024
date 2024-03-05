package abstraction.eqXRomu.encheres;

public class Enchere implements Comparable<Enchere>{
	private MiseAuxEncheres miseAuxEncheres;
	private IAcheteurAuxEncheres acheteur;
	private double prixTonne; // prix a la tonne
	
	public Enchere(MiseAuxEncheres miseAuxEncheres, IAcheteurAuxEncheres acheteur, double prixT) {
		this.miseAuxEncheres = miseAuxEncheres;
		this.acheteur = acheteur;
		this.prixTonne = prixT;
	}

	public MiseAuxEncheres getMiseAuxEncheres() {
		return miseAuxEncheres;
	}

	public IAcheteurAuxEncheres getAcheteur() {
		return acheteur;
	}

	public double getPrixTonne() {
		return prixTonne;
	}

	public String toString() {
		return "["+miseAuxEncheres+" a="+acheteur+" px="+prixTonne+"]";
	}
	
	public int compareTo(Enchere o) {
		return this.getPrixTonne()<o.getPrixTonne()? -1 : (this.getPrixTonne()==o.getPrixTonne()? 0 : 1);
	}
}
