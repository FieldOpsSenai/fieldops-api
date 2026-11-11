ALTER TABLE public.inspecoes DROP CONSTRAINT IF EXISTS inspecoes_status_check;

ALTER TABLE public.inspecoes ADD CONSTRAINT inspecoes_status_check
    CHECK (status IN ('AGENDADA', 'PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA'));
