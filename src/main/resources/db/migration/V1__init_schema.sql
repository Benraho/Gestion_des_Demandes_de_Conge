CREATE TABLE utilisateur (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    solde_conges INTEGER DEFAULT 30,
    role VARCHAR(50) CHECK (role IN ('EMPLOYE', 'MANAGER', 'ADMIN')) NOT NULL,
    manager_id BIGINT REFERENCES utilisateur(id)
);

CREATE TABLE demande_conge (
    id BIGSERIAL PRIMARY KEY,
    employe_id BIGINT REFERENCES utilisateur(id),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    status VARCHAR(50) CHECK (status IN ('EN_ATTENTE', 'APPROUVE', 'REFUSE')) DEFAULT 'EN_ATTENTE',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    raison VARCHAR(255)
);

CREATE TABLE historique_action (
    id BIGSERIAL PRIMARY KEY,
    demande_id BIGINT REFERENCES demande_conge(id),
    manager_id BIGINT REFERENCES utilisateur(id),
    action VARCHAR(50) CHECK (action IN ('APPROUVE', 'REFUSE')) NOT NULL,
    date_action TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);