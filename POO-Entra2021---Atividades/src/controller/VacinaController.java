package controller;

import model.entidade.Vacina;
import model.repository.VacinaRepository;

public class VacinaController {

	private VacinaRepository repository = new VacinaRepository();
	public Vacina salvar(Vacina novaVacina) {
		return novaVacina;
	}
	
}
