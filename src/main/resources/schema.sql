--
-- CREATE OR REPLACE PROCEDURE updateavgprice()
-- LANGUAGE plpgsql
-- AS $$
-- DECLARE
-- 	holding record;
-- BEGIN
-- 	for holding in (select * from valuepal.holdings) loop
-- 		update valuepal.holdings set avg_purchase_price =
-- 		(select avg(price) from valuepal.orders where ticker = holding.ticker
-- 		and wallet_id = holding.wallet_id) where ticker=holding.ticker
-- 		and wallet_id = holding.wallet_id;
-- 	end loop;
-- END;
-- $$

CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice1() RETURNS int
LANGUAGE plpgsql
AS $$
DECLARE
holding record;
	num int;
BEGIN
    for holding in (select * from valuepaltest.holdings) loop
        update valuepaltest.holdings set avg_purchase_price =
        (select avg(price) from valuepaltest.orders where ticker = holding.ticker
        and wallet_id = holding.wallet_id) where ticker=holding.ticker
        and wallet_id = holding.wallet_id;
        num = 1;
            return num;
    end loop;
END;
$$

CREATE TRIGGER updateavgpricesingleholding
    AFTER INSERT OR UPDATE
                        ON valuepaltest.holdings
                        EXECUTE PROCEDURE valuepaltest.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice2()
RETURNS TRIGGER as $updateavgpricesingleholding$
DECLARE
avg_price numeric;
BEGIN
select avg(price) into avg_price from valuepaltest.orders
where ticker = NEW.ticker
  and wallet_id = NEW.wallet_id;
return NEW;
END
$updateavgpricesingleholding$ LANGUAGE plpgsql;

DROP TRIGGER updateavgpricesingleholding on valuepaltest.holdings;
CREATE TRIGGER updateavgpricesingleholding
    BEFORE INSERT OR UPDATE
                         ON valuepaltest.holdings
                         FOR EACH ROW
                         EXECUTE PROCEDURE valuepaltest.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaltest.calcavgprice(
	p_newprice valuepaltest.holdings.price%type,
	p_wallet_id valuepaltest.holdings.wallet_id%type,
	p_ticker valuepaltest.holdings.ticker%type
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_avg valuepaltest.holdings.avg_purchase_price%type;
BEGIN
select avg(price) into v_avg from valuepaltest.orders
where ticker = p_ticker
  and wallet_id = p_wallet_id;
return (v_avg + p_newprice) / 2;
END;
$$

CREATE OR REPLACE FUNCTION valuepaltest.updateavgprice2()
RETURNS TRIGGER as $updateavgpricesingleholding$
DECLARE
rec_count int;
BEGIN
select count(*) into rec_count from valuepaltest.orders
where ticker = NEW.ticker
  and wallet_id = NEW.wallet_id;
IF(rec_count > 0) THEN
		NEW.avg_purchase_price := valuepaltest.calcavgprice(NEW.price, NEW.wallet_id, NEW.ticker);
ELSE
		NEW.avg_purchase_price:=NEW.price;
END IF;
return NEW;
END
$updateavgpricesingleholding$ LANGUAGE plpgsql;