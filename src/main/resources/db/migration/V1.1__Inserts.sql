INSERT INTO public.tipo_usuario (descricao) VALUES
('ADMINISTRADOR'),
('MEDICO'),
('ENFERMEIRO'),
('PACIENTE')
ON CONFLICT (descricao) DO NOTHING;

INSERT INTO public.situacao_cadastro (descricao) VALUES
('ATIVO'),
('EXCLUIDO')
ON CONFLICT (descricao) DO NOTHING;

INSERT INTO public.status_consulta (descricao) VALUES
('AGENDADO'),
('FINALIZADO'),
('CANCELADO')
ON CONFLICT (descricao) DO NOTHING;

INSERT INTO public.usuario (login, senha, id_tipousuario) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1),
('joao.silva', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2),
('ana.oliveira', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 2),
('carlos.santos', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 3),
('mariana.costa', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 3),
('pedro.almeida', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 4),
('juliana.rocha', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 4),
('lucas.ferreira', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 4);

INSERT INTO public.medico (id_usuario, nome, sobrenome, crm, especialidade, endereco, id_situacaocadastro) VALUES
(2, 'JOAO', 'SILVA', 'CRM-SP-123456', 'CARDIOLOGIA', 'Rua das Flores, 100 - Sao Paulo - SP', 1),
(3, 'ANA', 'OLIVEIRA', 'CRM-SP-654321', 'PEDIATRIA', 'Rua Central, 250 - Campinas - SP', 1);

INSERT INTO public.enfermeiro (id_usuario, nome, sobrenome, coren, id_situacaocadastro) VALUES
(4, 'CARLOS', 'SANTOS', 'COREN-SP-123456', 1),
(5, 'MARIANA', 'COSTA', 'COREN-SP-654321', 1);

INSERT INTO public.paciente (id_usuario, nome, sobrenome, cpf, endereco, data_nascimento, id_situacaocadastro) VALUES
(6, 'PEDRO', 'ALMEIDA', '12345678901', 'Rua das Palmeiras, 50 - Limeira - SP', '1990-05-15', 1),
(7, 'JULIANA', 'ROCHA', '23456789012', 'Rua das Acacias, 120 - Limeira - SP', '1985-10-22', 1),
(8, 'LUCAS', 'FERREIRA', '34567890123', 'Rua dos Ipes, 300 - Campinas - SP', '2002-03-08', 1);

INSERT INTO public.agendamento (id_medico, id_paciente, datahora_consulta) VALUES
(1, 1, '2026-08-22 08:00:00'),
(1, 2, '2026-08-22 09:00:00'),
(1, 3, '2026-08-22 10:00:00'),
(1, 1, '2026-08-22 14:00:00'),
(1, 2, '2026-08-22 15:00:00'),
(2, 3, '2026-08-22 08:00:00'),
(2, 1, '2026-08-22 10:00:00'),
(2, 2, '2026-08-22 13:00:00'),
(2, 3, '2026-08-22 16:00:00');