package com.andresleao.os.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andresleao.os.domain.Cliente;
import com.andresleao.os.domain.Pessoa;
import com.andresleao.os.domain.Tecnico;
import com.andresleao.os.dtos.ClienteDTO;
import com.andresleao.os.repositories.ClienteRepository;
import com.andresleao.os.repositories.PessoaRepository;
import com.andresleao.os.services.exeptions.DataIntegratyViolationException;
import com.andresleao.os.services.exeptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Cliente findById(Integer id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(
			() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: "  + Tecnico.class.getName()));
	}
	
	public List<Cliente> findAll() {
		return repository.findAll();
	}
	
	public Cliente create(ClienteDTO objDTO) {
		if (findByCPF(objDTO) != null) {
			throw new DataIntegratyViolationException("CPF já cadastrado!");
		}
		
		Cliente newObj = new Cliente(null, objDTO.getNome(), objDTO.getCpf(), objDTO.getTelefone());
		return repository.save(newObj);
	}
	
	public Cliente update(Integer id, ClienteDTO objDTO) {
		Cliente oldObj = findById(id);
		
		// Se digitar um CPF pertecente a outro usuário (Cliente ou Técnico)
		if (findByCPF(objDTO) != null && findByCPF(objDTO).getId() != id ) {
			throw new DataIntegratyViolationException("CPF já cadastrado!");
		}
		
		oldObj.setNome(objDTO.getNome());
		oldObj.setCpf(objDTO.getCpf());
		oldObj.setTelefone(objDTO.getTelefone());
		return repository.save(oldObj);	
	}
	
	public void delete(Integer id) {
		Cliente obj = findById(id);
		
		if (obj.getList().size() > 0) {
			throw new DataIntegratyViolationException("O Cliente possuiu Ordens de Serviço, não pode ser deletado!");
		}
		repository.deleteById(id);
	}
	
	
	private Pessoa findByCPF(ClienteDTO objDTO) {
		Pessoa obj = pessoaRepository.findByCPF(objDTO.getCpf());
		if (obj != null) {
			return obj;
		}
		return null;
	}
	
}
