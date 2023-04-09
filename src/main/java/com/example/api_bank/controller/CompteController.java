package com.example.api_bank.controller;

import com.example.api_bank.model.Client;
import com.example.api_bank.model.Compte;
import com.example.api_bank.repo.CompteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/comptes")
public class CompteController {
    @Autowired
    public CompteRepo compteRepo;
    @GetMapping
    public List<Compte> getComptes(){
        return compteRepo.findAll();
    }

    @GetMapping(value="/{numCpt}")
    public Compte getCompteById(@PathVariable String numCpt){
        return compteRepo.findById(numCpt).get();
    }

    @DeleteMapping(value ="/{numCpt}")
    public String supprimerCompte(@PathVariable String numCpt){
        Compte compteSuppr = compteRepo.findById(numCpt).get();
        compteRepo.delete(compteSuppr);
        return "Compte spprimé id : "+ numCpt;
    }
    @PostMapping
    public String enregistrerCompte(@RequestBody Compte compte){
        compteRepo.save(compte);
        return "Enregistré ...";
    }

//    @PutMapping(value ="/comptes/{id}/modifier")
//    public String modifierCompte(@PathVariable long id, @RequestBody Compte compte){
//        Compte compteModifie = compteRepo.findById(id).get();
//        compteModifie.setSolde(compte.getSolde());
//        compteRepo.save(compteModifie);
//        return "Modifiée ...";
//    }

    @GetMapping("/{numCpt}/proprietaire")
    public Client getProprietaireByCompteId(@PathVariable  String numCpt) {
        Compte compte = compteRepo.findById(numCpt).get();//.orElseThrow(() -> new ResourceNotFoundException("Compte", "id", compteId));
        return compte.getProprietaire();
    }


    @PutMapping("/{numCompte}/retrait/{montant}")
    public ResponseEntity<?> faireRetrait(@PathVariable String numCompte, @PathVariable double montant) {
        Optional<Compte> optionalCompte = compteRepo.findById(numCompte);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            double nouveauSolde = compte.getSolde() - montant;
            if (nouveauSolde < 0) {
                return ResponseEntity.badRequest().body("Le solde du compte ne peut pas être négatif");
            }
            compte.setSolde(nouveauSolde);
            compteRepo.save(compte);
             return ResponseEntity.ok("Retrait effectué avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{numCompte}/depot/{montant}")
    public ResponseEntity<?> faireDepot(@PathVariable String numCompte, @PathVariable double montant) {
        Optional<Compte> optionalCompte = compteRepo.findById(numCompte);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            double nouveauSolde = compte.getSolde() + montant;
            compte.setSolde(nouveauSolde);
            compteRepo.save(compte);
            return ResponseEntity.ok("Dépôt effectué avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{numCompteSource}/virement/{numCompteDest}/{montant}")
    public ResponseEntity<?> faireVirement(@PathVariable String numCompteSource, @PathVariable String numCompteDest, @PathVariable double montant) {
        Optional<Compte> optionalCompteSource = compteRepo.findById(numCompteSource);
        Optional<Compte> optionalCompteDest = compteRepo.findById(numCompteDest);
        if (optionalCompteSource.isPresent() && optionalCompteDest.isPresent()) {
            Compte compteSource = optionalCompteSource.get();
            Compte compteDest = optionalCompteDest.get();
            double nouveauSoldeSource = compteSource.getSolde() - montant;
            if (nouveauSoldeSource < 0) {
                return ResponseEntity.badRequest().body("Le solde du compte source ne peut pas être négatif");
            }
            double nouveauSoldeDest = compteDest.getSolde() + montant;
            compteSource.setSolde(nouveauSoldeSource);
            compteDest.setSolde(nouveauSoldeDest);
            compteRepo.saveAll(Arrays.asList(compteSource, compteDest));
            return ResponseEntity.ok("Virement effectué avec succès");
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
