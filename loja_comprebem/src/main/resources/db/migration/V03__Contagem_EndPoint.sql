CREATE TABLE public.tabela_acesso_endpoint
(
  nome_endpoint character varying,
  qtd_acesso_endpoint integer
);

INSERT INTO public.tabela_acesso_endpoint(
            nome_endpoint, qtd_acesso_endpoint)
    VALUES ('END_POINT_PESSOA_FISICA', 0);
    

    INSERT INTO public.tabela_acesso_endpoint(
            nome_endpoint, qtd_acesso_endpoint)
    VALUES ('END_POINT_EMPRESA', 0);
    
alter table tabela_acesso_endpoint add constraint nome_end_point_unique UNIQUE (nome_endpoint)