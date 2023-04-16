package com.example.api_bank.controller;

import com.example.api_bank.model.Client;
import com.example.api_bank.model.Compte;
import com.example.api_bank.repo.CompteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> getCompteById(@PathVariable String numCpt) {
        Optional<Compte> optionalCompte = compteRepo.findById(numCpt);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            return ResponseEntity.ok(compte);
        } else {
            String message = "compte  " + numCpt + " non trouvé";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @DeleteMapping(value ="/{numCpt}")
    public ResponseEntity<?> supprimerCompte(@PathVariable String numCpt){
        Optional<Compte> optionalCompte = compteRepo.findById(numCpt);
        if (optionalCompte.isPresent()) {
            Compte compteSuppr = optionalCompte.get();
            compteRepo.delete(compteSuppr);
            return ResponseEntity.ok().body("Compte " + numCpt + " supprimé");
        } else {
            String message = "Le compte " + numCpt + " non trouvé";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
    @PostMapping
    public ResponseEntity<?> enregistrerCompte(@RequestBody Compte compte) {
        try {
            compteRepo.save(compte);
            return ResponseEntity.ok("Compte " + compte.getNumCompte() + " créé");
        } catch (Exception e) {
            String errorMessage = "Erreur lors de l'enregistrement du compte: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

//    @PutMapping(value ="/comptes/{id}/modifier")
//    public String modifierCompte(@PathVariable long id, @RequestBody Compte compte){
//        Compte compteModifie = compteRepo.findById(id).get();
//        compteModifie.setSolde(compte.getSolde());
//        compteRepo.save(compteModifie);
//        return "Modifiée ...";
//    }

    @GetMapping("/{numCpt}/proprietaire")
    public ResponseEntity<?> getProprietaireByCompteId(@PathVariable String numCpt) {
        Optional<Compte> optionalCompte = compteRepo.findById(numCpt);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            return ResponseEntity.ok(compte.getProprietaire());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte " + numCpt + " non trouvé");
        }
    }

    @PutMapping("/{numCompte}/retrait/{montant}")
    public ResponseEntity<?> faireRetrait(@PathVariable String numCompte, @PathVariable double montant) {
        Optional<Compte> optionalCompte = compteRepo.findById(numCompte);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            double nouveauSolde = compte.getSolde() - montant;
            if (nouveauSolde < 0) {
                return ResponseEntity.badRequest().body("Le solde du compte n'est pas suffisant pour un retrait ");
            }
            compte.setSolde(nouveauSolde);
            compteRepo.save(compte);
             return ResponseEntity.ok("Retrait effectué avec succès");
        } else {
            String message = "Erreur lors de l'opération, compte "+numCompte+" non trouvé, relisez bien la documentation.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @PutMapping("/{numCompte}/depot/{montant}")
    public ResponseEntity<?> faireDepot(@PathVariable String numCompte, @PathVariable double montant) {
        try {
        Optional<Compte> optionalCompte = compteRepo.findById(numCompte);
        if (optionalCompte.isPresent()) {
            Compte compte = optionalCompte.get();
            double nouveauSolde = compte.getSolde() + montant;
            compte.setSolde(nouveauSolde);
            compteRepo.save(compte);
            return ResponseEntity.ok("Dépôt effectué avec succès");
        } else {
            String message = "compte " + numCompte + " non trouvé ";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }


        }
        catch (Exception e) {
            String message = "Erreur lors de l'opération, compte "+numCompte+" non trouvé , relisez bien la documentation.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
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
                return ResponseEntity.badRequest().body("Le solde du compte n'est pas suffisant pour un virement ");
            }
            double nouveauSoldeDest = compteDest.getSolde() + montant;
            compteSource.setSolde(nouveauSoldeSource);
            compteDest.setSolde(nouveauSoldeDest);
            compteRepo.saveAll(Arrays.asList(compteSource, compteDest));
            return ResponseEntity.ok("Virement effectué avec succès");
        } else {
            String message = "Erreur lors de l'opération,l'un ou les 2 comptes n'existent pas, relisez bien la documentation.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }


}
