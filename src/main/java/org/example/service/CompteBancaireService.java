package org.example.service;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.CompteBancaire;

public class CompteBancaireService {

    public boolean effectuerTransaction(CompteBancaire compteDebiteur, CompteBancaire compteCrediteur, Double montant) {
        if (compteDebiteur == null || compteCrediteur == null) {
            throw new IllegalArgumentException("Les comptes ne peuvent pas être null");
        }
        if (montant == null || montant <= 0) {
            throw new IllegalArgumentException("Le montant de la transaction doit être positif et non null");
        }
        if (compteDebiteur.getSolde() < montant) {
            throw new NotEnoughMoneyException("Fonds insuffisants dans le compte débiteur");
        }

        compteDebiteur.setSolde(compteDebiteur.getSolde() - montant);
        compteCrediteur.setSolde(compteCrediteur.getSolde() + montant);
        return true;
    }
}
