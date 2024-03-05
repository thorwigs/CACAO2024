 package abstraction.eqXRomu.produits;

import abstraction.eqXRomu.filiere.Filiere;

public enum Chocolat implements IProduit {
	C_HQ_BE(Gamme.HQ, true, true), // CHOCOLAT HAUTE QUALITE BIO EQUITABLE
	C_HQ_E( Gamme.HQ, false,true), // CHOCOLAT HAUTE QUALITE EQUITABLE
	C_MQ_E( Gamme.MQ, false,true), // CHOCOLAT MOYENNE QUALITE EQUITABLE
	C_MQ  ( Gamme.MQ, false,false),// CHOCOLAT MOYENNE QUALITE (NI BIO NI EQUITABLE)
	C_BQ(   Gamme.BQ, false,false);// CHOCOLAT BASSE QUALITE (NI BIO NI EQUITABLE)
	
	private Gamme gamme;
	private boolean bio;
	private boolean equitable;
	
	Chocolat(Gamme gamme, boolean bio, boolean equitable) {
		this.gamme = gamme;
		this.bio=bio;
		this.equitable = equitable;
	}
	
	public String getType() {
		return "Chocolat";
	}
	
	public Gamme getGamme() {
		return this.gamme;
	}
	
	public boolean isBio() {
		return this.bio;
	}
	
	public boolean isEquitable() {
		return this.equitable;
	}

	/**
	 * @param f
	 * @return Retourne true si p est un Chocolat et le chocolat this est au moins de la meme 
	 * qualite que p (et qu'il est donc possible d'employer le chocolat this en substitution
	 *  du chocolat p).
	 * En particulier, un vendeur tenu de fournir du chocolat c1 peut fournir
	 * du chocolat c2 si c2.greaterThan(c1)
	 */
	public boolean greaterThan(IProduit p) {
		Chocolat c = (p instanceof Chocolat ? (Chocolat)p:null);
		boolean labelsOK = c!=null && (!c.isEquitable()||this.isEquitable()) // si c n'est pas equitable alors peu importe que this soit ou non equitable
				                                    // mais si c est equitable alors this doit forcement etre equitable
				&& (!c.isBio()||this.isBio()); // si c n'est pas bio alors peu importe que this soit ou non bio
                                            // mais si c est bio alors this doit forcement etre bio
		return labelsOK && this.gamme.greaterThan(c.getGamme());	
	}
	

	public static Chocolat get(Gamme gamme, boolean bio, boolean equitable) {
		for (Chocolat c : Chocolat.values()) {
			if (c.gamme==gamme && c.isEquitable()==equitable && c.isBio()==bio) {
				return c;
			}
		}
		return null;
	}
	
	public double qualite() {
		double qualite;
		switch (getGamme()) {
		case BQ : qualite=Filiere.LA_FILIERE.getParametre("qualite basse").getValeur();
		case MQ : qualite=Filiere.LA_FILIERE.getParametre("qualite moyenne").getValeur();
		default : qualite=Filiere.LA_FILIERE.getParametre("qualite haute").getValeur(); //HAUTE
		}
		if (isBio()) {
			qualite=qualite+Filiere.LA_FILIERE.getParametre("gain qualite bio").getValeur();
		}
		if (isEquitable()) {
			qualite=qualite+Filiere.LA_FILIERE.getParametre("gain qualite equitable").getValeur();
		}
		return qualite;
	}
	
	public static void main(String[] args) {
		System.out.println("== Les differentes chocolats ==");
		for (Chocolat c : Chocolat.values()) {
			System.out.println(c);
		}
		
		Chocolat c = Chocolat.C_MQ_E;
		System.out.println("\n"+c+" ->getGamme()="+c.getGamme()+"\n        ->IsBio()="+c.isBio()+"\n        ->IsEquitable()="+c.isEquitable()+"\n        ->getType()="+c.getType());

		System.out.println("\n Chocolat.get(Gamme.HQ, true, true) -> "+Chocolat.get(Gamme.HQ, true, true));
		for (Chocolat c1 : Chocolat.values()) {
			for (Chocolat c2 : Chocolat.values()) {
				if (c1.greaterThan(c2)) {
					System.out.println(c1+" greather than "+c2);
				}
			}
		}

	}
}
