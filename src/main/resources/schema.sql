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


