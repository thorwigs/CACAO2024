package abstraction;

import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.Test;

import abstraction.eqXRomu.filiere.Filiere;
import abstraction.eqXRomu.filiere.FiliereParDefaut;;

public class FiliereParDefaultTest {

	@Test
	public void testNext() {
		Filiere.LA_FILIERE = new FiliereParDefaut(LocalDate.now(ZoneId.of("Europe/Paris")).toEpochDay());

		Filiere.LA_FILIERE.initialiser();

		for (int i=0; i<300; i++)
			Filiere.LA_FILIERE.next();
	}

}
