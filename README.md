# API de gestion des clients et des comptes



## Introduction
La société bancaire(fictive) « Ega » voudrait mettre en place un système de gestion de ses clients et des comptes que ces derniers possèdent. Un client peut avoir plusieurs comptes. 
La banque met à disposition des clients deux types de comptes : un compte épargne et un compte courant. 
Un compte est caractérisé par le numéro de compte, le type de compte, la date de création du compte, le solde du compte et le propriétaire du compte. 
Pour le client on retiendra les informations suivantes : nom, prénom, date de naissance, sexe, adresse, numéro de téléphone, courriel et nationalité. 
Le numéro de compte est un ensemble de 5 caractères majuscules alphanumériques générés aléatoirement auquel on concatène l’année de création du compte. 
Le numéro de compte est unique pour un compte. 
Lors de la création du compte son solde est nul. 

### Objectifs:
1.	Mettre en place une API CRUD pour gérer les comptes et les clients.
2.	Ajouter les possibilités pour le client de : 
-	Faire un versement sur son compte ;
-	Faire un retrait sur son compte si le solde le permet ;
-	Faire un virement d’un compte à un autre


## Modèle

### Client
| Champ | Type | Description |
|-------|------|-------------|
| id | int | Identifiant unique du client générer automatiquement |
| nom | string | Nom du client |
| prenom | string | Prenom du client |
| dateNaissance | date | Date de de naissance du client (format "yyyy-mm-dd") |
| adresse | string | Adresse du client |
| numTel | string | Numéro de télephone du client |
| courriel | string | Courriel du client |
| nationalite | string | Nationalité du client |

### Compte
| Champ | Type | Description |
|-------|------|-------------|
| numCompte | int | Numéro de compte générer automatiquement |
| typeCompte | string | Type de compte, elle doit obligatoirement être "COURANT" ou "EPARGNE" |
| solde | double | Solde du compte |
| dateCreation | String | Date de création générer automatiquement avec la date du jour |
| proprietaire | Client | Client proprietaire du compte |

## Endpoints

### Opérations sur les clients

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /clients | Récupère tous les clients |
| GET | /clients/{id} | Récupère un client spécifique |
| POST | /clients | Crée un nouveau client |
| PUT | /clients/{id} | Met à jour un client existant |
| DELETE | /clients/{id} | Supprime un client existant |
| GET | /clients/{id}/comptes | Récupère les comptes d'un client spécifique |

### Opérations sur les comptes

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /comptes | Récupère tous les comptes |
| GET | /comptes/{numCompte} | Récupère un comptes spécifique |
| POST | /comptes | Crée un nouveau comptes |
| DELETE | /comptes/{numCompte} | Supprime un client existant |
| GET | /comptes/{numCpt}/proprietaire | Récupère le proprietaire du compte |
| PUT | /comptes/{numCompte}/retrait/{montant} | Faire le retrait d'un montant sur un compte |
| PUT | /comptes/{numCompte}/depot/{montant} | Faire le depot d'un montant sur un compte |
| PUT | /comptes/{numCompteSource}/virement/{numCompteDest}/{montant} | Faire le virement d'un compte(numCompteSource) source à un compte de destination (numCompteDest)  montant sur un compte |

## Exemple d'utilisation
