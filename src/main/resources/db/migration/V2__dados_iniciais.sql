INSERT INTO usuarios (username, password, role) VALUES ('admin', '$2a$10$SAhrP3QZ0d/gDDrfR6ua1ueXGDn5bCoWhSpnxIAQJmV2CFlVpO7kq', 'ADMIN');

INSERT INTO ambientes (capacidade_maxima, nome, tipo) VALUES (5, 'Sala 101 - Bloco A', 'SALA_DE_AULA');
INSERT INTO ambientes (capacidade_maxima, nome, tipo) VALUES (2, 'Lab de Informática 02', 'LABORATORIO');
INSERT INTO ambientes (capacidade_maxima, nome, tipo) VALUES (3, 'Sala de Estudos Silenciosa', 'SALA_DE_ESTUDOS');

INSERT INTO alunos (email, matricula, nome) VALUES ('carlos@faculdade.edu', '2026001', 'Carlos Silva');
INSERT INTO alunos (email, matricula, nome) VALUES ('mariana@faculdade.edu', '2026002', 'Mariana Costa');
INSERT INTO alunos (email, matricula, nome) VALUES ('lucas@faculdade.edu', '2026003', 'Lucas Oliveira');
