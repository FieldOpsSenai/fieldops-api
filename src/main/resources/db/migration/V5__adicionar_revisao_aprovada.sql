ALTER TABLE public.inspecoes
    ADD COLUMN IF NOT EXISTS revisao_aprovada boolean;

