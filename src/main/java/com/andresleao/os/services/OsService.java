package com.andresleao.os.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresleao.os.domain.Cliente;
import com.andresleao.os.domain.OS;
import com.andresleao.os.domain.Tecnico;
import com.andresleao.os.domain.enuns.Prioridade;
import com.andresleao.os.domain.enuns.Status;
import com.andresleao.os.dtos.OSDTO;
import com.andresleao.os.repositories.OSRepository;
import com.andresleao.os.services.exeptions.ObjectNotFoundException;

@Service
public class OsService {

	@Autowired
	private OSRepository repository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public OS findById(Integer id) {
		Optional<OS> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + "Tipo: " + OS.class.getName()));
	}
	
	public List<OS> findAll() {
		return repository.findAll();
	}

	public OS create(@Valid OSDTO objDTO) {
		return fromDTO(objDTO);
	}
	
	public OS update(@Valid OSDTO objDTO) {
		findById(objDTO.getId());
		return fromDTO(objDTO);
	}
	
	private OS fromDTO(OSDTO objDTO) {
		OS newObj = new OS();
		newObj.setId(objDTO.getId());
		newObj.setObservacoes(objDTO.getObservacoes());
		newObj.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
		newObj.setStatus(Status.toEnum(objDTO.getPrioridade()));
		
		Tecnico tec = tecnicoService.findById(objDTO.getTecnico());
		Cliente cli = clienteService.findById(objDTO.getCliente());
		newObj.setTecnico(tec);
		newObj.setCliente(cli);
		
		if (newObj.getStatus().getCod().equals(2)) {
			newObj.setDataFechamento(LocalDateTime.now());
		}
		
		return repository.save(newObj);
	}
}
