CREATE TRIGGER updateavgpricesingleholding1
    BEFORE
        INSERT
        OR
UPDATE
    ON
    valuepaldev.holdings
    FOR EACH ROW
    EXECUTE PROCEDURE valuepaldev.updateavgprice2();

CREATE OR REPLACE FUNCTION valuepaldev.calcavgprice(
	p_price valuepaldev.holdings.price%TYPE,
	p_wallet_id valuepaldev.holdings.wallet_id%TYPE,
	p_ticker valuepaldev.holdings.ticker%TYPE,
	p_quantity valuepaldev.holdings.quantity%TYPE
)
RETURNS DOUBLE PRECISION
LANGUAGE plpgsql
AS $$
DECLARE
v_total_cost valuepaldev.orders.total_cost%type := 0;
v_quantity valuepaldev.holdings.quantity%type := 0;
v_record record;

BEGIN
SELECT SUM(total_cost) INTO v_total_cost FROM valuepaldev.orders
WHERE ticker = p_ticker
  AND wallet_id = p_wallet_id
  AND status = 'Filled'
  AND type = 'B';

v_total_cost := v_total_cost + p_price;

RETURN v_total_cost / p_quantity;

END;
$$

CREATE OR REPLACE FUNCTION valuepal.updateavgprice2()
 RETURNS trigger
 LANGUAGE plpgsql
AS $$
DECLARE
rec_count integer;
BEGIN
SELECT
    count(*)
INTO
    rec_count
FROM
    valuepal.orders
WHERE
        ticker = NEW.ticker
  AND wallet_id = NEW.wallet_id;

IF(rec_count > 0) then
	IF(new.process_flag = 'U') then
	return new;

end if;
		NEW.avg_purchase_price := valuepal.calcavgprice(NEW.last_cost,
		NEW.wallet_id,
		NEW.ticker,
		NEW.quantity);

RETURN NEW;
ELSE
		NEW.avg_purchase_price := NEW.price;
RETURN NEW;
END IF;
END
$$
;
