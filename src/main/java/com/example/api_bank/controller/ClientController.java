package com.example.api_bank.controller;


import com.example.api_bank.model.Client;
import com.example.api_bank.model.Compte;
import com.example.api_bank.repo.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController
{
    @Autowired
    private ClientRepo clientRepo;
    @GetMapping
    public List<Client> obtenirClients(){
        return clientRepo.findAll();
    }

    @GetMapping(value="/{id}")
    public Client getClientById(@PathVariable long id){
        return clientRepo.findById(id).get();
    }

    @PostMapping
    public String enregistrerClient(@RequestBody Client client){
        clientRepo.save(client);
        return "Enregistré ...";
    }

    @PutMapping(value ="/{id}")
    public String modifierClient(@PathVariable long id, @RequestBody Client client){
        Client clientModifie = clientRepo.findById(id).get();
        clientModifie.setNom(client.getNom());
        clientModifie.setPrenom(client.getPrenom());
        clientModifie.setAdresse(client.getAdresse());
        clientModifie.setSexe(client.getSexe());
        clientModifie.setDateNaissance(client.getDateNaissance());
        clientModifie.setNumTel(client.getNumTel());
        clientModifie.setCourriel(client.getCourriel());
        clientModifie.setNationalite(client.getNationalite());
        clientRepo.save(clientModifie);

        return "Modifiée ...";
    }

    @DeleteMapping(value ="/{id}")
    public String supprimerClient(@PathVariable long id){
        Client clientSuppr = clientRepo.findById(id).get();
        clientRepo.delete(clientSuppr);
        return "Client spprimé id : "+ id;
    }

//    @GetMapping("/{clientId}/comptes")
//    public List<Compte> getComptesByClientId(@PathVariable Long clientId) {
//       Client client = clientRepo.findById(clientId).get()
////                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", clientId))
//              ;
//        return client.getComptes();
//    }

    @GetMapping("/{clientId}/comptes")
    public ResponseEntity<List<Compte>> getComptesByClientId(@PathVariable Long clientId) {
        Optional<Client> optionalClient = clientRepo.findById(clientId);
        if (optionalClient.isPresent()) {
            List<Compte> comptes = optionalClient.get().getComptes();
            return ResponseEntity.ok(comptes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}












