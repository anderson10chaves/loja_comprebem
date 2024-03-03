CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON avaliacao_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON avaliacao_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON avaliacao_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON categoria_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON categoria_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON categoria_produto
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoafornecedorInsert
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoafornecedorDelete
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoafornecedorUpdate
    BEFORE UPDATE
    ON conta_pagar
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON conta_receber
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON conta_receber
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON empresa
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON empresa
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON empresa
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON endereco
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON endereco
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON endereco
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON nota_fiscal_compra
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON nota_fiscal_compra
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON nota_fiscal_compra
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON usuario
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON usuario
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON usuario
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaUpdate
    BEFORE UPDATE
    ON vd_cp_loja_virt
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaInsert
    BEFORE UPDATE
    ON vd_cp_loja_virt
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();

CREATE TRIGGER validachavepessoaDelete
    BEFORE UPDATE
    ON vd_cp_loja_virt
    FOR EACH ROW
    EXECUTE PROCEDURE validachavepessoa();