package com.example.api_bank.service;

import com.example.api_bank.model.Compte;
import com.example.api_bank.repo.CompteRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    @Autowired
    private CompteRepo compteRepository;

    @Transactional
    public void transferer(String idCompteSource, String idCompteDestination, double montant) {
        Compte compteSource = compteRepository.findById(idCompteSource).orElseThrow(() -> new EntityNotFoundException("Compte source introuvable"));
        Compte compteDestination = compteRepository.findById(idCompteDestination).orElseThrow(() -> new EntityNotFoundException("Compte de destination introuvable"));

        if (compteSource.getSolde() < montant) {
            throw new RuntimeException("Solde insuffisant");
        }

        compteSource.setSolde(compteSource.getSolde() - montant);
        compteDestination.setSolde(compteDestination.getSolde() + montant);

        compteRepository.save(compteSource);
        compteRepository.save(compteDestination);
    }
}
