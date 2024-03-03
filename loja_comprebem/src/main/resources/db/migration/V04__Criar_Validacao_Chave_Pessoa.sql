CREATE OR REPLACE FUNCTION validachavepessoa()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS $$
declare existe integer;

BEGIN 

        existe = (select count(1) from pessoa_fisica where id = NEW.pessoa_id);
    if (existe <= 0) then
        existe = (select count(1) from empresa where id = NEW.empresa_id);
    if (existe <= 0) then
    if (existe <= 0) then
        existe (select count(1) from conta_pagar where id = NEW.pessoa_fornecedor_id);
        RAISE EXCEPTION 'Não foi encontrado o ID ou PK da pessoa_fisica ou empresa para realizar a associacao do cadastro';
    end if;
    end if;
    end if;
return new;
END;
$$