package org.example;

import org.example.exception.NotEnoughMoneyException;
import org.example.model.CompteBancaire;
import org.example.service.CompteBancaireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CompteBancaireServiceTest {

    private CompteBancaireService compteBancaireService;
    private CompteBancaire compteDebiteur;
    private CompteBancaire compteCrediteur;

    @BeforeEach
    void setUp() {
        compteBancaireService = new CompteBancaireService();
        compteDebiteur = new CompteBancaire("123456", 1000);
        compteCrediteur = new CompteBancaire("789012", 500);
    }

    @Test
    void testTransactionAvecMontantPositif() {
        assertAll("Transaction réussie",
                () -> assertTrue(compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, 100.00)),
                () -> assertEquals(900, compteDebiteur.getSolde()),
                () -> assertEquals(600, compteCrediteur.getSolde())
        );
    }

    @Test
    void testTransactionAvecMontantNegatif() {
        assertThrows(IllegalArgumentException.class, () ->
                compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, -10.00));
    }

    @Test
    void testTransactionAvecComptesNull() {
        assertThrows(IllegalArgumentException.class, () ->
                compteBancaireService.effectuerTransaction(null, compteCrediteur, 100.00));
    }

    @Test
    void testTransactionAvecMontantNull() {
        assertThrows(IllegalArgumentException.class, () ->
                compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, null));
    }

    @Test
    void testTransactionAvecSoldeInsuffisant() {
        assertThrows(NotEnoughMoneyException.class, () ->
                compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, 1500.00));
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 200, 50, -10})
    void testTransactionAvecMontantParametrise(double montant) {
        if (montant <= 0) {
            assertThrows(IllegalArgumentException.class, () ->
                    compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, montant));
        } else {
            assertTrue(compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, montant));
        }
    }

    @Test
    void testTransactionTempsExecution() {
        assertTimeoutPreemptively(
                java.time.Duration.ofSeconds(1),
                () -> compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, 100.00)
        );
    }

    @Test
    void testTransactionImbriquee() {
        assertAll("Transaction imbriquée",
                () -> assertTrue(compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, 100.00)),
                () -> assertThrows(NotEnoughMoneyException.class, () ->
                        compteBancaireService.effectuerTransaction(compteDebiteur, compteCrediteur, 1000.00))
        );
    }
}
