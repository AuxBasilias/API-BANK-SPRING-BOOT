package com.example.api_bank.service;

import com.example.api_bank.model.Compte;
import com.example.api_bank.repo.CompteRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompteService {

    @Autowired
    private CompteRepo compteRepository;

    public void retirer(String numCpt, double montant) {
        Compte compte = compteRepository.findById(numCpt).orElseThrow(() -> new EntityNotFoundException("Compte introuvable"));

        if (compte.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde() - montant);
        compteRepository.save(compte);
    }
}
