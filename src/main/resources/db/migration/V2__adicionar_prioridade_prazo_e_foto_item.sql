ALTER TABLE public.inspecoes 
    ADD COLUMN prioridade character varying(20) DEFAULT 'MEDIA' NOT NULL,
    ADD COLUMN data_limite timestamp(6) without time zone,
    ADD COLUMN instrucoes character varying(1000);

ALTER TABLE public.tb_fotos_inspecao
    ADD COLUMN snapshot_item_id bigint;

ALTER TABLE public.tb_fotos_inspecao
    ADD CONSTRAINT fk_foto_snapshot_item FOREIGN KEY (snapshot_item_id) REFERENCES public.inspecoes_snapshot_itens(id) ON DELETE SET NULL;

