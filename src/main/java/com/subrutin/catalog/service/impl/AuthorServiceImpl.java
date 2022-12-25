package com.subrutin.catalog.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.subrutin.catalog.domain.Author;
import com.subrutin.catalog.dto.AuthorCreateRequestDTO;
import com.subrutin.catalog.dto.AuthorResponseDTO;
import com.subrutin.catalog.dto.AuthorUpdateRequestDTO;
import com.subrutin.catalog.exception.BadRequestException;
import com.subrutin.catalog.repository.AuthorRepository;
import com.subrutin.catalog.service.AuthorService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

	private final AuthorRepository authorRepository;

	@Override
	public AuthorResponseDTO findAuthorById(Long id) {
		// 1. fetch from database
		Author author = authorRepository.findById(id)
		.orElseThrow(() -> new BadRequestException("invalid.authorid"));
		// 2. author -> authorResponseDTO
		AuthorResponseDTO dto  = new AuthorResponseDTO();
		dto.setAuthorName(author.getName());
		dto.setBirthDate(author.getBirthDate().toEpochDay());
		
		return dto;
	}

	@Override
	public void createNewAuthor(List<AuthorCreateRequestDTO> dtos) {

		List<Author> authors= dtos.stream().map((dto)->{
			Author author = new Author();
			author.setName(dto.getAuthorName());
			author.setBirthDate(LocalDate.ofEpochDay(dto.getBirthDate()));
			return author;
		}).collect(Collectors.toList());

		
		authorRepository.saveAll(authors);
	}

	@Override
	public void updateAuthor(Long authorId, AuthorUpdateRequestDTO dto) {
		Author author = authorRepository.findById(authorId)
				.orElseThrow(() -> new BadRequestException("invalid.authorId"));
		author.setName(dto.getAuthorName() == null ? author.getName() : dto.getAuthorName());
		author.setBirthDate(
				dto.getBirthDate() == null ? author.getBirthDate() : LocalDate.ofEpochDay(dto.getBirthDate()));
		
		authorRepository.save(author);		
	}
	
	// oracle db -> flashback technologies
	// softdelete
	@Override
	public void deleteAuthor(Long authorId) {
		// 1 select data
		// 2 delete
		// or
		// 1 delete (harddelete)
		authorRepository.deleteById(authorId);

		// softdelete
		// 1. select data deleted=false
//		Author author = authorRepository.findByIdAndDeletedFalse(authorId)
//				.orElseThrow(() -> new BadRequestException("invalid.authorId"));
//
//		// 2. update deleted=true
//		author.setDeleted(Boolean.TRUE);
//		authorRepository.save(author);
	}

}
