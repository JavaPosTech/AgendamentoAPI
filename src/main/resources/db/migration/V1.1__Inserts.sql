INSERT INTO public.tipo_usuario (descricao) VALUES
('ADMINISTRADOR'),
('MEDICO'),
('ENFERMEIRO'),
('RECEPCIONISTA'),
('PACIENTE')
ON CONFLICT (descricao) DO NOTHING;

INSERT INTO public.situacao_cadastro (descricao) VALUES
('ATIVO'),
('EXCLUIDO')
ON CONFLICT (descricao) DO NOTHING;

INSERT INTO public.usuario (login, senha, id_tipousuario) VALUES
('admin', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 1),
('joao.silva', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 2),
('ana.oliveira', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 2),
('carlos.santos', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 3),
('mariana.costa', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 3),
('fernanda.lima', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 4),
('pedro.almeida', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 5),
('juliana.rocha', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 5),
('lucas.ferreira', '$2y$05$ZpywJEw26dx/wK55JdAE7uSjF00ckF.qZwx4zqlVrKUjVxsIXr66a', 5);

INSERT INTO public.medico (id_usuario, nome, sobrenome, crm, especialidade, endereco, id_situacaocadastro) VALUES
(2, 'JOAO', 'SILVA', 'CRM-SP-123456', 'CARDIOLOGIA', 'Rua das Flores, 100 - Sao Paulo - SP', 1),
(3, 'ANA', 'OLIVEIRA', 'CRM-SP-654321', 'PEDIATRIA', 'Rua Central, 250 - Campinas - SP', 1);

INSERT INTO public.enfermeiro (id_usuario, nome, sobrenome, coren, id_situacaocadastro) VALUES
(4, 'CARLOS', 'SANTOS', 'COREN-SP-123456', 1),
(5, 'MARIANA', 'COSTA', 'COREN-SP-654321', 1);

INSERT INTO public.recepcionista (id_usuario, nome, sobrenome, id_situacaocadastro) VALUES
(6, 'FERNANDA', 'LIMA', 1);

INSERT INTO public.paciente (id_usuario, nome, sobrenome, cpf, email, telefone, endereco, data_nascimento, id_situacaocadastro) VALUES
(7, 'PEDRO', 'ALMEIDA', '12345678901', 'pedro.almeida@email.com', '(19) 99999-1001', 'Rua das Palmeiras, 50 - Limeira - SP', '1990-05-15', 1),
(8, 'JULIANA', 'ROCHA', '23456789012', 'juliana.rocha@email.com', '(19) 99999-1002', 'Rua das Acacias, 120 - Limeira - SP', '1985-10-22', 1),
(9, 'LUCAS', 'FERREIRA', '34567890123', 'lucas.ferreira@email.com', '(19) 99999-1003', 'Rua dos Ipes, 300 - Campinas - SP', '2002-03-08', 1);

INSERT INTO public.historico_paciente (id_paciente, queixa_principal, historico_doenca, medicamentos, alergias, observacoes) VALUES
(1, 'Dor no peito', 'Paciente relata dores no peito recorrentes há aproximadamente 2 meses.', 'Losartana 50mg', 'Nenhuma alergia conhecida.', 'Recomendada avaliação cardiológica.'),
(2, 'Febre e dor de garganta', 'Histórico de episódios frequentes de infecção de garganta.', 'Dipirona 500mg', 'Alergia a penicilina.', 'Acompanhar evolução dos sintomas.'),
(3, 'Dor de cabeça', 'Paciente relata dores de cabeça frequentes durante a semana.', 'Paracetamol 750mg', 'Nenhuma alergia conhecida.', 'Investigar possíveis causas da cefaleia.');

INSERT INTO public.agendamento (id_medico, id_paciente, datahora_consulta, observacao) VALUES
(1, 1, '2026-08-22 08:00:00', 'Primeira consulta cardiológica.'),
(1, 2, '2026-08-22 09:00:00', 'Avaliação de sintomas.'),
(1, 3, '2026-08-22 10:00:00', 'Retorno para avaliação.'),
(1, 1, '2026-08-22 14:00:00', 'Retorno cardiológico.'),
(1, 2, '2026-08-22 15:00:00', 'Acompanhamento do tratamento.'),
(2, 3, '2026-08-22 08:00:00', 'Consulta pediátrica.'),
(2, 1, '2026-08-22 10:00:00', 'Avaliação clínica.'),
(2, 2, '2026-08-22 13:00:00', 'Consulta de acompanhamento.'),
(2, 3, '2026-08-22 16:00:00', 'Retorno médico.');