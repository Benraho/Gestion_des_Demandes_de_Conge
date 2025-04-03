CREATE TABLE utilisateurs (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50) CHECK (role IN ('EMPLOYE', 'MANAGER', 'ADMIN')) NOT NULL
);


CREATE TABLE demandes_conge (
    id SERIAL PRIMARY KEY,
    employe_id INTEGER REFERENCES utilisateurs(id),
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut VARCHAR(50) CHECK (statut IN ('EN_ATTENTE', 'APPROUVE', 'REFUSE')) DEFAULT 'EN_ATTENTE',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
