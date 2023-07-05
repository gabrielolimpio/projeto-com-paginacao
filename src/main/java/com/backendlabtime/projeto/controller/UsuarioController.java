package com.backendlabtime.projeto.controller;

import com.backendlabtime.projeto.dtos.UsuarioDto;
import com.backendlabtime.projeto.entity.Usuario;
import com.backendlabtime.projeto.service.UsuarioService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    // adicionar: paginação
// na hora de filtrar: aparecer apenas o campo
// update so no campo, atualmente muda o campo mas o resto fica tudo null, precisa deixar o resto como tava
    final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @PostMapping(value = "/save")
    public ResponseEntity<Object> saveCliente(@RequestBody UsuarioDto usuarioDto){
        var usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDto, usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
    }

    @PostMapping(value = "/registro-csv")
    public ResponseEntity<List<Usuario>> readCSVAndSaveToDatabase() throws IOException, CsvException {
        usuarioService.readCSVAndSaveToDatabase();
        List<Usuario> usuarios = usuarioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }

//    @GetMapping
//    public List<Usuario> listaFiltroUsuario(@RequestParam(required = false) String filtro){
//        if(filtro != null && !filtro.isEmpty()) {
//            return usuarioService.findByNomeCompletoContainingIgnoreCaseOrNomeSocialContainingIgnoreCaseOrEmailContainingIgnoreCase(filtro, filtro, filtro);
//        }
//        Pageable pageable = PageRequest.of(5, 6);
//        return usuarioService.findAll(pageable);
//    }

//    @GetMapping
//    public ResponseEntity<Page<Usuario>> getAllClientes(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable pageable){
//        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.findAll(pageable));
//    }


// ------------------------------------- funciona os dois de baixo separado, mas nao juntos ---------------------------------------------------------
    @GetMapping("/page")
    public Page<Usuario> getAllClientes(@PageableDefault(page = 0, size = 5, direction = Sort.Direction.ASC) Pageable pageable){
        return usuarioService.findAllUsersPageable(pageable);
    }

    @GetMapping
    public List<Usuario> listaFiltroUsuario(@RequestParam(required = false) String filtro){
        if(filtro != null && !filtro.isEmpty()) {
            return usuarioService.findByNomeCompletoContainingIgnoreCaseOrNomeSocialContainingIgnoreCaseOrEmailContainingIgnoreCase(filtro, filtro, filtro);
        }
        return usuarioService.findAllUsersByFilter();
    }

// ------------------------------------- funciona os dois de cima separado, mas nao juntos ---------------------------------------------------------


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserCliente (@PathVariable(value = "id") Long id){
        Optional<Usuario> clienteModelOptional = usuarioService.findById(id);
        if(!clienteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(clienteModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserCliente (@PathVariable(value = "id") Long id){
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if(!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }
        usuarioService.delete(usuarioOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Cliente deletado!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserCliente (@PathVariable(value = "id") Long id,
                                                     @RequestBody UsuarioDto usuarioDto){
        Optional<Usuario> usuarioOptional = usuarioService.findById(id);
        if(!usuarioOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }
        var clienteModel = usuarioOptional.get();
        clienteModel.setNomeCompleto(usuarioDto.getNomeCompleto());
        clienteModel.setNomeSocial(usuarioDto.getNomeSocial());
        clienteModel.setDataDeNascimento(usuarioDto.getDataDeNascimento());
        clienteModel.setCodigo(usuarioDto.getCodigo());
        clienteModel.setSexo(usuarioDto.getSexo());
        clienteModel.setEmail(usuarioDto.getEmail());
        clienteModel.setEstado(usuarioDto.getEstado());
        clienteModel.setMunicipio(usuarioDto.getMunicipio());
        clienteModel.setNumeroDeAcessosAoCurso(usuarioDto.getNumeroDeAcessosAoCurso());
        clienteModel.setSituacaoNoCurso(usuarioDto.getSituacaoNoCurso());
        clienteModel.setDataDeVinculo(usuarioDto.getDataDeVinculo());

        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(clienteModel));
    }
}
