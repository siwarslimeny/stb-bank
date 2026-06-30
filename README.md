# STB Bank Platform — PFE

> Société Tunisienne de Banque — Plateforme Bancaire Spring Boot + Thymeleaf

## Structure du projet

```
stb-bank/
├── pom.xml
└── src/main/
    ├── java/tn/stb/bank/
    │   ├── StbBankApplication.java          # Point d'entrée Spring Boot
    │   ├── model/                           # Entités JPA
    │   │   ├── Utilisateur.java
    │   │   ├── Client.java
    │   │   ├── CompteBancaire.java
    │   │   ├── Transaction.java
    │   │   ├── Virement.java
    │   │   ├── Employe.java
    │   │   └── Role.java (enum)
    │   ├── repository/                      # Spring Data JPA
    │   ├── service/                         # Logique métier
    │   │   ├── DataInitializer.java         # Données de test
    │   │   ├── BankServices.java            # Services (impl)
    │   │   └── PublicServices.java          # Facades Spring Beans
    │   ├── security/
    │   │   └── SecurityConfig.java          # RBAC + Spring Security
    │   └── controller/
    │       ├── PublicController.java        # Pages publiques
    │       ├── DashboardController.java
    │       ├── ClientController.java        # CRUD Clients
    │       ├── CompteController.java        # CRUD Comptes
    │       ├── BankControllers.java         # Transactions, Employés, Virements
    │       └── AdminController.java
    └── resources/
        ├── application.properties
        ├── static/css/stb.css               # Design system complet
        └── templates/
            ├── fragments.html               # Sidebar + Topbar (Thymeleaf fragments)
            ├── public/                      # Site public STB (accueil, services, agences, contact)
            ├── auth/                        # Login, access-denied
            ├── dashboard/                   # Tableau de bord admin
            ├── clients/                     # List + Form + Detail
            ├── comptes/                     # List + Form
            ├── transactions/                # List + Form
            ├── virements/                   # List + Form
            ├── employes/                    # List + Form
            └── admin/                       # Gestion utilisateurs
```

## Comptes de démonstration

| Identifiant      | Mot de passe | Rôle          | Accès                        |
|-----------------|--------------|---------------|------------------------------|
| `admin`          | `admin123`   | ADMIN         | Dashboard + Tous les services|
| `alice.info`     | `pass123`    | INFO          | Service Clients uniquement   |
| `bob.compta`     | `pass123`    | COMPTABILITE  | Comptes + Transactions + Virements|
| `carol.rh`       | `pass123`    | RH            | Service Employés uniquement  |

## Lancement

```bash
# Prérequis : Java 17+ et Maven 3.8+

cd stb-bank
mvn spring-boot:run

# Accès :
# Site public  : http://localhost:8080/
# Login interne: http://localhost:8080/login
# H2 Console   : http://localhost:8080/h2-console
```

## Fonctionnalités

### Site public STB
- **Accueil** avec héro, affiches publicitaires (6 promos), stats, services, footer complet
- **Nos Services** : grille de produits bancaires avec affiches
- **Agences** : liste des 6 principales agences avec horaires
- **Contact** : formulaire de contact + coordonnées

### Plateforme interne (authentifié)
- **Dashboard** : KPIs temps réel, transactions récentes, virements en attente
- **Clients** : CRUD complet (liste, fiche détail, formulaire création/modification)
- **Comptes bancaires** : Liste + ouverture de compte
- **Transactions** : Journal complet avec CRUD + filtres + recherche
- **Virements** : Soumission, validation, rejet, suppression
- **Employés** : CRUD complet avec dossier RH
- **Admin** : Gestion des utilisateurs système

### Sécurité
- Spring Security avec RBAC par rôle
- Redirection post-login selon le rôle
- Protection CSRF sur tous les formulaires
- Page 403 dédiée

## Technologies
- **Backend** : Spring Boot 3.2, Spring Security 6, Spring Data JPA
- **Frontend** : Thymeleaf + CSS natif (design system complet)
- **Base de données** : H2 en mémoire (dev), MySQL/PostgreSQL en prod
- **Build** : Maven
