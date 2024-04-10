package abstraction.eq5Transformateur2;

import java.util.List;

import abstraction.eqXRomu.encheres.Enchere;
import abstraction.eqXRomu.encheres.IVendeurAuxEncheres;
import abstraction.eqXRomu.general.Journal;

public class Transformateur2VendeurAuxEncheres extends Transformateur2VendeurAppelDOffre implements IVendeurAuxEncheres {
	protected Journal JournalEncheres;
	
	public Enchere choisir(List<Enchere> propositions) {
		return null;
	}

}
