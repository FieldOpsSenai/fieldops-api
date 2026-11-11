ALTER TABLE public.itens_inspecao
    ADD COLUMN evidencia_obrigatoria boolean NOT NULL DEFAULT false;

ALTER TABLE public.inspecoes_snapshot_itens
    ADD COLUMN evidencia_obrigatoria boolean NOT NULL DEFAULT false;
