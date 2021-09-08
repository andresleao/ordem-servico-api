package com.andresleao.os.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresleao.os.domain.Cliente;
import com.andresleao.os.domain.OS;
import com.andresleao.os.domain.Tecnico;
import com.andresleao.os.domain.enuns.Prioridade;
import com.andresleao.os.domain.enuns.Status;
import com.andresleao.os.repositories.ClienteRepository;
import com.andresleao.os.repositories.OSRepository;
import com.andresleao.os.repositories.TecnicoRepository;

@Service
public class DBService {

	@Autowired
	private TecnicoRepository tecncioRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private OSRepository OSRepository;
	
	public void instanciaDB() {
		Tecnico t1 = new Tecnico(null, "André Leão", "956.389.960-10", "(81) 99999-9999");
		Tecnico t2 = new Tecnico(null, "David Gilmour", "410.786.210-04", "(81) 97777-7777");
		Cliente c1 = new Cliente(null, "Tadeu Souza", "379.966.470-03", "(81) 98888-8888");
		Cliente c2 = new Cliente(null, "Pink Anderson", "967.478.650-30", "(81) 91313-1313");
		OS os1 = new OS(null, Prioridade.ALTA, "Teste: Criação OS", Status.ANDAMENTO, t1, c1);
		
		t1.getList().add(os1);
		c1.getList().add(os1);
		
		tecncioRepository.saveAll(Arrays.asList(t1, t2));
		clienteRepository.saveAll(Arrays.asList(c1, c2));
		OSRepository.saveAll(Arrays.asList(os1));
	}
}
