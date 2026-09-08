package stb.authz

import future.keywords.if

default allow_validation = false

# Un ADMIN peut valider n'importe quel virement, sans limite de montant.
allow_validation if {
    input.role == "ADMIN"
}

# Un COMPTABILITE ne peut valider que les virements <= 10000 DT.
allow_validation if {
    input.role == "COMPTABILITE"
    input.montant <= 10000
}

# --- Suppression de virement ---
# Seul ADMIN peut supprimer un virement, quel que soit son montant ou son statut.
default allow_deletion = false

allow_deletion if {
    input.role == "ADMIN"
}